package com.game.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pet_actions")
public class PetActions {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pets_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String actionType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public PetActions(){};

    public PetActions(LocalDateTime timestamp, String actionType, User user, Pet pet, Long id) {
        this.timestamp = timestamp;
        this.actionType = actionType;
        this.user = user;
        this.pet = pet;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {this.user = user;}

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public LocalDateTime getTimestamp() {return timestamp;}

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
