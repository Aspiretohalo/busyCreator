package com.busymango.busymangoBackend.AI.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.busymango.busymangoBackend.AI.model.entity.AiAnswer;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;

/**
* @author 95788
* @description 针对表【ai_answer】的数据库操作Service
* @createDate 2024-10-30 23:13:52
*/
public interface AiAnswerService extends IService<AiAnswer> {
    Long saveAnswer(Long questionId, String answer, UserDTO userDTO);
}
