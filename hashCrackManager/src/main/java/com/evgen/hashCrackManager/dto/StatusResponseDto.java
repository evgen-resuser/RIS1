package com.evgen.hashCrackManager.dto;

import com.evgen.hashCrackManager.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponseDto {
    private Status status;
    private Set<String> data;

    public static StatusResponseDto TaskNotFound() {
        StatusResponseDto task = new StatusResponseDto();
        task.status = Status.NOT_FOUND;
        return task;
    }

}
