package com.busymango.busymangoBackend.AI.service;

import com.busymango.busymangoBackend.AI.model.entity.AiReasoning;
import com.baomidou.mybatisplus.extension.service.IService;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;

/**
* @author 95788
* @description 针对表【bcmcreate_ai_reasoning(AI回复思维链表)】的数据库操作Service
* @createDate 2025-03-27 13:57:38
*/
public interface AiReasoningService extends IService<AiReasoning> {

    Long saveReasoning(Long questionId, String Reasoning, UserDTO userDTO);
}
