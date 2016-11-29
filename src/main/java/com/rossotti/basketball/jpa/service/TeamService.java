package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Team;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface TeamService extends CrudService<Team> {
	List<Team> findByTeamKey(String teamKey);
	List<Team> findByDate(LocalDate asOfDate);
	Team findByTeamKeyAndDate(String teamKey, LocalDate date);
	Team findByLastNameAndDate(String lastName, LocalDate date);
}