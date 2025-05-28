package com.busymango.busymangoBackend.task.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TaskCreateRequest {
    private String content;
    private Long projectId;
    private Long assigneeId;
    private Date finishDate;
}
