package com.fantasy.service.match.api.mapper;

import com.fantasy.service.match.api.dto.MatchDTO;
import com.fantasy.service.match.api.request.CreateMatchRequest;
import com.fantasy.service.match.api.request.GetMatchRequest;
import com.fantasy.service.match.domain.Match;
import com.fantasy.service.match.domain.MatchSearchObject;
import com.fantasy.service.match.domain.Team;
import com.fantasy.service.match.service.DateTimeProvider;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MatchMapper {
    private final TeamMapper teamMapper;
    private final DateTimeProvider dateTimeProvider;

    public MatchDTO map(Match source) {
        return MatchDTO.builder().
                id(source.getId())
                .awayTeam(teamMapper.map(source.getAwayTeam()))
                .homeTeam(teamMapper.map(source.getHomeTeam()))
                .awayScore(source.getAwayScore())
                .homeScore(source.getHomeScore())
                .startDate(source.getStartDateTime())
                .winnerTeam(teamMapper.map(source.getWinnerTeam()))
                .build();
    }

    public Match map(MatchDTO source) {
        return Match.builder()
                .id(source.getId())
                .homeTeam(teamMapper.map(source.getHomeTeam()))
                .awayTeam(teamMapper.map(source.getAwayTeam()))
                .awayScore(source.getAwayScore())
                .homeScore(source.getHomeScore())
                .startDateTime(source.getStartDate())
                .build();
    }

    public Match map(CreateMatchRequest request) {
        return Match.builder()
                .homeTeam(new Team(request.getHomeTeamId(), null))
                .awayTeam(new Team(request.getAwayTeamId(), null))
                .homeScore(request.getHomeScore())
                .awayScore(request.getAwayScore())
                .startDateTime(request.getStartTime())
                .build();
    }

    public MatchSearchObject map(GetMatchRequest source) {
        MatchSearchObject target = new MatchSearchObject();
        target.setTeamID(source.getTeamID());
        if (BooleanUtils.isTrue(source.getUpcomingOnly())) {
            target.setStartAfter(dateTimeProvider.getCurrentDateTime());
        }
        return target;
    }

}
