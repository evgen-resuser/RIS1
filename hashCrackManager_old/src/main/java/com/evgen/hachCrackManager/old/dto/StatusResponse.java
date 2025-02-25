package com.evgen.hachCrackManager.old.dto;

import com.evgen.hachCrackManager.TaskStatus;
import lombok.Data;

import java.util.Set;

@Data
public class StatusResponse {
    private TaskStatus status;
    private Set<String> data;
}
