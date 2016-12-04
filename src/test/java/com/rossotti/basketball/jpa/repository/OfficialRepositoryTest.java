package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Official;
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
public class OfficialRepositoryTest {

	private OfficialRepository officialRepository;

	@Autowired
	public void setOfficialRepository(OfficialRepository officialRepository) {
		this.officialRepository = officialRepository;
	}

	@Test
	public void getById() {
		Official official = officialRepository.findOne(1L);
		Assert.assertEquals("LateCa'll", official.getLastName());
	}

	@Test
	public void findAll() {
		List<Official> teams = officialRepository.findAll();
		Assert.assertEquals(11, teams.size());
	}

	@Test
	public void findByLastNameFirstName_Found_FromDate() {
		Official official = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCa'll", "Joe", LocalDate.of(2009, 7, 1), LocalDate.of(2009, 7, 1));
		Assert.assertEquals("96", official.getNumber());
	}

	@Test
	public void findByLastNameFirstName_Found_ToDate() {
		Official official = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCa'll", "Joe", LocalDate.of(2010, 6, 30), LocalDate.of(2010, 6, 30));
		Assert.assertEquals("96", official.getNumber());
	}

	@Test
	public void findByLastNameFirstName_NotFound_LastName() {
		Official official = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCall", "Joe", LocalDate.of(2010, 6, 30), LocalDate.of(2010, 6, 30));
		Assert.assertNull(official);
	}

 	@Test
	public void findByLastNameFirstName_NotFound_FirstName() {
	    Official official = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCa'll", "Joey", LocalDate.of(2010, 6, 30), LocalDate.of(2010, 6, 30));
		Assert.assertNull(official);
	}

	@Test
	public void findByLastNameFirstName_NotFound_BeforeAsOfDate() {
		Official team = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCa'll", "Joe", LocalDate.of(2009, 6, 30), LocalDate.of(2009, 7, 1));
		Assert.assertNull(team);
	}

	@Test
	public void findByLastNameFirstName_NotFound_AfterAsOfDate() {
		Official team = officialRepository.findByLastNameAndFirstNameAndFromDateAndToDate("LateCa'll", "Joe", LocalDate.of(2010, 6, 30), LocalDate.of(2010, 7, 1));
		Assert.assertNull(team);
	}

	@Test
	public void findByAsOfDate_Found() {
		List<Official> officials = officialRepository.findByFromDateAndToDate(LocalDate.of(2009, 7, 1), LocalDate.of(2009, 7, 1));
		Assert.assertEquals(4, officials.size());
	}

	@Test
	public void findByAsOfDate_NotFound() {
		List<Official> officials = officialRepository.findByFromDateAndToDate(LocalDate.of(1989, 7, 1), LocalDate.of(1989, 7, 1));
		Assert.assertEquals(0, officials.size());
	}

//	@Test
//	public void create_Created() {
//		officialRepository.save(createMockOfficial("baltimore-bullets", LocalDate.of(2006, 7, 1), LocalDate.of(9999, 12, 31), "Baltimore Bullets2"));
//		Official findOfficial = officialRepository.findByOfficialKeyAndFromDateBeforeAndToDateAfter("baltimore-bullets", LocalDate.of(2006, 7, 2), LocalDate.of(2006, 7, 20));
//		Assert.assertEquals("Baltimore Bullets2", findOfficial.getFullName());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void create_Existing() {
//		officialRepository.save(createMockOfficial("baltimore-bullets", LocalDate.of(2005, 7, 1), LocalDate.of(2006, 6, 30), "Baltimore Bullets"));
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void create_MissingRequiredData() {
//		officialRepository.save(createMockOfficial("baltimore-bullets", LocalDate.of(2005, 7, 1), LocalDate.of(2006, 6, 30), null));
//	}
//
//	@Test
//	public void update_Updated() {
//		officialRepository.save(updateMockOfficial(3L, "st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), "St. Louis Bombier's"));
//		Official team = officialRepository.findByOfficialKeyAndFromDateBeforeAndToDateAfter("st-louis-bomber's", LocalDate.of(2010, 5, 30), LocalDate.of(2010, 5, 30));
//		Assert.assertEquals("St. Louis Bombier's", team.getFullName());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void update_MissingRequiredData() {
//		officialRepository.save(updateMockOfficial(3L,"st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), null));
//	}
//
//	@Test
//	public void delete_Deleted() {
//		officialRepository.delete(10L);
//		Official findOfficial = officialRepository.findOne(10L);
//		Assert.assertNull(findOfficial);
//	}
//
//	@Test(expected = EmptyResultDataAccessException.class)
//	public void delete_NotFound() {
//		officialRepository.delete(101L);
//	}
//
//	private Official createMockOfficial(String key, LocalDate fromDate, LocalDate toDate, String fullName) {
//		Official team = new Official();
//		team.setOfficialKey(key);
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
//	private Official updateMockOfficial(Long id, String key, LocalDate fromDate, LocalDate toDate, String fullName) {
//		Official team = createMockOfficial(key, fromDate, toDate, fullName);
//		team.setId(id);
//		return team;
//	}
}