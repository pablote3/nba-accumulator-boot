package com.rossotti.basketball.jpa.service;

import com.rossotti.basketball.jpa.model.Player;
import com.rossotti.basketball.jpa.model.RosterPlayer;
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
public class RosterPlayerServiceTest {

	private RosterPlayerService rosterPlayerService;

	@Autowired
	public void setRosterPlayerService(RosterPlayerService rosterPlayerService) {
		this.rosterPlayerService = rosterPlayerService;
	}

	@Test
	public void getById() {
		RosterPlayer rosterPlayer = rosterPlayerService.getById(1L);
		Assert.assertEquals("21", rosterPlayer.getNumber());
		Assert.assertEquals("chicago-zephyr's", rosterPlayer.getTeam().getTeamKey());
		Assert.assertEquals("Luke", rosterPlayer.getPlayer().getFirstName());
		Assert.assertTrue(rosterPlayer.isFound());
	}

	@Test
	public void listAll() {
		List<RosterPlayer> rosterPlayers = (List<RosterPlayer>)rosterPlayerService.listAll();
		Assert.assertTrue(rosterPlayers.size() >= 13);
	}

	@Test
	public void findByLastNameFirstNameBirthdateAsOfDate_Found() {
		RosterPlayer rosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 20), LocalDate.of(2009, 10, 30));
		Assert.assertEquals("31", rosterPlayer.getNumber());
		Assert.assertTrue(rosterPlayer.isFound());
	}

	@Test
	public void findByLastNameFirstNameBirthdateAsOfDate_NotFound_LastName() {
		RosterPlayer rosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiew'iczy", "Luke", LocalDate.of(2002, 2, 20), LocalDate.of(2009, 10, 30));
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void findByLastNameFirstNameBirthdateAsOfDate_NotFound_FirstName() {
		RosterPlayer rosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiew'icz", "Lukey", LocalDate.of(2002, 2, 20), LocalDate.of(2009, 10, 30));
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void findByLastNameFirstNameBirthdateAsOfDate_NotFound_Birthdate() {
		RosterPlayer rosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 21), LocalDate.of(2009, 10, 30));
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void findByLastNameFirstNameBirthdateAsOfDate_NotFound_AsOfDate() {
		RosterPlayer rosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 20), LocalDate.of(2009, 10, 29));
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void findByLastNameFirstNameTeamKeyAsOfDate_Found() {
		RosterPlayer rosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate("Puzdrakiew'icz", "Luke", "salinas-cowboys", LocalDate.of(2009, 10, 30));
		Assert.assertEquals("31", rosterPlayer.getNumber());
		Assert.assertTrue(rosterPlayer.isFound());
	}

	@Test
	public void findByLastNameFirstNameTeamKeyAsOfDate_NotFound_LastName() {
		RosterPlayer rosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate("Puzdrakiew'iczy", "Luke", "salinas-cowboys", LocalDate.of(2009, 10, 30));
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void findByLastNameFirstNameTeamKeyAsOfDate_NotFound_FirstName() {
		RosterPlayer rosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate("Puzdrakiew'icz", "Lukey", "salinas-cowboys", LocalDate.of(2009, 10, 30));
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void findByLastNameFirstNameTeamKeyAsOfDate_NotFound_TeamKey() {
		RosterPlayer rosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate("Puzdrakiew'icz", "Luke", "salinas-cowboy", LocalDate.of(2009, 10, 30));
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void findByLastNameFirstNameTeamKeyAsOfDate_NotFound_AsOfDate() {
		RosterPlayer rosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndTeamKeyAndAsOfDate("Puzdrakiew'icz", "Luke", "salinas-cowboys", LocalDate.of(2009, 10, 29));
		Assert.assertTrue(rosterPlayer.isNotFound());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_Found() {
		List<RosterPlayer> rosterPlayers = rosterPlayerService.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 20));
		Assert.assertEquals(2, rosterPlayers.size());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_LastName() {
		List<RosterPlayer> rosterPlayers = rosterPlayerService.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'iczy", "Luke", LocalDate.of(2002, 2, 20));
		Assert.assertEquals(0, rosterPlayers.size());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_FirstName() {
		List<RosterPlayer> rosterPlayers = rosterPlayerService.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Lukey", LocalDate.of(2002, 2, 20));
		Assert.assertEquals(0, rosterPlayers.size());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_Birthdate() {
		List<RosterPlayer> rosterPlayers = rosterPlayerService.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 21));
		Assert.assertEquals(0, rosterPlayers.size());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_Found() {
		List<RosterPlayer> rosterPlayers = rosterPlayerService.findByTeamKeyAndAsOfDate("detroit-pistons", LocalDate.of(2015, 10, 30));
		Assert.assertEquals(3, rosterPlayers.size());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_NotFound_TeamKey() {
		List<RosterPlayer> rosterPlayers = rosterPlayerService.findByTeamKeyAndAsOfDate("detroit-pistols", LocalDate.of(2015, 10, 30));
		Assert.assertEquals(0, rosterPlayers.size());
	}

	@Test
	public void findByTeamKeyAndAsOfDate_NotFound_AsOfDate() {
		List<RosterPlayer> rosterPlayers = rosterPlayerService.findByTeamKeyAndAsOfDate("detroit-pistons", LocalDate.of(2014, 10, 30));
		Assert.assertEquals(0, rosterPlayers.size());
	}

	@Test
	public void create_Created() {
		RosterPlayer createRosterPlayer = rosterPlayerService.create(createMockRosterPlayer(2L, 2L, "Puzdrakiewicz", "Thad", LocalDate.of(1966, 6, 2), LocalDate.of(2010, 1, 22), LocalDate.of(2010, 1, 28), "33"));
		RosterPlayer findRosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndBirthdateAndAsOfDate("Puzdrakiewicz", "Thad", LocalDate.of(1966, 6, 2), LocalDate.of(2010, 1, 28));
		Assert.assertTrue(createRosterPlayer.isCreated());
		Assert.assertEquals("33", findRosterPlayer.getNumber());
	}

	@Test
	public void create_Existing() {
		RosterPlayer createRosterPlayer = rosterPlayerService.create(createMockRosterPlayer(2L, 2L, "Puzdrakiewicz", "Thad", LocalDate.of(1966, 6, 2), LocalDate.of(2010, 1, 1), LocalDate.of(2010, 1, 5), "33"));
		Assert.assertTrue(createRosterPlayer.isFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		rosterPlayerService.create(createMockRosterPlayer(2L, 2L, "Puzdrakiewicz", "Thad", LocalDate.of(1966, 6, 2), LocalDate.of(2010, 2, 1), LocalDate.of(2010, 2, 5), null));
	}

//	@Test
//	public void update_Updated() {
//		Player updatePlayer = rosterPlayerService.update(createMockPlayer("Puzdrakiewicz", "Thad", LocalDate.of(2000, 3, 13), "Thad Puzdrakiewicz2"));
//		Player rosterPlayer = rosterPlayerService.findByLastNameAndFirstNameAndBirthdate("Puzdrakiewicz", "Thad", LocalDate.of(2000, 3, 13));
//		Assert.assertEquals("Thad Puzdrakiewicz2", rosterPlayer.getDisplayName());
//		Assert.assertTrue(updatePlayer.isUpdated());
//	}
//
//	@Test
//	public void update_NotFound() {
//		Player rosterPlayer = rosterPlayerService.update(createMockPlayer("Puzdrakiewicz", "Thad", LocalDate.of(2000, 3, 14), "Thad Puzdrakiewicz"));
//		Assert.assertTrue(rosterPlayer.isNotFound());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void update_MissingRequiredData() {
//		Player rosterPlayer = rosterPlayerService.update(createMockPlayer("Puzdrakiewicz", "Thad", LocalDate.of(2000, 3, 13), null));
//	}
//
//	@Test
//	public void delete_Deleted() {
//		Player deletePlayer = rosterPlayerService.delete(6L);
//		Player findPlayer = rosterPlayerService.getById(6L);
//		Assert.assertNull(findPlayer);
//		Assert.assertTrue(deletePlayer.isDeleted());
//	}
//
//	@Test
//	public void delete_NotFound() {
//		Player deletePlayer = rosterPlayerService.delete(101L);
//		Assert.assertTrue(deletePlayer.isNotFound());
//	}

	private RosterPlayer createMockRosterPlayer(Long playerId, Long teamId, String lastName, String firstName, LocalDate birthdate, LocalDate fromDate, LocalDate toDate, String number) {
		RosterPlayer rosterPlayer = new RosterPlayer();
		rosterPlayer.setPlayer(getMockPlayer(playerId, lastName, firstName, birthdate));
		rosterPlayer.setTeam(getMockTeam(teamId));
		rosterPlayer.setFromDate(fromDate);
		rosterPlayer.setToDate(toDate);
		rosterPlayer.setNumber(number);
		rosterPlayer.setPosition(RosterPlayer.Position.G);
		return rosterPlayer;
	}

	private Player getMockPlayer(Long playerId, String lastName, String firstName, LocalDate birthdate) {
		Player player = new Player();
		player.setId(playerId);
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(birthdate);
		return player;
	}

	private Team getMockTeam(Long teamId) {
		Team team = new Team();
		team.setId(teamId);
		return team;
	}
}