package com.quizzbattle.ws.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.quizzbattle.ws.exception.ForbiddenException;
import com.quizzbattle.ws.exception.NotFoundException;
import com.quizzbattle.ws.model.Game;
import com.quizzbattle.ws.model.Player;
import com.quizzbattle.ws.model.User;
import com.quizzbattle.ws.model.User.Role;
import com.quizzbattle.ws.repository.FriendshipRepository;
import com.quizzbattle.ws.repository.GameRepository;
import com.quizzbattle.ws.repository.UserRepository;
import com.quizzbattle.ws.security.JwtUtils;
import com.quizzbattle.ws.service.UserService;
import com.quizzbattle.ws.validation.groups.OnUserCreate;
import com.quizzbattle.ws.validation.groups.OnUserUpdate;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FriendshipRepository friendshipRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public List<User> findAll(Role[] roles, String fullName) {

		return userRepository.findAllByFilters(roles, fullName);
	}

	@Override
	public User getByUsername(@NotBlank String username) {

		return userRepository.findByUsername(username)
				.orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.NotFound.resource.by.id",
						new String[] { "User", username }, LocaleContextHolder.getLocale())));
	}

	@Override
	public boolean existsById(@NotBlank String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	@Validated(OnUserCreate.class)
	public User save(@NotNull @Valid User user) {
		if (!(jwtUtils.isAdmin() || user.getRole().equals(Role.PLAYER))) {
			throw new ForbiddenException(
					messageSource.getMessage("error.Forbidden.users.create", null, LocaleContextHolder.getLocale()));
		}

		if (userRepository.existsByUsername(user.getUsername())) {
			throw new ValidationException(messageSource.getMessage("error.UserService.username.exists",
					new String[] { user.getUsername() }, LocaleContextHolder.getLocale()));
		}

		return userRepository.saveAndFlush(user);
	}

	@Override
	@Validated(OnUserUpdate.class)
	public User update(@NotNull @Valid User user) {

		User dbUser = userRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("User not found"));

		// Si el email no es null, lo actualiza
		if (user.getUsername() != null) {
			dbUser.setUsername(user.getUsername());
		}

		// Si la contraseña es diferente de null, la actualiza
		if (user.getPassword() != null) {
			dbUser.setPassword(user.getPassword());
		}

		// Si el email no es null, lo actualiza
		if (user.getEmail() != null) {
			dbUser.setEmail(user.getEmail());
		}

		if (user instanceof Player player && dbUser instanceof Player dbplayer) {
			dbplayer.setGoogleId(player.getGoogleId());
			dbplayer.setFcmToken(player.getFcmToken());
			dbplayer.setProfilePicture(player.getProfilePicture());
		}
		return userRepository.saveAndFlush(dbUser);
	}

	@Override
	@Transactional
	public void deleteByUsername(@NotBlank String username) {
		if (!jwtUtils.isAdmin()) {
			throw new ForbiddenException(
					messageSource.getMessage("error.Forbidden.users.delete", null, LocaleContextHolder.getLocale()));
		}

		if (!userRepository.existsByUsername(username)) {
			throw new NotFoundException(messageSource.getMessage("error.NotFound.resource.by.id",
					new String[] { "User", username }, LocaleContextHolder.getLocale()));
		}

		// Obtener el usuario por su nombre de usuario
		Optional<User> playerOpt = userRepository.findByUsername(username);
		if (!playerOpt.isPresent()) {
			throw new NotFoundException(messageSource.getMessage("error.NotFound.resource.by.id",
					new String[] { "User", username }, LocaleContextHolder.getLocale()));
		}
		User player = playerOpt.get();
		Long playerId = player.getId(); // Aquí obtienes el ID del usuario

		// Buscar juegos donde el usuario es player1 o player2
		List<Game> gamesAsPlayer1 = gameRepository.findByPlayer1Id(playerId);
		List<Game> gamesAsPlayer2 = gameRepository.findByPlayer2Id(playerId);

		// Actualizar los juegos donde el usuario está involucrado, sin mostrar error
		for (Game game : gamesAsPlayer1) {
			game.setPlayer1(null);
			game.setStatus(Game.Status.FINISHED);
			game.setTurn(null);
			gameRepository.saveAndFlush(game); // Guardar los cambios
		}

		for (Game game : gamesAsPlayer2) {
			game.setPlayer2(null);
			game.setStatus(Game.Status.FINISHED);
			game.setTurn(null);
			gameRepository.saveAndFlush(game); // Guardar los cambios
		}

		// Eliminar las relaciones en la tabla de amistad
		friendshipRepository.deleteBySenderId(playerId);
		friendshipRepository.deleteByReceiverId(playerId);

		// Eliminar al usuario
		userRepository.deleteByUsername(username);
	}

}
