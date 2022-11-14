package com.fantasy.service.match.dao.impl;

import com.fantasy.service.match.dao.TeamDAO;
import com.fantasy.service.match.domain.Team;
import com.fantasy.service.match.domain.TeamSearchObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
class TeamDAOTest {
    @Autowired
    private TeamDAO teamDAO;


    @Test
    public void getWinnersQueryTest() {
        TeamSearchObject searchObject = new TeamSearchObject();
        searchObject.setWinnersOnly(true);
        Map<Long, Team> result = teamDAO.findAll(searchObject)
                .stream()
                .collect(Collectors.toMap(Team::getId, team -> team));

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.containsKey(1L));
        Assertions.assertTrue(result.containsKey(7L));

        for (Team team : result.values()) {
            Assertions.assertNotNull(team.getName());
        }
    }

}