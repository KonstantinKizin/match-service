package com.fantasy.service.match.dao.impl;

import com.fantasy.service.match.dao.TeamDAO;
import com.fantasy.service.match.dao.exception.EntityNotFoundException;
import com.fantasy.service.match.domain.Team;
import com.fantasy.service.match.domain.TeamSearchObject;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamDAOImpl implements TeamDAO {
    private static final String SELECT_TEAMS_QUERY = "SELECT id, name FROM teams;";
    private static final String SELECT_WINNERS_QUERY = "SELECT DISTINCT(t.id, t.name) FROM teams t\n" +
            " JOIN matches m\n" +
            " ON (t.id = m.away_id AND m.away_score > m.home_score)\n" +
            "       OR (t.id = m.home_id AND m.home_id > m.away_score);";

    private static final String UPDATE_QUERY = "UPDATE teams SET name = ? WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Team> mapper;


    @Override
    public Collection<Team> findAll(TeamSearchObject searchObject) {
        if (searchObject.isWinnersOnly()) {
            return jdbcTemplate.query(SELECT_WINNERS_QUERY, mapper);
        }
        return jdbcTemplate.query(SELECT_TEAMS_QUERY, mapper);
    }

    @Override
    public Team update(Team team) {
        int affectedRows = jdbcTemplate.update(UPDATE_QUERY, ps -> {
            ps.setString(1, team.getName());
            ps.setLong(2, team.getId());
        });
        if (affectedRows == 0) {
            throw new EntityNotFoundException(team.getId(), Team.class);
        }
        return team;
    }

    @Component
    static class TeamRowMapper implements RowMapper<Team> {
        @Override
        public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Team(rs.getLong("id"), rs.getString("name"));
        }
    }
}
