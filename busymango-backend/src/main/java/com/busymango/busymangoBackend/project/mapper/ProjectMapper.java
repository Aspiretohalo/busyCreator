package com.busymango.busymangoBackend.project.mapper;

import com.busymango.busymangoBackend.project.model.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.busymango.busymangoBackend.project.model.vo.ProjectRecordVO;
import com.busymango.busymangoBackend.project.model.vo.ProjectVO;

import java.util.List;

/**
* @author 95788
* @description 针对表【bcmcreate_project(项目（作品）信息表)】的数据库操作Mapper
* @createDate 2025-02-05 02:00:42
* @Entity com.busymango.busymangoBackend.project.model.entity.Project
*/
public interface ProjectMapper extends BaseMapper<Project> {
    Long getCount(List<Long> teamIdList);

    Long getworksCount(List<Long> teamIdList);

    List<ProjectVO> listItems(List<Long> teamIdList);

    ProjectVO getItemById(Long id);

    List<ProjectVO> listWorks();

    List<ProjectVO> listItemsByTeamId(Long teamId);

    Long saveProject(Project project);

    List<ProjectVO> searchItems(String keyword);

    void updateVersion(Long versionId,Long projectId);

    List<ProjectVO> listProjectsByTeamIds(List<Long> teamIds);

    void updatePublicStatus(Long projectId);
}




