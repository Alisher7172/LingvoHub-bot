package uz.lingvohub.bot.state;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSession {

    private Long selectedLanguageId;
    private Long selectedTeacherId;
    private Long selectedCourseId;
}
