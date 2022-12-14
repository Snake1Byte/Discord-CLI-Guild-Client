package com.github.snake1byte.discord_cli_guild_client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
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

    public GuildMessage(@JsonProperty("id") long id, @JsonProperty("author") String author, @JsonProperty("discriminator") String discriminator, @JsonProperty("message") String message, @JsonProperty("timestamp") OffsetDateTime timestamp, @JsonProperty("repliedTo") @Nullable Message repliedTo, @JsonProperty("attachments") @Nullable List<Attachment> attachments, @JsonProperty("usedStickerName") @Nullable String usedStickerName, @JsonProperty("guildId") @Nullable String guildId, @JsonProperty("guildName") String guildName, @JsonProperty("nickname") @Nullable String nickname, @JsonProperty("channelId") String channelId, @JsonProperty("channelName") String channelName, @JsonProperty("channelCategoryId") @Nullable String channelCategoryId, @JsonProperty("channelCategoryName") @Nullable String channelCategoryName) {
        //    public GuildMessage(long id, String author, String discriminator, String message, OffsetDateTime timestamp, @Nullable Message repliedTo, @Nullable List<Attachment> attachments, @Nullable String usedStickerName, @Nullable String guildId, String guildName, @Nullable String nickname, String channelId, String channelName, @Nullable String channelCategoryId, @Nullable String channelCategoryName) {
        super(id, author, discriminator, message, timestamp, repliedTo, attachments, usedStickerName);
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
    protected String headerToString() {
        StringBuilder builder = new StringBuilder();
        String timeOfDay = timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME);
        builder.append(guildName).append(" in #").append(channelName).append(":\n");
        if (nickname != null) {
            builder.append(nickname).append(" at ").append(timeOfDay);
        } else {
            builder.append(author).append("#").append(discriminator).append(" at ").append(timeOfDay).append(":");
        }
        builder.append('\n');

        return builder.toString();
    }

    @Override
    protected String replyToString() {
        StringBuilder builder = new StringBuilder();
        if (repliedTo != null) {
            if (nickname != null) {
                builder.append("replied to \"").append(repliedTo.getMessage()).append("\"").append(" by ")
                        .append(nickname).append("\n");
            } else {
                builder.append("replied to \"").append(repliedTo.getMessage()).append("\"").append(" by ")
                        .append(author).append("#").append(discriminator).append("\n");
            }
        }

        return builder.toString();
    }
}
