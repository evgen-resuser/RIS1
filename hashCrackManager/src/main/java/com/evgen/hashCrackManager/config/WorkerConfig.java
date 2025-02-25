package com.evgen.hashCrackManager.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class WorkerConfig {
    private final String[] workerUrls;

    public WorkerConfig(@Value("${manager.WorkerUrls}") String urls) {
        this.workerUrls = urls.split(",");
    }

}
