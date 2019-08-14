package com.actimel.celibrary.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

import com.actimel.celibrary.models.audit.DateAudit;


@Entity
@Table(name="users")
public class User extends DateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotBlank (message = "Nombre requerido")
    private String name;

    private String username;

    private String password;
    
    @NaturalId
    @NotBlank
    @Email
    private String email;

    private String photo_name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_has_roles",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<Role> roles = new HashSet<>();
    
    public User() {
    }

    public User(String name, String username, String email,String password) {
        this.id=0;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
   
    public User(long id, String name, String username, String email,String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }    
   
    @Override
    public String toString() {
        return String.format("%s (%s)", this.username, this.email);
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail(){
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto_name() {
        return photo_name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    
    
    
    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }

    

}