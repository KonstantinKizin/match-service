package com.fantasy.service.match.api;

import com.fantasy.service.match.api.dto.TeamDTO;
import com.fantasy.service.match.api.request.GetTeamRequest;
import com.fantasy.service.match.api.response.GetTeamResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/v1/teams")
@AllArgsConstructor
public class TeamController {
    private final TeamFacade teamFacade;

    @GetMapping
    public GetTeamResponse getTeams(GetTeamRequest request) {
        return teamFacade.getTeams(request);
    }

    @PutMapping
    public TeamDTO update(@RequestBody TeamDTO request) {
        return teamFacade.update(request);
    }

}
