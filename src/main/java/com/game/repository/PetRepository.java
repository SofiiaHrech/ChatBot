package com.game.repository;

import com.game.model.Pet;
import com.game.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner(User owner);
    Optional<Pet> findByOwnerAndName(User owner, String petName);
    List<Pet> findAllByOwner(User owner);
}
