package com.quizzbattle.ws.service;

import java.util.List;

import com.quizzbattle.ws.model.User;
import com.quizzbattle.ws.model.User.Role;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface UserService {

	List<User> findAll(Role[] roles, String fullName);

	User getByUsername(@NotBlank String username);

	User save(@NotNull @Valid User user);

	User update(@NotNull @Valid User user);

	void deleteByUsername(@NotBlank String username);

	boolean existsById(@NotBlank String username);
}