package com.fantasy.service.match.dao;

import com.fantasy.service.match.domain.Match;
import com.fantasy.service.match.domain.MatchSearchObject;

import java.util.List;
import java.util.Optional;

public interface MatchDAO {

    List<Match> findAllBy(MatchSearchObject searchObject);

    void remove(Long matchID);

    Match save(Match match);

    Optional<Match> findById(long matchID);
}
