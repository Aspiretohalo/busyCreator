package com.busymango.busymangoBackend.team.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.core.utils.StringUtil;
import com.busymango.busymangoBackend.team.model.dto.TeamCreateRequest;
import com.busymango.busymangoBackend.team.model.dto.TeamJoinDTO;
import com.busymango.busymangoBackend.team.model.dto.TeamUpdateRequest;
import com.busymango.busymangoBackend.team.model.entity.Team;
import com.busymango.busymangoBackend.team.model.enums.TeamRoleEnums;
import com.busymango.busymangoBackend.team.model.vo.TeamMemberVO;
import com.busymango.busymangoBackend.team.model.vo.TeamVO;
import com.busymango.busymangoBackend.team.service.TeamService;
import com.busymango.busymangoBackend.team.mapper.TeamMapper;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import com.busymango.busymangoBackend.user.model.entity.User;
import com.busymango.busymangoBackend.user.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 95788
 * @description 针对表【bcmcreate_team(团队信息表)】的数据库操作Service实现
 * @createDate 2025-02-06 22:59:03
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {
    @Resource
    private TeamMapper teamMapper;

    @Resource
    private UserService userService;

    @Override
    public TeamVO getItemById(Long id) {
        TeamVO teamVO = teamMapper.getItemById(id);
        String memberList = teamVO.getMemberList();
        teamVO.setMemberCount(StringUtil.countMembers(memberList));
        return teamVO;
    }

    @Override
    public Long saveTeam(TeamCreateRequest teamCreateRequest) {
        UserDTO userDTO = UserContext.getUserDTO();

        Team team = new Team();
        team.setName(teamCreateRequest.getName());
        if (teamCreateRequest.getAvatar() != null)
            team.setAvatar(teamCreateRequest.getAvatar());
        team.setProfile(teamCreateRequest.getProfile());
        team.setAdminId(userDTO.getUserId());
        // 将 userDTO.getUserId() 添加到 memberList 的开头
        String memberList = userDTO.getUserId() + "";
        team.setMemberList(memberList);
        team.setInsertUser(userDTO.getUserId());

        return teamMapper.saveTeam(team);
    }

    @Override
    public Long updateItem(TeamUpdateRequest teamUpdateRequest) {
        UserDTO userDTO = UserContext.getUserDTO();

        Team team = new Team();
        team.setId(teamUpdateRequest.getId());
        team.setName(teamUpdateRequest.getName());
        if (teamUpdateRequest.getAvatar() != null)
            team.setAvatar(teamUpdateRequest.getAvatar());
        team.setProfile(teamUpdateRequest.getProfile());
        team.setUpdateUser(userDTO.getUserId());

        return teamMapper.updateTeam(team);
    }

    /**
     * 获取当前用户加入的团队列表
     *
     * @return
     */
    @Override
    public List<TeamVO> listTeams() {
        UserDTO userDTO = UserContext.getUserDTO();
        Long userId = userDTO.getUserId();
        List<TeamVO> teamVOS = teamMapper.listTeams(userId);
        for (TeamVO teamVO : teamVOS) {
            String memberList = teamVO.getMemberList();
            // 调用封装好的方法计算成员数量
            teamVO.setMemberCount(StringUtil.countMembers(memberList));
        }

        return teamVOS;
    }

    /**
     * 获取当前用户管理的团队列表
     *
     * @return
     */
    @Override
    public List<TeamVO> listAdminTeams() {
        UserDTO userDTO = UserContext.getUserDTO();
        Long userId = userDTO.getUserId();
        return teamMapper.listAdminTeams(userId);
    }

    /**
     * 获取当前用户未加入的团队列表
     *
     * @return
     */
    @Override
    public List<TeamVO> listTeamsNotJoin() {
        UserDTO userDTO = UserContext.getUserDTO();
        Long userId = userDTO.getUserId();
        List<TeamVO> teamVOS = teamMapper.listTeamsNotJoin(userId);
        for (TeamVO teamVO : teamVOS) {
            String memberList = teamVO.getMemberList();
            // 调用封装好的方法计算成员数量
            teamVO.setMemberCount(StringUtil.countMembers(memberList));
        }

        return teamVOS;
    }

    /**
     * 加入团队
     *
     * @param teamId
     * @return
     */
    @Override
    public Long joinTeam(Long teamId) {
        UserDTO userDTO = UserContext.getUserDTO();
        Long userId = userDTO.getUserId();
        TeamVO teamVO = teamMapper.getItemById(teamId);
        String memberList = teamVO.getMemberList();
        String newMemberList = memberList + "," + userId; // 如果不为空，在末尾添加 userId

        TeamJoinDTO teamJoinDTO = new TeamJoinDTO();
        teamJoinDTO.setNewMemberList(newMemberList);
        teamJoinDTO.setTeamId(teamId);
        return teamMapper.joinTeam(teamJoinDTO);
    }

    @Override
    public List<TeamMemberVO> listMemberInfoByTeamId(Long id) {
        TeamVO teamVO = teamMapper.getItemById(id);
        String memberList = teamVO.getMemberList();
        Long adminId = teamVO.getAdminId();

        // 调用封装好的方法将 memberList 转换为 List<Long>
        List<Long> memberListConverted = StringUtil.stringToLongList(memberList);

        // 通过 userService 获取用户列表
        List<User> members = userService.listUsersByIds(memberListConverted);

        // 根据查询到的用户信息创建 TeamMemberVO 列表
        List<TeamMemberVO> memberInfos = members.stream()
                .map(user -> {
                    TeamMemberVO memberInfo = new TeamMemberVO();
                    memberInfo.setId(user.getId());
                    memberInfo.setUserName(user.getUserName());
                    memberInfo.setUserAvatar(user.getUserAvatar());
                    memberInfo.setEmail(user.getEmail());

                    // 判断 adminId 和 user.getId() 是否相等
                    if (adminId.equals(user.getId())) {
                        memberInfo.setTeamRole(TeamRoleEnums.ADMIN.getCode()); // 如果相等，设置为管理员角色
                    } else {
                        memberInfo.setTeamRole(TeamRoleEnums.MEMBER.getCode()); // 如果不相等，设置为普通成员角色
                    }
                    return memberInfo;
                })
                .collect(Collectors.toList());

        return memberInfos;
    }


}




