package com.rossotti.basketball.jpa.service.impl;

import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.jpa.repository.GameRepository;
import com.rossotti.basketball.jpa.service.GameService;
import com.rossotti.basketball.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

	private GameRepository gameRepository;

	@Autowired
	public void setGameRepository(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Override
	public Game findByTeamKeyAndAsOfDate(String teamKey, LocalDate asOfDate) {
		Game game = gameRepository.findByTeamKeyAndFromDateAndToDate(teamKey, DateTimeUtil.getLocalDateTimeMin(asOfDate), DateTimeUtil.getLocalDateTimeMax(asOfDate));
		if (game != null) {
			game.setStatusCode(StatusCodeDAO.Found);
		}
		else {
			game = new Game(StatusCodeDAO.NotFound);
		}
		return game;
	}

	@Override
	public List<Game> findByTeamKeyAndAsOfDateSeason(String teamKey, LocalDate asOfDate) {
		return gameRepository.findByTeamKeyAndFromDateAndToDateSeason(teamKey, DateTimeUtil.getLocalDateTimeSeasonMin(asOfDate), DateTimeUtil.getLocalDateTimeMax(asOfDate));
	}

	@Override
	public List<Game> findByAsOfDate(LocalDate asOfDate) {
		return gameRepository.findByFromDateAndToDate(DateTimeUtil.getLocalDateTimeMin(asOfDate), DateTimeUtil.getLocalDateTimeMax(asOfDate));
	}

	@Override
	public LocalDateTime findPreviousByTeamKeyAsOfDate(String teamKey, LocalDate asOfDate) {
		List<LocalDateTime> gameDateTime = gameRepository.findPreviousByTeamKeyAndAsOfDate(teamKey, DateTimeUtil.getLocalDateTimeMin(asOfDate));
		if (gameDateTime != null && gameDateTime.size() > 0) {
			return gameDateTime.get(0);
		}
		else {
			return null;
		}
	}

	@Override
	public Game getById(Long id) {
		return gameRepository.findOne(id);
	}

	@Override
	public List<?> listAll() {
		List<Game> games = new ArrayList<>();
		gameRepository.findAll().forEach(games::add);
		return games;
	}

	@Override
	public Game create(Game createGame) {
		Game game = findByTeamKeyAndAsOfDate(createGame.getBoxScoreAway().getTeam().getTeamKey(), DateTimeUtil.getLocalDate(createGame.getGameDateTime()));
		if (game.isNotFound()) {
			gameRepository.save(createGame);
			createGame.setStatusCode(StatusCodeDAO.Created);
			return createGame;
		}
		else {
			return game;
		}
	}

	@Override
	public Game update(Game updateGame) {
		return null;
//		Game game = findByLastNameAndFirstNameAndAsOfDate(updateGame.getLastName(), updateGame.getFirstName(), updateGame.getFromDate());
//		if (game.isFound()) {
//			game.setLastName(updateGame.getLastName());
//			game.setFirstName(updateGame.getFirstName());
//			game.setFromDate(updateGame.getFromDate());
//			game.setToDate(updateGame.getToDate());
//			game.setNumber(updateGame.getNumber());
//			gameRepository.save(game);
//			game.setStatusCode(StatusCodeDAO.Updated);
//		}
//		return game;
	}

	@Override
	public Game delete(Long id) {
		return null;
//		Game findGame = getById(id);
//		if (findGame != null && findGame.isFound()) {
//			gameRepository.delete(findGame.getId());
//			findGame.setStatusCode(StatusCodeDAO.Deleted);
//			return findGame;
//		}
//		else {
//			return new Game(StatusCodeDAO.NotFound);
//		}
	}
}