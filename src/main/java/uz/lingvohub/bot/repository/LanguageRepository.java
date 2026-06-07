package uz.lingvohub.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.lingvohub.bot.entity.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
