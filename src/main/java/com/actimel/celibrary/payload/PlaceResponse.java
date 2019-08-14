package com.actimel.celibrary.payload;

import java.time.Instant;
import java.util.List;

import com.actimel.celibrary.models.Comment;

public class PlaceResponse {
    private Long id;
    private String name;
    // private List<ChoiceResponse> choices; //mapear para comentarios
    private List<CommentResponse> comments;
    private UserSummary createdBy;
    private Instant creationDateTime;
    // private Instant expirationDateTime;
    // private Boolean isExpired;
    private String latitude;
    private String longitude;

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

    public List<CommentResponse> getComments() {
        return comments;
    }
    
    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
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

    public String getLongitude() {
        return longitude;
    }
    
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }
    
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
}