package com.fantasy.service.match.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class Match {
    @NonNull
    private Team homeTeam;
    @NonNull
    private Team awayTeam;
    @NonNull
    @Setter
    private LocalDateTime startDateTime;
    @Setter
    private Long id;
    @Setter
    private Integer homeScore;
    @Setter
    private Integer awayScore;

    public Team getWinnerTeam() {
        if (Objects.equals(homeScore, awayScore)) {
            return null;
        }
        return homeScore > awayScore ? homeTeam : awayTeam;
    }

    public Long getHomeID() {
        return homeTeam.getId();
    }

    public Long getAwayID() {
        return awayTeam.getId();
    }

}
