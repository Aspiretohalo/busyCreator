package com.busymango.busymangoBackend.inspiration.controller;

import com.busymango.busymangoBackend.core.common.BaseResponse;
import com.busymango.busymangoBackend.core.common.ResultUtils;
import com.busymango.busymangoBackend.inspiration.model.vo.InspirationVO;
import com.busymango.busymangoBackend.inspiration.service.InspirationService;
import com.busymango.busymangoBackend.project.model.vo.ProjectVO;
import com.busymango.busymangoBackend.task.model.dto.TaskCreateRequest;
import com.busymango.busymangoBackend.task.model.dto.TaskUpdateRequest;
import com.busymango.busymangoBackend.task.model.vo.TaskVO;
import com.busymango.busymangoBackend.task.service.TaskService;
import com.busymango.busymangoBackend.team.model.vo.TeamMemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/inspiration")
public class InspirationController {
    @Resource
    private InspirationService inspirationService;

    /**
     * 查询灵感
     *
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<InspirationVO>> listInspiration(@RequestParam("inspiration") String inspiration) {
        List<InspirationVO> items = inspirationService.listInspiration(inspiration);
        return ResultUtils.success(items);
    }

}
