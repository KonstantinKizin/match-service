package com.fantasy.service.match.api;

import com.fantasy.service.match.api.dto.TeamDTO;
import com.fantasy.service.match.api.mapper.TeamMapper;
import com.fantasy.service.match.api.request.GetTeamRequest;
import com.fantasy.service.match.api.response.GetTeamResponse;
import com.fantasy.service.match.dao.TeamDAO;
import com.fantasy.service.match.domain.Team;
import com.fantasy.service.match.domain.TeamSearchObject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
class TeamFacade {
    private final TeamDAO teamDAO;
    private final TeamMapper mapper;

    public GetTeamResponse getTeams(GetTeamRequest request) {
        TeamSearchObject searchObject = mapper.map(request);
        Collection<Team> teams = teamDAO.findAll(searchObject);
        return GetTeamResponse.of(this.map(teams));
    }

    public TeamDTO update(TeamDTO request) {
        Team team = teamDAO.update(mapper.map(request));
        return mapper.map(team);
    }

    private Collection<TeamDTO> map(Collection<Team> teams){
        return teams.stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }
}
