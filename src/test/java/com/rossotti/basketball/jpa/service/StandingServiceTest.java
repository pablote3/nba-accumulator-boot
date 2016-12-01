package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Standing;
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

//	@Test
//	public void create_Created_AsOfDate() {
//		Team createTeam = teamService.create(createMockTeam("sacramento-hornets", LocalDate.of(2012, 7, 1), LocalDate.of(9999, 12, 31), "Sacramento Hornets"));
//		Team findTeam = teamService.findByTeamKeyAndDate("sacramento-hornets", LocalDate.of(2012, 7, 1));
//		Assert.assertTrue(createTeam.isCreated());
//		Assert.assertEquals("Sacramento Hornets", findTeam.getFullName());
//	}
//
//	@Test
//	public void create_Created_DateRange() {
//		Team createTeam = teamService.create(createMockTeam("sacramento-rivercats", LocalDate.of(2006, 7, 1), LocalDate.of(2012, 7, 2), "Sacramento Rivercats"));
//		Team findTeam = teamService.findByTeamKeyAndDate("sacramento-rivercats", LocalDate.of(2006, 7, 1));
//		Assert.assertTrue(createTeam.isCreated());
//		Assert.assertEquals("Sacramento Rivercats", findTeam.getFullName());
//	}
//
//	@Test
//	public void create_OverlappingDates() {
//		Team createTeam = teamService.create(createMockTeam("cleveland-rebels", LocalDate.of(2010, 7, 1), LocalDate.of(2010, 7, 1), "Cleveland Rebels"));
//		Assert.assertTrue(createTeam.isFound());
//	}
//
//	@Test(expected=NullPointerException.class)
//	public void create_MissingRequiredData() {
//		Team team = new Team();
//		team.setTeamKey("missing-required-data-key");
//		teamService.create(team);
//	}
//
//	@Test
//	public void update_Updated() {
//		Team updateTeam = teamService.update(updateMockTeam("st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), "St. Louis Bombier's"));
//		Team team = teamService.findByTeamKeyAndDate("st-louis-bomber's", LocalDate.of(2010, 5, 30));
//		Assert.assertEquals("St. Louis Bombier's", team.getFullName());
//		Assert.assertTrue(updateTeam.isUpdated());
//	}
//
//	@Test
//	public void update_NotFound() {
//		Team team = teamService.update(updateMockTeam("st-louis-bomb's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 7, 1), "St. Louis Bombier's"));
//		Assert.assertTrue(team.isNotFound());
//	}
//
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
//
//	private Team createMockTeam(String key, LocalDate fromDate, LocalDate toDate, String fullName) {
//		Team team = new Team();
//		team.setTeamKey(key);
//		team.setFromDate(fromDate);
//		team.setToDate(toDate);
//		team.setAbbr("SEA");
//		team.setFirstName("Seattle");
//		team.setLastName("Supersonics");
//		team.setConference(Conference.West);
//		team.setDivision(Division.Pacific);
//		team.setSiteName("Key Arena");
//		team.setCity("Seattle");
//		team.setState("WA");
//		team.setFullName(fullName);
//		return team;
//	}
//
//	private Team updateMockTeam(String key, LocalDate fromDate, LocalDate toDate, String fullName) {
//		Team team = new Team();
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
