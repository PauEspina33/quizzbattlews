package com.quizzbattle.ws.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
@Table(name = "category", indexes = { @Index(name = "idx_category_name", columnList = "name", unique = false) })

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
public class Category {
	private static final long serialVersionUID = 1L;

	public static final int MIN_CATEGORY_NAME = 3;
	public static final int MAX_CATEGORY_NAME = 50;

	/* JPA: ID with auto-increment */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = MIN_CATEGORY_NAME, max = MAX_CATEGORY_NAME, message = "Category name must be between {min} and {max} characters")
	@NotBlank(message = "Category name cannot be blank")
	@Column(name = "name", unique = true)
	private String name;

	@Column(name = "image_url")
	private String imageUrl;

}
