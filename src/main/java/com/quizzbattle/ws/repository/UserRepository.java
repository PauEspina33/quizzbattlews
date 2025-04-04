package com.quizzbattle.ws.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quizzbattle.ws.model.Player;
import com.quizzbattle.ws.model.User;
import com.quizzbattle.ws.model.User.Role;

import jakarta.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE " + "(u.role IN ?1 OR ?1 IS NULL) AND " + "(u.username LIKE %?2% OR ?2 IS NULL) "
			+ "ORDER BY u.username ASC ")
	List<User> findAllByFilters(Role[] roles, String fullName);

	Optional<User> findByUsername(String username);

	@Query("SELECT u FROM Player u WHERE u.role = 'PLAYER'")
	List<Player> findPlayer();

	boolean existsByUsername(@NotBlank String username);

	void deleteByUsername(@NotBlank String username);
}
