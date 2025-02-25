package com.evgen.hashCrackManager.controllers;

import com.evgen.hashCrackManager.dto.ManagerNewTaskDto;
import com.evgen.hashCrackManager.dto.RequestIdDto;
import com.evgen.hashCrackManager.dto.StatusResponseDto;
import com.evgen.hashCrackManager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OuterController {

    private final TaskService taskService;

    public OuterController(@Autowired TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/api/hash/crack")
    public ResponseEntity<RequestIdDto> newTask(@RequestBody ManagerNewTaskDto taskDto) {
        RequestIdDto dto = taskService.createTask(taskDto);
        if (dto == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/api/hash/status")
    public ResponseEntity<StatusResponseDto> getStatus(@RequestParam String requestId) {
        return ResponseEntity.ok(taskService.getTaskStatus(requestId));
    }

}
