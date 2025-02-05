package com.game.service;

import com.game.bot.TelegramBot;
import com.game.model.Pet;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramNotificationService {
    private final TelegramBot bot;

    public TelegramNotificationService(TelegramBot bot) {
        this.bot = bot;
    }

    public void notifyUser(Pet pet) {
        String message = generateNotificationMessage(pet);
        Long userTelegramId = pet.getOwner().getChatId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userTelegramId.toString());
        sendMessage.setText(message);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String generateNotificationMessage(Pet pet) {
        StringBuilder message = new StringBuilder();
        message.append("Ваш питомец ").append(pet.getName()).append(" нуждается в вашей помощи!\n");

        if (pet.getHunger() >= 80) {
            message.append("- Он очень голоден (Голод: ").append(pet.getHunger()).append(")\n");
        }
        if (pet.getHappiness() <= 20) {
            message.append("- Ему скучно и грустно (Счастье: ").append(pet.getHappiness()).append(")\n");
        }
        if (pet.getEnergy() <= 20) {
            message.append("- Он устал (Энергия: ").append(pet.getEnergy()).append(")\n");
        }

        message.append("\nВыполните действие: кормление, игра или прогулка!");
        return message.toString();
    }
}

