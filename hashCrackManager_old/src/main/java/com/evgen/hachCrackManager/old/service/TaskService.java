package com.evgen.hachCrackManager.old.service;

import com.evgen.hachCrackManager.TaskStatus;
import com.evgen.hachCrackManager.old.controller.InternalController;
import com.evgen.hachCrackManager.old.dto.RequestDto;
import com.evgen.hachCrackManager.old.dto.StatusResponse;
import com.evgen.hachCrackManager.old.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@EnableScheduling
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final InternalController internalController;
    private final ConcurrentHashMap<UUID, Task> tasks;
    private final int workerAmount;

    @Autowired
    public TaskService(
            InternalController internalController,
            @Value("${workers.amount}") int workerAmount
    ) {
        this.internalController = internalController;
        this.tasks = new ConcurrentHashMap<>();
        this.workerAmount = workerAmount;
        logger.info("Worker amount: {}", workerAmount);
    }

    public UUID createNewTask(RequestDto requestDto) {
        UUID uuid = UUID.randomUUID();
        Task task = Task.fromRequest(requestDto);

        tasks.put(uuid, task);
        internalController.sendTasks(DistributionService.distributeTask(task, workerAmount, uuid));

        return uuid;
    }

    public StatusResponse getStatus(UUID taskId) {
        StatusResponse response = new StatusResponse();
        if (!tasks.containsKey(taskId)) {
            response.setStatus(TaskStatus.NOT_FOUND);
            response.setData(new HashSet<>());
            return response;
        }
        response.setStatus(tasks.get(taskId).getStatus());
        response.setData(tasks.get(taskId).getResult());

        checkTasks();

        return response;
    }

//    @Scheduled(fixedRate = 2000)
//    @Async
    public void checkTasks() {
        logger.info("Checking tasks...");

        for (UUID taskId : tasks.keySet()) {
            logger.info("Checking task {}", taskId);
            var responses = internalController.getStatus(taskId.toString());
            checkResults(taskId, responses);
        }

    }

    private void checkResults(UUID id, StatusResponse[] responses) {
        for (StatusResponse response : responses) {
            if (response.getData() != null && !response.getData().isEmpty()) {
                System.out.println("found new result!!");
                tasks.get(id).addResults(response.getData());
            }
        }
        logger.info("Results: {}", tasks.get(id).getResult());
    }

}
