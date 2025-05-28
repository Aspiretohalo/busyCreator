package com.busymango.busymangoBackend.task.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TaskUpdateDTO {
    private Long id;
    private Integer workTime;
    private String note;
}
