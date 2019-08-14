package com.actimel.celibrary.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    //private long previousId;
    private String text;
    
    private int likes;
    private int dislikes;
    //private int ranking;
    //private int ranking_count;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;
    
    private Long placesId;
    private Long usersId;
    private String photo_name;
   
    private Date createdAt;
    
    
    //CONSTRUCTOR PELAT
    public Comment(){}

   
    // CONSTRUCTOR SIN ID
    public Comment(String text, Long placesId, Long usersId) {
        this.id=0;
        this.text = text;
        this.placesId = placesId;
        this.usersId = usersId;
    }
   
    //CONSTRUCTOR CON ID
    public Comment(long id, String text, Long placesId, Long usersId) {
        this.id = id;
        this.text = text;
        this.placesId = placesId;
        this.usersId = usersId;
    }
   
    @Override
    public String toString() {
        return String.format("%s (%d)", this.text, this.usersId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
    
    public int getDislikes() {
        return dislikes;
    }
    
    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public Place getPlace() {
        return place;
    }
    
    public void setPlace(Place place) {
        this.place = place;
    }

    public Long getPlacesId() {
        return placesId;
    }

    public void setPlacesId(Long placesId) {
        this.placesId = placesId;
    }

    public Long getUsersId() {
        return usersId;
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }
  
}