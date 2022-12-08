package com.github.snake1byte.discord_cli_guild_client;

import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;
import java.util.List;

public class GuildMessage extends Message {
    private String guildId;
    private String guildName;
    private String nickname;
    private String channelId;
    private String channelName;

    private String channelCategoryId;

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

    public String getNickname() {
        return nickname;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelCategoryId() {
        return channelCategoryId;
    }

    public String getChannelCategoryName() {
        return channelCategoryName;
    }

    @Override
    public String toString() {
        return "GuildMessage{" +
                "guildId='" + guildId + '\'' +
                ", guildName='" + guildName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", channelId='" + channelId + '\'' +
                ", channelName='" + channelName + '\'' +
                ", channelCategoryId='" + channelCategoryId + '\'' +
                ", channelCategoryName='" + channelCategoryName + '\'' +
                "} " + super.toString();
    }
}
