package com.rossotti.basketball.jpa.service.impl;

import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.jpa.repository.TeamRepository;
import com.rossotti.basketball.jpa.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

	private TeamRepository teamRepository;

	@Autowired
	public void setTeamRepository(TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
	}

	@Override
	public List<Team> findByTeamKey(String teamKey) {
		return teamRepository.findByTeamKey(teamKey);
	}

	@Override
	public Team findByTeamKeyAndDate(String teamKey, LocalDate date) {
		Team team = teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter(teamKey, date.plusDays(1), date.minusDays(1));
		if (team != null) {
			team.setStatusCode(StatusCodeDAO.Found);
		}
		else {
			team = new Team(StatusCodeDAO.NotFound);
		}
		return team;
	}

	@Override
	public List<?> listAll() {
		List<Team> teams = new ArrayList<>();
		teamRepository.findAll().forEach(teams::add);
		return teams;
	}

	@Override
	public Team getById(Long id) {
		return teamRepository.findOne(id);
	}

	@Override
	public Team saveOrUpdate(Team team) {
		return teamRepository.save(team);
	}

	@Override
	public void delete(Long id) {
		teamRepository.delete(id);
	}
}