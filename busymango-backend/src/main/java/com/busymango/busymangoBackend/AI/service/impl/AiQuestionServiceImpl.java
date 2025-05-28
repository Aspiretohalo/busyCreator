package com.busymango.busymangoBackend.AI.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.AI.model.dto.AIQuestionRequest;
import com.busymango.busymangoBackend.AI.model.dto.AISessionTitleModifyDTO;
import com.busymango.busymangoBackend.AI.model.entity.AiQuestion;
import com.busymango.busymangoBackend.AI.service.AiQuestionService;
import com.busymango.busymangoBackend.AI.mapper.AiQuestionMapper;
import com.busymango.busymangoBackend.AI.service.AiSessionService;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 95788
 * @description 针对表【ai_question】的数据库操作Service实现
 * @createDate 2024-10-30 23:13:52
 */
@Service
public class AiQuestionServiceImpl extends ServiceImpl<AiQuestionMapper, AiQuestion>
        implements AiQuestionService {

    @Resource
    private AiQuestionMapper aiQuestionMapper;
    @Resource
    private AiSessionService aiSessionService;

    /**
     * 保存问题请求
     * 若该问题为所属会话的第一个问题，则修改会话标题
     *
     * @param aiQuestionRequest
     * @return
     */
    @Override
    public Long saveQuestion(AIQuestionRequest aiQuestionRequest) {
        UserDTO userDTO = UserContext.getUserDTO();

        AiQuestion aiQuestion = new AiQuestion();
        aiQuestion.setSessionId(aiQuestionRequest.getSessionId());
        aiQuestion.setQuestionText(aiQuestionRequest.getQuestion());
        aiQuestion.setInsertUser(userDTO.getUserId());
        aiQuestion.setInsertTime(new Date());
        aiQuestionMapper.insert(aiQuestion);
        Long questionId = aiQuestion.getId();

        /**
         * 修改会话标题逻辑
         */
        Long countBySessionId = aiQuestionMapper.getCountBySessionId(aiQuestionRequest.getSessionId());
        if (countBySessionId == 1) {
            // 创建 DTO 对象
            AISessionTitleModifyDTO aiSessionTitleModifyDTO = new AISessionTitleModifyDTO();
            aiSessionTitleModifyDTO.setSessionId(aiQuestionRequest.getSessionId());

            // 处理 setQuestionText
            String setQuestionText = aiQuestionRequest.getQuestion(); // 假设这是原始文本
            String modifiedText;

            if (setQuestionText.length() > 20) {
                // 如果长度大于 20 个字符，截取前 10 个字符并加上省略号
                modifiedText = setQuestionText.substring(0, 20) + "...";
            } else {
                // 如果不足 20 个字符，直接使用原字符串
                modifiedText = setQuestionText;
            }

            // 设置处理后的标题
            aiSessionTitleModifyDTO.setSessionTitle(modifiedText);

            // 调用服务修改标题
            aiSessionService.modifySessionTitle(aiSessionTitleModifyDTO);
        }
        return questionId;
    }
}




