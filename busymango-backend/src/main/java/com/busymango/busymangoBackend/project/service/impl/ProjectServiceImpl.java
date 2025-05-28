package com.busymango.busymangoBackend.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.core.annotation.IsLogin;
import com.busymango.busymangoBackend.core.utils.StringUtil;
import com.busymango.busymangoBackend.project.mapper.ProjectMapper;
import com.busymango.busymangoBackend.project.model.dto.*;
import com.busymango.busymangoBackend.project.model.enums.ProjectAuditStatusEnum;
import com.busymango.busymangoBackend.project.model.vo.ProjectThumbStatusVO;
import com.busymango.busymangoBackend.project.model.entity.Project;
import com.busymango.busymangoBackend.project.model.vo.ProjectRecordVO;
import com.busymango.busymangoBackend.project.model.vo.ProjectVO;
import com.busymango.busymangoBackend.project.service.ProjectAuditService;
import com.busymango.busymangoBackend.project.service.ProjectRecordService;
import com.busymango.busymangoBackend.project.service.ProjectService;
import com.busymango.busymangoBackend.project.service.ProjectThumbService;
import com.busymango.busymangoBackend.team.model.vo.TeamVO;
import com.busymango.busymangoBackend.team.service.TeamService;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import com.busymango.busymangoBackend.user.model.vo.UserProjectCountVO;
import com.busymango.busymangoBackend.user.model.vo.UserVO;
import com.busymango.busymangoBackend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 95788
 * @description 针对表【bcmcreate_project(项目（作品）信息表)】的数据库操作Service实现
 * @createDate 2025-02-05 02:00:42
 */
