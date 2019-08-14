package com.actimel.celibrary.models;

import com.actimel.celibrary.models.audit.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Date;


@Entity
@Table(name="events")
public class Event extends UserDateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private long places_id;
    private Date date;
    private String photo_name;
    private Date createdAt;
    
    //CONSTRUCTOR PELAT
    public Event(){}

    public long getId() {
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
   
    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }

  
}