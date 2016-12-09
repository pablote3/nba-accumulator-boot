package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.*;
import com.rossotti.basketball.util.DateTimeUtil;
import com.rossotti.basketball.jpa.model.RosterPlayer.Position;
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
		Assert.assertEquals(12, games.size());
	}

	@Test
	public void findByTeamKeyAndFromDateAndToDate_Found() {
		Game game = gameRepository.findByTeamKeyAndFromDateAndToDate("chicago-zephyr's", DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2015, 10, 27)), DateTimeUtil.getLocalDateTimeMax(LocalDate.of(2015, 10, 27)));
		Assert.assertEquals(LocalDateTime.of(2015, 10, 27, 20, 0), game.getGameDateTime());
		Assert.assertEquals("Harlem Globetrotter's", game.getBoxScoreAway().getTeam().getFullName());
		Assert.assertEquals(3, game.getGameOfficials().size());
		Assert.assertEquals("QuestionableCall", game.getGameOfficials().get(2).getOfficial().getLastName());
		Assert.assertTrue(game.getBoxScoreAway().getPoints().equals((short)98));
		Assert.assertEquals(1, game.getBoxScoreHome().getBoxScorePlayers().size());
		Assert.assertEquals(RosterPlayer.Position.SG, game.getBoxScoreHome().getBoxScorePlayers().get(0).getRosterPlayer().getPosition());
		Assert.assertEquals(2, game.getBoxScoreAway().getBoxScorePlayers().size());
		Assert.assertTrue(game.getBoxScoreAway().getBoxScorePlayers().get(1).getPoints().equals((short)5));
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

	@Test
	public void create_Created() {
		gameRepository.save(createMockGame(30L, LocalDateTime.of(2016, 10, 10, 21, 0), 21L, 1L, "chicago-zephyr's", 22L, 2L, "harlem-globetrotter's", Game.GameStatus.Scheduled));
		Game findGame = gameRepository.findByTeamKeyAndFromDateAndToDate("chicago-zephyr's", DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2016, 10, 10)), DateTimeUtil.getLocalDateTimeMax(LocalDate.of(2016, 10, 10)));
		Assert.assertEquals(2, findGame.getBoxScores().size());
		Assert.assertEquals("Harlem Globetrotter's", findGame.getBoxScoreAway().getTeam().getFullName());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		gameRepository.save(createMockGame(31L, LocalDateTime.of(2015, 12, 27, 21, 0), 3L, 3L, "st-louis-bomber's", 4L, 4L, "salinas-cowboys", null));
	}

	@Test
	public void update_Updated() {
		gameRepository.save(updateMockGame(8L, LocalDateTime.of(2015, 10, 15, 10, 0), 15L, 6L, "cleveland-rebels", 16L, 5L, "baltimore-bullets", Game.GameStatus.Completed));
		Game findGame = gameRepository.findByTeamKeyAndFromDateAndToDate("cleveland-rebels", DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2015, 10, 15)), DateTimeUtil.getLocalDateTimeMax(LocalDate.of(2015, 10, 15)));
		Assert.assertEquals(3, findGame.getGameOfficials().size());
		Assert.assertEquals("MissedCa'll", findGame.getGameOfficials().get(1).getOfficial().getLastName());
		Assert.assertEquals(2, findGame.getBoxScores().size());
		Assert.assertEquals("Baltimore Bullets", findGame.getBoxScoreAway().getTeam().getFullName());
		Assert.assertTrue(findGame.getBoxScoreAway().getFreeThrowMade().equals((short)18));
		Assert.assertEquals(1, findGame.getBoxScoreAway().getBoxScorePlayers().size());
		Assert.assertTrue(findGame.getBoxScoreAway().getBoxScorePlayers().get(0).getFreeThrowMade().equals((short)2));
		Assert.assertEquals(2, findGame.getBoxScoreHome().getBoxScorePlayers().size());
		Assert.assertTrue(findGame.getBoxScoreHome().getBoxScorePlayers().get(0).getFreeThrowMade().equals((short)4));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void update_MissingRequiredData() {
		gameRepository.save(updateMockGame(9L, LocalDateTime.of(2015, 11, 24, 10, 0), 17L, 9L, "detroit-pistons", 18L, 8L, "st-louis-bomber's", null));
	}

	@Test
	public void delete_Deleted() {
		gameRepository.delete(11L);
		Game standing = gameRepository.findOne(11L);
		Assert.assertNull(standing);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void delete_NotFound() {
		gameRepository.delete(101L);
	}

	private Game createMockGame(Long id, LocalDateTime gameDateTime, Long boxScoreIdHome, Long teamIdHome, String teamKeyHome, Long boxScoreIdAway, Long teamIdAway, String teamKeyAway, Game.GameStatus status) {
		Game game = new Game();
		game.setId(id);
		game.setGameDateTime(gameDateTime);
		game.setSeasonType(Game.SeasonType.Regular);
		game.setStatus(status);
		game.addBoxScore(createMockBoxScore(game, boxScoreIdHome, teamIdHome, teamKeyHome, BoxScore.Location.Home));
		game.addBoxScore(createMockBoxScore(game, boxScoreIdAway, teamIdAway, teamKeyAway, BoxScore.Location.Away));
		return game;
	}

	private BoxScore createMockBoxScore(Game game, Long boxScoreId, Long teamId, String teamKey, BoxScore.Location location) {
		BoxScore boxScore = new BoxScore();
		boxScore.setId(boxScoreId);
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

	private Game updateMockGame(Long id, LocalDateTime gameDateTime, Long boxScoreIdHome, Long teamIdHome, String teamKeyHome, Long boxScoreIdAway, Long teamIdAway, String teamKeyAway, Game.GameStatus status) {
		Game game = createMockGame(id, gameDateTime, boxScoreIdHome, teamIdHome, teamKeyHome, boxScoreIdAway, teamIdAway, teamKeyAway, status);
		game.addGameOfficial(createMockGameOfficial(game, 1L, "LateCall", "Joe"));
		game.addGameOfficial(createMockGameOfficial(game, 3L, "MissedCa'll", "Mike"));
		game.addGameOfficial(createMockGameOfficial(game, 4L, "QuestionableCall", "Hefe"));
		updateMockBoxScoreHome(game.getBoxScoreHome());
		updateMockBoxScoreAway(game.getBoxScoreAway());
		return game;
	}

	private GameOfficial createMockGameOfficial(Game game, Long officialId, String lastName, String firstName) {
		GameOfficial gameOfficial = new GameOfficial();
		gameOfficial.setGame(game);
		gameOfficial.setOfficial(getMockOfficial(officialId, lastName, firstName));
		return gameOfficial;
	}

	private Official getMockOfficial(Long officialId, String lastName, String firstName) {
		Official official = new Official();
		official.setId(officialId);
		official.setLastName(lastName);
		official.setFirstName(firstName);
		return official;
	}

	private void updateMockBoxScoreHome(BoxScore homeBoxScore) {
		homeBoxScore.addBoxScorePlayer(createMockBoxScorePlayerHome_0(homeBoxScore));
		homeBoxScore.addBoxScorePlayer(createMockBoxScorePlayerHome_1(homeBoxScore));
		homeBoxScore.setMinutes((short)240);
		homeBoxScore.setPoints((short)98);
		homeBoxScore.setAssists((short)14);
		homeBoxScore.setTurnovers((short)5);
		homeBoxScore.setSteals((short)7);
		homeBoxScore.setBlocks((short)5);
		homeBoxScore.setFieldGoalAttempts((short)44);
		homeBoxScore.setFieldGoalMade((short)22);
		homeBoxScore.setFieldGoalPercent((float).500);
		homeBoxScore.setThreePointAttempts((short)10);
		homeBoxScore.setThreePointMade((short)6);
		homeBoxScore.setThreePointPercent((float).6);
		homeBoxScore.setFreeThrowAttempts((short)20);
		homeBoxScore.setFreeThrowMade((short)10);
		homeBoxScore.setFreeThrowPercent((float).500);
		homeBoxScore.setReboundsOffense((short)25);
		homeBoxScore.setReboundsDefense((short)5);
		homeBoxScore.setPersonalFouls((short)18);
	}

	private void updateMockBoxScoreAway(BoxScore awayBoxScore) {
		awayBoxScore.addBoxScorePlayer(createMockBoxScorePlayerAway(awayBoxScore));
		awayBoxScore.setMinutes((short)240);
		awayBoxScore.setPoints((short)98);
		awayBoxScore.setAssists((short)14);
		awayBoxScore.setTurnovers((short)5);
		awayBoxScore.setSteals((short)7);
		awayBoxScore.setBlocks((short)5);
		awayBoxScore.setFieldGoalAttempts((short)44);
		awayBoxScore.setFieldGoalMade((short)22);
		awayBoxScore.setFieldGoalPercent((float).500);
		awayBoxScore.setThreePointAttempts((short)10);
		awayBoxScore.setThreePointMade((short)6);
		awayBoxScore.setThreePointPercent((float).6);
		awayBoxScore.setFreeThrowAttempts((short)20);
		awayBoxScore.setFreeThrowMade((short)18);
		awayBoxScore.setFreeThrowPercent((float).500);
		awayBoxScore.setReboundsOffense((short)25);
		awayBoxScore.setReboundsDefense((short)5);
		awayBoxScore.setPersonalFouls((short)18);
	}

	private BoxScorePlayer createMockBoxScorePlayerHome_0(BoxScore boxScore) {
		BoxScorePlayer homeBoxScorePlayer = new BoxScorePlayer();
		homeBoxScorePlayer.setBoxScore(boxScore);
		homeBoxScorePlayer.setRosterPlayer(getMockRosterPlayer(2L, "Puzdrakiewicz", "Luke", LocalDate.of(2002, 2, 20), LocalDate.of(2009, 11, 30), LocalDate.of(9999, 12, 31)));
		homeBoxScorePlayer.setPosition(Position.F);
		homeBoxScorePlayer.setFreeThrowMade((short)4);
		return homeBoxScorePlayer;
	}

	private BoxScorePlayer createMockBoxScorePlayerHome_1(BoxScore boxScore) {
		BoxScorePlayer homeBoxScorePlayer = new BoxScorePlayer();
		homeBoxScorePlayer.setBoxScore(boxScore);
		homeBoxScorePlayer.setRosterPlayer(getMockRosterPlayer(3L, "Puzdrakiewicz", "Thad", LocalDate.of(1966, 6, 2), LocalDate.of(2009, 10, 30), LocalDate.of(2009, 11, 4)));
		homeBoxScorePlayer.setPosition(Position.C);
		homeBoxScorePlayer.setFreeThrowMade((short)0);
		return homeBoxScorePlayer;
	}

	private BoxScorePlayer createMockBoxScorePlayerAway(BoxScore boxScore) {
		BoxScorePlayer awayBoxScorePlayer = new BoxScorePlayer();
		awayBoxScorePlayer.setBoxScore(boxScore);
		awayBoxScorePlayer.setRosterPlayer(getMockRosterPlayer(5L, "Puzdrakiewicz", "Junior", LocalDate.of(1966, 6, 10), LocalDate.of(2009, 10, 30), LocalDate.of(9999, 12, 31)));
		awayBoxScorePlayer.setPosition(Position.SG);
		awayBoxScorePlayer.setFreeThrowMade((short)2);
		return awayBoxScorePlayer;
	}

	private RosterPlayer getMockRosterPlayer(Long rosterPlayerId, String lastName, String firstName, LocalDate birthdate, LocalDate fromDate, LocalDate toDate) {
		RosterPlayer rosterPlayer = new RosterPlayer();
		rosterPlayer.setId(rosterPlayerId);
		rosterPlayer.setPlayer(getMockPlayer(lastName, firstName, birthdate));
		rosterPlayer.setFromDate(fromDate);
		rosterPlayer.setToDate(toDate);
		rosterPlayer.setPosition(Position.C);
		rosterPlayer.setNumber("99");
		return rosterPlayer;
	}

	private Player getMockPlayer(String lastName, String firstName, LocalDate birthdate) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(birthdate);
		return player;
	}
}