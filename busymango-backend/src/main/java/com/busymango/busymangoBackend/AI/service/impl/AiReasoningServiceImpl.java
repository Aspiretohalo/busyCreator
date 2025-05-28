package com.busymango.busymangoBackend.AI.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.AI.mapper.AiReasoningMapper;
import com.busymango.busymangoBackend.AI.model.entity.AiReasoning;
import com.busymango.busymangoBackend.AI.service.AiReasoningService;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
* @author 95788
* @description 针对表【bcmcreate_ai_reasoning(AI回复思维链表)】的数据库操作Service实现
* @createDate 2025-03-27 13:57:38
*/
@Service
public class AiReasoningServiceImpl extends ServiceImpl<AiReasoningMapper, AiReasoning>
    implements AiReasoningService{
    @Resource
    private AiReasoningMapper aiReasoningMapper;

    @Override
    public Long saveReasoning(Long questionId, String Reasoning, UserDTO userDTO) {

        AiReasoning aiReasoning = new AiReasoning();
        aiReasoning.setReasoningContent(Reasoning);
        aiReasoning.setQuestionId(questionId);
        aiReasoning.setInsertUser(userDTO.getUserId());
        aiReasoning.setInsertTime(new Date());
        aiReasoningMapper.insert(aiReasoning);
        Long ReasoningId = aiReasoning.getId();
        return ReasoningId;
    }
}




