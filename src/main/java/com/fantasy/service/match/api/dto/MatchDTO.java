package com.fantasy.service.match.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@Builder
public class MatchDTO {
    private Long id;
    @NonNull
    private TeamDTO awayTeam;
    @NonNull
    private TeamDTO homeTeam;
    @NonNull
    private LocalDateTime startDate;
    private TeamDTO winnerTeam;
    private Integer awayScore;
    private Integer homeScore;
}
