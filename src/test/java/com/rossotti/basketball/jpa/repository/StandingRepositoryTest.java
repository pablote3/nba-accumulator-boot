package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Standing;
import com.rossotti.basketball.jpa.model.Team;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StandingRepositoryTest {

	private StandingRepository standingRepository;

	@Autowired
	public void setStandingRepository(StandingRepository standingRepository) {
		this.standingRepository = standingRepository;
	}

	@Test
	public void getById() {
		Standing standing = standingRepository.findOne(1L);
		Assert.assertEquals("1st", standing.getOrdinalRank());
		Assert.assertEquals("Chicago Zephyr\'s", standing.getTeam().getFullName());
	}

	@Test
	public void findAll() {
		List<Standing> standings = (List<Standing>)standingRepository.findAll();
		Assert.assertEquals(4, standings.size());
	}

	@Test
	public void findByTeamKeyAsOfDate_Found() {
		Standing standing = standingRepository.findByTeamKeyAndStandingDate("chicago-zephyr's", LocalDate.of(2015, 10, 30));
		Assert.assertEquals("1st", standing.getOrdinalRank());
	}

	@Test
	public void findByTeamKeyAsOfDate_NotFound_TeamKey() {
		Standing standing = standingRepository.findByTeamKeyAndStandingDate("chicago-zephyr", LocalDate.of(2015, 10, 30));
		Assert.assertNull(standing);
	}

	@Test
	public void findByTeamKeyAsOfDate_NotFound_AsOfDate() {
		Standing standing = standingRepository.findByTeamKeyAndStandingDate("chicago-zephyr's", LocalDate.of(2015, 10, 29));
		Assert.assertNull(standing);
	}

	@Test
	public void findByAsOfDate_Found() {
		List<Standing> standings = standingRepository.findByStandingDate(LocalDate.of(2015, 10, 30));
		Assert.assertEquals(2, standings.size());
	}

	@Test
	public void findByAsOfDate_NotFound() {
		List<Standing> standings = standingRepository.findByStandingDate(LocalDate.of(2015, 10, 29));
		Assert.assertEquals(0, standings.size());
	}

	@Test
	public void findByTeamKey_Found() {
		List<Standing> standings = standingRepository.findByTeamKey("st-louis-bomber's");
		Assert.assertEquals(2, standings.size());
	}

	@Test
	public void findByTeamKey_NotFound() {
		List<Standing> standings = standingRepository.findByTeamKey("st-louis-bomber");
		Assert.assertEquals(0, standings.size());
	}

	@Test
	public void create_Created() {
		standingRepository.save(createMockStanding(20L, LocalDate.of(2012, 7, 1)));
		Standing findStanding = standingRepository.findByTeamKeyAndStandingDate("chicago-bulls", LocalDate.of(2012, 7, 1));
		Assert.assertTrue(findStanding.getConferenceWins().equals((short)7));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_Existing() {
		standingRepository.save(createMockStanding(1L, LocalDate.of(2015, 10, 30)));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		Standing standing = new Standing();
		standing.setStandingDate(LocalDate.of(2012, 7, 1));
		standingRepository.save(standing);
	}

//	@Test
//	public void update_Updated() {
//		teamRepository.save(updateMockTeam(3L, "st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), "St. Louis Bombier's"));
//		Team team = teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter("st-louis-bomber's", LocalDate.of(2010, 5, 30), LocalDate.of(2010, 5, 30));
//		Assert.assertEquals("St. Louis Bombier's", team.getFullName());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void update_MissingRequiredData() {
//		teamRepository.save(updateMockTeam(3L,"st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), null));
//	}
//
//	@Test
//	public void delete_Deleted() {
//		teamRepository.delete(10L);
//		Team findTeam = teamRepository.findOne(10L);
//		Assert.assertNull(findTeam);
//	}
//
//	@Test(expected = EmptyResultDataAccessException.class)
//	public void delete_NotFound() {
//		teamRepository.delete(101L);
//	}

	private Standing createMockStanding(Long id, LocalDate asOfDate) {
		Standing standing = new Standing();
		standing.setTeam(getMockTeam(id));
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

	private Team getMockTeam(Long id) {
		Team team = new Team();
		team.setId(id);
		return team;
	}

//	private Team updateMockTeam(Long id, String key, LocalDate fromDate, LocalDate toDate, String fullName) {
//		Team team = new Team();
//		team.setId(id);
//		team.setTeamKey(key);
//		team.setAbbr("SLB");
//		team.setFromDate(fromDate);
//		team.setToDate(toDate);
//		team.setFirstName("St. Louis");
//		team.setLastName("Bombiers");
//		team.setConference(Conference.East);
//		team.setDivision(Division.Southwest);
//		team.setSiteName("St. Louis Arena");
//		team.setCity("St. Louis");
//		team.setState("MO");
//		team.setFullName(fullName);
//		return team;
//	}
}