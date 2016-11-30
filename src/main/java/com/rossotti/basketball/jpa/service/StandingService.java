package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Standing;
import com.rossotti.basketball.jpa.model.Team;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface StandingService extends CrudService<Standing> {
//	List<Standing> findByTeamKey(String teamKey);
//	List<Standing> findByDate(LocalDate asOfDate);
//	Standing findByTeamKeyAndDate(String teamKey, LocalDate date);
}