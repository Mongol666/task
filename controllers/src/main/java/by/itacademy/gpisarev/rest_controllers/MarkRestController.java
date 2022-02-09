package by.itacademy.gpisarev.rest_controllers;

import by.itacademy.gpisarev.entity.AbstractEntity;
import by.itacademy.gpisarev.mark.MarkRepository;
import by.itacademy.gpisarev.person.PersonRepository;
import by.itacademy.gpisarev.role.Role;
import by.itacademy.gpisarev.secondary.Mark;
import by.itacademy.gpisarev.users.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

@RestController
@RequestMapping("/rest/students/{studentID}/marks")
public class MarkRestController {
    private final MarkRepository markRepository;
    private final PersonRepository personRepository;

    @Autowired
    public MarkRestController(MarkRepository markRepository, PersonRepository personRepository) {
        this.markRepository = markRepository;
        this.personRepository = personRepository;
    }

    @GetMapping
    public Set<Mark> getAllStudentMarks(@PathVariable("studentID") int studentID) {
        return markRepository.getMarksByStudentID(studentID);
    }

    @GetMapping("/{markID}")
    public Mark getMarkByID(@PathVariable("studentID") int studentID, @PathVariable("markID") int markID) {
        Person person = personRepository.getPersonById(studentID);
        Mark mark = markRepository.getMarkByID(markID);
        if (mark != null && person != null && person.getRole() == Role.STUDENT) {
            return mark;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Оценка по ID " + markID + " не найдена");
        }
    }

    @PostMapping
    public Mark addSalary(@PathVariable("studentID") int studentID, @RequestBody Mark newMark) {
        if (markRepository.createMark(newMark, studentID)) {
            return Collections.max(markRepository.getAllMarks(), Comparator.comparing(AbstractEntity::getId));
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Оценка не добавлена");
        }
    }

    @PutMapping("/{markID}")
    public Mark updateMark(@PathVariable("markID") int markID, @PathVariable("studentID") int studentID,
                           @RequestBody Mark newMark) {
        Person person = personRepository.getPersonById(studentID);
        Mark mark = markRepository.getMarkByID(markID);
        if (mark != null && person != null && person.getRole() == Role.STUDENT) {
            newMark.setId(markID);
            if (markRepository.updateMark(newMark)) {
                return mark;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Оценка не обновлена");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Оценка не обновлена");
        }
    }

    @DeleteMapping("/{markID}")
    public Mark deleteMark(@PathVariable("studentID") int studentID, @PathVariable("markID") int markID) {
        Mark mark = markRepository.getMarkByID(markID);
        Person person = personRepository.getPersonById(studentID);

        if (mark != null && person != null && person.getRole() == Role.STUDENT) {
            if (markRepository.deleteMarkById(markID)) {
                return mark;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Оценка не удалена");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Оценка не удалена");
        }
    }
}