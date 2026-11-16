package uz.lingvohub.bot.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.lingvohub.bot.teacher.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
