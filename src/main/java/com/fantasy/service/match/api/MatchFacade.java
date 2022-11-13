package com.fantasy.service.match.api;

import com.fantasy.service.match.api.dto.MatchDTO;
import com.fantasy.service.match.api.mapper.MatchMapper;
import com.fantasy.service.match.api.request.CreateMatchRequest;
import com.fantasy.service.match.api.request.GetMatchRequest;
import com.fantasy.service.match.api.request.UpdateMatchRequest;
import com.fantasy.service.match.api.response.GetMatchResponse;
import com.fantasy.service.match.dao.MatchDAO;
import com.fantasy.service.match.domain.Match;
import com.fantasy.service.match.domain.MatchSearchObject;
import com.fantasy.service.match.service.MatchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
class MatchFacade {
    private final MatchMapper mapper;
    private final MatchDAO matchDAO;
    private final MatchService matchService;

    public GetMatchResponse getMatches(GetMatchRequest request) {
        MatchSearchObject searchObject = mapper.map(request);
        List<Match> matches = matchDAO.findAllBy(searchObject);
        return GetMatchResponse.of(this.mapToDTO(matches));
    }

    public MatchDTO update(long matchID, UpdateMatchRequest request) {
        Match match = matchService.update(matchID, request.getStartDate());
        return mapper.map(match);
    }

    public void remove(Long matchID) {
        matchDAO.remove(matchID);
    }

    public MatchDTO create(CreateMatchRequest request) {
        Match match = matchService.create(mapper.map(request));
        return mapper.map(match);
    }


    private List<MatchDTO> mapToDTO(Collection<Match> matches) {
        return matches.stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }
}
