package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.BoxScore;
import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.util.DateTimeUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

	private GameService gameService;

	@Autowired
	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

	@Test
	public void getById() {
		Game game = gameService.getById(5L);
		Assert.assertEquals(Game.GameStatus.Postponed, game.getStatus());
		Assert.assertEquals("Baltimore Bullets", game.getBoxScoreHome().getTeam().getFullName());
	}

	@Test
	public void listAll() {
		List<Game> games = (List<Game>)gameService.listAll();
		Assert.assertTrue(games.size() >= 9);
	}

	@Test
	public void findByTeamKeyAndAsOfDate_Found() {
		Game game = gameService.findByTeamKeyAndAsOfDate("chicago-zephyr's", LocalDate.of(2015, 10, 27));
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 0), game.getGameDateTime());
		Assert.assertEquals("Harlem Globetrotter's", game.getBoxScoreAway().getTeam().getFullName());
		Assert.assertTrue(game.getBoxScoreAway().getPoints().equals((short)98));
		Assert.assertTrue(game.isFound());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_NotFound_TeamKey() {
		Game game = gameService.findByTeamKeyAndAsOfDate("chicago-zephyrd", LocalDate.of(2015, 10, 27));
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_NotFound_AsOfDate() {
		Game game = gameService.findByTeamKeyAndAsOfDate("chicago-zephyr's", LocalDate.of(2015, 10, 20));
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void findByTeamKeyAndAsOfDateSeason_Found() {
		List<Game> games = gameService.findByTeamKeyAndAsOfDateSeason("chicago-zephyr's", LocalDate.of(2015, 10, 28));
		Assert.assertEquals(2, games.size());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 0), games.get(0).getGameDateTime());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 28, 20, 0), games.get(1).getGameDateTime());
	}

	@Test
	public void findByTeamKeyAndAsOfDateSeason_NotFound() {
		List<Game> games = gameService.findByTeamKeyAndAsOfDateSeason("chicago-zephyr's", LocalDate.of(2015, 10, 25));
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void findByAsOfDate_Found() {
		List<Game> games = gameService.findByAsOfDate(LocalDate.of(2015, 10, 27));
		Assert.assertEquals(3, games.size());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 30), games.get(0).getGameDateTime());
		Assert.assertTrue(games.get(0).isScheduled());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 21, 0), games.get(1).getGameDateTime());
		Assert.assertTrue(games.get(1).isScheduled());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 0), games.get(2).getGameDateTime());
		Assert.assertTrue(games.get(2).isCompleted());
	}

	@Test
	public void findByAsOfDate_NotFound() {
		List<Game> games = gameService.findByAsOfDate(LocalDate.of(2014, 10, 27));
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void findPreviousByTeamKeyAndFromDateAndToDate_Found() {
		LocalDateTime gameDateTime = gameService.findPreviousByTeamKeyAsOfDate("chicago-zephyr's", LocalDate.of(2015, 10, 30));
		Assert.assertEquals(LocalDateTime.of(2015, 10, 28, 20, 0), gameDateTime);
	}

	@Test
	public void findPreviousByTeamKeyAndFromDateAndToDate_NotFound_TeamKey() {
		LocalDateTime gameDateTime = gameService.findPreviousByTeamKeyAsOfDate("chicago-zephyry", LocalDate.of(2015, 10, 30));
		Assert.assertNull(gameDateTime);
	}

	@Test
	public void findPreviousByTeamKeyAndFromDateAndToDate_NotFound_AsOfDate() {
		LocalDateTime gameDateTime = gameService.findPreviousByTeamKeyAsOfDate("chicago-zephyr's", LocalDate.of(2015, 10, 27));
		Assert.assertNull(gameDateTime);
	}

	@Test
	public void create_Created() {
		Game createGame = gameService.create(createMockGame(LocalDateTime.of(2016, 10, 11, 22, 0), 23L, 1L, "chicago-zephyr's", 24L, 2L, "harlem-globetrotter's", Game.GameStatus.Scheduled));
		Game findGame = gameService.findByTeamKeyAndAsOfDate("chicago-zephyr's", LocalDate.of(2016, 10, 11));
		Assert.assertTrue(createGame.isCreated());
		Assert.assertEquals(2, findGame.getBoxScores().size());
		Assert.assertEquals("Harlem Globetrotter's", findGame.getBoxScoreAway().getTeam().getFullName());
	}

	@Test
	public void create_Exists() {
		Game createGame = gameService.create(createMockGame(LocalDateTime.of(2015, 10, 27, 20, 30), 3L, 3L, "st-louis-bomber's", 4L, 4L, "salinas-cowboys", Game.GameStatus.Scheduled));
		Assert.assertTrue(createGame.isFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		gameService.create(createMockGame(LocalDateTime.of(2016, 10, 13, 22, 0), 21L, 1L, "chicago-zephyr's", 22L, 2L, "harlem-globetrotter's", null));
	}

//	@Test
//	public void update_Updated() {
//		Game updateGame = gameService.update(createMockGame("Forte", "Brian", LocalDate.of(2010, 4, 25), LocalDate.of(2012, 12, 31), "19"));
//		Game game = gameService.findByLastNameAndFirstNameAndAsOfDate("Forte", "Brian", LocalDate.of(2010, 4, 25));
//		Assert.assertEquals("19", game.getNumber());
//		Assert.assertEquals(LocalDate.of(2012, 12, 31), game.getToDate());
//		Assert.assertTrue(updateGame.isUpdated());
//	}
//
//	@Test
//	public void update_NotFound() {
//		Game updateGame = gameService.update(createMockGame("Forte", "Brian", LocalDate.of(2009, 4, 25), LocalDate.of(2009, 12, 31), "19"));
//		Assert.assertTrue(updateGame.isNotFound());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void update_MissingRequiredData() {
//		gameService.update(createMockGame("Forte", "Brian", LocalDate.of(2010, 4, 25), LocalDate.of(2012, 12, 31), null));
//	}
//
//	@Test
//	public void delete_Deleted() {
//		Game deleteGame = gameService.delete(21L);
//		Game findGame = gameService.getById(21L);
//		Assert.assertNull(findGame);
//		Assert.assertTrue(deleteGame.isDeleted());
//	}
//
//	@Test
//	public void delete_NotFound() {
//		Game deleteGame = gameService.delete(101L);
//		Assert.assertTrue(deleteGame.isNotFound());
//	}

	private Game createMockGame(LocalDateTime gameDateTime, Long boxScoreIdHome, Long teamIdHome, String teamKeyHome, Long boxScoreIdAway, Long teamIdAway, String teamKeyAway, Game.GameStatus status) {
		Game game = new Game();
		game.setGameDateTime(gameDateTime);
		game.setSeasonType(Game.SeasonType.Regular);
		game.setStatus(status);
		game.addBoxScore(createMockBoxScore(game, boxScoreIdHome, teamIdHome, teamKeyHome, BoxScore.Location.Home));
		game.addBoxScore(createMockBoxScore(game, boxScoreIdAway, teamIdAway, teamKeyAway, BoxScore.Location.Away));
		return game;
	}

	private BoxScore createMockBoxScore(Game game, Long boxScoreId, Long teamId, String teamKey, BoxScore.Location location) {
		BoxScore boxScore = new BoxScore();
		boxScore.setGame(game);
		boxScore.setTeam(getMockTeam(teamId, teamKey));
		boxScore.setLocation(location);
		return boxScore;
	}

	private Team getMockTeam(Long teamId, String teamKey) {
		Team team = new Team();
		team.setId(teamId);
		team.setTeamKey(teamKey);
		return team;
	}

}