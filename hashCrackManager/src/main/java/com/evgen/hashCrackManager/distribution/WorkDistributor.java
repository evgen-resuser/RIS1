package com.evgen.hashCrackManager.distribution;

import com.evgen.hashCrackManager.services.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkDistributor {

    public static List<WorkerTask> distributeWork(Task task, int workers) {

        int maxLength = task.getMaxLength();
        int alphabetSize = task.getAlphabet().length();

        long[] wordCounts = new long[maxLength];
        long sum = 0;
        for (int i = 1; i <= maxLength; i++) {
            wordCounts[i - 1] = (long) Math.pow(alphabetSize, i);
            sum += wordCounts[i - 1];
        }
        long totalWords = sum;

        List<WorkerTask> workersL = new ArrayList<>();

        long wordsPerWorker = totalWords / workers;
        long remainder = totalWords % workers;

        long startIndex = 0;
        for (int i = 0; i < workers; i++) {
            long workSize = wordsPerWorker + (i < remainder ? 1 : 0);
            workersL.add(new WorkerTask(task, startIndex, workSize));
            startIndex += workSize;
        }

        return workersL;
    }
}
