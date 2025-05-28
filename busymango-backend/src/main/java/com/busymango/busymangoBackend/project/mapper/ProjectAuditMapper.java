package com.busymango.busymangoBackend.project.mapper;

import com.busymango.busymangoBackend.project.model.dto.ProjectAuditUpdateDTO;
import com.busymango.busymangoBackend.project.model.entity.ProjectAudit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 95788
* @description 针对表【bcmcreate_project_audit(项目审核表)】的数据库操作Mapper
* @createDate 2025-03-20 14:02:50
* @Entity com.busymango.busymangoBackend.project.model.entity.ProjectAudit
*/
public interface ProjectAuditMapper extends BaseMapper<ProjectAudit> {

    Long createAuditRecord(ProjectAudit projectAudit);

    Long updateAuditRecord(ProjectAuditUpdateDTO projectAuditUpdateDTO);
}




