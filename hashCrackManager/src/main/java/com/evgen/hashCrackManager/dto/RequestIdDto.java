package com.evgen.hashCrackManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RequestIdDto {
    private UUID requestId;
}
