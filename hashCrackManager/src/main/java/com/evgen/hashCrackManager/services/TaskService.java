package com.evgen.hashCrackManager.services;

import com.evgen.hashCrackManager.distribution.WorkDistributor;
import com.evgen.hashCrackManager.dto.ManagerNewTaskDto;
import com.evgen.hashCrackManager.dto.RequestIdDto;
import com.evgen.hashCrackManager.dto.StatusResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class TaskService {

    private final int workers;
    private final ConcurrentHashMap<UUID, Task> tasks = new ConcurrentHashMap<>();
    private final TaskSenderService taskSenderService;
    private final StatusSenderService statusSenderService;

    @Autowired
    public TaskService(@Value("${manager.workersCount}") int workers,
                       TaskSenderService taskSenderService,
                       StatusSenderService statusSenderService) {
        this.workers = workers;
        this.taskSenderService = taskSenderService;
        this.statusSenderService = statusSenderService;
    }


    public RequestIdDto createTask(ManagerNewTaskDto taskDto) {
        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000"); //todo make random
        Task task = Task.fromRequest(taskDto);

        tasks.put(uuid, task);
        log.info("New task | {}", task);

        try {
            var list = WorkDistributor.distributeWork(task, workers);
            taskSenderService.sendToWorkers(list, uuid.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

        return new RequestIdDto(uuid);
    }

    public StatusResponseDto getTaskStatus(String taskId) {
        UUID taskUUID;
        try {
            taskUUID = UUID.fromString(taskId);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid task ID format: {}", taskId);
            return StatusResponseDto.TaskNotFound();
        }

        Task task = tasks.get(taskUUID);
        if (task == null) {
            return StatusResponseDto.TaskNotFound();
        }

        StatusResponseDto statusResponseDto = statusSenderService.getTaskStatus(taskId);

        tasks.computeIfPresent(taskUUID, (uuid, t) -> {
            t.setStatus(statusResponseDto.getStatus());
            t.saveResults(statusResponseDto.getData());
            return t;
        });

        return statusResponseDto;
    }

}
