package com.busymango.busymangoBackend.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.project.mapper.ProjectMapper;
import com.busymango.busymangoBackend.project.model.dto.ProjectRecordDTO;
import com.busymango.busymangoBackend.project.model.dto.ProjectRecordSaveRequest;
import com.busymango.busymangoBackend.project.model.dto.ProjectRecordSubmitDTO;
import com.busymango.busymangoBackend.project.model.dto.ProjectRecordSubmitRequest;
import com.busymango.busymangoBackend.project.model.entity.ProjectRecord;
import com.busymango.busymangoBackend.project.model.vo.ProjectRecordVO;
import com.busymango.busymangoBackend.project.service.ProjectRecordService;
import com.busymango.busymangoBackend.project.mapper.ProjectRecordMapper;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 95788
 * @description 针对表【bcmcreate_project_record(项目内容及更新记录表)】的数据库操作Service实现
 * @createDate 2025-02-10 18:46:25
 */
@Service
public class ProjectRecordServiceImpl extends ServiceImpl<ProjectRecordMapper, ProjectRecord>
        implements ProjectRecordService {
    @Resource
    private ProjectRecordMapper projectRecordMapper;
    @Resource
    private ProjectMapper projectMapper;

    @Override
    public ProjectRecordVO getProjectRecordById(Long version) {
        return projectRecordMapper.getProjectRecordById(version);
    }

    @Override
    public List<ProjectRecordVO> listProjectRecord(Long projectId) {
        UserDTO userDTO = UserContext.getUserDTO();
        return projectRecordMapper.listProjectRecord(projectId, userDTO.getUserId());
    }

    @Override
    public List<ProjectRecordVO> listProjectRecordFormal(Long projectId) {
        return projectRecordMapper.listProjectRecordFormal(projectId);
    }

    @Override
    public Long createProjectRecord(ProjectRecordSaveRequest projectRecordSaveRequest) {
        UserDTO userDTO = UserContext.getUserDTO();

        ProjectRecordDTO projectRecordDTO = new ProjectRecordDTO();
        projectRecordDTO.setProjectId(projectRecordSaveRequest.getProjectId());
        projectRecordDTO.setContent(projectRecordSaveRequest.getContent());
        projectRecordDTO.setUserId(userDTO.getUserId());

        return projectRecordMapper.createProjectRecord(projectRecordDTO);
    }

    @Override
    public Long saveProjectRecord(ProjectRecordSaveRequest projectRecordSaveRequest) {
        ProjectRecordDTO projectRecordDTO = new ProjectRecordDTO();

        projectRecordDTO.setId(projectRecordSaveRequest.getId());
        projectRecordDTO.setContent(projectRecordSaveRequest.getContent());

        return projectRecordMapper.saveProjectRecord(projectRecordDTO);
    }

    @Override
    public Long submitProjectRecord(ProjectRecordSubmitRequest projectRecordSubmitRequest) {
        Long projectRecordId = projectRecordSubmitRequest.getId();

        ProjectRecordSubmitDTO projectRecordSubmitDTO = new ProjectRecordSubmitDTO();
        projectRecordSubmitDTO.setId(projectRecordSubmitRequest.getId());
        projectRecordSubmitDTO.setRecordComment(projectRecordSubmitRequest.getRecordComment());
        Long version = projectRecordMapper.submitProjectRecord(projectRecordSubmitDTO);
        ProjectRecordVO projectRecordById = projectRecordMapper.getProjectRecordById(projectRecordId);
        projectMapper.updateVersion(projectRecordId, projectRecordById.getProjectId());
        return version;
    }
}




