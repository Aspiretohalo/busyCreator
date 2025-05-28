package com.busymango.busymangoBackend.task.mapper;

import com.busymango.busymangoBackend.task.model.dto.TaskDTO;
import com.busymango.busymangoBackend.task.model.dto.TaskUpdateDTO;
import com.busymango.busymangoBackend.task.model.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.busymango.busymangoBackend.task.model.vo.TaskVO;

import java.util.List;

/**
* @author Admin
* @description 针对表【bcmcreate_task(任务表)】的数据库操作Mapper
* @createDate 2025-03-10 23:02:47
* @Entity com.busymango.busymangoBackend.task.model.entity.Task
*/
public interface TaskMapper extends BaseMapper<Task> {

    Long createTask(TaskDTO taskDTO);

    List<TaskVO> listAdminTask(Long userId);

    List<TaskVO> listAssigneeTask(Long userId);

    Long updateTask(TaskUpdateDTO taskUpdateDTO);
}




