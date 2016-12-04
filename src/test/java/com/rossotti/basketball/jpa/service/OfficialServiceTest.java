package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Official;
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
public class OfficialServiceTest {

	private OfficialService officialService;

	@Autowired
	public void setOfficialService(OfficialService officialService) {
		this.officialService = officialService;
	}

	@Test
	public void getById() {
		Official team = officialService.getById(1L);
		Assert.assertEquals("LateCa'll", team.getLastName());
	}

	@Test
	public void listAll() {
		List<Official> teams = (List<Official>)officialService.listAll();
		Assert.assertTrue(teams.size() >= 11);
	}

	@Test
	public void findByLastNameFirstName_Found_FromDate() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCa'll", "Joe", LocalDate.of(2009, 7, 1));
		Assert.assertEquals("96", official.getNumber());
		Assert.assertTrue(official.isFound());
	}

	@Test
	public void findByLastNameFirstName_Found_ToDate() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCa'll", "Joe", LocalDate.of(2010, 6, 30));
		Assert.assertEquals("96", official.getNumber());
		Assert.assertTrue(official.isFound());
	}

	@Test
	public void findByLastNameFirstName_NotFound_LastName() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCall", "Joe", LocalDate.of(2010, 6, 30));
		Assert.assertTrue(official.isNotFound());
	}

	@Test
	public void findByLastNameFirstName_NotFound_FirstName() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCa'll", "Joey", LocalDate.of(2010, 6, 30));
		Assert.assertTrue(official.isNotFound());
	}

	@Test
	public void findByLastNameFirstName_NotFound_BeforeAsOfDate() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCa'll", "Joe", LocalDate.of(2009, 6, 30));
		Assert.assertTrue(official.isNotFound());
	}

	@Test
	public void findByLastNameFirstName_NotFound_AfterAsOfDate() {
		Official official = officialService.findByLastNameAndFirstNameAndAsOfDate("LateCa'll", "Joe", LocalDate.of(2010, 7, 1));
		Assert.assertTrue(official.isNotFound());
	}

	@Test
	public void findByAsOfDate_Found() {
		List<Official> officials = officialService.findByAsOfDate(LocalDate.of(2009, 10, 30));
		Assert.assertEquals(4, officials.size());
	}

	@Test
	public void findByDateRange_NotFound() {
		List<Official> officials = officialService.findByAsOfDate(LocalDate.of(1989, 10, 30));
		Assert.assertEquals(0, officials.size());
	}

//	@Test
//	public void create_Created_AsOfDate() {
//		Official createOfficial = officialService.create(createMockOfficial("sacramento-hornets", LocalDate.of(2012, 7, 1), LocalDate.of(9999, 12, 31), "Sacramento Hornets"));
//		Official findOfficial = officialService.findByOfficialKeyAndAsOfDate("sacramento-hornets", LocalDate.of(2012, 7, 1));
//		Assert.assertTrue(createOfficial.isCreated());
//		Assert.assertEquals("Sacramento Hornets", findOfficial.getFullName());
//	}
//
//	@Test
//	public void create_Created_DateRange() {
//		Official createOfficial = officialService.create(createMockOfficial("sacramento-rivercats", LocalDate.of(2006, 7, 1), LocalDate.of(2012, 7, 2), "Sacramento Rivercats"));
//		Official findOfficial = officialService.findByOfficialKeyAndAsOfDate("sacramento-rivercats", LocalDate.of(2006, 7, 1));
//		Assert.assertTrue(createOfficial.isCreated());
//		Assert.assertEquals("Sacramento Rivercats", findOfficial.getFullName());
//	}
//
//	@Test
//	public void create_OverlappingDates() {
//		Official createOfficial = officialService.create(createMockOfficial("cleveland-rebels", LocalDate.of(2010, 7, 1), LocalDate.of(2010, 7, 1), "Cleveland Rebels"));
//		Assert.assertTrue(createOfficial.isFound());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void create_MissingRequiredData() {
//		Official createOfficial = officialService.create(createMockOfficial("chavo-del-ocho", LocalDate.of(2010, 7, 1), LocalDate.of(2010, 7, 1), null));
//	}
//
//	@Test
//	public void update_Updated() {
//		Official updateOfficial = officialService.update(createMockOfficial("st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(9999, 12, 31), "St. Louis Bombier's"));
//		Official team = officialService.findByOfficialKeyAndAsOfDate("st-louis-bomber's", LocalDate.of(9999, 12, 31));
//		Assert.assertEquals("St. Louis Bombier's", team.getFullName());
//		Assert.assertTrue(updateOfficial.isUpdated());
//	}
//
//	@Test
//	public void update_NotFound() {
//		Official team = officialService.update(createMockOfficial("st-louis-bomb's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 7, 1), "St. Louis Bombier's"));
//		Assert.assertTrue(team.isNotFound());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void update_MissingRequiredData() {
//		officialService.update(createMockOfficial("st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), null));
//	}
//
//	@Test
//	public void delete_Deleted() {
//		Official deleteOfficial = officialService.delete(7L);
//		Official findOfficial = officialService.getById(7L);
//		Assert.assertNull(findOfficial);
//		Assert.assertTrue(deleteOfficial.isDeleted());
//	}
//
//	@Test
//	public void delete_NotFound() {
//		Official deleteOfficial = officialService.delete(101L);
//		Assert.assertTrue(deleteOfficial.isNotFound());
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
}
