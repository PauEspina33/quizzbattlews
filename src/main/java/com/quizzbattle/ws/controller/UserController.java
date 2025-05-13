package com.quizzbattle.ws.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.quizzbattle.ws.model.Admin;
import com.quizzbattle.ws.model.ImageRequest;
import com.quizzbattle.ws.model.ImageResponse;
import com.quizzbattle.ws.model.Player;
import com.quizzbattle.ws.model.User;
import com.quizzbattle.ws.model.User.Role;
import com.quizzbattle.ws.service.UserService;
import com.quizzbattle.ws.validation.groups.OnUserCreate;
import com.quizzbattle.ws.validation.groups.OnUserUpdate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.websocket.server.PathParam;

@Tag(name = "User", description = "User API")

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Operation(summary = "Check if user exists", description = "Check if a user exists in the database. The response is true if exists, false otherwise")
	@ApiResponse(responseCode = "200", content = { @Content() }, description = "Check goes ok")
	@ApiResponse(responseCode = "500", content = {
			@Content() }, description = "Error checking the user. See response body for more details")
	@GetMapping(value = "/check/{username}")
	public @ResponseBody boolean check(@PathParam("username") @NotBlank String username) throws Exception {
		return userService.existsById(username);
	}

	/* Swagger */
	@Operation(summary = "Get user by id")
	@ApiResponse(responseCode = "200", description = "User retrieved ok")
	@ApiResponse(responseCode = "404", description = "Resource not found")
	/**/
	@GetMapping("/get/by/username/{username}")
	public User findByUsername(@PathVariable("username") @NotBlank String username) {
		return userService.getByUsername(username);
	}

	@Operation(summary = "Retrieve all users", description = "Retrieve all users from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))) }, description = "Users retrieved ok")
	@GetMapping(value = "/find/all")
	public @ResponseBody List<User> findAll(@RequestParam(value = "roles", required = false) Role[] roles,
			@RequestParam(value = "fullName", required = false) String fullName) {

		return userService.findAll(roles, fullName);
	}
	
	@Operation(summary = "Save a user", description = "Saves a user in the database. The response is the stored user from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ImageRequest.class)) }, description = "User saved ok")
	@ApiResponse(responseCode = "500", content = {
			@Content() }, description = "Error saving the user. See response body for more details")
	@PostMapping(value = "/upload/image-profile")
	@Validated(OnUserCreate.class)
	public ResponseEntity<String> uploadProfilePicture(@RequestBody @Valid ImageRequest imageRequest) throws Exception {
		 if (imageRequest.getImageBase64()== null || imageRequest.getImageBase64().isEmpty()) {
		        return ResponseEntity.badRequest().body("Base64 vacío o nulo");
		   }
		System.out.println("Imagen Base64 recibida: " + imageRequest.getImageBase64()); // Log para depuración

	    Player player = (Player) userService.getByUsername(imageRequest.getUsername());
	    player.setImage(imageRequest.decodeImageBase64(imageRequest.getImageBase64()));
	    userService.update(player);
	    return ResponseEntity.ok("Imagen de perfil actualizada correctamente");
	}
	
	@GetMapping("/profile-picture/{username}")
	@Operation(
	    summary = "Obtener imagen de perfil en Base64",
	    description = "Devuelve la imagen de perfil del usuario codificada en Base64"
	)
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Imagen obtenida correctamente",
	        content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
	    @ApiResponse(responseCode = "404", description = "Usuario o imagen no encontrada")
	})
	public ResponseEntity<ImageResponse> getProfilePictureBase64(@PathVariable String username) {
	    Player player = (Player) userService.getByUsername(username);
	    if (player == null || player.getImage() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }

	    String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(player.getImage());
	    return ResponseEntity.ok(new ImageResponse(base64Image));
	}


	@Operation(summary = "Save a user", description = "Saves a user in the database. The response is the stored user from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }, description = "User saved ok")
	@ApiResponse(responseCode = "500", content = {
			@Content() }, description = "Error saving the user. See response body for more details")
	@PostMapping(value = "/save")
	@Validated(OnUserCreate.class)
	public @ResponseBody User save(@RequestBody @Valid User user) throws Exception {
		return userService.save(convertAndEncodePassword(user));
	}

	/* Swagger */
	@Operation(summary = "Update a user")
	@ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(anyOf = {
			Admin.class, Player.class }, discriminatorProperty = "role")) }, description = "User updated ok")
	@ApiResponse(responseCode = "404", content = {
			@Content(mediaType = "application/json") }, description = "Resource not found")
	/**/
	@PutMapping("/update")
	@Validated(OnUserUpdate.class)
	public @ResponseBody User update(@RequestBody @Valid User user) {
		return userService.update(convertAndEncodePassword(user));
	}

	/* Swagger */
	@Operation(summary = "Delete a user")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json") }, description = "User deleted ok")
	/**/
	@DeleteMapping("/delete/by/username/{username}")
	public void deleteByUsername(@PathVariable("username") @NotBlank String username) {
		userService.deleteByUsername(username);
	}

	private User convertAndEncodePassword(User user) {
		String rawPassword = user.getPassword();
		if (rawPassword != null) {
			user.setPassword(passwordEncoder.encode(rawPassword));
		}
		return user;
	}
}
