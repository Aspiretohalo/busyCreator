package com.busymango.busymangoBackend.team.mapper;

import com.busymango.busymangoBackend.team.model.dto.TeamJoinDTO;
import com.busymango.busymangoBackend.team.model.entity.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.busymango.busymangoBackend.team.model.vo.TeamVO;

import java.util.List;

/**
* @author 95788
* @description 针对表【bcmcreate_team(团队信息表)】的数据库操作Mapper
* @createDate 2025-02-06 22:59:02
* @Entity com.busymango.busymangoBackend.team.model.entity.Team
*/
public interface TeamMapper extends BaseMapper<Team> {

    TeamVO getItemById(Long id);

    List<TeamVO> listTeams(Long userId);

    Long saveTeam(Team team);

    Long updateTeam(Team team);

    List<TeamVO> listTeamsNotJoin(Long userId);

    Long joinTeam(TeamJoinDTO teamJoinDTO);

    List<TeamVO> listAdminTeams(Long userId);
}




