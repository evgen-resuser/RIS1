package com.evgen.hashCrackManager.distribution;

import com.evgen.hashCrackManager.services.Task;
import lombok.Data;

@Data
public class WorkerTask {

    private long startIndex;
    private long workChunk;
    private final String alphabet;
    private final int maxLength;
    private final String hash;

    public WorkerTask(Task task, long startIndex, long workChunk) {
        this.startIndex = startIndex;
        this.workChunk = workChunk;
        this.alphabet = task.getAlphabet();
        this.maxLength = task.getMaxLength();
        this.hash = task.getHash();
    }

    @Override
    public String toString() {
        return String.format("Hash: %s, start: %d, count: %d", hash, startIndex, workChunk);
    }

}
