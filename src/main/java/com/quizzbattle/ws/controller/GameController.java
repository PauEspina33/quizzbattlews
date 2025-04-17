package com.quizzbattle.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.quizzbattle.ws.model.Game;
import com.quizzbattle.ws.model.Game.Status;
import com.quizzbattle.ws.model.Player;
import com.quizzbattle.ws.service.GameService;
import com.quizzbattle.ws.service.UserService;
import com.quizzbattle.ws.validation.groups.OnUserUpdate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Game", description = "Game API")
@RestController
@RequestMapping("/games")
@SecurityRequirement(name = "Bearer Authentication")
public class GameController {

	@Autowired
	private GameService gameService;

	@Autowired
	private UserService userService;

	@Operation(summary = "Find all games filtered by player", description = "Retrieve all games where the given player participates, ordered by turn")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Game.class))) }, description = "Games retrieved successfully")
	@GetMapping("/find/all")
	public List<Game> findAll(
			@Parameter(description = "Username of the player", required = true) @RequestParam(value = "username") String username,
			@RequestParam(value = "status", required = true) Status status) {
		Player player = (Player) userService.getByUsername(username);
		return gameService.findAll(player, status);
	}

	@Operation(summary = "Update a game")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }, description = "Game updated ok")
	@ApiResponse(responseCode = "404", content = {
			@Content(mediaType = "application/json") }, description = "Resource not found")
	@PutMapping("/update")
	@Validated(OnUserUpdate.class)
	public @ResponseBody Game update(@RequestBody @Valid Game game) {
		return gameService.update(game);
	}

	@Operation(summary = "Create a new game", description = "Creates a new game between two players")
	@ApiResponse(responseCode = "201", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }, description = "Game created successfully")
	@ApiResponse(responseCode = "400", content = {
			@Content(mediaType = "application/json") }, description = "Invalid request data")
	@PostMapping("/create")
	public Game createGame(@RequestParam(value = "player1") String player1Username,
			@RequestParam(value = "player2", required = false) String player2Username) {
		Player player2 = null;
		Player player1 = (Player) userService.getByUsername(player1Username);
		if (player2Username != null) {
			player2 = (Player) userService.getByUsername(player2Username);
		}

		return gameService.createGame(player1, player2);
	}
}
