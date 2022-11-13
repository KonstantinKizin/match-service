package com.fantasy.service.match.api;

import com.fantasy.service.match.api.dto.MatchDTO;
import com.fantasy.service.match.api.dto.TeamDTO;
import com.fantasy.service.match.api.request.CreateMatchRequest;
import com.fantasy.service.match.api.request.GetMatchRequest;
import com.fantasy.service.match.api.request.UpdateMatchRequest;
import com.fantasy.service.match.api.response.GetMatchResponse;
import com.fantasy.service.match.dao.exception.EntityConstraintException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MatchFacade matchFacade;


    @Test
    public void okGetMatchersTest() throws Exception {
        final MatchDTO matchDTO = createValidDTO();

        mockGetMatchesFacadeResult(1L, true, Collections.singletonList(matchDTO));
        mockMvc.perform(get("/v1/matches?teamID=1&upcomingOnly=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches.size()", is(1)))
                .andExpect(jsonPath("$.matches.[0].id", is(matchDTO.getId().intValue())))
                .andExpect(jsonPath("$.matches.[0].startDate", is("2023-01-01T10:10:10")))
                .andExpect(jsonPath("$.matches.[0].homeTeam.id", is(matchDTO.getHomeTeam().getId().intValue())))
                .andExpect(jsonPath("$.matches.[0].homeTeam.name", is(matchDTO.getHomeTeam().getName())))
                .andExpect(jsonPath("$.matches.[0].awayTeam.id", is(matchDTO.getAwayTeam().getId().intValue())))
                .andExpect(jsonPath("$.matches.[0].awayTeam.name", is(matchDTO.getAwayTeam().getName())))
                .andExpect(jsonPath("$.matches.[0].winnerTeam.id", is(matchDTO.getWinnerTeam().getId().intValue())))
                .andExpect(jsonPath("$.matches.[0].winnerTeam.name", is(matchDTO.getWinnerTeam().getName())))
                .andExpect(jsonPath("$.matches.[0].homeScore", is(matchDTO.getHomeScore())))
                .andExpect(jsonPath("$.matches.[0].awayScore", is(matchDTO.getAwayScore())));


        mockGetMatchesFacadeResult(1L, null, Arrays.asList(matchDTO, matchDTO));
        mockMvc.perform(get("/v1/matches?teamID=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches.size()", is(2)));


        mockGetMatchesFacadeResult(null, null, Arrays.asList(matchDTO, matchDTO, matchDTO));
        mockMvc.perform(get("/v1/matches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matches.size()", is(3)));
    }


    @Test
    public void okCreateMatchTest() throws Exception {
        CreateMatchRequest request = getValidCreateMatchRequest();
        MatchDTO response = createValidDTO();
        Mockito.when(matchFacade.create(request))
                .thenReturn(response);

        mockMvc.perform(post("/v1/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId().intValue())))
                .andExpect(jsonPath("$.startDate", is("2023-01-01T10:10:10")))
                .andExpect(jsonPath("$.homeTeam.id", is(response.getHomeTeam().getId().intValue())))
                .andExpect(jsonPath("$.homeTeam.name", is(response.getHomeTeam().getName())))
                .andExpect(jsonPath("$.awayTeam.id", is(response.getAwayTeam().getId().intValue())))
                .andExpect(jsonPath("$.awayTeam.name", is(response.getAwayTeam().getName())))
                .andExpect(jsonPath("$.winnerTeam.id", is(response.getWinnerTeam().getId().intValue())))
                .andExpect(jsonPath("$.winnerTeam.name", is(response.getWinnerTeam().getName())))
                .andExpect(jsonPath("$.homeScore", is(response.getHomeScore())))
                .andExpect(jsonPath("$.awayScore", is(response.getAwayScore())));
    }

    @Test
    public void badRequestCreateMatchWhenInvalidJsonTest() throws Exception {
        mockMvc.perform(post("/v1/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("AAAA"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Invalid format")));

        mockMvc.perform(post("/v1/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"awayTeamId\":1,\"startTime\":\"2022-11-13T20:38:28.971\"}\n"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Invalid property")))
                .andExpect(jsonPath("$.detailMessage", is("Invalid value for field homeTeamId")));

        Mockito.when(matchFacade.create(Mockito.any()))
                .thenThrow(new EntityConstraintException("detail message"));

        mockMvc.perform(post("/v1/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getValidCreateMatchRequest())))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Bad Request")))
                .andExpect(jsonPath("$.detailMessage", is("detail message")));
    }

    @Test
    public void okUpdateMatchStartDateTest() throws Exception {
        UpdateMatchRequest request = new UpdateMatchRequest();
        request.setStartDate(LocalDateTime.of(2023, 1, 1, 10, 10, 10));

        MatchDTO response = createValidDTO();
        Mockito.when(matchFacade.update(1L, request))
                .thenReturn(response);

        mockMvc.perform(patch("/v1/matches/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId().intValue())))
                .andExpect(jsonPath("$.startDate", is("2023-01-01T10:10:10")))
                .andExpect(jsonPath("$.homeTeam.id", is(response.getHomeTeam().getId().intValue())))
                .andExpect(jsonPath("$.homeTeam.name", is(response.getHomeTeam().getName())))
                .andExpect(jsonPath("$.awayTeam.id", is(response.getAwayTeam().getId().intValue())))
                .andExpect(jsonPath("$.awayTeam.name", is(response.getAwayTeam().getName())))
                .andExpect(jsonPath("$.winnerTeam.id", is(response.getWinnerTeam().getId().intValue())))
                .andExpect(jsonPath("$.winnerTeam.name", is(response.getWinnerTeam().getName())))
                .andExpect(jsonPath("$.homeScore", is(response.getHomeScore())))
                .andExpect(jsonPath("$.awayScore", is(response.getAwayScore())));
    }

    @Test
    public void okDeleteMatchTest() throws Exception {
        mockMvc.perform(delete("/v1/matches/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    private CreateMatchRequest getValidCreateMatchRequest() {
        CreateMatchRequest request = new CreateMatchRequest();
        request.setHomeTeamId(1L);
        request.setAwayTeamId(2L);
        request.setHomeScore(2);
        request.setAwayScore(1);
        request.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 10, 10));
        return request;
    }

    private void mockGetMatchesFacadeResult(Long teamID, Boolean upcoming, List<MatchDTO> result) {
        GetMatchRequest request = new GetMatchRequest();
        request.setTeamID(teamID);
        request.setUpcomingOnly(upcoming);
        Mockito.when(matchFacade.getMatches(request))
                .thenReturn(GetMatchResponse.of(result));
    }


    private MatchDTO createValidDTO() {
        return MatchDTO.builder()
                .id(1L)
                .startDate(LocalDateTime.of(2023, 1, 1, 10, 10, 10))
                .homeTeam(new TeamDTO(1L, "A"))
                .awayTeam(new TeamDTO(2L, "B"))
                .awayScore(1)
                .homeScore(2)
                .winnerTeam(new TeamDTO(1L, "A"))
                .build();
    }

}