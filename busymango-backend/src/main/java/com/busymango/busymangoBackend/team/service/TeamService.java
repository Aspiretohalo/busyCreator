package com.busymango.busymangoBackend.team.service;

import com.busymango.busymangoBackend.team.model.dto.TeamCreateRequest;
import com.busymango.busymangoBackend.team.model.dto.TeamUpdateRequest;
import com.busymango.busymangoBackend.team.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.busymango.busymangoBackend.team.model.vo.TeamMemberVO;
import com.busymango.busymangoBackend.team.model.vo.TeamVO;

import java.util.List;

/**
 * @author 95788
 * @description 针对表【bcmcreate_team(团队信息表)】的数据库操作Service
 * @createDate 2025-02-06 22:59:03
 */
public interface TeamService extends IService<Team> {

    TeamVO getItemById(Long id);

    Long saveTeam(TeamCreateRequest teamCreateRequest);

    Long updateItem(TeamUpdateRequest teamUpdateRequest);

    List<TeamVO> listTeams();

    List<TeamVO> listAdminTeams();

    List<TeamVO> listTeamsNotJoin();

    List<TeamMemberVO> listMemberInfoByTeamId(Long id);

    Long joinTeam(Long teamId);
}
