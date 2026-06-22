package uz.lingvohub.bot.state;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSession {

    private AdminState state = AdminState.NONE;
    private Long selectedLanguageId;
    private Long selectedTeacherId;
    private Long selectedCourseId;
    private Long selectedLessonId;
    private String pendingTeacherName;
    private String pendingTeacherBio;
    private String pendingCourseTitle;
    private String pendingLessonTitle;
    private Integer pendingLessonOrder;
    private String pendingLessonChannelId;
    private Integer pendingLessonMessageId;
    private boolean pendingLessonPremium;
}
