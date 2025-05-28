package com.busymango.busymangoBackend.task.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TaskDTO {
    private Long id;
    private Long projectId;
    private String projectName;
    private String content;
    private Long assigneeId;
    private String assigneeName;
    private Date finishDate;
    private Integer workTime;
    private String note;

    private Long insertUser;

    private Date insertTime;
}
