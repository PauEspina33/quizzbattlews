package com.quizzbattle.ws.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.quizzbattle.ws.model.Category;
import com.quizzbattle.ws.repository.CategoryRepository;
import com.quizzbattle.ws.service.CategoryService;

import jakarta.validation.constraints.NotBlank;

@Validated
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Category getByName(@NotBlank String name) {
		return categoryRepository.findByName(name);
	}

}
