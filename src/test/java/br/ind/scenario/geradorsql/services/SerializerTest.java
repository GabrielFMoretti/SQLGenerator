package br.ind.scenario.geradorsql.services;

import br.ind.scenario.geradorsql.models.config.MapFaker;
import br.ind.scenario.geradorsql.models.table.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class SerializerTest {

    @Test
    @SuppressWarnings("all")
    public void mustThrowExceptionIfPathIsIncorrect() {
        Path path = Paths.get("enderecoinvalido");
        Assertions.assertThrows(IOException.class, () -> Serializer.deserializer(path, Table[].class));
        Path pathNull = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> Serializer.deserializer(pathNull, Table[].class));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Serializer.deserializer(path, null));
    }

    @Test
    public void mustDeserializer() throws URISyntaxException, IOException {
        Path pathTables = Path.of(Objects.requireNonNull(this.getClass().getResource("/tables.json")).toURI());
        Path pathConfiguration = Path.of(Objects.requireNonNull(this.getClass().getResource("/configuration.json")).toURI());
        var tables = Serializer.deserializer(pathTables, Table[].class);
        var configuration = Serializer.deserializer(pathConfiguration, MapFaker[].class);
        Assertions.assertFalse(tables.isEmpty());
        Assertions.assertFalse(configuration.isEmpty());
        Assertions.assertTrue(tables.get().length > 0);
        Assertions.assertTrue(configuration.get().length > 0);
    }


    @Test
    public void deserializerMustFailFileIsAnotherObjectType() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(this.getClass().getResource("/configuration.json")).toURI());
        var optional = Serializer.deserializer(path, Table[].class);
        Assertions.assertTrue(optional.isEmpty());
    }
}
