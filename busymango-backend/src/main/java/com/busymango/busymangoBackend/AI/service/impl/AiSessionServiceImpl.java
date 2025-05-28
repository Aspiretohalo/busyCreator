package com.busymango.busymangoBackend.AI.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.AI.mapper.AiAnswerMapper;
import com.busymango.busymangoBackend.AI.mapper.AiQuestionMapper;
import com.busymango.busymangoBackend.AI.model.dto.AISessionRequest;
import com.busymango.busymangoBackend.AI.model.dto.AISessionTitleModifyDTO;
import com.busymango.busymangoBackend.AI.model.dto.SessionWithQuestionsAndAnswers;
import com.busymango.busymangoBackend.AI.model.entity.AiAnswer;
import com.busymango.busymangoBackend.AI.model.entity.AiQuestion;
import com.busymango.busymangoBackend.AI.model.entity.AiSession;
import com.busymango.busymangoBackend.AI.service.AiSessionService;
import com.busymango.busymangoBackend.AI.mapper.AiSessionMapper;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author 95788
 * @description 针对表【ai_session】的数据库操作Service实现
 * @createDate 2024-11-01 00:20:41
 */
@Service
@Slf4j
public class AiSessionServiceImpl extends ServiceImpl<AiSessionMapper, AiSession>
        implements AiSessionService {
    @Resource
    private AiSessionMapper aiSessionMapper;
    @Resource
    private AiQuestionMapper aiQuestionMapper;
    @Resource
    private AiAnswerMapper aiAnswerMapper;

    @Override
    public Long saveSession(AISessionRequest aiSessionRequest) {
        UserDTO userDTO = UserContext.getUserDTO();
        AiSession aiSession = new AiSession();
        Long count = aiSessionMapper.getCount(userDTO.getUserId());
        if (count == 0) {
            aiSession.setTitle("新创建的一个会话");
        } else {
            aiSession.setTitle("新创建的一个会话（" + count + "）");
        }
        aiSession.setUserId(userDTO.getUserId());
        aiSession.setInsertUser(userDTO.getUserId());
        aiSession.setInsertTime(new Date());
        aiSessionMapper.insert(aiSession);
        Long sessionId = aiSession.getId();
        return sessionId;
    }

    public List<AiSession> listSessions() {
        UserDTO userDTO = UserContext.getUserDTO();

        LambdaQueryWrapper<AiSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AiSession::getUserId, userDTO.getUserId())
                .orderByDesc(AiSession::getInsertTime); // 按照 insert_time 倒序排序
        return aiSessionMapper.selectList(queryWrapper);
    }

    public List<SessionWithQuestionsAndAnswers> getSessionWithQuestionsAndAnswers(Long sessionId) {
        UserDTO userDTO = UserContext.getUserDTO();
        log.info(String.valueOf(userDTO));
        // 查询会话
        AiSession session = aiSessionMapper.getById(sessionId);
        if (session == null) return Collections.emptyList();

        // 查询该会话下的所有问题
        QueryWrapper<AiQuestion> questionWrapper = new QueryWrapper<>();
        questionWrapper.eq("session_id", sessionId);
        List<AiQuestion> questions = aiQuestionMapper.selectList(questionWrapper);

        // 查询每个问题下的所有答案
        List<SessionWithQuestionsAndAnswers> result = new ArrayList<>();
        for (AiQuestion question : questions) {
            QueryWrapper<AiAnswer> answerWrapper = new QueryWrapper<>();
            answerWrapper.eq("question_id", question.getId());
            List<AiAnswer> answers = aiAnswerMapper.selectList(answerWrapper);
            result.add(new SessionWithQuestionsAndAnswers(session, question, answers));
        }
        return result;
    }

    /**
     * 修改会话标题
     *
     * @param aiSessionTitleModifyDTO
     * @return
     */
    public boolean modifySessionTitle(AISessionTitleModifyDTO aiSessionTitleModifyDTO) {
        log.info(String.valueOf(aiSessionTitleModifyDTO));
        return aiSessionMapper.modifyTitle(aiSessionTitleModifyDTO);
    }
}




