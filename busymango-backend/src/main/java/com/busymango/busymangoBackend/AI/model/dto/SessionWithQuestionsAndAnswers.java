package com.busymango.busymangoBackend.AI.model.dto;

import com.busymango.busymangoBackend.AI.model.entity.AiAnswer;
import com.busymango.busymangoBackend.AI.model.entity.AiQuestion;
import com.busymango.busymangoBackend.AI.model.entity.AiSession;
import lombok.Data;

import java.util.List;

@Data
public class SessionWithQuestionsAndAnswers {
    private AiSession session;
    private AiQuestion question;
    private List<AiAnswer> answers;

    public SessionWithQuestionsAndAnswers(AiSession session, AiQuestion question, List<AiAnswer> answers) {
        this.answers = answers;
        this.question = question;
        this.session = session;
    }
}
