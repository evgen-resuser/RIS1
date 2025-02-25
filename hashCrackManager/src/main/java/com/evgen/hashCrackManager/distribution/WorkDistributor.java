package com.evgen.hashCrackManager.distribution;

import com.evgen.hashCrackManager.services.Task;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class WorkDistributor {

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ALPHABET_SIZE = ALPHABET.length();

    public static List<WorkerTask> distributeWork(Task task, int workers) {
        List<WorkerTask> workersL = new ArrayList<>();

        int maxLen = task.getMaxLength();
        String hash = task.getHash();

        long totalCombinations = 0;
        List<Long> combinationsPerLength = new ArrayList<>();
        for (int length = 1; length <= maxLen; length++) {
            long combinationsForLength = (long) Math.pow(ALPHABET_SIZE, length);
            combinationsPerLength.add(combinationsForLength);
            totalCombinations += combinationsForLength;
        }
        log.info("{}: Total combinations: {}", hash, totalCombinations);

        for (int i = 0; i < workers; i++) {
            WorkerTask taskDto = WorkerTask.fromTask(task);
            workersL.add(taskDto);
        }

        for (int length = 0; length < maxLen; length++) {
            long combinationsForLength = combinationsPerLength.get(length);
            distribute(combinationsForLength, workers, workersL, length+1);
        }

        log.info("Tasks: {}", workersL);

        return workersL;
    }

    private static void distribute(long totalCombinations, int numHandlers, List<WorkerTask> list, int combinationLength) {
        long workPerHandler = totalCombinations / numHandlers;
        long remainder = totalCombinations % numHandlers;

        long start = 0;
        for (int i = 0; i < numHandlers; i++) {
            long end = start + workPerHandler + (i < remainder ? 1 : 0) - 1;
            list.get(i).add(combinationLength-1, start, end);
            start = end + 1;
        }
    }
}
