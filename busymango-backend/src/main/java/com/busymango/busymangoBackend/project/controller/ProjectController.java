package com.busymango.busymangoBackend.project.controller;

import com.busymango.busymangoBackend.core.annotation.IsLogin;
import com.busymango.busymangoBackend.core.common.BaseResponse;
import com.busymango.busymangoBackend.core.common.ResultUtils;
import com.busymango.busymangoBackend.project.model.dto.ProjectCreateRequest;
import com.busymango.busymangoBackend.project.model.dto.ProjectRecordSaveRequest;
import com.busymango.busymangoBackend.project.model.dto.ProjectRecordSubmitRequest;
import com.busymango.busymangoBackend.project.model.dto.ProjectThumbRequest;
import com.busymango.busymangoBackend.project.model.vo.ProjectRecordVO;
import com.busymango.busymangoBackend.project.model.vo.ProjectThumbStatusVO;
import com.busymango.busymangoBackend.project.model.vo.ProjectVO;
import com.busymango.busymangoBackend.project.service.ProjectRecordService;
import com.busymango.busymangoBackend.project.service.ProjectService;
import com.busymango.busymangoBackend.project.service.ProjectThumbService;
import com.busymango.busymangoBackend.project.service.SearchRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Resource
    private ProjectService projectService;
    @Resource
    private ProjectRecordService projectRecordService;
    @Resource
    @Lazy
    private SearchRecordService searchRecordService;
    @Resource
    private ProjectThumbService projectThumbService;

    /**
     * 创建新的项目
     */
    @PostMapping("/create")
    public BaseResponse<Long> createItem(@RequestBody ProjectCreateRequest projectCreateRequest) {
        Long id = projectService.saveProject(projectCreateRequest);
        log.info("新项目创建情况：" + id);
        return ResultUtils.success(id);
    }

    /**
     * 获取所有已发布项目（首页作品）（包括点赞、收藏、团队信息）
     *
     * @return
     */
    @GetMapping("/works/list")
    public BaseResponse<List<ProjectVO>> listWorks() {
        List<ProjectVO> items = projectService.listWorks();
        return ResultUtils.success(items);
    }
    /**
     * 获取所有已发布项目（首页作品）（包括点赞、收藏、团队信息）
     *
     * @return
     */
    @GetMapping("/works/list/logout")
    @IsLogin
    public BaseResponse<List<ProjectVO>> listWorksLogout() {
        List<ProjectVO> items = projectService.listWorksLogout();
        return ResultUtils.success(items);
    }
    /**
     * 获取个人加入的团队所有项目（已发布、未发布）（包括点赞、收藏、团队信息）
     *
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<ProjectVO>> listItems() {
        List<ProjectVO> items = projectService.listItems();
        return ResultUtils.success(items);
    }

    /**
     * 获取某个团队所有项目（已发布、未发布）（包括点赞、收藏、团队信息）
     *
     * @return
     */
    @GetMapping("/team/list")
    public BaseResponse<List<ProjectVO>> listItemsByTeamId(@RequestParam("teamId") Long teamId) {
        List<ProjectVO> items = projectService.listItemsByTeamId(teamId);
        return ResultUtils.success(items);
    }

    /**
     * 根据id获取单个项目信息（包括点赞、收藏、团队信息）
     *
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<ProjectVO> getItemById(@RequestParam("id") Long id) {
        ProjectVO item = projectService.getItemById(id);
        return ResultUtils.success(item);
    }


    /**
     * 指定项目的版本
     *
     * @return
     */
    @GetMapping("/version/specified")
    public BaseResponse<String> setProjectVersion(@RequestParam("projectId") Long projectId, @RequestParam("version") Long version) {
        projectService.setProjectVersion(projectId, version);
        return ResultUtils.success("修改版本成功");
    }

    /**
     * 根据记录id，返回项目修改记录详情
     *
     * @return
     */
    @GetMapping("/version/get")
    public BaseResponse<ProjectRecordVO> getProjectVersion(@RequestParam("id") Long id) {
        ProjectRecordVO item = projectService.getProjectVersion(id);
        return ResultUtils.success(item == null ? new ProjectRecordVO() : item);
    }

    /**
     * 根据项目id，获取某个项目所有已提交的版本、个人保存的版本
     *
     * @return
     */
    @GetMapping("/version/list")
    public BaseResponse<List<ProjectRecordVO>> listProjectRecord(@RequestParam("id") Long id) {
        List<ProjectRecordVO> items = projectRecordService.listProjectRecord(id);
        return ResultUtils.success(items.isEmpty() ? Collections.emptyList() : items);
    }

    /**
     * 根据项目id，获取某个项目所有已提交的版本
     *
     * @return
     */
    @GetMapping("/version/formal/list")
    public BaseResponse<List<ProjectRecordVO>> listProjectRecordFormal(@RequestParam("id") Long id) {
        List<ProjectRecordVO> items = projectRecordService.listProjectRecordFormal(id);
        return ResultUtils.success(items.isEmpty() ? Collections.emptyList() : items);
    }

    /**
     * 保存项目记录修改
     * 1. 若暂无记录：   新建记录 √
     * 2. 若该记录已提交：新建记录
     * 3. 若该记录未提交：修改内容 √
     *
     * @param projectRecordSaveRequest
     * @return
     */
    @PostMapping("/version/save")
    public BaseResponse<Long> saveProjectRecord(@RequestBody ProjectRecordSaveRequest projectRecordSaveRequest) {
        Long id = projectService.saveProjectRecord(projectRecordSaveRequest);
        log.info("新项目创建情况：" + id);
        return ResultUtils.success(id);
    }

    /**
     * 根据记录id，返回项目修改记录详情
     *
     * @return
     */
    @PostMapping("/version/submit")
    public BaseResponse<Long> submitProjectRecord(@RequestBody ProjectRecordSubmitRequest projectRecordSubmitRequest) {
        Long item = projectRecordService.submitProjectRecord(projectRecordSubmitRequest);
        return ResultUtils.success(item);
    }

    /**
     * 搜索作品
     *
     * @return
     */
    @GetMapping("/works/search")
    public BaseResponse<List<ProjectVO>> searchItems(@RequestParam("keyword") String keyword) {
        List<ProjectVO> items = projectService.searchItems(keyword);
        searchRecordService.saveSearchRecord(keyword);
        return ResultUtils.success(items);
    }

    /**
     * todo 本功能应优化为 Redis 实现
     * 根据项目id和用户id来存储点赞、收藏情况，通过枚举值来区分
     *
     * @return
     */
    @PostMapping("/thumb")
    public BaseResponse<Long> handleThumbAndCollect(@RequestBody ProjectThumbRequest projectThumbRequest) {
        Long item = projectThumbService.handleThumbAndCollect(projectThumbRequest);
        return ResultUtils.success(item);
    }

    /**
     * 根据id获取当前用户是否点赞收藏
     *
     * @return
     */
    @GetMapping("/thumb/get")
    public BaseResponse<ProjectThumbStatusVO> getThumbStatus(@RequestParam("id") Long id) {
        ProjectThumbStatusVO item = projectThumbService.getThumbStatus(id);
        return ResultUtils.success(item);
    }

    /**
     * 根据项目 id 查询对应的内容，交给大模型进行审核，审核成功将会把项目状态改为已发布
     * 此流程使用 RabbitMQ 消息队列实现
     * todo 逻辑上有问题，后续应改为每次对项目指定了不同的版本，都要重新审核
     *
     * @return
     */
    @GetMapping("/publish")
    public BaseResponse<String> handlePublish(@RequestParam("projectId") Long projectId, @RequestParam("projectRecordId") Long projectRecordId) {
        String res = projectService.handlePublish(projectId, projectRecordId);
        return ResultUtils.success(res);
    }
}
