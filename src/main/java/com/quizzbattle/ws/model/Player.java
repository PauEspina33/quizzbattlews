package com.quizzbattle.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/* JPA annotations */
@Entity
/* A client is identified in the user table with role=CLIENT */
@DiscriminatorValue(User.PLAYER)

/* Lombok */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Player extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	/* ID de Google (opcional, si inicia sesión con Google) */
	@Column(name = "google_id", unique = true)
	private String googleId;

	/* Token para notificaciones push (FCM) */
	@Column(name = "fcm_token", columnDefinition = "TEXT")
	private String fcmToken;

	/* Foto de perfil (URL) */
	@Lob
	@Column(name = "profile_picture", columnDefinition = "LONGBLOB")
	@JsonIgnore
	private byte[] profilePicture;

	@Override
	public String getInfo() {
		return "Player: " + username + " (" + email + ")";
	}
	
	// Getters y Setters
    public byte[] getImage() {
        return profilePicture;
    }

    public void setImage(byte[] picture) {
        this.profilePicture = picture;
    }

}
