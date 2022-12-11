package com.github.snake1byte.discord_cli_guild_client;

import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GuildMessage extends Message {
    private String guildId;
    private String guildName;
    @Nullable
    private String nickname;
    private String channelId;
    private String channelName;
    @Nullable
    private String channelCategoryId;
    @Nullable
    private String channelCategoryName;

    public GuildMessage(String author, String discriminator, String message, OffsetDateTime timestamp, @Nullable Message repliedTo, @Nullable List<Attachment> attachments, @Nullable String usedStickerName, @Nullable String guildId, String guildName, @Nullable String nickname, String channelId, String channelName, @Nullable String channelCategoryId, @Nullable String channelCategoryName) {
        super(author, discriminator, message, timestamp, repliedTo, attachments, usedStickerName);
        this.guildId = guildId;
        this.nickname = nickname;
        this.guildName = guildName;
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelCategoryId = channelCategoryId;
        this.channelCategoryName = channelCategoryName;
    }

    public String getGuildId() {
        return guildId;
    }


    public String getGuildName() {
        return guildName;
    }

    @Nullable
    public String getNickname() {
        return nickname;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    @Nullable
    public String getChannelCategoryId() {
        return channelCategoryId;
    }

    @Nullable
    public String getChannelCategoryName() {
        return channelCategoryName;
    }

    @Override
    public String toString() {
        return "GuildMessage{" + "guildId='" + guildId + '\'' + ", guildName='" + guildName + '\'' + ", nickname='" + nickname + '\'' + ", channelId='" + channelId + '\'' + ", channelName='" + channelName + '\'' + ", channelCategoryId='" + channelCategoryId + '\'' + ", channelCategoryName='" + channelCategoryName + '\'' + "} " + super.toString();
    }

    @Override
    public String toMessageReceivedString() {
        StringBuilder builder = new StringBuilder();
        // header
        String timeOfDay = timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME);
        builder.append(guildName).append(" in #").append(channelName).append(":\n");
        if (nickname != null) {
            builder.append(nickname).append(" at ").append(timeOfDay);
        } else {
            builder.append(author).append("#").append(discriminator).append(" at ").append(timeOfDay).append(":");
        }
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
                if (nickname != null) {
                    builder.append("\n").append("replied to \"").append(repliedTo.getMessage()).append("\"")
                            .append(" by ").append(nickname);
                } else {
                    builder.append("\n").append("replied to \"").append(repliedTo.getMessage()).append("\"")
                            .append(" by ").append(author).append("#").append(discriminator);
                }
            }
        }
        return builder.toString();
    }

    @Override
    public String toMessageEditedString(@Nullable Message original) {
        return null;
    }

    @Override
    public String toMessageDeletedString(@Nullable Message restored) {
        return null;
    }

    @Override
    public String headerToString() {
        return null;
    }

    @Override
    public String attachmentsToString() {
        return null;
    }
}
