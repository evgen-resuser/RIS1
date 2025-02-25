package com.evgen.hashCrackManager.services;

import com.evgen.hashCrackManager.Status;
import com.evgen.hashCrackManager.config.WorkerConfig;
import com.evgen.hashCrackManager.dto.StatusResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Log4j2
@Service
public class StatusSenderService {

    private final RestTemplate restTemplate;
    private final String[] workerUrls;

    @Value("${manager.worker.getStatusUrl}")
    private String getStatusUrl;

    @Autowired
    public StatusSenderService(RestTemplate restTemplate, WorkerConfig workerConfig) {
        this.restTemplate = restTemplate;
        this.workerUrls = workerConfig.getWorkerUrls();
    }

    public StatusResponseDto getTaskStatus(String taskId) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();
        Set<String> results = new HashSet<>();
        boolean isError = false;
        boolean isRunning = false;

        for (int i = 0; i < workerUrls.length; i++) {
            String url = "http://" + workerUrls[i] + getStatusUrl + "?requestId=" + taskId;

            try {
                StatusResponseDto response = restTemplate.getForObject(url, StatusResponseDto.class);
                if (response != null) {
                    log.info("Status from worker{}: {}, found: {}", i, response.getStatus(), response.getData().size());
                    results.addAll(response.getData());
                    if (response.getStatus() == Status.ERROR) isError = true;
                    if (response.getStatus() == Status.IN_PROGRESS) isRunning = true;
                }
            } catch (RestClientException e) {
                log.error("Error while checking the {}: {}", url, e.getMessage());
            }

        }

        statusResponseDto.setData(results);
        statusResponseDto.setStatus(isError ? Status.ERROR : isRunning ? Status.IN_PROGRESS : Status.READY);

        return statusResponseDto;
    }

}
