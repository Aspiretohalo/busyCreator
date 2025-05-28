package com.busymango.busymangoBackend.project.service;

import com.busymango.busymangoBackend.project.model.dto.ProjectRecordSaveRequest;
import com.busymango.busymangoBackend.project.model.dto.ProjectRecordSubmitRequest;
import com.busymango.busymangoBackend.project.model.entity.ProjectRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.busymango.busymangoBackend.project.model.vo.ProjectRecordVO;

import java.util.List;

/**
* @author 95788
* @description 针对表【bcmcreate_project_record(项目内容及更新记录表)】的数据库操作Service
* @createDate 2025-02-10 18:46:25
*/
public interface ProjectRecordService extends IService<ProjectRecord> {

    ProjectRecordVO getProjectRecordById(Long version);

    List<ProjectRecordVO> listProjectRecord(Long projectId);
    List<ProjectRecordVO> listProjectRecordFormal(Long projectId);
    Long createProjectRecord(ProjectRecordSaveRequest projectRecordSaveRequest);

    Long saveProjectRecord(ProjectRecordSaveRequest projectRecordSaveRequest);

    Long submitProjectRecord(ProjectRecordSubmitRequest projectRecordSubmitRequest);

}
