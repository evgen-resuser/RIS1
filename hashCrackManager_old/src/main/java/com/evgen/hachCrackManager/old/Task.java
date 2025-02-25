package com.evgen.hachCrackManager.old;

import com.evgen.hachCrackManager.TaskStatus;
import com.evgen.hachCrackManager.old.dto.RequestDto;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Task {
    private String hash;
    private int maxLength;
    private TaskStatus status;
    private Set<String> result = new HashSet<>();

    public static Task fromRequest(RequestDto task) {
        Task newTask = new Task();
        newTask.hash = task.getHash();;
        newTask.maxLength = task.getMaxLength();
        newTask.status = TaskStatus.IN_PROGRESS;
        return newTask;
    }

    public void addResults(Set<String> results) {
        result.addAll(results);
    }
}
