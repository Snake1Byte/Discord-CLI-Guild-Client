package com.github.snake1byte.discord_cli_guild_client;

import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;
import java.util.List;

public class Message {
    private String author;
    private String discriminator;
    private String message;
    private OffsetDateTime timestamp;
    private Message repliedTo;
    private List<Attachment> attachments;
    private String usedStickerName;

    public Message(String author, String discriminator, String message, OffsetDateTime timestamp, @Nullable Message repliedTo, @Nullable List<Attachment> attachments, @Nullable String usedStickerName) {
        this.author = author;
        this.discriminator = discriminator;
        this.message = message;
        this.timestamp = timestamp;
        this.repliedTo = repliedTo;
        this.attachments = attachments;
        this.usedStickerName = usedStickerName;
    }

    public String getAuthor() {
        return author;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    @Nullable
    public Message getRepliedTo() {
        return repliedTo;
    }

    @Nullable
    public String getUsedStickerName() {
        return usedStickerName;
    }

    @Nullable
    public List<Attachment> getAttachments() {
        return attachments;
    }

    @Override
    public String toString() {
        return "Message{" +
                "author='" + author + '\'' +
                ", discriminator='" + discriminator + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", repliedTo=" + repliedTo +
                ", attachments=" + attachments +
                ", usedStickerName='" + usedStickerName + '\'' +
                '}';
    }
}
