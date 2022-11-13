package com.fantasy.service.match.api.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CreateMatchRequest {
    @NotNull(message = "away team must be present")
    private Long awayTeamId;
    @NotNull(message = "home team must be present")
    private Long homeTeamId;
    @NotNull(message = "start date must be present")
    private LocalDateTime startTime;

    private Integer awayScore;

    private Integer homeScore;
}
