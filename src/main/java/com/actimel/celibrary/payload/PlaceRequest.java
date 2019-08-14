package com.actimel.celibrary.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class PlaceRequest {
    @NotBlank
    @Size(max = 140)
    private String name;

    @NotBlank
    private String latitude;

    @NotBlank
    private String longitude;

    // @NotNull
    // @Size(min = 2, max = 6)
    // @Valid
    // private List<ChoiceRequest> choices;

    // @NotNull
    // @Valid
    // private PollLength pollLength;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
   
}