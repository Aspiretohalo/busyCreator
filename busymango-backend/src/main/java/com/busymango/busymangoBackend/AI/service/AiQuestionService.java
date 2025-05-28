package com.busymango.busymangoBackend.AI.service;

import com.busymango.busymangoBackend.AI.model.dto.AIQuestionRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.busymango.busymangoBackend.AI.model.entity.AiQuestion;

/**
 * @author 95788
 * @description 针对表【ai_question】的数据库操作Service
 * @createDate 2024-10-30 23:13:52
 */
public interface AiQuestionService extends IService<AiQuestion> {
    Long saveQuestion(AIQuestionRequest aiQuestionRequest);
}
