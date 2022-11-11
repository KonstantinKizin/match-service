package com.fantasy.service.match.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Match {
    private final Team homeTeam;
    private final Team awayTeam;
    private final LocalDateTime startTime;
    @Setter
    private Long id;
    @Setter
    private Integer homeScore;
    @Setter
    private Integer awayScore;

}
