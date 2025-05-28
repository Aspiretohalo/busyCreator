package com.busymango.busymangoBackend.AI.controller;

import com.busymango.busymangoBackend.AI.model.dto.AIQuestionRequest;
import com.busymango.busymangoBackend.AI.model.dto.AISessionRequest;
import com.busymango.busymangoBackend.AI.model.dto.IdeaGenerateRequest;
import com.busymango.busymangoBackend.AI.model.dto.SessionWithQuestionsAndAnswers;
import com.busymango.busymangoBackend.AI.model.entity.AiSession;
import com.busymango.busymangoBackend.AI.service.*;
import com.busymango.busymangoBackend.core.common.BaseResponse;
import com.busymango.busymangoBackend.core.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ai")
public class AIChatController {
    @Resource
    private AIChatService aiChatService;
    @Resource
    private AiSessionService aiSessionService;
    @Resource
    private AiParseService aiParseService;

    /**
     * 创建新的ai聊天会话
     *
     * @param aiSessionRequest
     * @return
     */
    @PostMapping("/create/session")
    public BaseResponse<Long> createSession(@RequestBody AISessionRequest aiSessionRequest) {
        Long sessionId = aiSessionService.saveSession(aiSessionRequest);
        log.info("AI会话存储情况：" + sessionId);
        return ResultUtils.success(sessionId);
    }

    /**
     * 向AI模型提问，模型给出回答，问题和答复分别存储
     *
     * @param aiQuestionRequest
     * @return
     */
    @PostMapping("/ask")
    public SseEmitter ask(@RequestBody AIQuestionRequest aiQuestionRequest) {
        return aiChatService.getAnswer(aiQuestionRequest);
    }

    /**
     * 根据 id 获取包装类
     *
     * @return
     */
    @GetMapping("/get/sessions")
    public BaseResponse<List<AiSession>> getSessions() {
        List<AiSession> sessions = aiSessionService.listSessions();
        return ResultUtils.success(sessions);
    }

    @GetMapping("/get/QandA")
    public BaseResponse<List<SessionWithQuestionsAndAnswers>> getQandA(long sessionId) {
        List<SessionWithQuestionsAndAnswers> sessionWithQandA = aiSessionService.getSessionWithQuestionsAndAnswers(sessionId);
        return ResultUtils.success(sessionWithQandA);
    }

    /**
     * 根据创意生成内容，需要传入具体 bcmcreate_project_record 的 id，修改其 content
     *
     * @param ideaGenerateRequest
     * @return
     */
    @PostMapping("/idea/create")
    public SseEmitter generateIdea(@RequestBody IdeaGenerateRequest ideaGenerateRequest) {
        return aiParseService.generateIdea(ideaGenerateRequest);
    }
}
