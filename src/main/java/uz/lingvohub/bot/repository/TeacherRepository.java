package uz.lingvohub.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.lingvohub.bot.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
