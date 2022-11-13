package com.fantasy.service.match.service;

import com.fantasy.service.match.domain.Match;

import java.time.LocalDateTime;

public interface MatchService {

    Match create(Match match);

    Match update(long matchID, LocalDateTime startDate);
}
