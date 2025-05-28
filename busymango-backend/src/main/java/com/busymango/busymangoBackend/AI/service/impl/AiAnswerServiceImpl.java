package com.busymango.busymangoBackend.AI.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.AI.model.entity.AiAnswer;
import com.busymango.busymangoBackend.AI.service.AiAnswerService;
import com.busymango.busymangoBackend.AI.mapper.AiAnswerMapper;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 95788
 * @description 针对表【ai_answer】的数据库操作Service实现
 * @createDate 2024-10-30 23:13:52
 */
@Service
public class AiAnswerServiceImpl extends ServiceImpl<AiAnswerMapper, AiAnswer>
        implements AiAnswerService {
    @Resource
    private AiAnswerMapper aiAnswerMapper;

    @Override
    public Long saveAnswer(Long questionId, String answer,UserDTO userDTO) {

        AiAnswer aiAnswer = new AiAnswer();
        aiAnswer.setAnswerText(answer);
        aiAnswer.setQuestionId(questionId);
        aiAnswer.setInsertUser(userDTO.getUserId());
        aiAnswer.setInsertTime(new Date());
        aiAnswerMapper.insert(aiAnswer);
        Long answerId = aiAnswer.getId();
        return answerId;
    }
}




