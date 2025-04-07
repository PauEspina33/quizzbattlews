package com.quizzbattle.ws.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizzbattle.ws.model.Category;
import com.quizzbattle.ws.model.Question;
import com.quizzbattle.ws.repository.QuestionRepository;
import com.quizzbattle.ws.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	@Override
	public List<Question> findAll(Category category) {
		return questionRepository.findAllByFilters(category);
	}

	@Override
	public List<Question> findRandom(Category category) {
		List<Question> questions = questionRepository.findAllByFilters(category);

		if (questions == null || questions.isEmpty()) {
			return Collections.emptyList(); // O lanza una excepción si prefieres
		}

		Random random = new Random();
		Question randomQuestion = questions.get(random.nextInt(questions.size()));

		return List.of(randomQuestion); // Devuelves la pregunta envuelta en una lista
	}

}
