package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Game;
import com.rossotti.basketball.util.DateTimeUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameRepositoryTest {

	private GameRepository gameRepository;

	@Autowired
	public void setGameRepository(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Test
	public void getById() {
		Game game = gameRepository.findOne(5L);
		Assert.assertEquals(Game.GameStatus.Postponed, game.getStatus());
		Assert.assertEquals("Baltimore Bullets", game.getBoxScoreHome().getTeam().getFullName());
	}

	@Test
	public void findAll() {
		List<Game> games = gameRepository.findAll();
		Assert.assertEquals(10, games.size());
	}

	@Test
	public void findByTeamKeyAndFromDateAndToDate_Found() {
		Game game = gameRepository.findByTeamKeyAndFromDateAndToDate("chicago-zephyr's", DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2015, 10, 27)), DateTimeUtil.getLocalDateTimeMax(LocalDate.of(2015, 10, 27)));
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 0), game.getGameDateTime());
		Assert.assertEquals("Harlem Globetrotter's", game.getBoxScoreAway().getTeam().getFullName());
		Assert.assertTrue(game.getBoxScoreAway().getPoints().equals((short)98));
	}

	@Test
	public void findByTeamKeyAndFromDateAndToDate_NotFound_TeamKey() {
		Game game = gameRepository.findByTeamKeyAndFromDateAndToDate("baltimore-bullys", DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2015, 10, 29)), DateTimeUtil.getLocalDateTimeMax(LocalDate.of(2015, 10, 29)));
		Assert.assertNull(game);
	}

	@Test
	public void findByTeamKeyAndFromDateAndToDate_NotFound_AsOfDate() {
		Game game = gameRepository.findByTeamKeyAndFromDateAndToDate("baltimore-bullets", DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2014, 10, 29)), DateTimeUtil.getLocalDateTimeMax(LocalDate.of(2014, 10, 29)));
		Assert.assertNull(game);
	}

	@Test
	public void findByTeamKeyAndFromDateAndToDateSeason_Found() {
		List<Game> games = gameRepository.findByTeamKeyAndFromDateAndToDateSeason("baltimore-bullets", DateTimeUtil.getLocalDateTimeSeasonMin(LocalDate.of(2015, 10, 30)), DateTimeUtil.getLocalDateTimeSeasonMax(LocalDate.of(2015, 10, 27)));
		Assert.assertEquals(3, games.size());
	}

	@Test
	public void findByTeamKeyAndFromDateAndToDateSeason_NotFound_TeamKey() {
		List<Game> games = gameRepository.findByTeamKeyAndFromDateAndToDateSeason("baltimore-bullys", DateTimeUtil.getLocalDateTimeSeasonMin(LocalDate.of(2015, 10, 30)), DateTimeUtil.getLocalDateTimeSeasonMax(LocalDate.of(2015, 10, 27)));
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void findByTeamKeyAndFromDateAndToDateSeason_NotFound_AsOfDate() {
		List<Game> games = gameRepository.findByTeamKeyAndFromDateAndToDateSeason("baltimore-bullets", DateTimeUtil.getLocalDateTimeSeasonMin(LocalDate.of(2014, 10, 30)), DateTimeUtil.getLocalDateTimeSeasonMax(LocalDate.of(2014, 10, 27)));
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void findByFromDateAndToDate_Found() {
		List<Game> games = gameRepository.findByFromDateAndToDate(DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2015, 10, 27)), DateTimeUtil.getLocalDateTimeMax(LocalDate.of(2015, 10, 27)));
		Assert.assertEquals(3, games.size());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 30), games.get(0).getGameDateTime());
		Assert.assertTrue(games.get(0).isScheduled());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 21, 0), games.get(1).getGameDateTime());
		Assert.assertTrue(games.get(1).isScheduled());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 0), games.get(2).getGameDateTime());
		Assert.assertTrue(games.get(2).isCompleted());
	}

	@Test
	public void findByFromDateAndToDate_NotFound() {
		List<Game> games = gameRepository.findByFromDateAndToDate(DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2015, 10, 26)), DateTimeUtil.getLocalDateTimeMax(LocalDate.of(2015, 10, 26)));
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void findPreviousByTeamKeyAndFromDateAndToDate_Found() {
		List<LocalDateTime> gameDates = gameRepository.findPreviousByTeamKeyAndAsOfDate("chicago-zephyr's", DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2015, 10, 30)));
		Assert.assertEquals(2, gameDates.size());
		Assert.assertEquals(LocalDateTime.of(2015, 10, 28, 20, 0), gameDates.get(0));
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 0), gameDates.get(1));
	}

	@Test
	public void findPreviousByTeamKeyAndFromDateAndToDate_NotFound_TeamKey() {
		List<LocalDateTime> games = gameRepository.findPreviousByTeamKeyAndAsOfDate("chicago-zephyry", DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2015, 10, 28)));
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void findPreviousByTeamKeyAndFromDateAndToDate_NotFound_AsOfDate() {
		List<LocalDateTime> games = gameRepository.findPreviousByTeamKeyAndAsOfDate("chicago-zephyr's", DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2015, 10, 27)));
		Assert.assertEquals(0, games.size());
	}

