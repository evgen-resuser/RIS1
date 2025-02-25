package com.evgen.hashCrackManager.distribution;

import com.evgen.hashCrackManager.services.Task;
import lombok.Data;

import java.util.Arrays;

@Data
public class WorkerTask {

    private long[] startIndex;
    private long[] endIndex;
    private String alphabet;
    private int maxLength;
    private String hash;

    public static WorkerTask fromTask(Task task) {
        WorkerTask workerTask = new WorkerTask();
        workerTask.alphabet = task.getAlphabet();
        workerTask.maxLength = task.getMaxLength();
        workerTask.hash = task.getHash();
        workerTask.startIndex = new long[workerTask.maxLength];
        workerTask.endIndex = new long[workerTask.maxLength];
        return workerTask;
    }

    public void add(int index, long startIndex, long endIndex) {
        this.startIndex[index] = startIndex;
        this.endIndex[index] = endIndex;
    }

    @Override
    public String toString() {
        return String.format("Hash: %s, start: %s, count: %s", hash, Arrays.toString(startIndex), Arrays.toString(endIndex));
    }

}
