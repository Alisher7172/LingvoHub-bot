package uz.lingvohub.bot.telegram.state;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    private final Map<Long, UserSession> userSessions = new ConcurrentHashMap<>();
    private final Map<Long, AdminSession> adminSessions = new ConcurrentHashMap<>();

    public UserSession userSession(Long telegramId) {
        return userSessions.computeIfAbsent(telegramId, k -> new UserSession());
    }

    public AdminSession adminSession(Long telegramId) {
        return adminSessions.computeIfAbsent(telegramId, k -> new AdminSession());
    }
}
