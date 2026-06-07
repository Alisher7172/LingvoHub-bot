package uz.lingvohub.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.lingvohub.bot.entity.User;
import uz.lingvohub.bot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getOrCreate(Long telegramId, String username) {
        Optional<User> existing = userRepository.findByTelegramId(telegramId);
        if (existing.isPresent()) {
            User user = existing.get();
            user.setUsername(username);
            return userRepository.save(user);
        }

        User user = new User();
        user.setTelegramId(telegramId);
        user.setUsername(username);
        user.setPremiumActive(false);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Optional<User> findByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public long count() {
        return userRepository.count();
    }
}
