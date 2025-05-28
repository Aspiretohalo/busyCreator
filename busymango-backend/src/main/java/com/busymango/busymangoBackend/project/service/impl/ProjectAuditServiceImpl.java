package com.busymango.busymangoBackend.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.project.model.dto.ProjectAuditDTO;
import com.busymango.busymangoBackend.project.model.dto.ProjectAuditUpdateDTO;
import com.busymango.busymangoBackend.project.model.entity.ProjectAudit;
import com.busymango.busymangoBackend.project.model.enums.ProjectAuditStatusEnum;
import com.busymango.busymangoBackend.project.service.ProjectAuditService;
import com.busymango.busymangoBackend.project.mapper.ProjectAuditMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 95788
 * @description 针对表【bcmcreate_project_audit(项目审核表)】的数据库操作Service实现
 * @createDate 2025-03-20 14:02:51
 */
@Service
public class ProjectAuditServiceImpl extends ServiceImpl<ProjectAuditMapper, ProjectAudit>
        implements ProjectAuditService {

    @Resource
    private ProjectAuditMapper projectAuditMapper;

    /**
     * 创建审核记录
     *
     * @param projectAuditDTO
     * @return
     */
    @Override
    public Long createAuditRecord(ProjectAuditDTO projectAuditDTO) {
        ProjectAudit projectAudit = new ProjectAudit();
        projectAudit.setProjectId(projectAuditDTO.getProjectId());
        projectAudit.setProjectRecordId(projectAuditDTO.getProjectRecordId());
        projectAudit.setStatus(ProjectAuditStatusEnum.AUDITING.getStatus());
        return projectAuditMapper.createAuditRecord(projectAudit);
    }

    /**
     * 更改审核状态
     *
     * @param projectAuditUpdateDTO
     * @return
     */
    @Override
    public Long updateAuditRecord(ProjectAuditUpdateDTO projectAuditUpdateDTO) {
        return projectAuditMapper.updateAuditRecord(projectAuditUpdateDTO);
    }
}




