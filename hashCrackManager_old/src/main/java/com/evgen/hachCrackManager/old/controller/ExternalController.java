package com.evgen.hachCrackManager.old.controller;

import com.evgen.hachCrackManager.old.dto.ManagerResponseDto;
import com.evgen.hachCrackManager.old.dto.RequestDto;
import com.evgen.hachCrackManager.old.dto.StatusResponse;
import com.evgen.hachCrackManager.old.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ExternalController {

    private final TaskService taskService;

    @Autowired
    public ExternalController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/api/hash/crack")
    public ResponseEntity<ManagerResponseDto> getNewTask(@RequestBody RequestDto requestDto) {
//        UUID uuid = UUID.randomUUID();
//        Task task = Task.fromRequest(requestDto);
//
//        ManagerResponseDto responseDto = new ManagerResponseDto();
//        try {
//            taskService.createNewTask(task, uuid);
//            responseDto.setRequestId(uuid.toString());
//        } catch (Exception e) {
//            //e.printStackTrace();
//            return ResponseEntity.internalServerError().body(responseDto);
//        }
//        return ResponseEntity.ok(responseDto);


    }

    @GetMapping("/api/hash/status")
    public ResponseEntity<StatusResponse> getStatus(@RequestParam String requestId) {
        return ResponseEntity.ok(taskService.getStatus(UUID.fromString(requestId)));
    }

}