@Service
@Slf4j
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project>
        implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private TeamService teamService;
    @Resource
    private ProjectRecordService projectRecordService;
    @Resource
    private UserService userService;
    @Resource
    private ProjectThumbService projectThumbService;
    @Resource
    private ProjectAuditService projectAuditService;

    /**
     * 已加入的团队拥有的项目数
     *
     * @return
     */
    @Override
    public UserProjectCountVO getCount() {
        List<TeamVO> teams = teamService.listTeams();
        List<Long> teamIdList = new ArrayList<>();

        // 判断 teams 是否为空
        if (teams == null || teams.isEmpty()) {
            // 如果 teams 为空，直接返回默认值
            UserProjectCountVO userProjectCountVO = new UserProjectCountVO();
            userProjectCountVO.setProjectCount(0L); // 项目数量默认为 0
            userProjectCountVO.setWorksCount(0L);   // 作品数量默认为 0
            return userProjectCountVO;
        }

        // 如果 teams 不为空，继续处理
        for (TeamVO teamVO : teams) {
            teamIdList.add(teamVO.getId());
        }
        Long projectCount = projectMapper.getCount(teamIdList);
        Long worksCount = projectMapper.getworksCount(teamIdList);

        UserProjectCountVO userProjectCountVO = new UserProjectCountVO();
        userProjectCountVO.setProjectCount(projectCount);
        userProjectCountVO.setWorksCount(worksCount);

        return userProjectCountVO;
    }

    @Override
    public Long saveProject(ProjectCreateRequest projectCreateRequest) {
        UserDTO userDTO = UserContext.getUserDTO();

        Project project = new Project();
        project.setName(projectCreateRequest.getName());
        if (projectCreateRequest.getCover() != null)
            project.setCover(projectCreateRequest.getCover());
        project.setProfile(projectCreateRequest.getProfile());

        String tags = StringUtil.listToString(projectCreateRequest.getTags());
        project.setTags(tags);
        project.setTeamId(projectCreateRequest.getTeamId());
        project.setVersion(0L);
        project.setInsertUser(userDTO.getUserId());

        return projectMapper.saveProject(project);
    }

    @Override
    public List<ProjectVO> listWorks() {
        List<ProjectVO> projectVOS = projectMapper.listWorks();
        // 遍历 projectVOS 列表，将 tags 转换为 List<String> 并存入 tagList
        for (ProjectVO projectVO : projectVOS) {
            String tags = projectVO.getTags(); // 获取 tags 字段
            List<String> tagList = StringUtil.stringToList(tags); // 转换为 List<String>
            projectVO.setTagList(tagList); // 存入 tagList 属性
            // 获取点赞、收藏数
            ProjectThumbCountDTO projectThumbCountDTO = projectThumbService.getThumbCount(projectVO.getId());
            projectVO.setThumbCount(projectThumbCountDTO.getThumbCount());
            projectVO.setBookmarkCount(projectThumbCountDTO.getBookmarkCount());
        }
        return projectVOS;
    }

    @Override
    public List<ProjectVO> listWorksLogout() {
        List<ProjectVO> projectVOS = projectMapper.listWorks();
        // 遍历 projectVOS 列表，将 tags 转换为 List<String> 并存入 tagList
        for (ProjectVO projectVO : projectVOS) {
            String tags = projectVO.getTags(); // 获取 tags 字段
            List<String> tagList = StringUtil.stringToList(tags); // 转换为 List<String>
            projectVO.setTagList(tagList); // 存入 tagList 属性
            // 获取点赞、收藏数
            ProjectThumbCountDTO projectThumbCountDTO = projectThumbService.getThumbCount(projectVO.getId());
            projectVO.setThumbCount(projectThumbCountDTO.getThumbCount());
            projectVO.setBookmarkCount(projectThumbCountDTO.getBookmarkCount());
        }
        return projectVOS;
    }

    @Override
    public List<ProjectVO> listItems() {
        List<TeamVO> items = teamService.listTeams();
        List<Long> teamIdList = new ArrayList<>();

        for (TeamVO teamVO : items) {
            teamIdList.add(teamVO.getId());
        }
        List<ProjectVO> projectVOS = projectMapper.listItems(teamIdList);
        // 遍历 projectVOS 列表，将 tags 转换为 List<String> 并存入 tagList
        for (ProjectVO projectVO : projectVOS) {
            String tags = projectVO.getTags(); // 获取 tags 字段
            List<String> tagList = StringUtil.stringToList(tags); // 转换为 List<String>
            projectVO.setTagList(tagList); // 存入 tagList 属性
            // 获取点赞、收藏数
            ProjectThumbCountDTO projectThumbCountDTO = projectThumbService.getThumbCount(projectVO.getId());
            projectVO.setThumbCount(projectThumbCountDTO.getThumbCount());
            projectVO.setBookmarkCount(projectThumbCountDTO.getBookmarkCount());
        }
        return projectVOS;
    }

    @Override
    public ProjectVO getItemById(Long id) {
        ProjectVO projectVO = projectMapper.getItemById(id);
        TeamVO teamVO = teamService.getItemById(projectVO.getTeamId());

        Long version = projectVO.getVersion();
        if (version != null && version != 0L) {
            ProjectRecordVO projectRecordVO = projectRecordService.getProjectRecordById(version);
            if (projectRecordVO != null) {
                projectVO.setContent(projectRecordVO.getContent());
            } else {
                // 如果 projectRecordVO 为 null，可以根据业务需求处理
                // 例如，记录日志或设置默认值
                log.info("ProjectRecord with version {} not found", version);
            }
        }

        String tags = projectVO.getTags(); // 获取 tags 字段
        List<String> tagList = StringUtil.stringToList(tags); // 转换为 List<String>
        projectVO.setTagList(tagList); // 存入 tagList 属性
        projectVO.setTeamInfo(teamVO);

        // 获取点赞、收藏数
        ProjectThumbCountDTO projectThumbCountDTO = projectThumbService.getThumbCount(id);
        projectVO.setThumbCount(projectThumbCountDTO.getThumbCount());
        projectVO.setBookmarkCount(projectThumbCountDTO.getBookmarkCount());

        return projectVO;
    }

    @Override
    public List<ProjectVO> listItemsByTeamId(Long teamId) {
        List<ProjectVO> projectVOS = projectMapper.listItemsByTeamId(teamId);
        // 遍历 projectVOS 列表，将 tags 转换为 List<String> 并存入 tagList
        for (ProjectVO projectVO : projectVOS) {
            String tags = projectVO.getTags(); // 获取 tags 字段
            List<String> tagList = StringUtil.stringToList(tags); // 转换为 List<String>
            projectVO.setTagList(tagList); // 存入 tagList 属性
        }
        return projectVOS;
    }

    @Override
    public List<ProjectVO> searchItems(String keyword) {
        List<ProjectVO> projectVOS = projectMapper.searchItems(keyword);
        for (ProjectVO projectVO : projectVOS) {
            TeamVO teamVO = teamService.getItemById(projectVO.getTeamId());
            projectVO.setTeamInfo(teamVO);

            String tags = projectVO.getTags(); // 获取 tags 字段
            List<String> tagList = StringUtil.stringToList(tags); // 转换为 List<String>
            projectVO.setTagList(tagList); // 存入 tagList 属性
        }

        return projectVOS;
    }

    @Override
    public ProjectRecordVO getProjectVersion(Long id) {
        // 检查 ID 是否为 0L
        if (id == null || id == 0L) {
            return new ProjectRecordVO(); // 返回一个空的 ProjectRecordVO 对象
        }

        // 获取项目记录
        ProjectRecordVO projectRecordVO = projectRecordService.getProjectRecordById(id);

        // 检查 projectRecordVO 是否为 null
        if (projectRecordVO == null) {
            throw new IllegalArgumentException("ProjectRecord with id " + id + " not found");
        }

        // 获取用户信息
        Long insertUserId = projectRecordVO.getInsertUser();
        if (insertUserId == null) {
            projectRecordVO.setInsertUserInfo(null);
        } else {
            UserVO userVO = userService.getUsersById(insertUserId);
            projectRecordVO.setInsertUserInfo(userVO);
        }

        return projectRecordVO;
    }

    /**
     * 保存项目记录修改
     * 1. 若暂无记录：新建记录
     * 2. 若该记录已提交：新建记录
     * 3. 若该记录未提交：修改内容
     *
     * @param projectRecordSaveRequest
     * @return
     */
    @Override
    public Long saveProjectRecord(ProjectRecordSaveRequest projectRecordSaveRequest) {
        if (projectRecordSaveRequest.getId() == 80L) {
            return projectRecordService.createProjectRecord(projectRecordSaveRequest);
        }

        if ("1".equals(projectRecordSaveRequest.getIsSubmit())) {
            return projectRecordService.createProjectRecord(projectRecordSaveRequest);
        }

        return projectRecordService.saveProjectRecord(projectRecordSaveRequest);
    }

    @Override
    public void setProjectVersion(Long projectId, Long version) {
        projectMapper.updateVersion(version, projectId);
    }

    /**
     * 根据团队id列表批量查询项目
     *
     * @param teamIds
     * @return
     */
    @Override
    public List<ProjectVO> listProjectsByTeamIds(List<Long> teamIds) {
        return projectMapper.listProjectsByTeamIds(teamIds);
    }

    @Override
    public String handlePublish(Long projectId, Long projectRecordId) {
        try {
            Boolean audit = false;
            /**
             * todo 接入大模型进行审核
             */
            audit = true;
            ProjectAuditDTO projectAuditDTO = new ProjectAuditDTO();
            projectAuditDTO.setProjectId(projectId);
            projectAuditDTO.setProjectRecordId(projectRecordId);
            Long projectAuditId = projectAuditService.createAuditRecord(projectAuditDTO);

            /**
             * todo 临时在这加了一个直接审核成功的调用
             */
            ProjectAuditUpdateDTO projectAuditUpdateDTO = new ProjectAuditUpdateDTO();
            projectAuditUpdateDTO.setProjectAuditId(projectAuditId);
            projectAuditUpdateDTO.setStatus(ProjectAuditStatusEnum.APPROVED.getStatus());
            projectAuditService.updateAuditRecord(projectAuditUpdateDTO);
            projectMapper.updatePublicStatus(projectId);
        } catch (Exception e) {
            throw new RuntimeException("提交审核失败，请稍后再试！");
        }

        return "提交审核成功，已加入审核队列！";
    }


}




