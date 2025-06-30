package com.agendador.api_agendador.web.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Generic DTO for paginated responses")
public record PageResponse<T>(

        @Schema(description = "List of items in the current page")
        List<T> content,

        @Schema(description = "Current page number (zero-based)", example = "0")
        int page,

        @Schema(description = "Number of items per page", example = "10")
        int size,

        @Schema(description = "Total number of items available", example = "100")
        long totalElements,

        @Schema(description = "Total number of pages available", example = "10")
        int totalPages

) {
    public PageResponse(Page<T> page) {
        this(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getTotalPages());
    }
}