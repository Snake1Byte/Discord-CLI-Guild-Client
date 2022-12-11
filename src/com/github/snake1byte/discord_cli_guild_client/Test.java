package com.github.snake1byte.discord_cli_guild_client;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public class Test {
    private JDA jda;
    private List<net.dv8tion.jda.api.entities.Message> cache = new ArrayList<>();
    private Map<Long, List<String>> changeHistory = new HashMap<>();

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
        jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.MESSAGE_CONTENT).build();
        jda.addEventListener(listener);
    }
    @Nullable
    private Message createMessageObject(net.dv8tion.jda.api.entities.Message discordApiMessageObject) {
        Message repliedTo = null;
        if (discordApiMessageObject.getMessageReference() != null && discordApiMessageObject.getMessageReference().getMessage() != null) {
            repliedTo = createMessageObject(discordApiMessageObject.getMessageReference().getMessage());
        }

        String usedStickerName = null;
        if (discordApiMessageObject.getStickers().size() > 0) {
            usedStickerName = discordApiMessageObject.getStickers().get(0).getName();
        }

        List<Attachment> attachments = null;
        if (discordApiMessageObject.getAttachments().size() > 0) {
            attachments = new ArrayList<>();
            for (net.dv8tion.jda.api.entities.Message.Attachment attachment : discordApiMessageObject.getAttachments()) {
                attachments.add(new Attachment(attachment.getFileName(), attachment.getSize(), attachment.isImage(), attachment.isVideo(), attachment.getProxyUrl()));
            }
        }

        if (discordApiMessageObject.getChannelType() == ChannelType.PRIVATE) {      // DM
            return new Message(discordApiMessageObject.getAuthor().getName(), discordApiMessageObject.getAuthor().getDiscriminator(), discordApiMessageObject.getContentDisplay(), discordApiMessageObject.getTimeCreated(), repliedTo, attachments, usedStickerName);
        } else if (discordApiMessageObject.getChannelType() == ChannelType.TEXT) {  // Guild
            String nickname = null, channelCategoryId = null, channelCategoryName = null;
            if (discordApiMessageObject.getMember() != null) {
                nickname = discordApiMessageObject.getMember().getNickname();
            }
            if (discordApiMessageObject.getCategory() != null) {
                channelCategoryId = discordApiMessageObject.getCategory().getId();
                channelCategoryName = discordApiMessageObject.getCategory().getName();
            }
            return new GuildMessage(discordApiMessageObject.getAuthor().getName(), discordApiMessageObject.getAuthor().getDiscriminator(), discordApiMessageObject.getContentDisplay(), discordApiMessageObject.getTimeCreated(), repliedTo, attachments, usedStickerName, discordApiMessageObject.getGuild().getId(), discordApiMessageObject.getGuild().getName(), nickname, discordApiMessageObject.getChannel().getId(), discordApiMessageObject.getChannel().getName(), channelCategoryId, channelCategoryName);
        } else {
            // TODO log that a message of type ChannelType has been received for now
            System.out.printf("Received a message in channel type %s.%n", discordApiMessageObject.getChannelType());
            if (!discordApiMessageObject.getContentDisplay().isBlank()) {
                System.out.printf("Content of the message was \"%s\".%n", discordApiMessageObject.getContentDisplay());
            }
            return null;
        }
    }

    ListenerAdapter listener = new ListenerAdapter() {
        @Override
        public void onReady(@NotNull ReadyEvent event) {
            System.out.println("Ready.\n");
        }

        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            Message message = createMessageObject(event.getMessage());
            cache.add(event.getMessage());
            if (message != null) {
                System.out.println(message.toMessageReceivedString());
            }
        }

        @Override
        public void onMessageUpdate(MessageUpdateEvent event) {
            Message message = createMessageObject(event.getMessage());
            Optional<net.dv8tion.jda.api.entities.Message> updatedMessageOpt = cache.stream().filter(e -> e.getIdLong() == event.getMessageIdLong()).findFirst();
            if (updatedMessageOpt.isEmpty()) {
                cache.add(event.getMessage());
                System.out.printf("Message updated:%n%s%n", message);
            } else {
                // System.out.printf(""); //TODO toString() methods of Message (with header, body...)
                List<String> messageHistoryOfId = changeHistory.computeIfAbsent(updatedMessageOpt.get().getIdLong(), k -> {
                    List<String> messageHistory = new ArrayList<>();
                    messageHistory.add(updatedMessageOpt.get().getContentDisplay());
                    return messageHistory;
                });
                messageHistoryOfId.add(event.getMessage().getContentDisplay());
            }
        }

        @Override
        public void onMessageDelete(@NotNull MessageDeleteEvent event) {
            Optional<net.dv8tion.jda.api.entities.Message> deletedMessageOpt = cache.stream().filter(e -> e.getIdLong() == event.getMessageIdLong()).findFirst();
            if (deletedMessageOpt.isEmpty()) {
                System.out.printf("Message with ID %d was deleted.%n", event.getMessageIdLong());
            } else {
                System.out.printf("Message deleted:%n%s%n", createMessageObject(deletedMessageOpt.get()));
            }
        }

        @Override
        public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
            System.out.println("onMessageReactionAdd");
        }

        @Override
        public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
            System.out.println("onMessageReactionRemove");
        }

        @Override
        public void onMessageReactionRemoveAll(@NotNull MessageReactionRemoveAllEvent event) {
            System.out.println("onMessageReactionRemoveAll");
        }
    };
}
