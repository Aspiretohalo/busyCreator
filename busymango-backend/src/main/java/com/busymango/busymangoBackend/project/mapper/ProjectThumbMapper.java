package com.busymango.busymangoBackend.project.mapper;

import com.busymango.busymangoBackend.project.model.dto.ProjectThumbCountDTO;
import com.busymango.busymangoBackend.project.model.dto.ProjectThumbDTO;
import com.busymango.busymangoBackend.project.model.entity.ProjectThumb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.busymango.busymangoBackend.project.model.vo.ProjectThumbStatusVO;

/**
 * @author Admin
 * @description 针对表【bcmcreate_project_thumb(项目点赞收藏表)】的数据库操作Mapper
 * @createDate 2025-03-18 22:40:19
 * @Entity com.busymango.busymangoBackend.project.model.entity.ProjectThumb
 */
public interface ProjectThumbMapper extends BaseMapper<ProjectThumb> {

    boolean judegExists(ProjectThumbDTO projectThumbDTO);

    void cancelStatus(ProjectThumbDTO projectThumbDTO);

    void saveStatus(ProjectThumb projectThumb);

    ProjectThumbStatusVO getThumbStatus(Long projectId, Long userId);

    ProjectThumbCountDTO getThumbCount(Long id);
}




