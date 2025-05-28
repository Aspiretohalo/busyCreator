package com.busymango.busymangoBackend.team.controller;

import com.busymango.busymangoBackend.core.common.BaseResponse;
import com.busymango.busymangoBackend.core.common.ResultUtils;
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
@RequestMapping("/team")
public class TeamController {
    @Resource
    private TeamService teamService;

    /**
     * 创建新的团队
     *
     * @param teamCreateRequest
     * @return
     */
    @PostMapping("/create")
    public BaseResponse<Long> createItem(@RequestBody TeamCreateRequest teamCreateRequest) {
        Long id = teamService.saveTeam(teamCreateRequest);
        log.info("新项目创建情况：" + id);
        return ResultUtils.success(id);
    }

    /**
     * 修改团队信息
     *
     * @param teamUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Long> updateItem(@RequestBody TeamUpdateRequest teamUpdateRequest) {
        Long id = teamService.updateItem(teamUpdateRequest);
        log.info("新项目创建情况：" + id);
        return ResultUtils.success(id);
    }

    /**
     * 获取当前用户加入的团队列表
     *
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<TeamVO>> listTeams() {
        List<TeamVO> items = teamService.listTeams();
        return ResultUtils.success(items);
    }

    /**
     * 获取当前用户未加入的团队列表
     *
     * @return
     */
    @GetMapping("/list/not")
    public BaseResponse<List<TeamVO>> listTeamsNotJoin() {
        List<TeamVO> items = teamService.listTeamsNotJoin();
        return ResultUtils.success(items);
    }

    /**
     * 根据id获取团队信息
     *
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<TeamVO> getItemById(@RequestParam("id") Long id) {
        TeamVO item = teamService.getItemById(id);
        return ResultUtils.success(item);
    }

    /**
     * 根据团队id获取全体成员信息
     *
     * @return
     */
    @GetMapping("/member/list")
    public BaseResponse<List<TeamMemberVO>> listMemberInfoByTeamId(@RequestParam("id") Long id) {
        List<TeamMemberVO> teamMemberVOs = teamService.listMemberInfoByTeamId(id);
        return ResultUtils.success(teamMemberVOs);
    }

    /**
     * 加入团队
     *
     * @return
     */
    @GetMapping("/join")
    public BaseResponse<Long> joinTeam(@RequestParam("teamId") Long teamId) {
        Long id = teamService.joinTeam(teamId);
        return ResultUtils.success(id);
    }
}
