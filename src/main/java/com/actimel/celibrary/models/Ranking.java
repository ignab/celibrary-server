package com.actimel.celibrary.models;

import com.actimel.celibrary.models.audit.DateAudit;
import javax.persistence.*;

@Entity
@Table(name = "rankings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "place_id",
                "user_id"
        })
})
public class Ranking extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    // @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // @JoinColumn(name = "choice_id", nullable = false)
    // private Choice choice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPoll(Place place) {
        this.place = place;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}