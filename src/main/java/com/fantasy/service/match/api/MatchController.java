package com.fantasy.service.match.api;

import com.fantasy.service.match.api.dto.MatchDTO;
import com.fantasy.service.match.api.request.CreateMatchRequest;
import com.fantasy.service.match.api.request.GetMatchRequest;
import com.fantasy.service.match.api.request.UpdateMatchRequest;
import com.fantasy.service.match.api.response.GetMatchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/v1/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchFacade matchFacade;

    @GetMapping
    public GetMatchResponse getMatches(GetMatchRequest request) {
        return matchFacade.getMatches(request);
    }

    @PostMapping
    public MatchDTO crete(@RequestBody @Valid CreateMatchRequest request) {
        return matchFacade.create(request);
    }

    @PatchMapping("/{matchID}")
    public MatchDTO update(@RequestBody UpdateMatchRequest request, @PathVariable Long matchID) {
        return matchFacade.update(matchID, request);
    }

    @DeleteMapping("/{matchID}")
    public ResponseEntity<?> delete(@PathVariable Long matchID) {
        matchFacade.remove(matchID);
        return ResponseEntity.ok().build();
    }


}
