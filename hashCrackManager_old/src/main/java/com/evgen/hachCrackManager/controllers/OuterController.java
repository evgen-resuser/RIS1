package com.evgen.hachCrackManager.controllers;

import com.evgen.hachCrackManager.dto.ManagerCreateTaskResponse;
import com.evgen.hachCrackManager.dto.TaskRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller("/api/hash")
public class OuterController {

    @PostMapping("/crack")
    public ResponseEntity<ManagerCreateTaskResponse> createNewTask(@RequestBody TaskRequest taskRequest) {

    }

}
