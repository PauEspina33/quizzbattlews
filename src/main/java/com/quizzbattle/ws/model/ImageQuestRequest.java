package com.quizzbattle.ws.model;

import java.util.Base64;

public class ImageQuestRequest {
	private Long id;
	
	private String imageBase64;
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
