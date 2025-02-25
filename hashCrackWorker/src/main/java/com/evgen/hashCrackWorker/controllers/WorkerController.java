package com.evgen.hashCrackWorker.controllers;

import com.evgen.hashCrackWorker.dto.StatusRequestDto;
import com.evgen.hashCrackWorker.service.CrackService;
import com.evgen.hashCrackWorker.service.WorkerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WorkerController {

    private final CrackService crackService;

    public WorkerController(@Autowired CrackService crackService) {
        this.crackService = crackService;
    }

    @PostMapping("/internal/api/worker/hash/crack/task")
    public ResponseEntity<String> post(@RequestBody WorkerTask dto, @RequestParam String requestId) {
        crackService.startNewTask(requestId, dto);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/internal/api/worker/hash/crack/status")
    public ResponseEntity<StatusRequestDto> getStatus(@RequestParam String requestId) {
        return ResponseEntity.ok(crackService.getStatus(requestId));
    }


}
