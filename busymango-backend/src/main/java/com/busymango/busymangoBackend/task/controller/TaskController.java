package com.busymango.busymangoBackend.task.controller;

import com.busymango.busymangoBackend.core.common.BaseResponse;
import com.busymango.busymangoBackend.core.common.ResultUtils;
import com.busymango.busymangoBackend.project.model.vo.ProjectVO;
import com.busymango.busymangoBackend.task.model.dto.TaskCreateRequest;
import com.busymango.busymangoBackend.task.model.dto.TaskUpdateRequest;
import com.busymango.busymangoBackend.task.model.vo.TaskVO;
import com.busymango.busymangoBackend.task.service.TaskService;
import com.busymango.busymangoBackend.team.model.dto.TeamCreateRequest;
import com.busymango.busymangoBackend.team.model.dto.TeamUpdateRequest;
import com.busymango.busymangoBackend.team.model.vo.TeamMemberVO;
import com.busymango.busymangoBackend.team.model.vo.TeamVO;
import com.busymango.busymangoBackend.team.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {
    @Resource
    private TaskService taskService;

    /**
     * 创建新的任务
     *
     * @param taskCreateRequest
     * @return
     */
    @PostMapping("/create")
    public BaseResponse<Long> createItem(@RequestBody TaskCreateRequest taskCreateRequest) {
        Long id = taskService.createTask(taskCreateRequest);
        log.info("新任务创建情况：" + id);
        return ResultUtils.success(id);
    }

    /**
     * 保存任务填写情况
     *
     * @param taskUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Long> updateTask(@RequestBody TaskUpdateRequest taskUpdateRequest) {
        Long id = taskService.updateTask(taskUpdateRequest);
        log.info("任务填写保存情况：" + id);
        return ResultUtils.success(id);
    }

    /**
     * 获取当前用户作为管理员的团队，其所持有的项目id和名称
     *
     * @return
     */
    @GetMapping("/team/project/list")
    public BaseResponse<List<ProjectVO>> listAdminProject() {
        List<ProjectVO> items = taskService.listAdminProject();
        return ResultUtils.success(items);
    }


    /**
     * 获取当前项目对应团队的成员列表
     *
     * @return
     */
    @GetMapping("/team/member/list")
    public BaseResponse<List<TeamMemberVO>> listTeamMember(@RequestParam("project_id") Long project_id) {
        List<TeamMemberVO> items = taskService.listTeamMember(project_id);
        return ResultUtils.success(items);
    }

    /**
     * 获取当前用户下发和受到的任务
     * 区分下发和受到两种状态
     * 自己发给自己的算收到
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<TaskVO>> listTask() {
        List<TaskVO> items = taskService.listTask();
        return ResultUtils.success(items);
    }

}
