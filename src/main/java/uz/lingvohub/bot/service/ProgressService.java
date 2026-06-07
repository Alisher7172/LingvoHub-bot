package uz.lingvohub.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.lingvohub.bot.entity.Lesson;
import uz.lingvohub.bot.entity.Progress;
import uz.lingvohub.bot.entity.User;
import uz.lingvohub.bot.repository.ProgressRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;

    public void markCompleted(User user, Lesson lesson) {
        Progress progress = progressRepository.findByUserIdAndLessonId(user.getId(), lesson.getId())
                .orElseGet(Progress::new);
        progress.setUserId(user.getId());
        progress.setLessonId(lesson.getId());
        progress.setCompleted(true);
        progressRepository.save(progress);
    }

    public Set<Long> completedLessonIds(User user) {
        List<Progress> completed = progressRepository.findByUserIdAndCompletedTrue(user.getId());
        return completed.stream().map(Progress::getLessonId).collect(Collectors.toSet());
    }
}
