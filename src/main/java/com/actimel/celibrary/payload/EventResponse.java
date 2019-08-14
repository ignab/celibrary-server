package com.actimel.celibrary.payload;

import java.util.Date;
import java.time.Instant;
import java.util.List;

public class EventResponse {
    private Long id;
    private String name;
    //private List<ChoiceResponse> choices;  //mapear para comentarios
    private UserSummary createdBy;
    private Instant creationDateTime;
    // private Instant expirationDateTime;
    // private Boolean isExpired;
    private Long places_id;
    private Date date;

    // @JsonInclude(JsonInclude.Include.NON_NULL)
    // private Long selectedChoice;
    // private Long totalVotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserSummary getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserSummary createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
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