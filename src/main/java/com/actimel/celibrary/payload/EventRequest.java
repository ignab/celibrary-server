package com.actimel.celibrary.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.sql.Date;

public class EventRequest {
    @NotBlank
    @Size(max = 140)
    private String name;

    private Long places_id;

    
    private Date date;

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

    public Long getPlaces_id() {
        return places_id;
    }

    public void setPlaces_id(Long places_id) {
        this.places_id = places_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}