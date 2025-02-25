package com.evgen.hashCrackWorker.service;

import lombok.Data;

@Data
public class WorkerTask {

    private long startIndex;
    private long workChunk;
    private final String alphabet;
    private final int maxLength;
    private final String hash;

    @Override
    public String toString() {
        return String.format("Hash: %s, start: %d, count: %d", hash, startIndex, workChunk);
    }

}
