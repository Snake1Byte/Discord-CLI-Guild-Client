package com.github.snake1byte.discord_cli_guild_client;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.sticker.Sticker;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;

public class Test {
    private JDA jda;

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        String token = null;
        try {
            token = new Constants().getToken();
        } catch (IOException e) {
            // TODO log
            System.exit(1);
        }
        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.MESSAGE_CONTENT)
                .build();
        jda.addEventListener(listener);
    }

    ListenerAdapter listener = new ListenerAdapter() {
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            System.out.println("onMessageReceived");
            System.out.println(event.getAuthor().getDiscriminator());
            System.out.println(event.getAuthor().getName());
            System.out.println(event.getChannel().getName());
            System.out.println(event.getChannelType());
            System.out.println(event.getGuild().getName());
            System.out.println(event.getMember().getNickname());
            event.getMessage().getAttachments().forEach(e -> System.out.println(e.getContentType()));
            event.getMessage().getAttachments().forEach(e -> System.out.println(e.getDescription()));
            event.getMessage().getAttachments().forEach(e -> System.out.println(e.getFileExtension()));
            event.getMessage().getAttachments().forEach(e -> System.out.println(e.getFileName()));
            event.getMessage().getAttachments().forEach(e -> System.out.println(e.getHeight()));
            event.getMessage().getAttachments().forEach(e -> System.out.println(e.getSize()));
            event.getMessage().getAttachments().forEach(e -> System.out.println(e.getWidth()));
            event.getMessage().getAttachments().forEach(e -> System.out.println(e.isImage()));
            event.getMessage().getAttachments().forEach(e -> System.out.println(e.isVideo()));
            System.out.println(event.getMessage().getCategory().getName());
            System.out.println(event.getMessage().getContentDisplay());
            event.getMessage().getEmbeds().forEach(e -> System.out.println(e.getDescription()));
            event.getMessage().getEmbeds().forEach(e -> System.out.println(e.getSiteProvider().getName()));
            event.getMessage().getEmbeds().forEach(e -> System.out.println(e.getLength()));
            event.getMessage().getEmbeds().forEach(e -> System.out.println(e.getTimestamp()));
            event.getMessage().getEmbeds().forEach(e -> System.out.println(e.getTitle()));
            event.getMessage().getEmbeds().forEach(e -> System.out.println(e.getType()));
            event.getMessage().getEmbeds().forEach(e -> System.out.println(e.getVideoInfo()));
            if (event.getMessage().getMessageReference() != null && event.getMessage().getMessageReference()
                    .getMessage() != null) {
                System.out.println(event.getMessage().getMessageReference().getMessage().getContentDisplay());
            }
            event.getMessage().getStickers().forEach(e -> System.out.println(e.getName()));
            System.out.println(event.getMessage().getTimeCreated());
            System.out.println(event.getMessage().getType().name());
            System.out.println("\n\n");
        }

        @Override
        public void onMessageUpdate(MessageUpdateEvent event) {
            System.out.println("onMessageUpdate");
        }

        @Override
        public void onMessageDelete(MessageDeleteEvent event) {
            System.out.println("onMessageDelete");
        }

        @Override
        public void onMessageEmbed(MessageEmbedEvent event) {
            System.out.println("onMessageEmbed");
        }

        @Override
        public void onUserTyping(UserTypingEvent event) {
            System.out.println("onUserTyping");
        }

        @Override
        public void onMessageReactionAdd(MessageReactionAddEvent event) {
            System.out.println("onMessageReactionAdd");
        }

        @Override
        public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
            System.out.println("onMessageReactionRemove");
        }

        @Override
        public void onMessageReactionRemoveAll(MessageReactionRemoveAllEvent event) {
            System.out.println("onMessageReactionRemoveAll");
        }

        @Override
        public void onMessageReactionRemoveEmoji(MessageReactionRemoveEmojiEvent event) {
            System.out.println("onMessageReactionRemoveEmoji");
        }
    };
}
