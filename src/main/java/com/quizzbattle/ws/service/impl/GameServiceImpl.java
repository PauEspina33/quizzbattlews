package com.quizzbattle.ws.service.impl;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.quizzbattle.ws.exception.ForbiddenException;
import com.quizzbattle.ws.exception.NotFoundException;
import com.quizzbattle.ws.model.Game;
import com.quizzbattle.ws.model.Player;
import com.quizzbattle.ws.repository.GameRepository;
import com.quizzbattle.ws.repository.UserRepository;
import com.quizzbattle.ws.security.JwtUtils;
import com.quizzbattle.ws.service.GameService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public List<Game> findAll(Player player) {
		if (!(jwtUtils.isAdmin() || jwtUtils.isAuthUser(player.getUsername()))) {
			throw new ForbiddenException(
					messageSource.getMessage("error.Forbidden.users.get", null, LocaleContextHolder.getLocale()));
		}
		return gameRepository.findAllByPlayerOrderedByTurn(player);
	}

	@Override
	public Game update(@NotNull @Valid Game game) {
		Game dbGame = gameRepository.findById(game.getId()).orElseThrow(() -> new NotFoundException("Game not found"));

		// Si player1 es null, eliminamos la asociación
		if (game.getPlayer1() == null) {
			dbGame.setPlayer1(null); // Actualizamos el campo a null
		} else {
			dbGame.setPlayer1(game.getPlayer1());
		}

		// Si player2 es null, eliminamos la asociación
		if (game.getPlayer2() == null) {
			dbGame.setPlayer2(null); // Actualizamos el campo a null
		} else {
			dbGame.setPlayer2(game.getPlayer2());
		}

		dbGame.setStatus(game.getStatus());
		dbGame.setScorePlayer1(game.getScorePlayer1());
		dbGame.setScorePlayer2(game.getScorePlayer2());
		dbGame.setStarsPlayer1(game.getStarsPlayer1());
		dbGame.setStarsPlayer2(game.getStarsPlayer2());
		dbGame.setWinner(game.getWinner());
		dbGame.setTurn(game.getTurn());

		return gameRepository.saveAndFlush(dbGame);
	}

	@Override
	public Game createGame(Player player1, Player player2) {
		if (player1 == null) {
			throw new IllegalArgumentException("Player 1 must be provided to create a game.");
		}
		Game game = new Game();
		if (player2 == null) {
			List<Player> availablePlayers = userRepository.findPlayer();
			availablePlayers.remove(player1);

			if (availablePlayers.isEmpty()) {
				throw new IllegalStateException("No available players to match against.");
			}

			Player player2random = availablePlayers.get(new Random().nextInt(availablePlayers.size()));
			game.setPlayer2(player2random);
		} else {
			game.setPlayer2(player2);
		}

		game.setPlayer1(player1);

		game.setStatus(Game.Status.ONGOING);
		game.setScorePlayer1(0);
		game.setScorePlayer2(0);
		game.setStarsPlayer1(0);
		game.setStarsPlayer2(0);
		game.setTurn(player1);

		return gameRepository.saveAndFlush(game);
	}

}
