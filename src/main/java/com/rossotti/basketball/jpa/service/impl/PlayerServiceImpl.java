package com.rossotti.basketball.jpa.service.impl;

import com.rossotti.basketball.jpa.model.AbstractDomainClass.StatusCodeDAO;
import com.rossotti.basketball.jpa.model.Player;
import com.rossotti.basketball.jpa.repository.PlayerRepository;
import com.rossotti.basketball.jpa.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

	private PlayerRepository playerRepository;

	@Autowired
	public void setPlayerRepository(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	@Override
	public List<Player> findByLastNameAndFirstName(String lastName, String firstName) {
		return playerRepository.findByLastNameAndFirstName(lastName, firstName);
	}

	@Override
	public Player findByLastNameAndFirstNameAndBirthdate(String lastName, String firstName, LocalDate birthdate) {
		Player player = playerRepository.findByLastNameAndFirstNameAndBirthdate(lastName, firstName, birthdate);
		if (player != null) {
			player.setStatusCode(StatusCodeDAO.Found);
		}
		else {
			player = new Player(StatusCodeDAO.NotFound);
		}
		return player;
	}

	@Override
	public List<?> listAll() {
		List<Player> players = new ArrayList<>();
		playerRepository.findAll().forEach(players::add);
		return players;
	}

	@Override
	public Player getById(Long id) {
		return playerRepository.findOne(id);
	}

	@Override
	public Player create(Player createPlayer) {
		Player player = findByLastNameAndFirstNameAndBirthdate(createPlayer.getLastName(), createPlayer.getFirstName(), createPlayer.getBirthdate());
		if (player.isNotFound()) {
			playerRepository.save(createPlayer);
			createPlayer.setStatusCode(StatusCodeDAO.Created);
			return createPlayer;
		}
		else {
			return player;
		}
	}

	@Override
	public Player update(Player updatePlayer) {
		return null;
//		Standing standing = findByTeamKeyAndAsOfDate(updateStanding.getTeam().getTeamKey(), updateStanding.getStandingDate());
//		if (standing.isFound()) {
//			standing.setRank(updateStanding.getRank());
//			standing.setOrdinalRank(updateStanding.getOrdinalRank());
//			standing.setGamesWon(updateStanding.getGamesWon());
//			standing.setGamesLost(updateStanding.getGamesLost());
//			standing.setStreak(updateStanding.getStreak());
//			standingRepository.save(standing);
//			standing.setStatusCode(StatusCodeDAO.Updated);
//		}
//		return standing;
	}

	@Override
	public Player delete(Long id) {
		return null;
//		Standing findStanding = getById(id);
//		if (findStanding != null && findStanding.isFound()) {
//			standingRepository.delete(findStanding.getId());
//			findStanding.setStatusCode(StatusCodeDAO.Deleted);
//			return findStanding;
//		}
//		else {
//			return new Standing(StatusCodeDAO.NotFound);
//		}
	}
}