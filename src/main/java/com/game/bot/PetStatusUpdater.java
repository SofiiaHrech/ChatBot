package com.game.bot;

import com.game.model.Pet;
import com.game.repository.PetRepository;
import com.game.service.TelegramNotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PetStatusUpdater {
    private PetRepository petRepository;

    private TelegramNotificationService telegramNotificationService;

    public PetStatusUpdater(PetRepository petRepository, TelegramNotificationService telegramNotificationService) {
        this.petRepository = petRepository;
        this.telegramNotificationService = telegramNotificationService;
    }

    @Scheduled(fixedRate = 60000) // Запускаем каждую минуту
    public void updatePetStatuses() {
        List<Pet> pets = petRepository.findAll();
        LocalDateTime now = LocalDateTime.now(); // Текущее время

        for (Pet pet : pets) {
            // Обновляем показатели питомца
            pet.setHunger(pet.getHunger() + 1);
            pet.setHappiness(pet.getHappiness() - 1);
            pet.setEnergy(pet.getEnergy() - 1);

            // Убеждаемся, что значения остаются в пределах 0-100
            pet.setHunger(Math.min(pet.getHunger(), 100));
            pet.setHappiness(Math.max(pet.getHappiness(), 0));
            pet.setEnergy(Math.max(pet.getEnergy(), 0));

            // Проверяем, нужно ли отправлять уведомление
            if (pet.getHunger() >= 80 || pet.getHappiness() <= 20 || pet.getEnergy() <= 20) {
                // Проверяем, прошло ли уже 60 минут с последнего уведомления
                if (pet.getLastNotificationTime() == null ||
                        Duration.between(pet.getLastNotificationTime(), now).toMinutes() >= 60) {

                    telegramNotificationService.notifyUser(pet); // Отправляем уведомление
                    pet.setLastNotificationTime(now); // Запоминаем время отправки
                }
            }
            // Сохраняем обновленные данные
            petRepository.save(pet);
        }
    }
}
