package com.busymango.busymangoBackend.task.service;

import com.busymango.busymangoBackend.project.model.vo.ProjectVO;
import com.busymango.busymangoBackend.task.model.dto.TaskCreateRequest;
import com.busymango.busymangoBackend.task.model.dto.TaskUpdateRequest;
import com.busymango.busymangoBackend.task.model.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import com.busymango.busymangoBackend.task.model.vo.TaskVO;
import com.busymango.busymangoBackend.team.model.vo.TeamMemberVO;

import java.util.List;

/**
* @author Admin
* @description 针对表【bcmcreate_task(任务表)】的数据库操作Service
* @createDate 2025-03-10 23:02:47
*/
public interface TaskService extends IService<Task> {

    Long createTask(TaskCreateRequest taskCreateRequest);

    Long updateTask(TaskUpdateRequest taskUpdateRequest);

    List<ProjectVO> listAdminProject();

    List<TeamMemberVO> listTeamMember(Long project_id);

    List<TaskVO> listTask();
}
