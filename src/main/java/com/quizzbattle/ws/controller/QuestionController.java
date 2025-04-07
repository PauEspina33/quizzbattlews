package com.quizzbattle.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizzbattle.ws.model.Category;
import com.quizzbattle.ws.model.Question;
import com.quizzbattle.ws.service.CategoryService;
import com.quizzbattle.ws.service.QuestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Question", description = "Question API")
@RestController
@RequestMapping("/questions")
@SecurityRequirement(name = "Bearer Authentication")
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private CategoryService categoryService; // Para obtener la categoría desde el ID

	@Operation(summary = "Find all questions", description = "Retrieve all questions filtered by category")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Question.class))) }, description = "Questions retrieved successfully")
	@GetMapping("/find/all")
	public List<Question> findAll(
			@Parameter(description = "Name of the category", required = false) @RequestParam(value = "categoryName", required = false) String categoryName) {

		Category category = categoryName != null ? categoryService.getByName(categoryName) : null;
		return questionService.findAll(category);
	}

	@Operation(summary = "Find random questions by category", description = "Retrieve random question filtered by category")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Question.class))) }, description = "Question retrieved successfully")
	@GetMapping("/find/random/by/category")
	public List<Question> findRandom(
			@Parameter(description = "Name of the category", required = false) @RequestParam(value = "categoryName", required = true) String categoryName) {

		Category category = categoryName != null ? categoryService.getByName(categoryName) : null;
		return questionService.findRandom(category);
	}
}
