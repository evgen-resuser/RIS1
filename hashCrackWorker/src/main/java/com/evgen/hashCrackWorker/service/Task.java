package com.evgen.hashCrackWorker.service;

import com.evgen.hashCrackWorker.WorkStatus;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Task {
    private long startIndex;
    private long workChunk;
    private String alphabet;
    private int maxLength;
    private String hash;

    private WorkStatus status;
    private Set<String> data = new HashSet<>();

    public static Task fromManagerRequest(WorkerTask request) {
        Task task = new Task();
        task.status = WorkStatus.IN_PROGRESS;
        task.startIndex = request.getStartIndex();
        task.workChunk = request.getWorkChunk();
        task.alphabet = request.getAlphabet();
        task.maxLength = request.getMaxLength();
        task.hash = request.getHash();
        return task;
    }

}
