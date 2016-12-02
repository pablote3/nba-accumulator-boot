package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Standing;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StandingRepository extends Repository<Standing, Long> {

	List<Standing> findAll();

	Standing findOne(Long id);

	Standing save(Standing standing);

	void delete(Long id);

	@Query("select s from Standing s inner join s.team t where t.teamKey = :teamKey")
	List<Standing> findByTeamKey(@Param("teamKey") String teamKey);

	List<Standing> findByStandingDate(LocalDate asOfDate);

	@Query("select s from Standing s inner join s.team t where t.teamKey = :teamKey and s.standingDate = :asOfDate")
	Standing findByTeamKeyAndStandingDate(@Param("teamKey") String teamKey, @Param("asOfDate") LocalDate asOfDate);
}