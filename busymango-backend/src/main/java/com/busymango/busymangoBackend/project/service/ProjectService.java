package com.busymango.busymangoBackend.project.service;

import com.busymango.busymangoBackend.project.model.dto.ProjectCreateRequest;
import com.busymango.busymangoBackend.project.model.dto.ProjectRecordSaveRequest;
import com.busymango.busymangoBackend.project.model.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;
import com.busymango.busymangoBackend.project.model.vo.ProjectRecordVO;
import com.busymango.busymangoBackend.project.model.vo.ProjectThumbStatusVO;
import com.busymango.busymangoBackend.project.model.vo.ProjectVO;
import com.busymango.busymangoBackend.team.model.vo.TeamVO;
import com.busymango.busymangoBackend.user.model.vo.UserProjectCountVO;

import java.util.List;

/**
 * @author 95788
 * @description 针对表【bcmcreate_project(项目（作品）信息表)】的数据库操作Service
 * @createDate 2025-02-05 02:00:42
 */
public interface ProjectService extends IService<Project> {

    UserProjectCountVO getCount();


    Long saveProject(ProjectCreateRequest projectCreateRequest);

    List<ProjectVO> listWorks();
    List<ProjectVO> listWorksLogout();

    List<ProjectVO> listItems();

    ProjectVO getItemById(Long id);

    List<ProjectVO> listItemsByTeamId(Long teamId);

    List<ProjectVO> searchItems(String keyword);

    ProjectRecordVO getProjectVersion(Long id);

    Long saveProjectRecord(ProjectRecordSaveRequest projectRecordSaveRequest);

    void setProjectVersion(Long projectId, Long version);

    List<ProjectVO> listProjectsByTeamIds(List<Long> teamIds);

    String handlePublish(Long projectId, Long projectRecordId);

}
