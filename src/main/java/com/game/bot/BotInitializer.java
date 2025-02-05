package com.game.bot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotInitializer {
    @Bean
    CommandLineRunner runner(ApplicationContext context) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(context.getBean("telegramBot", TelegramLongPollingBot.class));
            }
        };
    }
}

