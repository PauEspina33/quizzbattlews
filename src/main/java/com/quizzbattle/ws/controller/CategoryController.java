package com.quizzbattle.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizzbattle.ws.model.Category;
import com.quizzbattle.ws.service.CategoryService;
import com.quizzbattle.ws.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Category", description = "Category API")
@RestController
@RequestMapping("/category")
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;

	@Operation(summary = "Find all categories", description = "Retrieve all categories")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Category.class))) }, description = "Categories retrieved successfully")
	@GetMapping("/find/all")
	public List<Category> findAll() {

		return categoryService.findAll();
	}

	@Operation(summary = "Find category by name", description = "Retrieve category by name")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Category.class))) }, description = "Category retrieved successfully")
	@GetMapping("/find/by/name")
	public Category findAllByName(
			@Parameter(description = "Name of the category", required = true) @RequestParam(value = "name") String name) {

		return categoryService.getByName(name);
	}
}
