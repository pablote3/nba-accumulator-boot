package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Standing;
import com.rossotti.basketball.jpa.model.Team;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StandingServiceTest {

	private StandingService standingService;

	@Autowired
	public void setStandingService(StandingService standingService) {
		this.standingService = standingService;
	}

	@Test
	public void getById() {
		Standing standing = standingService.getById(1L);
		Assert.assertEquals("1st", standing.getOrdinalRank());
		Assert.assertEquals("Chicago Zephyr\'s", standing.getTeam().getFullName());
	}

	@Test
	public void listAll() {
		List<Standing> standings = (List<Standing>)standingService.listAll();
		Assert.assertTrue(standings.size() >= 4);
	}

	@Test
	public void findByTeamKey_Found() {
		List<Standing> standings = standingService.findByTeamKey("st-louis-bomber's");
		Assert.assertEquals(2, standings.size());
	}

	@Test
	public void findByTeamKey_NotFound() {
		List<Standing> standings = standingService.findByTeamKey("st-louis-bomber");
		Assert.assertEquals(0, standings.size());
	}

	@Test
	public void findByAsOfDate_Found() {
		List<Standing> standings = standingService.findByAsOfDate(LocalDate.of(2015, 10, 30));
		Assert.assertEquals(2, standings.size());
	}

	@Test
	public void findByAsOfDate_NotFound() {
		List<Standing> standings = standingService.findByAsOfDate(LocalDate.of(2015, 10, 29));
		Assert.assertEquals(0, standings.size());
	}

	@Test
	public void findByTeamKeyAsOfDate_Found() {
		Standing standing = standingService.findByTeamKeyAndAsOfDate("chicago-zephyr's", LocalDate.of(2015, 10, 30));
		Assert.assertEquals("1st", standing.getOrdinalRank());
		Assert.assertTrue(standing.isFound());
	}

	@Test
	public void findByTeamKeyAsOfDate_NotFound_TeamKey() {
		Standing standing = standingService.findByTeamKeyAndAsOfDate("chicago-zephyr", LocalDate.of(2015, 10, 30));
		Assert.assertTrue(standing.isNotFound());
	}

	@Test
	public void findByTeamKeyAsOfDate_NotFound_AsOfDate() {
		Standing standing = standingService.findByTeamKeyAndAsOfDate("chicago-zephyr's", LocalDate.of(2015, 10, 29));
		Assert.assertTrue(standing.isNotFound());
	}

	@Test
	public void create_Created() {
		Standing createStanding = standingService.create(createMockStanding(21L, "utah-jazz", LocalDate.of(2012, 7, 1)));
		Standing findStanding = standingService.findByTeamKeyAndAsOfDate("utah-jazz", LocalDate.of(2012, 7, 1));
		Assert.assertTrue(createStanding.isCreated());
		Assert.assertTrue(findStanding.getConferenceWins().equals((short)7));
	}

	@Test
	public void create_Existing() {
		Standing createStanding = standingService.create(createMockStanding(1L, "chicago-zephyr's", LocalDate.of(2015, 10, 30)));
		Assert.assertTrue(createStanding.isFound());
	}

	@Test(expected=NullPointerException.class)
	public void create_MissingRequiredData() {
		Standing standing = new Standing();
		standing.setStandingDate(LocalDate.of(2012, 7, 1));
		standingService.create(standing);
	}

	@Test
	public void update_Updated() {
		Standing updateStanding = standingService.update(updateMockStanding(3L, "st-louis-bomber's", LocalDate.of(2015, 10, 31), "10th"));
		Standing standing = standingService.findByTeamKeyAndAsOfDate("st-louis-bomber's", LocalDate.of(2015, 10, 31));
		Assert.assertEquals("10th", standing.getOrdinalRank());
		Assert.assertTrue(updateStanding.isUpdated());
	}

	@Test
	public void update_NotFound() {
		Standing standing = standingService.update(updateMockStanding(3L, "st-louis-bomber's", LocalDate.of(2015, 11, 11), "10th"));
		Assert.assertTrue(standing.isNotFound());
	}

//	@Test(expected=DataIntegrityViolationException.class)
//	public void update_MissingRequiredData() {
//		teamService.update(updateMockTeam("st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), null));
//	}
//
//	@Test
//	public void delete_Deleted() {
//		Team deleteTeam = teamService.delete(7L);
//		Team findTeam = teamService.getById(7L);
//		Assert.assertNull(findTeam);
//		Assert.assertTrue(deleteTeam.isDeleted());
//	}
//
//	@Test
//	public void delete_NotFound() {
//		Team deleteTeam = teamService.delete(101L);
//		Assert.assertTrue(deleteTeam.isNotFound());
//	}

	private Standing createMockStanding(Long teamId, String teamKey, LocalDate asOfDate) {
		Standing standing = new Standing();
		standing.setTeam(getMockTeam(teamId, teamKey));
		standing.setStandingDate(asOfDate);
		standing.setRank((short)3);
		standing.setOrdinalRank("3rd");
		standing.setGamesWon((short)15);
		standing.setGamesLost((short)25);
		standing.setStreak("L5");
		standing.setStreakType("loss");
		standing.setStreakTotal((short)5);
		standing.setGamesBack((float)3.5);
		standing.setPointsFor((short)1895);
		standing.setPointsAgainst((short)2116);
		standing.setHomeWins((short)10);
		standing.setHomeLosses((short)10);
		standing.setAwayWins((short)5);
		standing.setAwayLosses((short)15);
		standing.setConferenceWins((short)7);
		standing.setConferenceLosses((short)8);
		standing.setLastFive("0-5");
		standing.setLastTen("3-7");
		standing.setGamesPlayed((short)40);
		standing.setPointsScoredPerGame((float)95.5);
		standing.setPointsAllowedPerGame((float)102.5);
		standing.setWinPercentage((float)0.375);
		standing.setPointDifferential((short)221);
		standing.setPointDifferentialPerGame((float)7.0);
		standing.setOpptGamesWon(4);
		standing.setOpptGamesPlayed(5);
		standing.setOpptOpptGamesWon(15);
		standing.setOpptOpptGamesPlayed(20);
		return standing;
	}

	private Team getMockTeam(Long id, String teamKey) {
		Team team = new Team();
		team.setId(id);
		team.setTeamKey(teamKey);
		return team;
	}

	private Standing updateMockStanding(Long teamId, String teamKey, LocalDate asOfDate, String ordinalRank) {
		Standing standing = new Standing();
		standing.setTeam(getMockTeam(teamId, teamKey));
		standing.setStandingDate(asOfDate);
		standing.setRank((short)10);
		standing.setOrdinalRank(ordinalRank);
		standing.setGamesWon((short)15);
		standing.setGamesLost((short)25);
		standing.setStreak("L5");
		standing.setStreakType("loss");
		standing.setStreakTotal((short)5);
		standing.setGamesBack((float)3.5);
		standing.setPointsFor((short)1895);
		standing.setPointsAgainst((short)2116);
		standing.setHomeWins((short)10);
		standing.setHomeLosses((short)10);
		standing.setAwayWins((short)5);
		standing.setAwayLosses((short)15);
		standing.setConferenceWins((short)7);
		standing.setConferenceLosses((short)8);
		standing.setLastFive("0-5");
		standing.setLastTen("3-7");
		standing.setGamesPlayed((short)40);
		standing.setPointsScoredPerGame((float)95.5);
		standing.setPointsAllowedPerGame((float)102.5);
		standing.setWinPercentage((float)0.375);
		standing.setPointDifferential((short)221);
		standing.setPointDifferentialPerGame((float)7.0);
		standing.setOpptGamesWon(4);
		standing.setOpptGamesPlayed(5);
		standing.setOpptOpptGamesWon(15);
		standing.setOpptOpptGamesPlayed(20);
		return standing;
	}
}
