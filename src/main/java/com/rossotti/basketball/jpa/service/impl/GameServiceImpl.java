package com.rossotti.basketball.jpa.service.impl;

import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.model.BoxScore;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.jpa.model.GameOfficial;
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
		Game findGame = findByTeamKeyAndAsOfDate(updateGame.getBoxScoreAway().getTeam().getTeamKey(), DateTimeUtil.getLocalDate(updateGame.getGameDateTime()));
		if (findGame.isFound()) {
			if (!findGame.getBoxScoreHome().getTeam().getTeamKey().equals(updateGame.getBoxScoreHome().getTeam().getTeamKey())) {
				findGame.setStatusCode(StatusCodeDAO.NotFound);
				return findGame;
			}
			findGame.setStatus(updateGame.getStatus());
			for (int i = 0; i < updateGame.getGameOfficials().size(); i++) {
				GameOfficial gameOfficial = updateGame.getGameOfficials().get(i);
				gameOfficial.setGame(findGame);
				findGame.addGameOfficial(gameOfficial);
			}
			BoxScore findHomeBoxScore = findGame.getBoxScoreHome();
			BoxScore updateHomeBoxScore = updateGame.getBoxScoreHome();
			findHomeBoxScore.setResult(updateHomeBoxScore.getResult());
			findHomeBoxScore.setMinutes(updateHomeBoxScore.getMinutes());
			findHomeBoxScore.setPoints(updateHomeBoxScore.getPoints());
			findHomeBoxScore.setPointsPeriod1(updateHomeBoxScore.getPointsPeriod1());
			findHomeBoxScore.setPointsPeriod2(updateHomeBoxScore.getPointsPeriod2());
			findHomeBoxScore.setPointsPeriod3(updateHomeBoxScore.getPointsPeriod3());
			findHomeBoxScore.setPointsPeriod4(updateHomeBoxScore.getPointsPeriod4());
			findHomeBoxScore.setPointsPeriod5(updateHomeBoxScore.getPointsPeriod5());
			findHomeBoxScore.setPointsPeriod6(updateHomeBoxScore.getPointsPeriod6());
			findHomeBoxScore.setPointsPeriod7(updateHomeBoxScore.getPointsPeriod7());
			findHomeBoxScore.setPointsPeriod8(updateHomeBoxScore.getPointsPeriod8());
			findHomeBoxScore.setAssists(updateHomeBoxScore.getAssists());
			findHomeBoxScore.setTurnovers(updateHomeBoxScore.getTurnovers());
			findHomeBoxScore.setSteals(updateHomeBoxScore.getSteals());
			findHomeBoxScore.setBlocks(updateHomeBoxScore.getBlocks());
			findHomeBoxScore.setFieldGoalAttempts(updateHomeBoxScore.getFieldGoalAttempts());
			findHomeBoxScore.setFieldGoalMade(updateHomeBoxScore.getFieldGoalMade());
			findHomeBoxScore.setFieldGoalPercent(updateHomeBoxScore.getFieldGoalPercent());
			findHomeBoxScore.setThreePointAttempts(updateHomeBoxScore.getThreePointAttempts());
			findHomeBoxScore.setThreePointMade(updateHomeBoxScore.getThreePointMade());
			findHomeBoxScore.setThreePointPercent(updateHomeBoxScore.getThreePointPercent());
			findHomeBoxScore.setFreeThrowAttempts(updateHomeBoxScore.getFreeThrowAttempts());
			findHomeBoxScore.setFreeThrowMade(updateHomeBoxScore.getFreeThrowMade());
			findHomeBoxScore.setFreeThrowPercent(updateHomeBoxScore.getFreeThrowPercent());
			findHomeBoxScore.setReboundsOffense(updateHomeBoxScore.getReboundsOffense());
			findHomeBoxScore.setReboundsDefense(updateHomeBoxScore.getReboundsDefense());
			findHomeBoxScore.setPersonalFouls(updateHomeBoxScore.getPersonalFouls());
			findHomeBoxScore.setDaysOff(updateHomeBoxScore.getDaysOff());
//			findHomeBoxScore.setBoxScorePlayers(updateHomeBoxScore.getBoxScorePlayers());
//			for (int i = 0; i < findHomeBoxScore.getBoxScorePlayers().size(); i++) {
//				findHomeBoxScore.getBoxScorePlayers().get(i).setBoxScore(findHomeBoxScore);
//			}

			BoxScore findAwayBoxScore = findGame.getBoxScoreAway();
			BoxScore updateAwayBoxScore = updateGame.getBoxScoreAway();
			findAwayBoxScore.setResult(updateAwayBoxScore.getResult());
			findAwayBoxScore.setMinutes(updateAwayBoxScore.getMinutes());
			findAwayBoxScore.setPoints(updateAwayBoxScore.getPoints());
			findAwayBoxScore.setPointsPeriod1(updateAwayBoxScore.getPointsPeriod1());
			findAwayBoxScore.setPointsPeriod2(updateAwayBoxScore.getPointsPeriod2());
			findAwayBoxScore.setPointsPeriod3(updateAwayBoxScore.getPointsPeriod3());
			findAwayBoxScore.setPointsPeriod4(updateAwayBoxScore.getPointsPeriod4());
			findAwayBoxScore.setPointsPeriod5(updateAwayBoxScore.getPointsPeriod5());
			findAwayBoxScore.setPointsPeriod6(updateAwayBoxScore.getPointsPeriod6());
			findAwayBoxScore.setPointsPeriod7(updateAwayBoxScore.getPointsPeriod7());
			findAwayBoxScore.setPointsPeriod8(updateAwayBoxScore.getPointsPeriod8());
			findAwayBoxScore.setAssists(updateAwayBoxScore.getAssists());
			findAwayBoxScore.setTurnovers(updateAwayBoxScore.getTurnovers());
			findAwayBoxScore.setSteals(updateAwayBoxScore.getSteals());
			findAwayBoxScore.setBlocks(updateAwayBoxScore.getBlocks());
			findAwayBoxScore.setFieldGoalAttempts(updateAwayBoxScore.getFieldGoalAttempts());
			findAwayBoxScore.setFieldGoalMade(updateAwayBoxScore.getFieldGoalMade());
			findAwayBoxScore.setFieldGoalPercent(updateAwayBoxScore.getFieldGoalPercent());
			findAwayBoxScore.setThreePointAttempts(updateAwayBoxScore.getThreePointAttempts());
			findAwayBoxScore.setThreePointMade(updateAwayBoxScore.getThreePointMade());
			findAwayBoxScore.setThreePointPercent(updateAwayBoxScore.getThreePointPercent());
			findAwayBoxScore.setFreeThrowAttempts(updateAwayBoxScore.getFreeThrowAttempts());
			findAwayBoxScore.setFreeThrowMade(updateAwayBoxScore.getFreeThrowMade());
			findAwayBoxScore.setFreeThrowPercent(updateAwayBoxScore.getFreeThrowPercent());
			findAwayBoxScore.setReboundsOffense(updateAwayBoxScore.getReboundsOffense());
			findAwayBoxScore.setReboundsDefense(updateAwayBoxScore.getReboundsDefense());
			findAwayBoxScore.setPersonalFouls(updateAwayBoxScore.getPersonalFouls());
			findAwayBoxScore.setDaysOff(updateAwayBoxScore.getDaysOff());
//			findAwayBoxScore.setBoxScorePlayers(updateAwayBoxScore.getBoxScorePlayers());
//			for (int i = 0; i < findAwayBoxScore.getBoxScorePlayers().size(); i++) {
//				findAwayBoxScore.getBoxScorePlayers().get(i).setBoxScore(findAwayBoxScore);
//			}
			gameRepository.save(findGame);
			findGame.setStatusCode(StatusCodeDAO.Updated);
		}
		return findGame;
	}

	@Override
	public Game delete(Long id) {
		Game findGame = getById(id);
		if (findGame != null && findGame.isFound()) {
			gameRepository.delete(findGame.getId());
			findGame.setStatusCode(StatusCodeDAO.Deleted);
			return findGame;
		}
		else {
			return new Game(StatusCodeDAO.NotFound);
		}
	}
}