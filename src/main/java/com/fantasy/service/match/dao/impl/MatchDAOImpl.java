package com.fantasy.service.match.dao.impl;

import com.fantasy.service.match.dao.MatchDAO;
import com.fantasy.service.match.dao.exception.EntityConstraintException;
import com.fantasy.service.match.dao.exception.EntityNotFoundException;
import com.fantasy.service.match.domain.Match;
import com.fantasy.service.match.domain.MatchSearchObject;
import com.fantasy.service.match.domain.Team;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MatchDAOImpl implements MatchDAO {
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM matches WHERE id = ?";
    private static final String SELECT_ALL_QUERY = "SELECT m.* ," +
            " (SELECT name FROM teams t WHERE t.id = home_id) AS home_name, " +
            " (SELECT name FROM teams t WHERE t.id = away_id) AS away_name" +
            " FROM matches m";
    private static final String INSERT_QUERY = "INSERT INTO matches(start_date, home_id, home_score, away_id, away_score, winner_id)" +
            " VALUES(?, ?, ?, ?, ?, ?)";
    private static final String DELETE_MATCH_QUERY = "DELETE FROM matches WHERE id = ?";
    private static final String UPDATE_START_DATE_QUERY = "UPDATE matches" +
            " SET start_date = ?" +
            " AND home_id = ?" +
            " AND home_score = ?" +
            " AND away_id = ?" +
            " AND away_score = ?" +
            " AND winner_id = ?" +
            " WHERE id = ?";

    private final RowMapper<Match> mapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<Match> findAllBy(MatchSearchObject searchObject) {
        Map<String, Object> params = new HashMap<>(2);
        String query = SELECT_ALL_QUERY;
        if (searchObject.hasFilter()) {
            query = String.format("%s \n WHERE", query);
        }
        if (searchObject.getTeamID() != null) {
            params.put("teamID", searchObject.getTeamID());
            query = String.format("%s (home_id = :teamID or away_id = :teamID) AND ", query);
        }

        if (searchObject.getStartAfter() != null) {
            params.put("targetDate", toTimestamp(searchObject.getStartAfter()));
            query = String.format("%s m.start_date > :targetDate AND ", query);
        }

        query = StringUtils.substringBeforeLast(query, "AND");

        return namedParameterJdbcTemplate.query(query, params, mapper);
    }

    private Timestamp toTimestamp(LocalDateTime dateTime) {
        return Timestamp.from(dateTime.toInstant(ZoneOffset.UTC));
    }

    @Override
    public void remove(Long matchID) {
        jdbcTemplate.execute(DELETE_MATCH_QUERY, (PreparedStatementCallback<Object>) ps -> {
            ps.setLong(1, matchID);
            return ps.execute();
        });
    }

    @Override
    public Optional<Match> findById(long matchID) {
        try {
            Match result = jdbcTemplate.query(SELECT_ALL_QUERY, rs -> {
                return mapper.mapRow(rs, 1);
            });
            return Optional.ofNullable(result);
        } catch (DataIntegrityViolationException ex) {
            return Optional.empty();
        }
    }


    @Override
    public Match save(Match match) {
        try {
            if (match.getId() != null) {
                int affectedRows = jdbcTemplate.update(buildUpdateStatementCreator(match));
                if (affectedRows == 0) {
                    throw new EntityNotFoundException(match.getId(), Match.class);
                }
                return match;
            }
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(this.buildInsertStatementCreator(match), keyHolder);
            if (keyHolder.getKey() != null) {
                match.setId(keyHolder.getKey().longValue());
            }
            return match;
        } catch (DataIntegrityViolationException ex) {
            throw new EntityConstraintException("Home or Away team is not exists");
        }
    }

    private PreparedStatementCreator buildInsertStatementCreator(Match match) {
        return (connection) -> getMatchPreparedStatement(connection, match, INSERT_QUERY);
    }

    private PreparedStatementCreator buildUpdateStatementCreator(Match match) {
        return (connection) -> {
            PreparedStatement ps = getMatchPreparedStatement(connection, match, UPDATE_START_DATE_QUERY);
            ps.setLong(6, match.getId());
            return ps;
        };
    }

    private PreparedStatement getMatchPreparedStatement(Connection connection, Match match, String query) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setTimestamp(1, Timestamp.from(match.getStartDateTime().toInstant(ZoneOffset.UTC)));
        ps.setLong(2, match.getHomeTeam().getId());
        ps.setLong(4, match.getAwayTeam().getId());
        if (match.getHomeScore() != null) {
            ps.setLong(3, match.getHomeScore());
        } else {
            ps.setNull(3, Types.TINYINT);
        }

        if (match.getAwayScore() != null) {
            ps.setLong(5, match.getAwayScore());
        } else {
            ps.setNull(5, Types.TINYINT);
        }

        if (match.getWinnerTeam() != null) {
            ps.setLong(6, match.getWinnerTeam().getId());
        } else {
            ps.setNull(5, Types.TINYINT);
        }

        return ps;
    }

    @Component
    static class MatchRowMapper implements RowMapper<Match> {

        @Override
        public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Match.builder()
                    .id(rs.getLong("id"))
                    .homeTeam(new Team(rs.getLong("home_id"), rs.getString("home_name")))
                    .awayTeam(new Team(rs.getLong("away_id"), rs.getString("away_name")))
                    .awayScore(getScore(rs, "away_score"))
                    .homeScore(getScore(rs, "home_score"))
                    .startDateTime(rs.getTimestamp("start_date").toLocalDateTime())
                    .build();
        }

        private Integer getScore(ResultSet rs, String columnName) throws SQLException {
            Object score = rs.getObject(columnName);
            return score != null ? Integer.valueOf(score.toString()) : null;
        }

    }

}
