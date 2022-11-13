package com.fantasy.service.match.api.response;

import com.fantasy.service.match.api.dto.MatchDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor(staticName = "of")
public class GetMatchResponse {
    private final Collection<MatchDTO> matches;


}
