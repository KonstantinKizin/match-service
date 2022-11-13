package com.fantasy.service.match.api.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class UpdateMatchRequest {
    @NotNull
    private LocalDateTime startDate;
}
