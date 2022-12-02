package com.github.snake1byte.discord_cli_guild_client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Constants {
    private ObjectMapper mapper;

    private String token;

    public Constants() throws IOException {
        mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(getClass().getResourceAsStream("/com/github/snake1byte/discord_cli_guild_client/resources/constants.json"));
        if (node == null) {
            throw new IOException("Json file parsing failed.");
        }
        token = node.get("token").asText();
    }

    public String getToken() {
        return token;
    }
}
