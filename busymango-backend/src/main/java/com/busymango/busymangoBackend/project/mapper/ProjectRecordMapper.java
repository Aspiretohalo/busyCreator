package com.busymango.busymangoBackend.project.mapper;

import com.busymango.busymangoBackend.project.model.dto.ProjectRecordDTO;
import com.busymango.busymangoBackend.project.model.dto.ProjectRecordSubmitDTO;
import com.busymango.busymangoBackend.project.model.entity.ProjectRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.busymango.busymangoBackend.project.model.vo.ProjectRecordVO;

import java.util.List;

/**
* @author 95788
* @description 针对表【bcmcreate_project_record(项目内容及更新记录表)】的数据库操作Mapper
* @createDate 2025-02-10 18:46:24
* @Entity com.busymango.busymangoBackend.project.model.entity.ProjectRecord
*/
public interface ProjectRecordMapper extends BaseMapper<ProjectRecord> {

    ProjectRecordVO getProjectRecordById(Long version);

    List<ProjectRecordVO> listProjectRecord(Long projectId,Long userId);
    List<ProjectRecordVO> listProjectRecordFormal(Long projectId);
    Long createProjectRecord(ProjectRecordDTO projectRecordDTO);

    Long saveProjectRecord(ProjectRecordDTO projectRecordDTO);

    Long submitProjectRecord(ProjectRecordSubmitDTO projectRecordSubmitDTO);

}




