package com.busymango.busymangoBackend.AI.service;

import com.busymango.busymangoBackend.AI.model.dto.AISessionRequest;
import com.busymango.busymangoBackend.AI.model.dto.AISessionTitleModifyDTO;
import com.busymango.busymangoBackend.AI.model.dto.SessionWithQuestionsAndAnswers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.busymango.busymangoBackend.AI.model.entity.AiSession;

import java.util.List;

/**
 * @author 95788
 * @description 针对表【ai_session】的数据库操作Service
 * @createDate 2024-11-01 00:20:41
 */
public interface AiSessionService extends IService<AiSession> {
    Long saveSession(AISessionRequest aiSessionRequest);

    List<AiSession> listSessions();

    List<SessionWithQuestionsAndAnswers> getSessionWithQuestionsAndAnswers(Long sessionId);

    boolean modifySessionTitle(AISessionTitleModifyDTO aiSessionTitleModifyDTO);
}
