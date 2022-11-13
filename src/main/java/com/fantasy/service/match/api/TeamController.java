package com.fantasy.service.match.api;

import com.fantasy.service.match.api.dto.TeamDTO;
import com.fantasy.service.match.api.request.GetTeamRequest;
import com.fantasy.service.match.api.response.GetTeamResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class TeamController {
    private final TeamFacade teamFacade;

    @GetMapping(path = "/v1/teams")
    public GetTeamResponse getTeams(GetTeamRequest request){
        return teamFacade.getTeams(request);
    }

    @PutMapping("/v1/teams")
    public TeamDTO update(@RequestBody TeamDTO request){
        return teamFacade.update(request);
    }

}
