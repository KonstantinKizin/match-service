package com.fantasy.service.match.service;

import com.fantasy.service.match.dao.MatchDAO;
import com.fantasy.service.match.dao.exception.EntityNotFoundException;
import com.fantasy.service.match.domain.Match;
import com.fantasy.service.match.service.exception.ServiceClientException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchDAO matchDAO;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public Match create(Match match) {
        if (match == null) {
            return null;
        }
        if (Objects.equals(match.getAwayID(), match.getHomeID())) {
            throw new ServiceClientException("Try to save match for same teams");
        }

        if (!isUpcoming(match) && isScoreMissed(match)) {
            throw new ServiceClientException("Score is missing for finished match");
        }

        return matchDAO.save(match);
    }


    @Override
    public Match update(long matchID, LocalDateTime startDate) {
        Match match = matchDAO.findById(matchID)
                .orElseThrow(() -> new EntityNotFoundException(matchID, Match.class));
        match.setStartDateTime(startDate);
        return matchDAO.save(match);
    }

    private boolean isScoreMissed(Match match) {
        return match.getHomeScore() == null || match.getAwayScore() == null;
    }

    private boolean isUpcoming(Match match) {
        return dateTimeProvider.getCurrentDateTime().isBefore(match.getStartDateTime());
    }
}
