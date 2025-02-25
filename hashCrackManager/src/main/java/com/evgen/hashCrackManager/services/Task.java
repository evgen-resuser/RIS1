package com.evgen.hashCrackManager.services;

import com.evgen.hashCrackManager.Status;
import com.evgen.hashCrackManager.dto.ManagerNewTaskDto;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class Task {
    private Status status;
    private String hash;
    private int maxLength;
    private final String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
    private final Set<String> data = new HashSet<>();

    public static Task fromRequest(ManagerNewTaskDto taskDto) {
        Task task = new Task();
        task.hash = taskDto.getHash();
        task.maxLength = taskDto.getMaxLength();
        task.status = Status.IN_PROGRESS;
        return task;
    }

    public void saveResults(Collection<String> results) {
        data.addAll(results);
    }

    @Override
    public String toString() {
        return String.format("Task %s, maxLength: %d, status: %s, found: %s", hash, maxLength, status, data.size());
    }
}
