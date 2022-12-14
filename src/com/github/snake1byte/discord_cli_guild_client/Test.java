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
    private List<Message> cache;
    private Map<Long, List<Message>> changeHistory = new HashMap<>();

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
//        try {
//            cache = Persistence.loadCache();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        cache = new ArrayList<>();
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

    @Nullable
    private Message createMessageObject(net.dv8tion.jda.api.entities.Message discordApiMessageObject) {
        Message repliedTo = null;
        if (discordApiMessageObject.getMessageReference() != null && discordApiMessageObject.getMessageReference()
                .getMessage() != null) {
            repliedTo = createMessageObject(discordApiMessageObject.getMessageReference().getMessage());
        }

        long id = discordApiMessageObject.getIdLong();

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
            return new Message(id, discordApiMessageObject.getAuthor().getName(), discordApiMessageObject.getAuthor()
                    .getDiscriminator(), discordApiMessageObject.getContentDisplay(), discordApiMessageObject.getTimeCreated(), repliedTo, attachments, usedStickerName);
        } else if (discordApiMessageObject.getChannelType() == ChannelType.TEXT) {  // Guild
            String nickname = null, channelCategoryId = null, channelCategoryName = null;
            if (discordApiMessageObject.getMember() != null) {
                nickname = discordApiMessageObject.getMember().getNickname();
            }
            if (discordApiMessageObject.getCategory() != null) {
                channelCategoryId = discordApiMessageObject.getCategory().getId();
                channelCategoryName = discordApiMessageObject.getCategory().getName();
            }
            return new GuildMessage(id, discordApiMessageObject.getAuthor()
                    .getName(), discordApiMessageObject.getAuthor()
                    .getDiscriminator(), discordApiMessageObject.getContentDisplay(), discordApiMessageObject.getTimeCreated(), repliedTo, attachments, usedStickerName, discordApiMessageObject.getGuild()
                    .getId(), discordApiMessageObject.getGuild()
                    .getName(), nickname, discordApiMessageObject.getChannel()
                    .getId(), discordApiMessageObject.getChannel().getName(), channelCategoryId, channelCategoryName);
        } else {
            System.out.printf("Received a message in channel type %s.%n", discordApiMessageObject.getChannelType());
            if (!discordApiMessageObject.getContentDisplay().isBlank()) {
                System.out.printf("%nContent of the message was \"%s\".%n%n", discordApiMessageObject.getContentDisplay());
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
            if (message != null) {
                cache.add(message);
                try {
                    Persistence.saveCache(cache);
                } catch (IOException e) {
                    e.printStackTrace();
                    // TODO log lmao
                }
                System.out.println(message.toMessageReceivedString());
            }
        }

        @Override
        public void onMessageUpdate(MessageUpdateEvent event) {
            Message message = createMessageObject(event.getMessage());
            if (message != null) {
                Optional<Message> updatedMessageOpt = cache.stream().filter(e -> e.getId() == event.getMessageIdLong())
                        .findFirst();
                if (updatedMessageOpt.isEmpty()) {
                    cache.add(message);
                    System.out.println(message.toMessageEditedString(null));
                } else {
                    List<Message> messageHistoryOfId = changeHistory.computeIfAbsent(updatedMessageOpt.get()
                            .getId(), k -> {
                        List<Message> messageHistory = new ArrayList<>();
                        messageHistory.add(updatedMessageOpt.get());
                        return messageHistory;
                    });
                    messageHistoryOfId.add(message);
                    System.out.println(message.toMessageEditedString(updatedMessageOpt.get()));
                }
            }
        }

        @Override
        public void onMessageDelete(@NotNull MessageDeleteEvent event) {
            Optional<Message> deletedMessageOpt = cache.stream().filter(e -> e.getId() == event.getMessageIdLong())
                    .findFirst();
            if (deletedMessageOpt.isEmpty()) {
                System.out.printf("Message with ID %d deleted.%n%n", event.getMessageIdLong());
            } else {
                System.out.println(Message.toMessageDeletedString(deletedMessageOpt.get()));
            }
        }

        @Override
        public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
            Message message;
            Optional<Message> messageOpt = cache.stream().filter(e -> e.getId() == event.getMessageIdLong())
                    .findFirst();
            if (messageOpt.isEmpty()) {
                message = createMessageObject(event.retrieveMessage().complete());
            } else {
                message = messageOpt.get();
            }
            if (message != null) {
                String username;
                if (event.getMember() != null) {
                    username = event.getMember().getNickname();
                } else {
                    if (event.getUser() != null) {
                        username = event.getUser().getName() + "#" + event.getUser().getDiscriminator();
                    } else {
                        username = "<unknown user>";
                    }
                }
                System.out.println(Message.toReactionAddedString(event.getReaction().getEmoji()
                        .getName(), username, message));
            }
        }

        @Override
        public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
            Message message;
            Optional<Message> messageOpt = cache.stream().filter(e -> e.getId() == event.getMessageIdLong())
                    .findFirst();
            if (messageOpt.isEmpty()) {
                message = createMessageObject(event.retrieveMessage().complete());
            } else {
                message = messageOpt.get();
            }
            if (message != null) {
                String username;
                if (event.getMember() != null) {
                    username = event.getMember().getNickname();
                } else {
                    if (event.getUser() != null) {
                        username = event.getUser().getName() + "#" + event.getUser().getDiscriminator();
                    } else {
                        username = "<unknown user>";
                    }
                }
                System.out.println(Message.toReactionRemovedString(event.getReaction().getEmoji()
                        .getName(), username, message));
            }
        }

        @Override
        public void onMessageReactionRemoveAll(@NotNull MessageReactionRemoveAllEvent event) {
            System.out.println("onMessageReactionRemoveAll\n");
        }
    };
}
