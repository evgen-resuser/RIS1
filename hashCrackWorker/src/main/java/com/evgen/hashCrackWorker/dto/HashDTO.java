package com.evgen.hashCrackWorker.dto;

import lombok.Data;

import java.util.Arrays;

@Data
public class HashDTO {
    private String alphabet;
    private int maxLength;
    private long[] partFrom;
    private long[] partTo;
    private String hash;
    private String id;

    public HashDTO(String alphabet, int maxLength, String hash, String id) {
        this.alphabet = alphabet;
        this.maxLength = maxLength;
        this.hash = hash;
        this.partFrom = new long[maxLength];
        this.partTo = new long[maxLength];
        this.id = id;
    }

    @Override
    public String toString() {
        return "Start: " + Arrays.toString(partFrom) + " End: " + Arrays.toString(partTo);
    }

    public void add(long partFrom, long partTo, int length) {
        this.partFrom[length-1] = partFrom;
        this.partTo[length-1] = partTo;
    }
}
