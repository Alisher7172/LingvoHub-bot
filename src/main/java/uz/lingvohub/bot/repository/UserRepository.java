package uz.lingvohub.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.lingvohub.bot.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramId(Long telegramId);
}
