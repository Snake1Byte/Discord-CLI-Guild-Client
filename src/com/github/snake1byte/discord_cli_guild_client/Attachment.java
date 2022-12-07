package com.github.snake1byte.discord_cli_guild_client;

public record Attachment(String fileName, int size, boolean isImage, boolean isVideo, String downloadURL) {
}
