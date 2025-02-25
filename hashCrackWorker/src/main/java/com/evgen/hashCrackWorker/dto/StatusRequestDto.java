package com.evgen.hashCrackWorker.dto;

import com.evgen.hashCrackWorker.WorkStatus;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusRequestDto {
    private WorkStatus status;
    private Set<String> data;

    @Override
    public String toString() {
        return status.toString() + " " + data.toString();
    }
}
