package com.actimel.celibrary.models;

import java.awt.Choice;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.actimel.celibrary.models.audit.UserDateAudit;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;




@Entity
@Table(name="places")
public class Place extends UserDateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String name;
    /// verificar uso de ranking (Se creo tabla aparte)
    private int ranking;
    private int ranking_count;
    private String photo_name;


    //Verify 
    @OneToMany(
            mappedBy = "place", //poll
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 30)
    private List<Comment> commentaries = new ArrayList<>();

    // private long creator_id;
    // private long createdBy;
    private String latitude;
    private String longitude;
    
    
    //CONSTRUCTOR PELAT
    public Place(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getRanking_count() {
        return ranking_count;
    }

    public void setRanking_count(int ranking_count) {
        this.ranking_count = ranking_count;
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

    public String getPhoto_name() {
        return photo_name;
    }

    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }
  
}