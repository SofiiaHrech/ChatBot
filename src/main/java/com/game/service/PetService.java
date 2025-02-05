package com.game.service;

import com.game.model.Pet;
import com.game.model.User;
import com.game.repository.PetRepository;
import com.game.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    private final PetRepository petRepository;

    private final UserRepository userRepository;

    private PetActoinsService petActionsService;

    public PetService(PetRepository petRepository, UserRepository userRepository,
                      PetActoinsService petActionsService) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.petActionsService = petActionsService;
    }

    public String createPet(Long chatId, String petName) {
        Optional<User> userOpt = userRepository.findByChatId(chatId);
        if (userOpt.isEmpty()) {
            return "Сначала зарегистрируйтесь!";
        }

        User user = userOpt.get();

        // Создаем нового питомца
        Pet pet = new Pet();
        pet.setName(petName);
        pet.setHunger(50); // Начальные показатели
        pet.setHappiness(50);
        pet.setEnergy(50);
        pet.setOwner(user);

        petRepository.save(pet);
        return "Питомец " + petName + " успешно создан!";
    }
    public String performAction( Long chatId, String action, String petName ) {
        Optional<User> userOpt = userRepository.findByChatId(chatId);
        if (userOpt.isEmpty()) {
            return "Сначала зарегистрируйтесь!";
        }

        User user = userOpt.get();
        // Находим питомца по имени
        Optional<Pet> petOpt = petRepository.findByOwnerAndName(user, petName);
        if (petOpt.isEmpty()) {
            return "Питомец с именем " + petName + " не найден.";
        }

        Pet pet = petOpt.get();
        // Выполняем действие
        switch (action.toLowerCase()){
            case "кормление":
                petActionsService.feedPet(pet);
                petRepository.save(pet);
                return "Питомец " + petName + " теперь не голоден";
            case "игра":
               petActionsService.playPet(pet);
                petRepository.save(pet);
                return "Питомец " + petName + " счастлив";
            case "прогулка":
               petActionsService.walkPet(pet);
                petRepository.save(pet);
                return "Питомец " + petName + " счастлив и устал";
            default:
                System.out.println("Неизвестное действие!");
        }
        return String.valueOf(pet);
    }

    public String getPetsList(Long chatId) {
        Optional<User> userOpt = userRepository.findByChatId(chatId);
        if (userOpt.isEmpty()) {
            return "Сначала зарегистрируйтесь!";
        }

        User user = userOpt.get();
        List<Pet> pets = petRepository.findAllByOwner(user);

        if (pets.isEmpty()) {
            return "У вас пока нет питомцев.";
        }

        StringBuilder response = new StringBuilder("Ваши питомцы:\n");
        for (Pet pet : pets) {
            response.append("- ").append(pet.getName())
                    .append(" (Голод: ").append(pet.getHunger())
                    .append(", Счастье: ").append(pet.getHappiness())
                    .append(", Энергия: ").append(pet.getEnergy())
                    .append(")\n");
        }
        return response.toString();
    }
}
