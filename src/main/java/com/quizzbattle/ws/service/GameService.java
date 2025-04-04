package com.quizzbattle.ws.service;

import java.util.List;

import com.quizzbattle.ws.model.Game;
import com.quizzbattle.ws.model.Player;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GameService {

	List<Game> findAll(Player player);

	Game update(@NotNull @Valid Game game);

	Game createGame(Player player1, Player player2);

}
