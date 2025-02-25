package com.evgen.hashCrackWorker.crack;

import com.evgen.hashCrackWorker.service.Task;
import lombok.extern.slf4j.Slf4j;
import org.paukov.combinatorics3.Generator;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Slf4j
public class HashCrackCallable implements Callable<Set<String>> {

    private final List<Character> alphabet;
    private final long startIndex;
    private final long wordCount;
    private final long maxLength;
    private final String hash;

    public HashCrackCallable(Task task) {
        this.alphabet = task.getAlphabet().chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        this.startIndex = task.getStartIndex();
        this.wordCount = task.getWorkChunk();
        this.maxLength = task.getMaxLength();
        this.hash = task.getHash();
    }

    @Override
    public Set<String> call() {
        Set<String> results = new HashSet<>();

        for (int i = 0; i < maxLength; i++) {
            Generator.permutation(alphabet)
                    .withRepetitions(i)
                    .stream()
                    .skip(startIndex)
                    .limit(wordCount)
                    .forEachOrdered(word -> {
                        String comb = word.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining());
                        String curHash = md5(comb);
                        if (curHash.equals(this.hash)) {
                            log.info("Found hash: {}", curHash);
                            results.add(comb);
                        }
                    });
        }

        return results;
    }

    public static String md5(String input) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());

            BigInteger number = new BigInteger(1, hashBytes);
            StringBuilder hexString = new StringBuilder(number.toString(16));

            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }

            hash = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }
        return hash;
    }
}
