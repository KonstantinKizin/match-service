package com.fantasy.service.match.api;

import com.fantasy.service.match.api.dto.MatchDTO;
import com.fantasy.service.match.api.dto.TeamDTO;
import com.fantasy.service.match.api.request.CreateMatchRequest;
import com.fantasy.service.match.api.request.GetMatchRequest;
import com.fantasy.service.match.api.request.UpdateMatchRequest;
import com.fantasy.service.match.dao.exception.EntityConstraintException;
import com.fantasy.service.match.dao.exception.EntityNotFoundException;
import com.fantasy.service.match.service.DateTimeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
class MatchFacadeTest {
    private static final Map<Long, MatchDTO> DATA_SET = getMatchesDataSet();

    @Autowired
    private MatchFacade facade;

    @MockBean
    private DateTimeProvider dateTimeProvider;


    @BeforeEach
    public void setUp() {
        Mockito.when(dateTimeProvider.getCurrentDateTime()).thenReturn(LocalDateTime.now());
    }

    @Test
    public void getAllTest() {
        verifySearch(null, null, 7);
    }

    @Test
    public void getAllByTeamTest() {
        verifySearch(1L, null, 2);
        verifySearch(2L, null, 2);
        verifySearch(3L, null, 4);
        verifySearch(4L, null, 1);
        verifySearch(5L, null, 3);
        verifySearch(6L, null, 1);
        verifySearch(7L, null, 1);
    }

    @Test
    public void getAllUpcomingTest() {
        Mockito.when(dateTimeProvider.getCurrentDateTime())
                .thenReturn(LocalDateTime.of(2022, 1, 1, 1, 1, 1));
        verifySearch(null, true, 7);

        Mockito.when(dateTimeProvider.getCurrentDateTime())
                .thenReturn(LocalDateTime.of(2024, 1, 1, 1, 1, 1));
        verifySearch(null, true, 0);

        Mockito.when(dateTimeProvider.getCurrentDateTime())
                .thenReturn(LocalDateTime.of(2023, 1, 1, 1, 1, 1));
        verifySearch(null, true, 4);
    }

    @Test
    public void shouldFailWhenTryToSaveUnExistingTeamTest() {
        CreateMatchRequest request = new CreateMatchRequest();
        request.setHomeTeamId(100L);
        request.setAwayTeamId(1L);
        request.setStartTime(LocalDateTime.now().plusDays(1));

        Assertions.assertThrows(EntityConstraintException.class, () -> {
            facade.create(request);
        });
    }

    @Test
    public void createMatchTest(){
        CreateMatchRequest request = new CreateMatchRequest();
        request.setHomeTeamId(2L);
        request.setAwayTeamId(1L);
        request.setStartTime(LocalDateTime.now().plusDays(1));

        MatchDTO matchDTO = facade.create(request);
        Assertions.assertNotNull(matchDTO.getId());
        Assertions.assertNotNull(matchDTO.getHomeTeam());
        Assertions.assertNotNull(matchDTO.getAwayTeam());
        Assertions.assertNull(matchDTO.getAwayScore());
        Assertions.assertNull(matchDTO.getHomeScore());
        Assertions.assertNull(matchDTO.getWinnerTeam());

        request.setAwayScore(1);
        request.setHomeScore(3);
        matchDTO = facade.create(request);
        Assertions.assertEquals(matchDTO.getAwayScore(), 1);
        Assertions.assertEquals(matchDTO.getHomeScore(), 3);
        Assertions.assertEquals(matchDTO.getWinnerTeam().getId(), 2L);
    }

    @Test
    public void updateUnExistingMatchTest(){
        UpdateMatchRequest request = new UpdateMatchRequest();
        request.setStartDate(LocalDateTime.now().plusDays(1));
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            facade.update(100L, request);
        });
    }



    private void verifySearch(Long teamID, Boolean upcoming, int expectedCount) {
        GetMatchRequest request = new GetMatchRequest();
        request.setTeamID(teamID);
        request.setUpcomingOnly(upcoming);

        Collection<MatchDTO> matches = facade.getMatches(request).getMatches();
        Assertions.assertEquals(expectedCount, matches.size());

        for (MatchDTO actual : matches) {
            MatchDTO expected = DATA_SET.get(actual.getId());
            Assertions.assertEquals(expected, actual);
        }
    }

    private static Map<Long, MatchDTO> getMatchesDataSet() {
        List<MatchDTO> data = new ArrayList<>();
        data.add(MatchDTO.builder()
                .id(1L)
                .homeTeam(new TeamDTO(1L, "Chelsea FC"))
                .awayTeam(new TeamDTO(2L, "Brighton & Hove Albion FC"))
                .startDate(LocalDateTime.of(2023, 3, 22, 22, 0, 0))
                .build());

        data.add(MatchDTO.builder()
                .id(2L)
                .homeTeam(new TeamDTO(3L, "Aston Villa FC"))
                .awayTeam(new TeamDTO(4L, "Leeds United"))
                .startDate(LocalDateTime.of(2023, 3, 23, 22, 0, 0))
                .build());

        data.add(MatchDTO.builder()
                .id(3L)
                .homeTeam(new TeamDTO(5L, "Southampton FC"))
                .awayTeam(new TeamDTO(6L, "Manchester United FC"))
                .startDate(LocalDateTime.of(2023, 3, 24, 17, 0, 0))
                .build());

        data.add(MatchDTO.builder()
                .id(4L)
                .homeTeam(new TeamDTO(1L, "Chelsea FC"))
                .awayTeam(new TeamDTO(3L, "Aston Villa FC"))
                .startDate(LocalDateTime.of(2022, 3, 25, 23, 0, 0))
                .awayScore(0)
                .homeScore(3)
                .winnerTeam(new TeamDTO(1L, "Chelsea FC"))
                .build());

        data.add(MatchDTO.builder()
                .id(5L)
                .homeTeam(new TeamDTO(5L, "Southampton FC"))
                .awayTeam(new TeamDTO(3L, "Aston Villa FC"))
                .startDate(LocalDateTime.of(2023, 3, 25, 23, 0, 0))
                .build());

        data.add(MatchDTO.builder()
                .id(6L)
                .homeTeam(new TeamDTO(5L, "Southampton FC"))
                .awayTeam(new TeamDTO(3L, "Aston Villa FC"))
                .awayScore(0)
                .homeScore(0)
                .startDate(LocalDateTime.of(2022, 7, 29, 23, 0, 0))
                .build());

        data.add(MatchDTO.builder()
                .id(7L)
                .homeTeam(new TeamDTO(2L, "Brighton & Hove Albion FC"))
                .awayTeam(new TeamDTO(7L, "West Ham United FC"))
                .awayScore(1)
                .homeScore(0)
                .winnerTeam(new TeamDTO(7L, "West Ham United FC"))
                .startDate(LocalDateTime.of(2022, 3, 25, 23, 0, 0))
                .build());

        return data.stream()
                .collect(Collectors.toMap(MatchDTO::getId, matchDTO -> matchDTO));
    }
}