//	@Test
//	public void create_Created() {
//		gameRepository.save(createMockGame(20L, LocalDate.of(2012, 7, 1), "10th"));
//		Game findGame = gameRepository.findByTeamKeyAndGameDate("chicago-bulls", LocalDate.of(2012, 7, 1));
//		Assert.assertEquals("10th", findGame.getOrdinalRank());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void create_Existing() {
//		gameRepository.save(createMockGame(1L, LocalDate.of(2015, 10, 30), "10th"));
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void create_MissingRequiredData() {
//		gameRepository.save(createMockGame(20L, LocalDate.of(2012, 7, 1), null));
//	}
//
//	@Test
//	public void update_Updated() {
//		gameRepository.save(updateMockGame(4L, 4L, LocalDate.of(2015, 10, 31), "10th"));
//		Game standing = gameRepository.findByTeamKeyAndGameDate("salinas-cowboys", LocalDate.of(2015, 10, 31));
//		Assert.assertEquals("10th", standing.getOrdinalRank());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void update_MissingRequiredData() {
//		gameRepository.save(updateMockGame(4L, 4L, LocalDate.of(2015, 10, 31), null));
//	}
//
//	@Test
//	public void delete_Deleted() {
//		gameRepository.delete(5L);
//		Game standing = gameRepository.findOne(5L);
//		Assert.assertNull(standing);
//	}
//
//	@Test(expected = EmptyResultDataAccessException.class)
//	public void delete_NotFound() {
//		gameRepository.delete(101L);
//	}
//
//	private Game createMockGame(Long teamId, LocalDate asOfDate, String ordinalRank) {
//		Game standing = new Game();
//		standing.setTeam(getMockTeam(teamId));
//		standing.setGameDate(asOfDate);
//		standing.setRank((short)3);
//		standing.setOrdinalRank(ordinalRank);
//		standing.setGamesWon((short)15);
//		standing.setGamesLost((short)25);
//		standing.setStreak("L5");
//		standing.setStreakType("loss");
//		standing.setStreakTotal((short)5);
//		standing.setGamesBack((float)3.5);
//		standing.setPointsFor((short)1895);
//		standing.setPointsAgainst((short)2116);
//		standing.setHomeWins((short)10);
//		standing.setHomeLosses((short)10);
//		standing.setAwayWins((short)5);
//		standing.setAwayLosses((short)15);
//		standing.setConferenceWins((short)7);
//		standing.setConferenceLosses((short)8);
//		standing.setLastFive("0-5");
//		standing.setLastTen("3-7");
//		standing.setGamesPlayed((short)40);
//		standing.setPointsScoredPerGame((float)95.5);
//		standing.setPointsAllowedPerGame((float)102.5);
//		standing.setWinPercentage((float)0.375);
//		standing.setPointDifferential((short)221);
//		standing.setPointDifferentialPerGame((float)7.0);
//		standing.setOpptGamesWon(4);
//		standing.setOpptGamesPlayed(5);
//		standing.setOpptOpptGamesWon(15);
//		standing.setOpptOpptGamesPlayed(20);
//		return standing;
//	}
//
//	private Team getMockTeam(Long id) {
//		Team team = new Team();
//		team.setId(id);
//		return team;
//	}
//
//	private Game updateMockGame(Long id, Long teamId, LocalDate asOfDate, String ordinalRank) {
//		Game standing = createMockGame(teamId, asOfDate, ordinalRank);
//		standing.setId(id);
//		return standing;
//	}
}