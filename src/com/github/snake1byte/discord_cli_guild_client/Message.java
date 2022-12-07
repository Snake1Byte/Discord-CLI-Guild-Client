package com.github.snake1byte.discord_cli_guild_client;

import java.time.OffsetDateTime;
import java.util.List;

public record Message(String author, String discriminator, String nickname, String guildId, String guildName,
                      String channelId, String channelName, String channelCategoryId, String channelCategoryName,
                      String message, OffsetDateTime timestamp, Message repliedTo, String usedStickerName, List<Attachment> attachments) {
}
