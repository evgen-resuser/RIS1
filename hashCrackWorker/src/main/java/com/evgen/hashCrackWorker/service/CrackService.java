package com.evgen.hashCrackWorker.service;

import com.evgen.hashCrackWorker.WorkStatus;
import com.evgen.hashCrackWorker.crack.HashCrackCallable;
import com.evgen.hashCrackWorker.dto.StatusRequestDto;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.*;

@Slf4j
@Service
public class CrackService {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, StatusRequestDto> threadStatuses = new ConcurrentHashMap<>();

    public void startNewTask(String id, WorkerTask request) {
        Task task = Task.fromManagerRequest(request);
        threadStatuses.put(id, new StatusRequestDto(WorkStatus.IN_PROGRESS, ConcurrentHashMap.newKeySet()));

        HashCrackCallable work = new HashCrackCallable(task);
        Future<Set<String>> future = executor.submit(work);

        CompletableFuture.supplyAsync(() -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Task ({}) failed: {}", id, e.getMessage());
                return null;
            }
        }, executor).thenAccept(results -> {
            threadStatuses.computeIfPresent(id, (key, status) -> {
                if (results != null) {
                    status.setStatus(WorkStatus.READY);
                    status.setData(results);
                    log.info("Task ({}) result: {}", id, results);
                } else {
                    status.setStatus(WorkStatus.ERROR);
                }
                return status;
            });
        });


        log.info("Task started: {}", request);

    }

    public StatusRequestDto getStatus(String id) {
        return threadStatuses.getOrDefault(id, new StatusRequestDto(WorkStatus.NOT_FOUND, null));
    }

    @PreDestroy
    private void shutdown() {
        log.info("Shutting down executor...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                log.warn("Forcing executor shutdown...");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Shutdown interrupted", e);
            executor.shutdownNow();
        }
    }
}
