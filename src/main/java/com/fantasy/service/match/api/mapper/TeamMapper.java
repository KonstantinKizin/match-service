package com.fantasy.service.match.api.mapper;

import com.fantasy.service.match.api.dto.TeamDTO;
import com.fantasy.service.match.api.request.GetTeamRequest;
import com.fantasy.service.match.domain.Team;
import com.fantasy.service.match.domain.TeamSearchObject;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public TeamDTO map(Team source) {
        if (source != null) {
            TeamDTO target = new TeamDTO(source.getId(), null);
            target.setName(source.getName());
            return target;
        }
        return null;
    }

    public Team map(TeamDTO source) {
        if (source != null) {
            return new Team(source.getId(), source.getName());
        }
        return null;
    }

    public TeamSearchObject map(GetTeamRequest source) {
        if (source != null) {
            TeamSearchObject target = new TeamSearchObject();
            target.setWinnersOnly(source.getWinnersOnly());
            return target;
        }
        return null;
    }
}
