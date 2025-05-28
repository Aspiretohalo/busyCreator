package com.busymango.busymangoBackend.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.project.model.vo.ProjectVO;
import com.busymango.busymangoBackend.project.service.ProjectService;
import com.busymango.busymangoBackend.task.model.dto.TaskCreateRequest;
import com.busymango.busymangoBackend.task.model.dto.TaskDTO;
import com.busymango.busymangoBackend.task.model.dto.TaskUpdateDTO;
import com.busymango.busymangoBackend.task.model.dto.TaskUpdateRequest;
import com.busymango.busymangoBackend.task.model.entity.Task;
import com.busymango.busymangoBackend.task.model.vo.TaskVO;
import com.busymango.busymangoBackend.task.service.TaskService;
import com.busymango.busymangoBackend.task.mapper.TaskMapper;
import com.busymango.busymangoBackend.team.model.vo.TeamMemberVO;
import com.busymango.busymangoBackend.team.model.vo.TeamVO;
import com.busymango.busymangoBackend.team.service.TeamService;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import com.busymango.busymangoBackend.user.model.entity.User;
import com.busymango.busymangoBackend.user.model.vo.UserVO;
import com.busymango.busymangoBackend.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Admin
 * @description 针对表【bcmcreate_task(任务表)】的数据库操作Service实现
 * @createDate 2025-03-10 23:02:47
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
        implements TaskService {
    @Resource
    private ProjectService projectService;
    @Resource
    private UserService userService;
    @Resource
    private TeamService teamService;
    @Resource
    private TaskMapper taskMapper;

    @Override
    public Long createTask(TaskCreateRequest taskCreateRequest) {
        ProjectVO projectVO = projectService.getItemById(taskCreateRequest.getProjectId());
        UserVO userVO = userService.getUsersById(taskCreateRequest.getAssigneeId());
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setProjectId(taskCreateRequest.getProjectId());
        taskDTO.setProjectName(projectVO.getName());
        taskDTO.setContent(taskCreateRequest.getContent());
        taskDTO.setAssigneeId(taskCreateRequest.getAssigneeId());
        taskDTO.setAssigneeName(userVO.getUserName());
        taskDTO.setFinishDate(taskCreateRequest.getFinishDate());

        UserDTO userDTO = UserContext.getUserDTO();
        taskDTO.setInsertUser(userDTO.getUserId());
        return taskMapper.createTask(taskDTO);
    }

    @Override
    public Long updateTask(TaskUpdateRequest taskUpdateRequest) {
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setId(taskUpdateRequest.getTaskId());
        taskUpdateDTO.setWorkTime(taskUpdateRequest.getWorkTime());
        taskUpdateDTO.setNote(taskUpdateRequest.getNote());

        return taskMapper.updateTask(taskUpdateDTO);
    }

    /**
     * 获得当前用户管理的所有项目
     *
     * @return
     */
    @Override
    public List<ProjectVO> listAdminProject() {
        List<TeamVO> teamVOS = teamService.listAdminTeams();
        List<Long> teamIds = teamVOS.stream()       // 将 List<TeamVO> 转换为 Stream
                .map(TeamVO::getId)                     // 提取每个 TeamVO 的 id 属性
                .collect(Collectors.toList());          // 收集结果为一个新的 List<Long>
        return projectService.listProjectsByTeamIds(teamIds);
    }

    @Override
    public List<TeamMemberVO> listTeamMember(Long project_id) {
        // 拿到包含团队信息的项目VO
        ProjectVO projectVO = projectService.getItemById(project_id);
        String memberListStr = projectVO.getTeamInfo().getMemberList();
        List<Long> memberList =
                memberListStr == null || memberListStr.isEmpty()
                        ? Collections.emptyList()
                        : Pattern.compile(",")
                        .splitAsStream(memberListStr)
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
        // 根据member列表来拿到团队成员数据
        List<User> users = userService.listUsersByIds(memberList);

        List<TeamMemberVO> teamMemberVOS = users.stream().map(user -> {
            TeamMemberVO vo = new TeamMemberVO();
            vo.setId(user.getId());
            vo.setUserName(user.getUserName());
            vo.setUserAvatar(user.getUserAvatar());
            vo.setEmail(user.getEmail());
            // 如果需要设置成员权限，可以根据逻辑添加
            vo.setTeamRole("defaultRole"); // 示例值，根据实际逻辑设置
            return vo;
        }).collect(Collectors.toList());

        return teamMemberVOS;
    }

    /**
     * 获取当前用户下发和受到的任务
     * 区分下发和受到两种状态
     * 自己发给自己的算收到
     *
     * @return
     */
    @Override
    public List<TaskVO> listTask() {
        UserDTO userDTO = UserContext.getUserDTO();
        List<TaskVO> adminTaskList = taskMapper.listAdminTask(userDTO.getUserId());
        List<TaskVO> assigneeTaskList = taskMapper.listAssigneeTask(userDTO.getUserId());

        // 使用 Stream API 设置 status 并合并列表
        List<TaskVO> combinedTaskList = Stream.concat(
                adminTaskList.stream().peek(task -> task.setStatus("0")),
                assigneeTaskList.stream().peek(task -> task.setStatus("1"))
        ).collect(Collectors.toList());

        return combinedTaskList;
    }
}




