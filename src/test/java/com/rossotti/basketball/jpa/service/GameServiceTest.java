package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Game;
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

//	@Test
//	public void create_Created_AsOfDate() {
//		Game createGame = gameService.create(createMockGame("BadCall", "Melvin", LocalDate.of(2006, 7, 6), LocalDate.of(9999, 12, 31), "996"));
//		Game findGame = gameService.findByLastNameAndFirstNameAndAsOfDate("BadCall", "Melvin", LocalDate.of(2006, 7, 6));
//		Assert.assertTrue(createGame.isCreated());
//		Assert.assertEquals("996", findGame.getNumber());
//	}
//
//	@Test
//	public void create_Created_DateRange() {
//		Game createGame = gameService.create(createMockGame("BadCall", "Melvon", LocalDate.of(2006, 7, 6), LocalDate.of(2006, 7, 10), "995"));
//		Game findGame = gameService.findByLastNameAndFirstNameAndAsOfDate("BadCall", "Melvon", LocalDate.of(2006, 7, 7));
//		Assert.assertTrue(createGame.isCreated());
//		Assert.assertEquals("995", findGame.getNumber());
//	}
//
//	@Test
//	public void create_OverlappingDates() {
//		Game createGame = gameService.create(createMockGame("QuestionableCall", "Hefe", LocalDate.of(2005, 7, 1), LocalDate.of(2006, 6, 20), "18"));
//		Assert.assertTrue(createGame.isFound());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void create_MissingRequiredData() {
//		gameService.create(createMockGame("BadCaller", "Melvyn", LocalDate.of(2006, 7, 6), LocalDate.of(2006, 7, 10), null));
//	}
//
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
//
//	private Game createMockGame(String lastName, String firstName, LocalDate fromDate, LocalDate toDate, String number) {
//		Game game = new Game();
//		game.setLastName(lastName);
//		game.setFirstName(firstName);
//		game.setFromDate(fromDate);
//		game.setToDate(toDate);
//		game.setNumber(number);
//		return game;
//	}
}