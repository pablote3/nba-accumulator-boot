package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Team;
import org.springframework.data.repository.Repository;
import java.time.LocalDate;
import java.util.List;

public interface TeamRepository extends Repository<Team, Long> {

	List<Team> findAll();

	Team findOne(Long id);

	Team save(Team team);

	void delete(Long id);

	List<Team> findByTeamKey(String teamKey);

	List<Team> findByFromDateBeforeAndToDateAfter(LocalDate fromDate, LocalDate toDate);

	Team findByTeamKeyAndFromDateBeforeAndToDateAfter(String teamKey, LocalDate fromDate, LocalDate toDate);

	Team findByLastNameAndFromDateBeforeAndToDateAfter(String lastName, LocalDate fromDate, LocalDate toDate);
}