package com.quizzbattle.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
@Table(name = "question")

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
public class Question {

    private static final long serialVersionUID = 1L;

    public static final int MIN_STATEMENT_LENGTH = 3;
    public static final int MAX_STATEMENT_LENGTH = 255;

    /* JPA: ID with auto-increment */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* JPA: Relationship with Category */
    @Valid
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Category cannot be null")
    private Category category;

    /* Validation for statement */
    @Size(min = MIN_STATEMENT_LENGTH, max = MAX_STATEMENT_LENGTH, message = "Statement must be between {min} and {max} characters")
    @NotBlank(message = "Statement cannot be blank")
    @Column(name = "statement", nullable = false)
    private String statement;

    /* Validation for correct option */
    @NotBlank(message = "Correct option cannot be blank")
    @Column(name = "correct_option", nullable = false)
    private String correctOption;

    /* Validation for wrong options */
    @NotBlank(message = "Wrong option 1 cannot be blank")
    @Column(name = "wrong_option1", nullable = false)
    private String wrongOption1;

    @NotBlank(message = "Wrong option 2 cannot be blank")
    @Column(name = "wrong_option2", nullable = false)
    private String wrongOption2;

    @NotBlank(message = "Wrong option 3 cannot be blank")
    @Column(name = "wrong_option3", nullable = false)
    private String wrongOption3;

    /* Optional image URL for question */
    /* Foto de perfil (URL) */
	@Lob
	@Column(name = "image", columnDefinition = "LONGBLOB")
	@JsonIgnore
	private byte[] image;
	
	// Getters y Setters
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] picture) {
        this.image = picture;
    }
	
	
}
