package com.busymango.busymangoBackend.project.service;

import com.busymango.busymangoBackend.project.model.dto.ProjectThumbCountDTO;
import com.busymango.busymangoBackend.project.model.dto.ProjectThumbRequest;
import com.busymango.busymangoBackend.project.model.vo.ProjectThumbStatusVO;
import com.busymango.busymangoBackend.project.model.entity.ProjectThumb;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Admin
* @description 针对表【bcmcreate_project_thumb(项目点赞收藏表)】的数据库操作Service
* @createDate 2025-03-18 22:40:19
*/
public interface ProjectThumbService extends IService<ProjectThumb> {

    ProjectThumbStatusVO getThumbStatus(Long projectId);

    Long handleThumbAndCollect(ProjectThumbRequest projectThumbRequest);

    ProjectThumbCountDTO getThumbCount(Long id);

}
