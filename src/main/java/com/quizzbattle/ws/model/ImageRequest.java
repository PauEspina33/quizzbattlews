package com.quizzbattle.ws.model;

import java.util.Base64;

public class ImageRequest {
	private String username;
	
	private String imageBase64;
	
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// Getters y Setters
    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
	
    public byte[] decodeImageBase64(String imageBase64) {
        if (imageBase64.contains(",")) {
            imageBase64 = imageBase64.substring(imageBase64.indexOf(",") + 1);
        }
        return Base64.getDecoder().decode(imageBase64);
    }
	
}
