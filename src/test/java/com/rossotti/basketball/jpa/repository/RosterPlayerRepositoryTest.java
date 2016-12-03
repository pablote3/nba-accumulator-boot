package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.RosterPlayer;
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
public class RosterPlayerRepositoryTest {

	private RosterPlayerRepository rosterPlayerRepository;

	@Autowired
	public void setRosterPlayerRepository(RosterPlayerRepository rosterPlayerRepository) {
		this.rosterPlayerRepository = rosterPlayerRepository;
	}

	@Test
	public void getById() {
		RosterPlayer rosterPlayer = rosterPlayerRepository.findOne(1L);
		Assert.assertEquals("21", rosterPlayer.getNumber());
		Assert.assertEquals("chicago-zephyr's", rosterPlayer.getTeam().getTeamKey());
		Assert.assertEquals("Luke", rosterPlayer.getPlayer().getFirstName());
	}

//	@Test
//	public void findAll() {
//		List<RosterPlayer> rosterPlayers = (List<RosterPlayer>)rosterPlayerRepository.findAll();
//		Assert.assertEquals(16, rosterPlayers.size());
//	}
//
//	@Test
//	public void findByLastNameFirstNameBirthdate_Found() {
//		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 20));
//		Assert.assertEquals("Sacramento, CA, USA", rosterPlayer.getBirthplace());
//	}
//
//	@Test
//	public void findByLastNameFirstNameBirthdate_NotFound_LastName() {
//		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew''icz", "Luke", LocalDate.of(2002, 2, 20));
//		Assert.assertNull(rosterPlayer);
//	}
//
//	@Test
//	public void findByLastNameFirstNameBirthdate_NotFound_FirstName() {
//		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Like", LocalDate.of(2002, 2, 20));
//		Assert.assertNull(rosterPlayer);
//	}
//
//	@Test
//	public void findByLastNameFirstNameBirthdate_NotFound_Birthdate() {
//		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 21));
//		Assert.assertNull(rosterPlayer);
//	}
//
//	@Test
//	public void findByLastNameFirstName_Found() {
//		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByLastNameAndFirstName("Puzdrakiewicz", "Thad");
//		Assert.assertEquals(2, rosterPlayers.size());
//	}
//
//	@Test
//	public void findByLastNameFirstName_NotFound_LastName() {
//		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByLastNameAndFirstName("Puzdrakiewiczy", "Thad");
//		Assert.assertEquals(0, rosterPlayers.size());
//	}
//
//	@Test
//	public void findByLastNameFirstName_NotFound_FirstName() {
//		List<RosterPlayer> rosterPlayers = rosterPlayerRepository.findByLastNameAndFirstName("Puzdrakiewicz", "Thady");
//		Assert.assertEquals(0, rosterPlayers.size());
//	}
//
//	@Test
//	public void create_Created() {
//		rosterPlayerRepository.save(createMockRosterPlayer("Puzdrakiewicz", "Fred", LocalDate.of(1968, 11, 8), "Fred Puzdrakiewicz"));
//		RosterPlayer findRosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiewicz", "Fred", LocalDate.of(1968, 11, 8));
//		Assert.assertEquals("Fred Puzdrakiewicz", findRosterPlayer.getDisplayName());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void create_Existing() {
//		rosterPlayerRepository.save(createMockRosterPlayer("Puzdrakiewicz", "Michelle", LocalDate.of(1969, 9, 8), "Michelle Puzdrakiewicz"));
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void create_MissingRequiredData() {
//		RosterPlayer rosterPlayer = new RosterPlayer();
//		rosterPlayer.setLastName("missing");
//		rosterPlayer.setFirstName("required-data");
//		rosterPlayer.setBirthdate(LocalDate.of(1969, 9, 8));
//		rosterPlayerRepository.save(rosterPlayer);
//	}
//
//	@Test
//	public void update_Updated() {
//		rosterPlayerRepository.save(updateMockRosterPlayer(2L, "Puzdrakiewicz", "Thad", LocalDate.of(1966, 6, 2), "Thady Puzdrakiewicz"));
//		RosterPlayer rosterPlayer = rosterPlayerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiewicz", "Thad", LocalDate.of(1966, 6, 2));
//		Assert.assertEquals("Thady Puzdrakiewicz", rosterPlayer.getDisplayName());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void update_MissingRequiredData() {
//		rosterPlayerRepository.save(updateMockRosterPlayer(2L, "Puzdrakiewicz", "Thad", LocalDate.of(1966, 6, 2), null));
//	}
//
//	@Test
//	public void delete_Deleted() {
//		rosterPlayerRepository.delete(7L);
//		RosterPlayer findRosterPlayer = rosterPlayerRepository.findOne(7L);
//		Assert.assertNull(findRosterPlayer);
//	}
//
//	@Test(expected = EmptyResultDataAccessException.class)
//	public void delete_NotFound() {
//		rosterPlayerRepository.delete(101L);
//	}
//
//	private RosterPlayer createMockRosterPlayer(String lastName, String firstName, LocalDate birthdate, String displayName) {
//		RosterPlayer rosterPlayer = new RosterPlayer();
//		rosterPlayer.setLastName(lastName);
//		rosterPlayer.setFirstName(firstName);
//		rosterPlayer.setBirthdate(birthdate);
//		rosterPlayer.setDisplayName(displayName);
//		rosterPlayer.setHeight((short)79);
//		rosterPlayer.setWeight((short)195);
//		rosterPlayer.setBirthplace("Monroe, Louisiana, USA");
//		return rosterPlayer;
//	}
//
//	private RosterPlayer updateMockRosterPlayer(Long id, String lastName, String firstName, LocalDate birthdate, String displayName) {
//		RosterPlayer rosterPlayer = createMockRosterPlayer(lastName, firstName, birthdate, displayName);
//		rosterPlayer.setId(id);
//		return rosterPlayer;
//	}
}