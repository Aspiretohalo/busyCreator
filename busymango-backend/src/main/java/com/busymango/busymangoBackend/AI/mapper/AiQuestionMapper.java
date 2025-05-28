package com.busymango.busymangoBackend.AI.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.busymango.busymangoBackend.AI.model.entity.AiQuestion;
import com.busymango.busymangoBackend.AI.model.entity.AiSession;
import org.apache.ibatis.annotations.Param;

/**
 * @author 95788
 * @description 针对表【ai_question】的数据库操作Mapper
 * @createDate 2024-10-30 23:13:52
 * @Entity com.busymango.busymangoBackend.AI.model.entity.AiQuestion
 */
public interface AiQuestionMapper extends BaseMapper<AiQuestion> {

    Long getCountBySessionId(Long sessionId);

}




