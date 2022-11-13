package com.fantasy.service.match.api.response;

import com.fantasy.service.match.api.dto.TeamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor(staticName = "of")
public class GetTeamResponse {
    private Collection<TeamDTO> teams;
}
