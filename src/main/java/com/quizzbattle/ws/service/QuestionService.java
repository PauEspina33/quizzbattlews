package com.quizzbattle.ws.service;

import java.util.List;

import com.quizzbattle.ws.model.Category;
import com.quizzbattle.ws.model.Question;

public interface QuestionService {

	List<Question> findAll(Category category);

	List<Question> findRandom(Category category);

}
