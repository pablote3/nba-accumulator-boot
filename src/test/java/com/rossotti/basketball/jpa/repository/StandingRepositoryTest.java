package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Standing;
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

//	@Test
//	public void create_Created_AsOfDate() {
//		teamRepository.save(createMockTeam("seattle-supersonics", LocalDate.of(2012, 7, 1), LocalDate.of(9999, 12, 31), "Seattle Supersonics"));
//		Team findTeam = teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter("seattle-supersonics", LocalDate.of(2012, 7, 2), LocalDate.of(2012, 7, 2));
//		Assert.assertEquals("Seattle Supersonics", findTeam.getFullName());
//	}
//
//	@Test
//	public void create_Created_DateRange() {
//		teamRepository.save(createMockTeam("baltimore-bullets", LocalDate.of(2006, 7, 1), LocalDate.of(2006, 7, 3), "Baltimore Bullets2"));
//		Team findTeam = teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter("baltimore-bullets", LocalDate.of(2006, 7, 2), LocalDate.of(2006, 7, 2));
//		Assert.assertEquals("Baltimore Bullets2", findTeam.getFullName());
//	}
//
//	@Test(expected=IncorrectResultSizeDataAccessException.class)
//	public void create_OverlappingDates() {
//		teamRepository.save(createMockTeam("baltimore-bullets", LocalDate.of(2005, 7, 1), LocalDate.of(2005, 7, 3), "Baltimore Bullets"));
//		teamRepository.findByTeamKeyAndFromDateBeforeAndToDateAfter("baltimore-bullets", LocalDate.of(2005, 7, 2), LocalDate.of(2005, 7, 2));
//	}
//
//	@Test(expected=TransactionSystemException.class)
//	public void create_MissingRequiredData() {
//		Team team = new Team();
//		team.setTeamKey("missing-required-data-key");
//		teamRepository.save(team);
//	}
//
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