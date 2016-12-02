package com.rossotti.basketball.jpa.repository;

import com.rossotti.basketball.jpa.model.Player;
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
public class PlayerRepositoryTest {

	private PlayerRepository playerRepository;

	@Autowired
	public void setPlayerRepository(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	@Test
	public void getById() {
		Player player = playerRepository.findOne(1L);
		Assert.assertEquals("Puzdrakiew'icz", player.getLastName());
	}

	@Test
	public void findAll() {
		List<Player> players = (List<Player>)playerRepository.findAll();
		Assert.assertEquals(15, players.size());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_Found() {
		Player player = playerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 20));
		Assert.assertEquals("Sacramento, CA, USA", player.getBirthplace());
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_LastName() {
		Player player = playerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew''icz", "Luke", LocalDate.of(2002, 2, 20));
		Assert.assertNull(player);
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_FirstName() {
		Player player = playerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Like", LocalDate.of(2002, 2, 20));
		Assert.assertNull(player);
	}

	@Test
	public void findByLastNameFirstNameBirthdate_NotFound_Birthdate() {
		Player player = playerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiew'icz", "Luke", LocalDate.of(2002, 2, 21));
		Assert.assertNull(player);
	}

	@Test
	public void findByLastNameFirstName_Found() {
		List<Player> players = playerRepository.findByLastNameAndFirstName("Puzdrakiewicz", "Thad");
		Assert.assertEquals(2, players.size());
	}

	@Test
	public void findByLastNameFirstName_NotFound_LastName() {
		List<Player> players = playerRepository.findByLastNameAndFirstName("Puzdrakiewiczy", "Thad");
		Assert.assertEquals(0, players.size());
	}

	@Test
	public void findByLastNameFirstName_NotFound_FirstName() {
		List<Player> players = playerRepository.findByLastNameAndFirstName("Puzdrakiewicz", "Thady");
		Assert.assertEquals(0, players.size());
	}

	@Test
	public void create_Created() {
		playerRepository.save(createMockPlayer("Puzdrakiewicz", "Fred", LocalDate.of(1968, 11, 8), "Fred Puzdrakiewicz"));
		Player findPlayer = playerRepository.findByLastNameAndFirstNameAndBirthdate("Puzdrakiewicz", "Fred", LocalDate.of(1968, 11, 8));
		Assert.assertEquals("Fred Puzdrakiewicz", findPlayer.getDisplayName());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_Existing() {
		playerRepository.save(createMockPlayer("Puzdrakiewicz", "Michelle", LocalDate.of(1969, 9, 8), "Michelle Puzdrakiewicz"));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void create_MissingRequiredData() {
		Player player = new Player();
		player.setLastName("missing");
		player.setFirstName("required-data");
		player.setBirthdate(LocalDate.of(1969, 9, 8));
		playerRepository.save(player);
	}

//	@Test
//	public void update_Updated() {
//		playerRepository.save(updateMockPlayer(3L, "st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), "St. Louis Bombier's"));
//		Player player = playerRepository.findByPlayerKeyAndFromDateBeforeAndToDateAfter("st-louis-bomber's", LocalDate.of(2010, 5, 30), LocalDate.of(2010, 5, 30));
//		Assert.assertEquals("St. Louis Bombier's", player.getFullName());
//	}
//
//	@Test(expected=DataIntegrityViolationException.class)
//	public void update_MissingRequiredData() {
//		playerRepository.save(updateMockPlayer(3L,"st-louis-bomber's", LocalDate.of(2009, 7, 1), LocalDate.of(2010, 6, 30), null));
//	}
//
//	@Test
//	public void delete_Deleted() {
//		playerRepository.delete(10L);
//		Player findPlayer = playerRepository.findOne(10L);
//		Assert.assertNull(findPlayer);
//	}
//
//	@Test(expected = EmptyResultDataAccessException.class)
//	public void delete_NotFound() {
//		playerRepository.delete(101L);
//	}

	private Player createMockPlayer(String lastName, String firstName, LocalDate birthdate, String displayName) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(birthdate);
		player.setDisplayName(displayName);
		player.setHeight((short)79);
		player.setWeight((short)195);
		player.setBirthplace("Monroe, Louisiana, USA");
		return player;
	}

	private Player updateMockPlayer(String lastName, String firstName, LocalDate birthdate, String displayName) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(birthdate);
		player.setDisplayName(displayName);
		player.setHeight((short)79);
		player.setWeight((short)215);
		player.setBirthplace("Monroe, Louisiana, USA");
		return player;
	}
}