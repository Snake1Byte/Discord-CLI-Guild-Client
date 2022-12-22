package com.github.snake1byte.discord_cli_guild_client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Persistence {
    private static ObjectMapper mapper = new ObjectMapper();
    private static Path root = FileSystemView.getFileSystemView().getDefaultDirectory().toPath()
            .resolve("Discord CLI Client");
    private static File cacheFile = root.resolve("cache.json").toFile();
    private static File updateHistoryFile = root.resolve("updateHistory.json").toFile();
    private static boolean filesCreated = false;

    static {
        mapper.registerModule(new JavaTimeModule());

        try {
            if (!cacheFile.getParentFile().exists()) {
                cacheFile.getParentFile().mkdirs();
                cacheFile.createNewFile();
            } else if (!cacheFile.exists()) {
                cacheFile.createNewFile();
            }
            filesCreated = true;
        } catch (IOException e) {
            e.printStackTrace();
            // TODO log
        }
    }

    public static void saveCache(List<Message> cache) throws IOException {
        if (filesCreated) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(cacheFile, new Wrapper(cache));
        }
    }

    public static void saveUpdateHistory(Map<Long, List<Message>> updateHistory) {

    }

    public static List<Message> loadCache() throws IOException {
        return mapper.readValue(cacheFile, new TypeReference<Persistence.Wrapper>() {
        }).messages;
    }

    /**
     * Wrapper class needed to expose the types of the Messages the List is holding (either GuildMessage or Message) to the jackson library, since the list is type-erased at runtime.
     */
    private static class Wrapper {
        @JsonProperty
        private List<Message> messages;

        @JsonCreator
        public Wrapper(@JsonProperty("messages") List<Message> messages) {
            this.messages = messages;
        }
    }
}
