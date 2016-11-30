package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Standing;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.List;

public interface StandingRepository extends Repository<Standing, Long> {

	List<Standing> findAll();

	Standing findOne(Long id);

	Standing save(Standing team);

	void delete(Long id);

//	List<Standing> findByTeamKey(String teamKey);
//
//	List<Standing> findByStandingDate(LocalDate asOfDate);
//
//	Standing findByTeamKeyAndStandingDate(String teamKey, LocalDate asOfDate);
}