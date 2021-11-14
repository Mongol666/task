package mark;

import lombok.extern.slf4j.Slf4j;
import secondary.Group;
import secondary.Mark;
import secondary.Subject;

import java.time.LocalDate;
import java.util.*;

@Slf4j
public class MarkRepositoryLocalImpl implements MarkRepository {
    private static int ID = 0;
    private final Map<Integer, Mark> markMap = new HashMap<>();
    private static volatile MarkRepositoryLocalImpl instance;

    private MarkRepositoryLocalImpl() {
    }

    public static MarkRepositoryLocalImpl getInstance() {
        if (instance == null) {
            synchronized (MarkRepositoryLocalImpl.class) {
                if (instance == null) {
                    instance = new MarkRepositoryLocalImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Mark createMark(Mark mark) {
        log.debug("Попытка найти оценку в репозитории");
        Optional<Mark> optionalMark = markMap.values()
                .stream()
                .filter(mk -> mk.getId() == ID)
                .filter(mk -> mk.getSubject().equals(mark.getSubject()))
                .findAny();
        if (optionalMark.isEmpty()) {
            ID++;
            log.info("Добавлена новая оценка");
            markMap.put(ID, mark.withMark(ID));
            return mark;
        }
        log.error("Переданная оценка уже существует");
        return null;
    }

    @Override
    public List<Mark> getAllMarks() {
        log.info("Берём все оценки из репозитория");
        return new ArrayList<>(markMap.values());
    }

    @Override
    public Optional<Mark> updateSubjectMarkById(int id, Subject newSubject) {
        log.debug("Попытка взять оцеку по ID");
        Optional<Mark> optionalMark = markMap.values()
                .stream()
                .filter(mk -> id == mk.getId())
                .findAny();
        if (optionalMark.isPresent()) {
            log.info("Изменение оценки в репозитории");
            Mark markFromOptional = optionalMark.get();
            markFromOptional.setSubject(newSubject);
            markMap.put(id, markFromOptional);
            return optionalMark;
        }
        log.error("Оценка не найдена, изменений не произошло");
        return Optional.empty();
    }

    @Override
    public Optional<Mark> updateDateOfMarkById(int id, LocalDate newDateOfMark) {
        log.debug("Попытка взять оценку по ID");
        Optional<Mark> optionalMark = markMap.values()
                .stream()
                .filter(mk -> id == mk.getId())
                .findAny();
        if (optionalMark.isPresent()) {
            log.info("Изменение оценки в репозитории");
            Mark markFromOptional = optionalMark.get();
            markFromOptional.setDateOfMark(newDateOfMark);
            markMap.put(id, markFromOptional);
            return optionalMark;
        }
        log.error("Оценка не найдена, изменений не произошло");
        return Optional.empty();
    }

    @Override
    public Optional<Mark> updateGroupWhereMarkWasGiven(int id, Group newGroup) {
        log.debug("Попытка взять оценку по ID");
        Optional<Mark> optionalMark = markMap.values()
                .stream()
                .filter(mk -> id == mk.getId())
                .findAny();
        if (optionalMark.isPresent()) {
            log.info("Измнений оценки в репозитории");
            Mark markFromOptional = optionalMark.get();
            markFromOptional.setGroup(newGroup);
            markMap.put(id, markFromOptional);
            return optionalMark;
        }
        log.error("Оценка не найдена, изменений не произошло");
        return Optional.empty();
    }

    @Override
    public Optional<Mark> updateMarkById(int id, int newMark) {
        log.debug("Попытка взять оценку по ID");
        Optional<Mark> optionalMark = markMap.values()
                .stream()
                .filter(mk -> id == mk.getId())
                .findAny();
        if (optionalMark.isPresent()) {
            Mark markFromOptional = optionalMark.get();
            markFromOptional.setMark(newMark);
            markMap.put(id, markFromOptional);
            return optionalMark;
        }
        log.error("Оценка не найдена, изменений не произошло");
        return Optional.empty();
    }

    @Override
    public Optional<Mark> deleteMarkById(int id) {
        log.debug("Попытка взять оценку по ID");
        Optional<Mark> optionalMark = markMap.values()
                .stream()
                .filter(mk -> id == mk.getId())
                .findAny();
        if (optionalMark.isPresent()) {
            log.info("Удаление оценки из репозитория");
            markMap.remove(id);
            return optionalMark;
        }
        log.error("Оценка не найдена, удаления не произошло");
        return Optional.empty();
    }
}