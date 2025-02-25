package com.evgen.hachCrackManager.old.service;

import com.evgen.hachCrackManager.old.Task;
import com.evgen.hachCrackManager.old.dto.WorkerTasksDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DistributionService {

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ALPHABET_SIZE = ALPHABET.length();

    private static final Logger logger = LoggerFactory.getLogger(DistributionService.class);

    public static List<WorkerTasksDto> distributeTask(Task task, int workerAmount, UUID taskId) {

        int maxLen = task.getMaxLength();
        String hash = task.getHash();

        long totalCombinations = 0;
        List<Long> combinationsPerLength = new ArrayList<>();
        for (int length = 1; length <= maxLen; length++) {
            long combinationsForLength = (long) Math.pow(ALPHABET_SIZE, length);
            combinationsPerLength.add(combinationsForLength);
            totalCombinations += combinationsForLength;
        }
        logger.info("Total combinations: {}", totalCombinations);

        List<WorkerTasksDto> tasks = new ArrayList<>();
        for (int i = 0; i < workerAmount; i++) {
            WorkerTasksDto taskDto = new WorkerTasksDto(ALPHABET, maxLen, hash, taskId.toString());
            tasks.add(taskDto);
        }

        for (int length = 1; length <= maxLen; length++) {
            long combinationsForLength = combinationsPerLength.get(length - 1);
            distribute(combinationsForLength, workerAmount, tasks, length);
        }

        logger.info("Tasks: {}", tasks);

        return tasks;
    }

    private static void distribute(long totalCombinations, int numHandlers, List<WorkerTasksDto> list, int combinationLength) {
        long workPerHandler = totalCombinations / numHandlers;
        long remainder = totalCombinations % numHandlers;

        long start = 0;
        for (int i = 0; i < numHandlers; i++) {
            long end = start + workPerHandler + (i < remainder ? 1 : 0) - 1;
            list.get(i).add(start, end, combinationLength);
            start = end + 1;
        }
    }
}
