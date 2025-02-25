package com.evgen.hashCrackWorker.crack;

import com.evgen.hashCrackWorker.service.Task;
import lombok.extern.slf4j.Slf4j;
import org.paukov.combinatorics3.Generator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Slf4j
public class HashCrackCallable implements Callable<Set<String>> {

    private final List<Character> alphabet;
    private final long[] startIndex;
    private final long[] endIndex;
    private final long maxLength;
    private final String hash;

    public HashCrackCallable(Task task) {
        this.alphabet = task.getAlphabet().chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        this.startIndex = task.getStartIndex();
        this.endIndex = task.getEndIndex();
        this.maxLength = task.getMaxLength();
        this.hash = task.getHash();
    }

    @Override
    public Set<String> call() {
        Set<String> results = new HashSet<>();
        System.out.println(Arrays.toString(startIndex));
        System.out.println(Arrays.toString(endIndex));

        for (int i = 0; i < maxLength; i++) {
            Generator.permutation(alphabet)
                    .withRepetitions(i)
                    .stream()
                    .skip(startIndex[i])
                    .limit(endIndex[i])
                    .forEachOrdered(word -> {
                        String comb = word.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining());
                        String curHash = HashUtils.md5(comb);
                        if (curHash.equals(this.hash)) {
                            log.info("Found hash: {}", curHash);
                            results.add(comb);
                        }
                    });
        }

        return results;
    }
}
