package com.github.snake1byte.discord_cli_guild_client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = GuildMessage.class, name = "guild"), @JsonSubTypes.Type(value = Message.class, name = "dm")})
public class Message {
    protected long id;

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

    public Message(@JsonProperty("id") long id, @JsonProperty("author") String author, @JsonProperty("discriminator") String discriminator, @JsonProperty("message") String message, @JsonProperty("timestamp") OffsetDateTime timestamp, @JsonProperty("repliedTo") @Nullable Message repliedTo, @JsonProperty("attachments") @Nullable List<Attachment> attachments, @JsonProperty("usedStickerName") @Nullable String usedStickerName) {
        this.id = id;
        this.author = author;
        this.discriminator = discriminator;
        this.message = message;
        this.timestamp = timestamp;
        this.repliedTo = repliedTo;
        this.attachments = attachments;
        this.usedStickerName = usedStickerName;
    }

    public long getId() {
        return id;
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
        builder.append(headerToString());
        builder.append(messageContentToString());
        builder.append(attachmentsToString());
        builder.append(replyToString());
        return builder.toString();
    }

    public String toMessageEditedString(@Nullable Message original) {
        StringBuilder builder = new StringBuilder();
        builder.append("MESSAGE EDITED\n");
        builder.append(headerToString());
        if (original != null) {
            builder.append(original.messageContentToString());
        } else {
            builder.append("<unknown>\n");
        }
        builder.append("->\n");
        builder.append(messageContentToString());
        builder.append(replyToString());
        return builder.toString();
    }

    public static String toMessageDeletedString(@NotNull Message restored) {
        StringBuilder builder = new StringBuilder();
        builder.append("MESSAGE DELETED\n");
        builder.append(restored.headerToString());
        builder.append(restored.messageContentToString());
        builder.append(restored.attachmentsToString());
        builder.append(restored.replyToString());
        return builder.toString();
    }

    public static String toReactionAddedString(String reaction, String username, Message message) {
        StringBuilder builder = new StringBuilder();
        builder.append("REACTION ADDED\n");
        builder.append("by ").append(username).append("\n");
        builder.append(reaction).append("\n");
        builder.append(message.headerToString());
        builder.append(message.messageContentToString());
        builder.append(message.attachmentsToString());
        builder.append(message.replyToString());
        return builder.toString();
    }

    public static String toReactionRemovedString(String reaction, String username, Message message) {
        StringBuilder builder = new StringBuilder();
        builder.append("REACTION REMOVED\n");
        builder.append("by ").append(username).append("\n");
        builder.append(reaction).append("\n");
        builder.append(message.headerToString());
        builder.append(message.messageContentToString());
        builder.append(message.attachmentsToString());
        builder.append(message.replyToString());
        return builder.toString();
    }

    /**
     * Creates a string representation of this Message's header, which includes the message author's Discord name, discriminator and the timestamp of the message.
     *
     * @return Returns the header of this Message as a string.
     */
    protected String headerToString() {
        StringBuilder builder = new StringBuilder();
        String timeOfDay = timestamp.atZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_TIME);
        // header
        builder.append("DM from ").append(author).append("#").append(discriminator).append(" at ").append(timeOfDay)
                .append(":\n");

        return builder.toString();
    }

    /**
     * Creates a string representation of this Message object's message content, including stickers, if present.
     *
     * @return Returns the message content of this Message object.
     */
    protected String messageContentToString() {
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        if (usedStickerName != null) {
            if (!message.isBlank()) {
                builder.append(" ");
            }
            builder.append("++").append(usedStickerName).append("++\n");
        } else if (!message.isBlank()) {
            builder.append("\n");
        }

        return builder.toString();
    }

    protected String attachmentsToString() {
        StringBuilder builder = new StringBuilder();
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
                        builder.append(other).append(" others");
                    } else {
                        builder.append("1 other");
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
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    protected String replyToString() {
        StringBuilder builder = new StringBuilder();
        if (repliedTo != null) {
            builder.append("replied to \"").append(repliedTo.getMessage()).append("\"\n");
        }

        return builder.toString();
    }
}
