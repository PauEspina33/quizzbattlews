package com.quizzbattle.ws.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
@Table(name = "friendship")

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder

public class Friendship {
	public static final String ACCEPTED = "ACCEPTED";
	public static final String PENDING = "PENDING";

	public enum Status {
		ACCEPTED, PENDING
	}

	/* JPA: ID with auto-increment */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
	@JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
	@NotNull(message = "Sender cannot be null")
	private Player sender;

	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
	@JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
	@NotNull(message = "Receiver cannot be null")
	private Player receiver;

	/* JPA: Status field (enum mapped as String) */
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	@NotNull(message = "Status cannot be null")
	private Status status;
}