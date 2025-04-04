package com.quizzbattle.ws.service.impl;

import java.util.List;

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

}
