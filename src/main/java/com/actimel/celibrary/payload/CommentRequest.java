package com.actimel.celibrary.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CommentRequest {
    @NotBlank
    @Size(max = 40)
    private String text;

    @NotBlank
    private Long placesId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getPlacesId() {
        return placesId;
    }

    public void setPlacesId(Long placesId) {
        this.placesId = placesId;
    }
}
