package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Team;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamServiceTest {

	private TeamService teamService;

	@Autowired
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}

	@Test
	public void getById() {
		Team team = teamService.getById(1L);
		Assert.assertEquals("Chicago Zephyr's", team.getFullName());
	}

	@Test
	public void findByKey_Found_FromDate() {
		Team findTeam = teamService.findByTeamKeyAndDate("harlem-globetrotter's", LocalDate.of(2009, 7, 1));
		Assert.assertEquals("Harlem Globetrotter's", findTeam.getFullName());
	}

	@Test
	public void findByKey_Found_ToDate() {
		Team findTeam = teamService.findByTeamKeyAndDate("harlem-globetrotter's", LocalDate.of(2010, 06, 30));
		Assert.assertEquals("Harlem Globetrotter's", findTeam.getFullName());
		Assert.assertTrue(findTeam.isFound());
	}

	@Test
	public void findByKey_NotFound_TeamKey() {
		Team findTeam = teamService.findByTeamKeyAndDate("harlem-hooper's", LocalDate.of(2009, 07, 1));
		Assert.assertTrue(findTeam.isNotFound());
	}

	@Test
	public void findByKey_NotFound_BeforeAsOfDate() {
		Team findTeam = teamService.findByTeamKeyAndDate("harlem-globetrotter's", LocalDate.of(2009, 06, 30));
		Assert.assertTrue(findTeam.isNotFound());
	}

	@Test
	public void findByKey_NotFound_AfterAsOfDate() {
		Team findTeam = teamService.findByTeamKeyAndDate("harlem-globetrotter's", LocalDate.of(2010, 07, 1));
		Assert.assertTrue(findTeam.isNotFound());
	}

//	@Test
//	public void findTeamByLastName_Found_FromDate() {
//		Team findTeam = teamService.findTeamByLastName("Globetrotter's", new LocalDate("2009-07-01"));
//		Assert.assertEquals("Harlem Globetrotter's", findTeam.getFullName());
//		Assert.assertTrue(findTeam.isFound());
//	}
//
//	@Test
//	public void findTeamByLastName_Found_ToDate() {
//		Team findTeam = teamService.findTeamByLastName("Globetrotter's", new LocalDate("2010-06-30"));
//		Assert.assertEquals("Harlem Globetrotter's", findTeam.getFullName());
//		Assert.assertTrue(findTeam.isFound());
//	}
//
//	@Test
//	public void findTeamByLastName_NotFound_TeamKey() {
//		Team findTeam = teamService.findTeamByLastName("Globetreker's", new LocalDate("2009-07-01"));
//		Assert.assertTrue(findTeam.isNotFound());
//	}
//
//	@Test
//	public void findTeamByLastName_NotFound_BeforeAsOfDate() {
//		Team findTeam = teamService.findTeamByLastName("Globetrotter's", new LocalDate("2009-06-30"));
//		Assert.assertTrue(findTeam.isNotFound());
//	}
//
//	@Test
//	public void findTeamByLastName_NotFound_AfterAsOfDate() {
//		Team findTeam = teamService.findTeamByLastName("Globetrotter's", new LocalDate("2010-07-01"));
//		Assert.assertTrue(findTeam.isNotFound());
//	}

	@Test
	public void listAll() {
		List<Team> teams = (List<Team>)teamService.listAll();
		Assert.assertEquals(11, teams.size());
	}

	@Test
	public void findByTeamKey() {
		List<Team> teams = teamService.findByTeamKey("salinas-cowboys");
		Assert.assertEquals("Salinas Cowboys", teams.get(0).getFullName());
	}

//	@Test
//	public void findTeamsByKey_Found() {
//		List<Team> teams = teamService.findTeams("st-louis-bomber's");
//		Assert.assertEquals(2, teams.size());
//	}
//
//	@Test
//	public void findTeamsByKey_NotFound() {
//		List<Team> teams = teamService.findTeams("st-louis-bombber's");
//		Assert.assertEquals(0, teams.size());
//	}
//
//	@Test
//	public void findTeamsByDateRange_Found() {
//		List<Team> teams = teamService.findTeams(new LocalDate("2009-10-31"));
//		Assert.assertEquals(3, teams.size());
//	}
//
//	@Test
//	public void findTeamsByDateRange_NotFound() {
//		List<Team> teams = teamService.findTeams(new LocalDate("1909-10-31"));
//		Assert.assertEquals(0, teams.size());
//	}
//
//	@Test
//	public void createTeam_Created_AsOfDate() {
//		Team createTeam = teamService.createTeam(createMockTeam("seattle-supersonics", new LocalDate("2012-07-01"), new LocalDate("9999-12-31"), "Seattle Supersonics"));
//		Team findTeam = teamService.findTeam("seattle-supersonics", new LocalDate("2012-07-01"));
//		Assert.assertTrue(createTeam.isCreated());
//		Assert.assertEquals("Seattle Supersonics", findTeam.getFullName());
//	}
//
//	@Test
//	public void createTeam_Created_DateRange() {
//		Team createTeam = teamService.createTeam(createMockTeam("baltimore-bullets", new LocalDate("2006-07-01"), new LocalDate("2006-07-02"), "Baltimore Bullets2"));
//		Team findTeam = teamService.findTeam("baltimore-bullets", new LocalDate("2006-07-01"));
//		Assert.assertTrue(createTeam.isCreated());
//		Assert.assertEquals("Baltimore Bullets2", findTeam.getFullName());
//	}
//
//	public void createTeam_OverlappingDates() {
//		Team createTeam = teamService.createTeam(createMockTeam("baltimore-bullets", new LocalDate("2005-07-01"), new LocalDate("2005-07-01"), "Baltimore Bullets"));
//		Assert.assertTrue(createTeam.isFound());
//	}
//
//	@Test(expected=PropertyValueException.class)
//	public void createTeam_MissingRequiredData() {
//		Team team = new Team();
//		team.setTeamKey("missing-required-data-key");
//		teamService.createTeam(team);
//	}
//
//	@Test
//	public void updateTeam() {
//		teamService.updateTeam(updateMockTeam("st-louis-bomber's", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"), "St. Louis Bombier's"));
//		Team team = teamService.findTeam("st-louis-bomber's", new LocalDate("2010-05-30"));
//		Assert.assertEquals("St. Louis Bombier's", team.getFullName());
//	}
//
//	@Test
//	public void updateTeam_NotFound() {
//		teamService.updateTeam(updateMockTeam("st-louis-bomb's", new LocalDate("2009-07-01"), new LocalDate("2010-07-01"), "St. Louis Bombier's"));
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void updateTeam_MissingRequiredData() {
//		Team team = updateMockTeam("st-louis-bomber's", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"), null);
//		teamService.updateTeam(team);
//	}
//
//	@Test
//	public void deleteTeam_Deleted() {
//		Team deleteTeam = teamService.deleteTeam("rochester-royals", new LocalDate("2009-06-30"));
//		teamService.deleteTeam("rochester-royals", new LocalDate("2009-06-30"));
//		Team findTeam = teamService.findTeam("rochester-royals", new LocalDate("2009-06-30"));
//		Assert.assertTrue(deleteTeam.isDeleted());
//		Assert.assertTrue(findTeam.isNotFound());
//	}
//
//	@Test
//	public void deleteTeam_NotFound() {
//		Team deleteTeam = teamService.deleteTeam("rochester-royales", new LocalDate("2009-07-01"));
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
