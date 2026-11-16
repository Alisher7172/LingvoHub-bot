package uz.lingvohub.bot.progress.service;

import org.junit.jupiter.api.Test;
import uz.lingvohub.bot.lesson.entity.Lesson;
import uz.lingvohub.bot.progress.entity.Progress;
import uz.lingvohub.bot.progress.repository.ProgressRepository;
import uz.lingvohub.bot.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.lang.reflect.Proxy;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgressServiceTest {

    @Test
    void markCompletedUpdatesExistingProgress() {
        User user = new User();
        user.setId(10L);
        Lesson lesson = new Lesson();
        lesson.setId(20L);
        Progress existing = new Progress();
        existing.setCompleted(false);
        Progress[] saved = new Progress[1];
        ProgressRepository progressRepository = repository((method, arguments) -> switch (method) {
            case "findByUserIdAndLessonId" -> Optional.of(existing);
            case "save" -> {
                saved[0] = (Progress) arguments[0];
                yield saved[0];
            }
            default -> throw new UnsupportedOperationException(method);
        });

        new ProgressService(progressRepository).markCompleted(user, lesson);

        assertEquals(10L, existing.getUserId());
        assertEquals(20L, existing.getLessonId());
        assertTrue(existing.isCompleted());
        assertEquals(existing, saved[0]);
    }

    @Test
    void completedLessonIdsReturnsIdsFromCompletedProgress() {
        User user = new User();
        user.setId(10L);
        Progress first = new Progress();
        first.setLessonId(20L);
        Progress second = new Progress();
        second.setLessonId(21L);
        ProgressRepository progressRepository = repository((method, arguments) -> switch (method) {
            case "findByUserIdAndCompletedTrue" -> List.of(first, second);
            default -> throw new UnsupportedOperationException(method);
        });

        assertEquals(Set.of(20L, 21L), new ProgressService(progressRepository).completedLessonIds(user));
    }

    private ProgressRepository repository(BiFunction<String, Object[], Object> behavior) {
        return (ProgressRepository) Proxy.newProxyInstance(
                ProgressRepository.class.getClassLoader(),
                new Class<?>[]{ProgressRepository.class},
                (proxy, method, arguments) -> behavior.apply(method.getName(), arguments)
        );
    }
}
