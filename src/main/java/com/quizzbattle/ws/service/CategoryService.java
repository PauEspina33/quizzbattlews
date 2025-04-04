package com.quizzbattle.ws.service;

import java.util.List;

import com.quizzbattle.ws.model.Category;

import jakarta.validation.constraints.NotBlank;

public interface CategoryService {

	List<Category> findAll();

	Category getByName(@NotBlank String name);

}
