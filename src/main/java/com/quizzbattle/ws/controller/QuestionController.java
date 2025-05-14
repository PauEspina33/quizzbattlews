package com.quizzbattle.ws.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.quizzbattle.ws.model.Category;
import com.quizzbattle.ws.model.ImageQuestRequest;
import com.quizzbattle.ws.model.ImageRequest;
import com.quizzbattle.ws.model.ImageResponse;
import com.quizzbattle.ws.model.Player;
import com.quizzbattle.ws.model.Question;
import com.quizzbattle.ws.service.CategoryService;
import com.quizzbattle.ws.service.QuestionService;
import com.quizzbattle.ws.validation.groups.OnUserCreate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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

	@Operation(summary = "Find random question by category", description = "Retrieve a random question filtered by category")
	@ApiResponse(responseCode = "200", content = {
	        @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class)) }, description = "Question retrieved successfully")
	@GetMapping("/find/random/by/category")
	public Question findRandom(
	        @Parameter(description = "Name of the category", required = true) @RequestParam(value = "categoryName", required = true) String categoryName) {

	    Category category = categoryName != null ? categoryService.getByName(categoryName) : null;
	    List<Question> questions = questionService.findRandom(category);

	    if (questions == null || questions.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No questions found for the category");
	    }

	    return questions.get(0); // Devuelve solo UNA pregunta
	}
	
	@GetMapping("/image/{id}")
	@Operation(
	    summary = "Obtener imagen de perfil en Base64",
	    description = "Devuelve la imagen de perfil del usuario codificada en Base64"
	)
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Imagen obtenida correctamente",
	        content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
	    @ApiResponse(responseCode = "404", description = "Question o imagen no encontrada")
	})
	public ResponseEntity<ImageResponse> getProfilePictureBase64(@PathVariable Long id) {
	    Question question = (Question) questionService.getById(id);
	    if (question == null || question.getImage() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }

	    String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(question.getImage());
	    return ResponseEntity.ok(new ImageResponse(base64Image));
	}
	
	@Operation(summary = "Save image in question", description = "Saves a question with image in the database. The response is the stored question from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ImageQuestRequest.class)) }, description = "Question saved ok")
	@ApiResponse(responseCode = "500", content = {
			@Content() }, description = "Error saving the question. See response body for more details")
	@PostMapping(value = "/upload/image")
	@Validated(OnUserCreate.class)
	public ResponseEntity<String> uploadProfilePicture(@RequestBody @Valid ImageQuestRequest imageQuestRequest) throws Exception {
		 if (imageQuestRequest.getImageBase64()== null || imageQuestRequest.getImageBase64().isEmpty()) {
		        return ResponseEntity.badRequest().body("Base64 vacío o nulo");
		   }
		System.out.println("Imagen Base64 recibida: " + imageQuestRequest.getImageBase64()); // Log para depuración

	    Question question = (Question) questionService.getById(imageQuestRequest.getId());
	    question.setImage(imageQuestRequest.decodeImageBase64(imageQuestRequest.getImageBase64()));
	    questionService.update(question);
	    return ResponseEntity.ok("Imagen de perfil actualizada correctamente");
	}

}
