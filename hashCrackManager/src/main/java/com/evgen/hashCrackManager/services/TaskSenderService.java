package com.evgen.hashCrackManager.services;

import com.evgen.hashCrackManager.config.WorkerConfig;
import com.evgen.hashCrackManager.distribution.WorkerTask;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
@Service
public class TaskSenderService {

    private final RestTemplate restTemplate;
    private final String[] workerUrls;

    @Value("${manager.worker.newTaskUrl}")
    private String newTaskUrl;

    @Autowired
    public TaskSenderService(RestTemplate restTemplate, WorkerConfig workerConfig) {
        this.restTemplate = restTemplate;
        this.workerUrls = workerConfig.getWorkerUrls();
    }

    public void sendToWorkers(List<WorkerTask> tasks, String id) {
        if (tasks.size() != workerUrls.length) {
            throw new IllegalArgumentException(String.format("Tasks size (%d) does not match urls size (%d)", tasks.size(), workerUrls.length));
        }

        for (int i = 0; i < workerUrls.length; i++) {
            WorkerTask task = tasks.get(i);
            String url = "http://" + workerUrls[i] + newTaskUrl + id;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<WorkerTask> request = new HttpEntity<>(task, headers);

            try {
                restTemplate.postForObject(url, request, String.class);
                log.info("Task sent to {}", url);
            } catch (RestClientException e) {
                log.error("Error while sending to {}: {}", url, e.getMessage());
            }
        }
    }



}
