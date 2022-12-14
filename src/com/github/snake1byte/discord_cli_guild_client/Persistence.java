package com.github.snake1byte.discord_cli_guild_client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
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
        }
    }

    public static void saveCache(List<Message> cache) throws IOException {
        if (filesCreated) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(cacheFile, cache);
        }
    }

    public static void saveUpdateHistory(Map<Long, List<Message>> updateHistory) {

    }

    public static List<Message> loadCache() throws IOException {
        return mapper.readValue(cacheFile, new TypeReference<List<Message>>() {});
    }
}
