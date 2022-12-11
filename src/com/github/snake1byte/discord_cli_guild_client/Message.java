package com.github.snake1byte.discord_cli_guild_client;

import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Message {
    protected String author;
    protected String discriminator;
    protected String message;
    protected OffsetDateTime timestamp;
    @Nullable
    protected Message repliedTo;
    @Nullable
    protected List<Attachment> attachments;
    @Nullable
    protected String usedStickerName;

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
        return "Message{" + "author='" + author + '\'' + ", discriminator='" + discriminator + '\'' + ", message='" + message + '\'' + ", timestamp=" + timestamp + ", repliedTo=" + repliedTo + ", attachments=" + attachments + ", usedStickerName='" + usedStickerName + '\'' + '}';
    }

    public String toMessageReceivedString() {
        StringBuilder builder = new StringBuilder();
        String timeOfDay = timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME);
        // header
        builder.append("DM from ").append(author).append("#").append(discriminator).append(" at ").append(timeOfDay)
                .append(":");
        // body
        if (!message.isBlank()) {
            builder.append("\n").append(message);
        }
        if (usedStickerName != null) {
            if (message.isBlank()) {
                builder.append("\n");
            }
            builder.append(" ++").append(usedStickerName).append("++\n");
        } else {
            builder.append("\n");
        }
        if (attachments != null && !attachments.isEmpty()) {
            if (attachments.size() > 1) {
                builder.append(attachments.size()).append(" attachments: ");
                int images = 0, videos = 0;
                for (Attachment attachment : attachments) {
                    if (attachment.isImage()) {
                        ++images;
                    } else if (attachment.isVideo()) {
                        ++videos;
                    }
                }
                if (images > 0) {
                    if (images > 1) {
                        builder.append(images).append(" images");
                    } else {
                        builder.append(images).append(" image");
                    }
                }
                if (videos > 0) {
                    if (images > 0) {
                        builder.append(", ");
                    }
                    if (videos > 1) {
                        builder.append(videos).append(" videos");
                    } else {
                        builder.append(videos).append(" video");
                    }
                }
                int other = attachments.size() - videos - images;
                if (other > 0) {
                    if (images > 0 || videos > 0) {
                        builder.append(", ");
                    }
                    if (other > 1) {
                        builder.append("others");
                    } else {
                        builder.append("other");
                    }
                }
                builder.append("\n");
                for (int i = 0; i < attachments.size(); ++i) {
                    builder.append(i + 1).append(": ").append(attachments.get(i).fileName()).append(", ")
                            .append(attachments.get(i).size()).append(", ").append(attachments.get(i).downloadURL())
                            .append("\n");
                }
            } else {
                Attachment attachment = attachments.get(0);
                if (attachment.isImage()) {
                    builder.append("Image attached, ").append(attachment.fileName()).append(", ")
                            .append(attachment.size()).append(", ").append(attachment.downloadURL());
                } else if (attachment.isVideo()) {
                    builder.append("Video attached, ").append(attachment.fileName()).append(", ")
                            .append(attachment.size()).append(", ").append(attachment.downloadURL());
                } else {
                    builder.append(attachment.fileName()).append(", ").append(attachment.size()).append(", ")
                            .append(attachment.downloadURL());
                }
            }
            if (repliedTo != null) {
                builder.append("\n").append("replied to \"").append(repliedTo.getMessage()).append("\"");
            }
        }
        return builder.toString();
    }

    public String toMessageEditedString(@Nullable Message original) {
        return null;
    }

    public String toMessageDeletedString(@Nullable Message restored) {
        return null;
    }

    public String headerToString() {
        return null;
    }

    public String attachmentsToString() {
        return null;
    }
}
