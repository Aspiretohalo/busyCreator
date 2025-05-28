package com.busymango.busymangoBackend.project.service;

import com.busymango.busymangoBackend.project.model.dto.ProjectAuditDTO;
import com.busymango.busymangoBackend.project.model.dto.ProjectAuditUpdateDTO;
import com.busymango.busymangoBackend.project.model.entity.ProjectAudit;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 95788
* @description 针对表【bcmcreate_project_audit(项目审核表)】的数据库操作Service
* @createDate 2025-03-20 14:02:51
*/
public interface ProjectAuditService extends IService<ProjectAudit> {

    Long createAuditRecord(ProjectAuditDTO projectAuditDTO);

    Long updateAuditRecord(ProjectAuditUpdateDTO projectAuditUpdateDTO);
}
