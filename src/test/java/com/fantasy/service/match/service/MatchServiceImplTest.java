package com.fantasy.service.match.service;

import com.fantasy.service.match.dao.MatchDAO;
import com.fantasy.service.match.domain.Match;
import com.fantasy.service.match.domain.Team;
import com.fantasy.service.match.service.exception.ServiceClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MatchServiceImplTest {

    @Mock
    private MatchDAO matchDAO;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @InjectMocks
    private MatchServiceImpl matchService;

    @BeforeEach
    public void setUp() {
        Mockito.when(dateTimeProvider.getCurrentDateTime()).thenReturn(LocalDateTime.now());
    }

    @Test
    public void shouldThrowClientErrorWhenAwayAndHomeAreSameTeamsTest() {
        Match match = Match.builder()
                .awayTeam(new Team(1L, "A"))
                .homeTeam(new Team(1L, "A"))
                .startDateTime(LocalDateTime.now().plusDays(1))
                .build();

        Assertions.assertThrows(ServiceClientException.class, () -> {
            matchService.create(match);
        });
    }

    @Test
    public void shouldThrowClientErrorWhenScoreMissedForCompletedMatchTest() {
        Match match = Match.builder()
                .awayTeam(new Team(1L, "A"))
                .homeTeam(new Team(2L, "A"))
                .startDateTime(LocalDateTime.now().minusDays(1))
                .build();


        Assertions.assertThrows(ServiceClientException.class, () -> {
            matchService.create(match);
        });
    }


    @Test
    public void shouldCreateMatchTest() {
        Match match = Match.builder()
                .awayTeam(new Team(1L, "A"))
                .homeTeam(new Team(2L, "B"))
                .startDateTime(LocalDateTime.now().plusDays(1))
                .build();
        matchService.create(match);
        Mockito.verify(matchDAO).save(match);
    }


}