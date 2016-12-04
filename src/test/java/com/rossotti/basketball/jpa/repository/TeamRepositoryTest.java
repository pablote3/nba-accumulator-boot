package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Team;
import com.rossotti.basketball.jpa.model.Team.Conference;
import com.rossotti.basketball.jpa.model.Team.Division;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamRepositoryTest {

	private TeamRepository teamRepository;

	@Autowired
	public void setTeamRepository(TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
	}

	@Test
	public void getById() {
		Team team = teamRepository.findOne(1L);
		Assert.assertEquals("Chicago Zephyr's", team.getFullName());
		Assert.assertEquals(1, team.getStandings().size());
	}

	@Test
	public void findAll() {
		List<Team> teams = teamRepository.findAll();
		Assert.assertEquals(12, teams.size());
	}

	@Test
	public void findByKey_Found_FromDate() {
		Team team = teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter("harlem-globetrotter's", LocalDate.of(2009, 7, 2), LocalDate.of(2009, 7, 2));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
	}

	@Test
	public void findByKey_Found_ToDate() {
		Team team = teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter("harlem-globetrotter's", LocalDate.of(2010, 6, 29), LocalDate.of(2010, 6, 29));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
	}

	@Test
	public void findByKey_NotFound_TeamKey() {
		Team team = teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter("harlem-hoopers", LocalDate.of(2009, 7, 2), LocalDate.of(2009, 7, 2));
		Assert.assertNull(team);
	}

	@Test
	public void findByKey_NotFound_BeforeAsOfDate() {
		Team team = teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter("harlem-globetrotter's", LocalDate.of(2009, 6, 30), LocalDate.of(2009, 6, 30));
		Assert.assertNull(team);
	}

	@Test
	public void findByKey_NotFound_AfterAsOfDate() {
		Team team = teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter("harlem-globetrotter's", LocalDate.of(2010, 7, 1), LocalDate.of(2010, 7, 1));
		Assert.assertNull(team);
	}

	@Test
	public void findByLastName_Found_FromDate() {
		Team team = teamRepository.findByLastNameAndFromDateBeforeAndToDateAfter("Globetrotter's", LocalDate.of(2009, 7, 2), LocalDate.of(2009, 7, 2));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
	}

	@Test
	public void findByLastName_Found_ToDate() {
		Team team = teamRepository.findByLastNameAndFromDateBeforeAndToDateAfter("Globetrotter's", LocalDate.of(2010, 6, 29), LocalDate.of(2010, 6, 29));
		Assert.assertEquals("Harlem Globetrotter's", team.getFullName());
	}

	@Test
	public void findByLastName_NotFound_TeamKey() {
		Team team = teamRepository.findByLastNameAndFromDateBeforeAndToDateAfter("Globetreker's", LocalDate.of(2009, 7, 2), LocalDate.of(2009, 7, 2));
		Assert.assertNull(team);
	}

	@Test
	public void findByLastName_NotFound_BeforeAsOfDate() {
		Team team = teamRepository.findByLastNameAndFromDateBeforeAndToDateAfter("Globetrotter's", LocalDate.of(2009, 6, 30), LocalDate.of(2009, 6, 30));
		Assert.assertNull(team);
	}

	@Test
	public void findByLastName_NotFound_AfterAsOfDate() {
		Team team = teamRepository.findByLastNameAndFromDateBeforeAndToDateAfter("Globetrotter's", LocalDate.of(2010, 7, 1), LocalDate.of(2010, 7, 1));
		Assert.assertNull(team);
	}

	@Test
	public void findByKey_Found() {
		List<Team> teams = teamRepository.findByTeamKey("st-louis-bomber's");
		Assert.assertEquals(2, teams.size());
	}

	@Test
	public void findByKey_NotFound() {
		List<Team> teams = teamRepository.findByTeamKey("st-louis-bombber's");
		Assert.assertEquals(0, teams.size());
	}

	@Test
	public void findByDate_Found() {
		List<Team> teams = teamRepository.findByFromDateBeforeAndToDateAfter(LocalDate.of(2009, 10, 30), LocalDate.of(2009, 10, 30));
		Assert.assertEquals(3, teams.size());
	}

	@Test
	public void findByDate_NotFound() {
		List<Team> teams = teamRepository.findByFromDateBeforeAndToDateAfter(LocalDate.of(1909, 10, 30), LocalDate.of(1909, 10, 30));
		Assert.assertEquals(0, teams.size());
	}

	@Test
	public void create_Created() {
		teamRepository.save(createMockTeam("baltimore-bullets", LocalDate.of(2006, 7, 1), LocalDate.of(9999, 12, 31), "Baltimore Bullets2"));
		Team findTeam = teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter("baltimore-bullets", LocalDate.of(2006, 7, 2), LocalDate.of(2006, 7, 20));
		Assert.assertEquals("Baltimore Bullets2", findTeam.getFullName());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_Existing() {
		teamRepository.save(createMockTeam("baltimore-bullets", LocalDate.of(2005, 7, 1), LocalDate.of(2006, 6, 30), "Baltimore Bullets"));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		teamRepository.save(createMockTeam("baltimore-bullets", LocalDate.of(2005, 7, 1), LocalDate.of(2006, 6, 30), null));
	}

	@Test
	public void update_Updated() {
		teamRepository.save(updateMockTeam(3L, "st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), "St. Louis Bombier's"));
		Team team = teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter("st-louis-bomber's", LocalDate.of(2010, 5, 30), LocalDate.of(2010, 5, 30));
		Assert.assertEquals("St. Louis Bombier's", team.getFullName());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void update_MissingRequiredData() {
		teamRepository.save(updateMockTeam(3L,"st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), null));
	}

	@Test
	public void delete_Deleted() {
		teamRepository.delete(10L);
		Team findTeam = teamRepository.findOne(10L);
		Assert.assertNull(findTeam);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void delete_NotFound() {
		teamRepository.delete(101L);
	}

	private Team createMockTeam(String key, LocalDate fromDate, LocalDate toDate, String fullName) {
		Team team = new Team();
		team.setTeamKey(key);
		team.setFromDate(fromDate);
		team.setToDate(toDate);
		team.setAbbr("SEA");
		team.setFirstName("Seattle");
		team.setLastName("Supersonics");
		team.setConference(Conference.West);
		team.setDivision(Division.Pacific);
		team.setSiteName("Key Arena");
		team.setCity("Seattle");
		team.setState("WA");
		team.setFullName(fullName);
		return team;
	}

	private Team updateMockTeam(Long id, String key, LocalDate fromDate, LocalDate toDate, String fullName) {
		Team team = createMockTeam(key, fromDate, toDate, fullName);
		team.setId(id);
		return team;
	}
}