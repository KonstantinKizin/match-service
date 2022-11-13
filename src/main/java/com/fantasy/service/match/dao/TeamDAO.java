package com.fantasy.service.match.dao;

import com.fantasy.service.match.domain.Team;
import com.fantasy.service.match.domain.TeamSearchObject;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TeamDAO {

    Collection<Team> findAll(TeamSearchObject searchObject);

    Team update(Team team);
}
