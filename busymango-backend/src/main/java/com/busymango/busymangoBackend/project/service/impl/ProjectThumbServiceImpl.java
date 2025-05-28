package com.busymango.busymangoBackend.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.project.model.dto.ProjectThumbCountDTO;
import com.busymango.busymangoBackend.project.model.dto.ProjectThumbDTO;
import com.busymango.busymangoBackend.project.model.dto.ProjectThumbRequest;
import com.busymango.busymangoBackend.project.model.vo.ProjectThumbStatusVO;
import com.busymango.busymangoBackend.project.model.entity.ProjectThumb;
import com.busymango.busymangoBackend.project.model.enums.ProjectStatusTypeEnum;
import com.busymango.busymangoBackend.project.service.ProjectThumbService;
import com.busymango.busymangoBackend.project.mapper.ProjectThumbMapper;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Admin
 * @description 针对表【bcmcreate_project_thumb(项目点赞收藏表)】的数据库操作Service实现
 * @createDate 2025-03-18 22:40:19
 */
@Service
@Slf4j
public class ProjectThumbServiceImpl extends ServiceImpl<ProjectThumbMapper, ProjectThumb>
        implements ProjectThumbService {

    @Resource
    private ProjectThumbMapper projectThumbMapper;

    @Override
    public ProjectThumbStatusVO getThumbStatus(Long projectId) {
        UserDTO userDTO = UserContext.getUserDTO();
        return projectThumbMapper.getThumbStatus(projectId, userDTO.getUserId());
    }

    /**
     * 获得
     *
     * @param id
     * @return
     */
    @Override
    public ProjectThumbCountDTO getThumbCount(Long id) {
        return projectThumbMapper.getThumbCount(id);
    }

    @Override
    public Long handleThumbAndCollect(ProjectThumbRequest projectThumbRequest) {
        UserDTO userDTO = UserContext.getUserDTO();
        Long projectId = projectThumbRequest.getProjectId();
        Long userId = userDTO.getUserId();
        String type = projectThumbRequest.getType();

        if (type == null || (!ProjectStatusTypeEnum.THUMB.getType().equals(type) && !ProjectStatusTypeEnum.COLLECT.getType().equals(type))) {
            throw new IllegalArgumentException("Invalid type. Type must be 'thumb' or 'collect'.");
        }

        try {
            if (ProjectStatusTypeEnum.THUMB.getType().equals(type)) {
                // 点赞逻辑
                return handleThumb(projectId, userId);
            } else if (ProjectStatusTypeEnum.COLLECT.getType().equals(type)) {
                // 收藏逻辑
                return handleCollect(projectId, userId);
            }
        } catch (Exception e) {
            // 日志记录异常（这里假设有一个日志工具类 Logger）
            log.error("Error handling thumb or collect request", e);
            return -1L; // 返回错误代码
        }

        return 0L; // 默认返回值
    }

    private Long handleThumb(Long projectId, Long userId) {
        ProjectThumbDTO projectThumbDTO = new ProjectThumbDTO();
        projectThumbDTO.setProjectId(projectId);
        projectThumbDTO.setUserId(userId);
        projectThumbDTO.setType(ProjectStatusTypeEnum.THUMB.getType());
        // 检查是否已经点赞
        if (projectThumbMapper.judegExists(projectThumbDTO)) {
            // 如果已经点赞，取消点赞
            projectThumbMapper.cancelStatus(projectThumbDTO);
            return -1L; // 返回 -1 表示取消点赞
        } else {
            // 如果未点赞，新增点赞记录
            ProjectThumb projectThumb = new ProjectThumb();
            projectThumb.setProjectId(projectId);
            projectThumb.setUserId(userId);
            projectThumb.setType(ProjectStatusTypeEnum.THUMB.getType());
            projectThumbMapper.saveStatus(projectThumb);
            return 1L; // 返回 1 表示点赞成功
        }
    }

    private Long handleCollect(Long projectId, Long userId) {
        ProjectThumbDTO projectThumbDTO = new ProjectThumbDTO();
        projectThumbDTO.setProjectId(projectId);
        projectThumbDTO.setUserId(userId);
        projectThumbDTO.setType(ProjectStatusTypeEnum.COLLECT.getType());
        // 检查是否已经收藏
        if (projectThumbMapper.judegExists(projectThumbDTO)) {
            // 如果已经收藏，取消收藏
            projectThumbMapper.cancelStatus(projectThumbDTO);
            return -1L; // 返回 -1 表示取消收藏
        } else {
            // 如果未收藏，新增收藏记录
            ProjectThumb projectThumb = new ProjectThumb();
            projectThumb.setProjectId(projectId);
            projectThumb.setUserId(userId);
            projectThumb.setType(ProjectStatusTypeEnum.COLLECT.getType());
            projectThumbMapper.saveStatus(projectThumb);
            return 1L; // 返回 1 表示收藏成功
        }
    }
}




