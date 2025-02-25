package com.evgen.hashCrackWorker.service;

import com.evgen.hashCrackWorker.WorkStatus;
import com.evgen.hashCrackWorker.crack.HashCrackCallable;
import com.evgen.hashCrackWorker.dto.StatusRequestDto;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
@Service
public class CrackService {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, StatusRequestDto> threadStatuses = new ConcurrentHashMap<>();

    public void startNewTask(String id, WorkerTask request) {

        Task task = Task.fromManagerRequest(request);

        StatusRequestDto requestDto = new StatusRequestDto(WorkStatus.IN_PROGRESS, new HashSet<>());
        threadStatuses.put(id, requestDto);

        HashCrackCallable work = new HashCrackCallable(task);
        Future<Set<String>> future = executor.submit(work);

        CompletableFuture.runAsync(() -> {
            try {
                Set<String> results = future.get();
                StatusRequestDto updatedStatus = threadStatuses.get(id);
                updatedStatus.setStatus(WorkStatus.READY);
                updatedStatus.setData(results);
                log.info("Task ({}) result: {}", id, results);
            } catch (InterruptedException | ExecutionException e) {
                StatusRequestDto updatedStatus = threadStatuses.get(id);
                updatedStatus.setStatus(WorkStatus.ERROR);
                log.error("Task ({}) failed: {}", id, e.getMessage());
            }
        }, executor);

        log.info("Task started: {}", request);

    }

    public StatusRequestDto getStatus(String id) {
        if (!threadStatuses.containsKey(id)) return new StatusRequestDto(WorkStatus.NOT_FOUND, null);
        StatusRequestDto status = threadStatuses.get(id);
        log.info("Sending Task ({}) status: {}", id, status);
        return new StatusRequestDto(status.getStatus(), status.getData());
    }

    @PreDestroy
    private void shutdown() {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

}
