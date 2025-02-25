package com.evgen.hashCrackWorker.service;

import lombok.Data;

import java.util.Arrays;

@Data
public class WorkerTask {

    private long[] startIndex;
    private long[] endIndex;
    private final String alphabet;
    private final int maxLength;
    private final String hash;

    @Override
    public String toString() {
        return String.format("Hash: %s, start: %s, count: %s", hash, Arrays.toString(startIndex), Arrays.toString(endIndex));
    }

}
