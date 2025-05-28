package com.busymango.busymangoBackend.AI.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.busymango.busymangoBackend.AI.model.dto.AISessionTitleModifyDTO;
import com.busymango.busymangoBackend.AI.model.entity.AiSession;
import com.busymango.busymangoBackend.user.model.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author 95788
 * @description 针对表【ai_session】的数据库操作Mapper
 * @createDate 2024-11-01 00:20:41
 * @Entity com.busymango.busymangoBackend.AI.model.entity.AiSession
 */
public interface AiSessionMapper extends BaseMapper<AiSession> {
    AiSession getById(@Param("id") Long id);

    Long getCount(Long userId);

    boolean modifyTitle(AISessionTitleModifyDTO aiSessionTitleModifyDTO);
}




