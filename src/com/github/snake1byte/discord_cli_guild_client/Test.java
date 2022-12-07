package com.github.snake1byte.discord_cli_guild_client;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private Message createMessageObject(net.dv8tion.jda.api.entities.Message discordApiMessageObject) {
        String nickname = null, channelCategoryId = null, channelCategoryName = null;
        if (discordApiMessageObject.getMember() != null) {
            nickname = discordApiMessageObject.getMember().getNickname();
        }
        if (discordApiMessageObject.getCategory() != null) {
            channelCategoryId = discordApiMessageObject.getCategory().getId();
            channelCategoryName = discordApiMessageObject.getCategory().getName();
        }

        Message repliedTo = null;
        if (discordApiMessageObject.getMessageReference() != null && discordApiMessageObject.getMessageReference()
                .getMessage() != null) {
            repliedTo = createMessageObject(discordApiMessageObject.getMessageReference().getMessage());
        }

        String usedStickerName = null;
        if (discordApiMessageObject.getStickers().size() > 0) {
            usedStickerName = discordApiMessageObject.getStickers().get(0).getName();
        }

        List<Attachment> attachments = new ArrayList<>();
        for (net.dv8tion.jda.api.entities.Message.Attachment attachment : discordApiMessageObject.getAttachments()) {
            attachments.add(new Attachment(attachment.getFileName(), attachment.getSize(), attachment.isImage(), attachment.isVideo(), attachment.getProxyUrl()));
        }

        return new Message(discordApiMessageObject.getAuthor().getName(), discordApiMessageObject.getAuthor()
                .getDiscriminator(), nickname, discordApiMessageObject.getGuild()
                .getId(), discordApiMessageObject.getGuild().getName(), discordApiMessageObject.getChannel()
                .getId(), discordApiMessageObject.getChannel()
                .getName(), channelCategoryId, channelCategoryName, discordApiMessageObject.getContentDisplay(), discordApiMessageObject.getTimeCreated(), repliedTo, usedStickerName, attachments);
    }

    ListenerAdapter listener = new ListenerAdapter() {
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            Message message = createMessageObject(event.getMessage());
            System.out.println();
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
