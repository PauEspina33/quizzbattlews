package com.quizzbattle.ws.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.quizzbattle.ws.PasswordSerializer;
import com.quizzbattle.ws.validation.groups.OnUserCreate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users", indexes = { @Index(name = "role", columnList = "role", unique = false),
		@Index(name = "username", columnList = "username", unique = false) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "role", visible = true)
@JsonSubTypes({ @Type(value = Player.class, name = User.PLAYER), @Type(value = Admin.class, name = User.ADMIN) })
@Schema(oneOf = { Player.class, Admin.class }, discriminatorProperty = "role")
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class User implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ADMIN = "ADMIN";
	public static final String PLAYER = "PLAYER";

	public enum Role {
		ADMIN, PLAYER
	}

	public static final int MIN_USERNAME = 2;
	public static final int MAX_USERNAME = 25;
	public static final int MIN_PASSWORD = 10;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	protected Long id;

	@NotBlank
	@Size(min = MIN_USERNAME, max = MAX_USERNAME)
	@Column(unique = true, nullable = false)
	protected String username;

	@Email
	@Column(unique = true, nullable = true)
	protected String email;

	@JsonSerialize(using = PasswordSerializer.class)
	@NotNull(groups = OnUserCreate.class)
	@NotBlank(groups = OnUserCreate.class)
	@Column(nullable = false)
	protected String password;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "role", insertable = false, updatable = false, nullable = false)
	protected Role role;

	@Column(name = "created_at", nullable = true, updatable = false)
	protected LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = true)
	protected LocalDateTime updatedAt;

	@Column(name = "last_login")
	protected LocalDateTime lastLogin;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = createdAt;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	@JsonIgnore
	public boolean isAdmin() {
		return role == Role.ADMIN;
	}

	@JsonIgnore
	public boolean isPlayer() {
		return role == Role.PLAYER;
	}

	@JsonIgnore
	public String getInfo() {
		return "User:" + email;
	}
}