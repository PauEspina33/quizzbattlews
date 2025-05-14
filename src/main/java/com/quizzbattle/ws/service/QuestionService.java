package com.quizzbattle.ws.service;

import java.util.List;

import com.quizzbattle.ws.model.Category;
import com.quizzbattle.ws.model.Question;

import jakarta.validation.constraints.NotNull;

public interface QuestionService {

	List<Question> findAll(Category category);

	List<Question> findRandom(Category category);

	Question getById(Long id);

	Question update(Question question);

}
