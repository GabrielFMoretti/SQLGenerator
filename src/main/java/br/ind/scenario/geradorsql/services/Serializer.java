package br.ind.scenario.geradorsql.services;

import br.ind.scenario.geradorsql.utils.JsonUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Class to work with params file.
 */
public class Serializer {

    public static <T> Optional<T> deserializer(@NotNull Path path, @NotNull Class<T> objectClassDeserializer) throws IOException {
        String tablesJson = Files.readString(path);
        return JsonUtil.fromJson(tablesJson, objectClassDeserializer);
    }
}
