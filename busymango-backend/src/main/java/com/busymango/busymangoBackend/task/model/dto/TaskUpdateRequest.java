package com.busymango.busymangoBackend.task.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TaskUpdateRequest {
    private Long taskId;
    private Integer workTime;
    private String note;
}
