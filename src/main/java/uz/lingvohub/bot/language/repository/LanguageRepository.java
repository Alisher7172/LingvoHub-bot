package uz.lingvohub.bot.language.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.lingvohub.bot.language.entity.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
