package com.quizzbattle.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizzbattle.ws.model.Friendship;
import com.quizzbattle.ws.model.Friendship.Status;
import com.quizzbattle.ws.model.Player;
import com.quizzbattle.ws.service.FriendshipService;
import com.quizzbattle.ws.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Friendship", description = "Friendship API")
@RestController
@RequestMapping("/friendship")
@SecurityRequirement(name = "Bearer Authentication")
@Validated
public class FriendshipController {

	@Autowired
	private FriendshipService friendshipService;

	@Autowired
	private UserService userService;

	@Operation(summary = "Find all friendships filtered by player and accepted", description = "Retrieve all friendships from player")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Friendship.class))) }, description = "Friendships retrieved successfully")
	@GetMapping("/find/all/accepted")
	public List<Friendship> findAll(
			@Parameter(description = "Username of the player", required = true) @RequestParam(value = "username") String username) {

		Player player = (Player) userService.getByUsername(username);
		return friendshipService.findAll(player);
	}

	@Operation(summary = "Find all friendships filtered by receiver & status", description = "Retrieve all friendships from receiver")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Friendship.class))) }, description = "Friendships retrieved successfully")
	@GetMapping("/find/by/player/status")
	public List<Friendship> findAllByReciverAndStatus(
			@Parameter(description = "Username of the receiver", required = true) @RequestParam(value = "username") String username,
			@RequestParam(value = "status", required = true) Status status) {

		Player player = (Player) userService.getByUsername(username);
		return friendshipService.findAllByReceiverAndStatus(player, status);
	}

	@Operation(summary = "Update to accept a friendship", description = "Updates an existing friendship to accept it")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Friendship.class)) }, description = "Friendship updated successfully")
	@ApiResponse(responseCode = "404", content = {
			@Content(mediaType = "application/json") }, description = "Friendship not found")
	@PutMapping("/update/{id}") // Aquí se especifica que el id es parte de la URL
	public Friendship update(@PathVariable("id") Long id) {
		// Puedes usar el id recibido para actualizar la amistad
		return friendshipService.update(id);
	}

	/* Swagger */
	@Operation(summary = "Delete a frienship")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json") }, description = "Frienship deleted ok")
	/**/
	@DeleteMapping("/delete/by/id/{id}")
	public void deleteById(@PathVariable("id") Long Id) {
		friendshipService.deleteById(Id);
	}

}
