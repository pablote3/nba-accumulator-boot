package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Team;
import org.springframework.data.repository.Repository;
import java.time.LocalDate;
import java.util.List;

public interface TeamRepository extends Repository<Team, Long> {
	void delete(Long id);

	List<Team> findAll();

	Team findOne(Long id);

	Team save(Team persisted);

	List<Team> findByTeamKey(String teamKey);

	Team findByTeamKeyAndFromDateBeforeAndToDateAfter(String teamKey, LocalDate fromDate, LocalDate toDate);
}