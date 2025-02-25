package com.evgen.hachCrackManager.old.controller;

import com.evgen.hachCrackManager.old.dto.StatusResponse;
import com.evgen.hachCrackManager.old.dto.WorkerTasksDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class InternalController {

    private final String[] urls;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(InternalController.class);
    private static final String NEW_TASK_URL = "/internal/api/worker/hash/crack/task";
    private static final String GET_STATUS_URL = "/internal/api/worker/hash/crack/status";

    @Autowired
    public InternalController(@Value("${workers.url}") String workersUrl) {
        this.urls = workersUrl.split(",");
    }

    public void sendTasks(List<WorkerTasksDto> tasks) {
        if (tasks.size() != urls.length) {
            throw new IllegalArgumentException("Tasks size does not match urls size: " + tasks.size() + ", " + urls.length);
        }

        for (int i = 0; i < tasks.size(); i++) {
            WorkerTasksDto task = tasks.get(i);
            String url = "http://" + urls[i] + NEW_TASK_URL;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<WorkerTasksDto> request = new HttpEntity<>(task, headers);

            try {
                restTemplate.postForObject(url, request, String.class);
                logger.info("Task sent to {}", url);
            } catch (RestClientException e) {
                logger.error("Error while sending to {}", url);
            }
        }
    }

    public StatusResponse[] getStatus(String requestId) {
        StatusResponse[] responses = new StatusResponse[urls.length];

        for (int i = 0; i < urls.length; i++) {
            String url = "http://" + urls[i] + GET_STATUS_URL + "?requestId=" + requestId;

            try {
                StatusResponse response = restTemplate.getForObject(url, StatusResponse.class);
                if (response != null) {
                    logger.info("Status from worker{}: {}, found: {}", i, response.getStatus(), response.getData().size());
                    responses[i] = response;
                }
            } catch (RestClientException e) {
                logger.error("Error while checking the {}", url);
                //e.printStackTrace();
            }

        }
        return responses;
    }
}
