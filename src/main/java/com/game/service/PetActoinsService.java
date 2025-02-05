package com.game.service;

import com.game.model.Pet;
import org.springframework.stereotype.Service;

@Service
public class PetActoinsService {
    public void feedPet(Pet pet) {
        pet.setHunger(pet.getHunger() - 10);
        pet.setHappiness(pet.getHappiness() + 5);
        pet.setEnergy(pet.getEnergy() + 10);
        if (pet.getHunger() > 100) pet.setHunger(100);
        if (pet.getHappiness() > 100) pet.setHappiness(100);
        if (pet.getEnergy() > 100) pet.setEnergy(100);
        if (pet.getHunger() < 0) pet.setHunger(0);
        if (pet.getHappiness() < 0) pet.setHappiness(0);
        if (pet.getEnergy() < 0) pet.setEnergy(0);
    }

    public void playPet(Pet pet){
        pet.setHappiness(pet.getHappiness() + 10);
        pet.setEnergy(pet.getEnergy() - 15);
        if (pet.getHunger() > 100) pet.setHunger(100);
        if (pet.getHappiness() > 100) pet.setHappiness(100);
        if (pet.getEnergy() > 100) pet.setEnergy(100);
        if (pet.getHunger() < 0) pet.setHunger(0);
        if (pet.getHappiness() < 0) pet.setHappiness(0);
        if (pet.getEnergy() < 0) pet.setEnergy(0);
    }

    public void walkPet(Pet pet){
        pet.setHappiness(pet.getHappiness() + 15);
        pet.setEnergy(pet.getEnergy() - 20);
        pet.setHunger(pet.getHunger() + 5);
        if (pet.getHappiness() > 100) pet.setHappiness(100);
        if (pet.getEnergy() > 100) pet.setEnergy(100);
        if (pet.getHunger() < 0) pet.setHunger(0);
        if (pet.getHappiness() < 0) pet.setHappiness(0);
        if (pet.getEnergy() < 0) pet.setEnergy(0);
    }
}
