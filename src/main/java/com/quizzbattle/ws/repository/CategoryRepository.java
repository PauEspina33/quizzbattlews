package com.quizzbattle.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizzbattle.ws.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByName(String name);

}
