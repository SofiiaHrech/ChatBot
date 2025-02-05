package com.game.bot;

import com.game.model.User;
import com.game.repository.UserRepository;
import com.game.service.PetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
@PropertySource("classpath:telegram.properties")
public class TelegramBot  extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final PetService petService;

    private final UserRepository userRepository;

    public TelegramBot(PetService petService, UserRepository userRepository) {
        this.petService = petService;
        this.userRepository = userRepository;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void onUpdateReceived(Update update) {
        final long chatId = update.getMessage().getChatId();
        final String text = update.getMessage().getText();

        if (text.startsWith("/start")) {
            Optional<User> userOpt = userRepository.findByChatId(chatId);

            if (userOpt.isPresent()) {
                sendText(chatId, "Вы уже зарегистрированы! Можете посмотреть всех своих питомцев /my_pets");
            } else {
                // Если пользователь не зарегистрирован, создаем нового
                User newUser = new User();
                newUser.setChatId(chatId);
                userRepository.save(newUser);
                sendText(chatId, "Привет! Добро пожаловать в PetGame! Создайте питомца: /create <имя>");
            }
            return;
        }
        if (text.equals("/feed")) {// Проверяем, что команда введена без имени
            Optional<User> userOpt = userRepository.findByChatId(chatId);
            if (userOpt.isEmpty()) {
                sendText(chatId, "Сначала зарегистрируйтесь!");
                return;
            }
            sendText(chatId, "Укажите имя питомца. Например: /feed Rex");
        }else if (text.startsWith("/feed ")) {
            String[] parts = text.split(" ", 2);
            String petName = parts[1];
            String feedResult = petService.performAction(chatId, "кормление", petName);
            sendText(chatId, feedResult);
        }else if (text.equals("/play")) {// Проверяем, что команда введена без имени
            Optional<User> userOpt = userRepository.findByChatId(chatId);
            if (userOpt.isEmpty()) {
                sendText(chatId, "Сначала зарегистрируйтесь!");
                return;
            }
            sendText(chatId, "Укажите имя питомца. Например: /play Rex");
        }else if (text.startsWith("/play ")) {
            String[] parts = text.split(" ", 2);
            String petName = parts[1];
            String playResult = petService.performAction(chatId, "игра", petName);
            sendText(chatId, playResult);
        }else if (text.equals("/walk")) {// Проверяем, что команда введена без имени
            Optional<User> userOpt = userRepository.findByChatId(chatId);
            if (userOpt.isEmpty()) {
                sendText(chatId, "Сначала зарегистрируйтесь!");
                return;
            }
            sendText(chatId, "Укажите имя питомца. Например: /walk Rex");
        }else if (text.startsWith("/walk ")) {
            String[] parts = text.split(" ", 2);
            String petName = parts[1];
            String walkResult = petService.performAction(chatId, "прогулка", petName);
            sendText(chatId, walkResult);
        } else if (text.startsWith("/my_pets")) {
            String petsList = petService.getPetsList(chatId);
            sendText(chatId, petsList);
        } else if (text.startsWith("/create ")) {
            String[] parts = text.split(" ", 2);
            if (parts.length < 2) {
                sendText(chatId, "Укажите имя питомца. Например: /create Rex");
                return;
            }
            String petName = parts[1];
            String createResult = petService.createPet(chatId, petName);
            sendText(chatId, createResult);
        } else {
            sendText(chatId, "Неизвестная команда.");
        }
    }

    private void sendText(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
