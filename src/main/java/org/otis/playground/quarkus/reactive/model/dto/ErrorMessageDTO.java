package org.otis.playground.quarkus.reactive.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorMessageDTO {
    private String path;
    private String message;
}
