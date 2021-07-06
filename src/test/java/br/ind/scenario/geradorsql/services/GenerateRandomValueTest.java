package br.ind.scenario.geradorsql.services;

import br.ind.scenario.geradorsql.exceptions.GenerationValuesException;
import br.ind.scenario.geradorsql.models.table.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenerateRandomValueTest {

    private static Path path;
    private static Path pathConfigError;

    @BeforeAll
    static void initializePath() throws URISyntaxException {
        path = Path.of(Objects.requireNonNull(GenerateRandomValue.class.getResource("/configuration.json")).toURI());
        pathConfigError = Path.of(Objects.requireNonNull(GenerateRandomValue.class.getResource("/configuration.json")).toURI());
    }

    @Test
    public void mustGenerateRandomValueFromField() throws IOException, GenerationValuesException {
        GenerateRandomValue generateRandomValue = new GenerateRandomValue(path);
        String value = generateRandomValue.generateValue(new Field("name", "firstName", "integer", new FieldParams[0], null));
        String valueWithParams = generateRandomValue.generateValue(new Field("number", "numberBetween", "integer", new FieldParams[]{
                new FieldParams("int", "1"), new FieldParams("int", "10")}, null));
        Assertions.assertNotNull(value);
        Assertions.assertFalse(value.isBlank());
        Assertions.assertNotNull(valueWithParams);
        Assertions.assertFalse(valueWithParams.isBlank());
    }

    @Test
    @SuppressWarnings("all")
    public void mustThrowExceptions() throws IOException, URISyntaxException {
        GenerateRandomValue generateRandomValue = new GenerateRandomValue(path);
        GenerateRandomValue generateRandomValueError = new GenerateRandomValue(pathConfigError);
        Assertions.assertThrows(IllegalArgumentException.class, () -> generateRandomValue.generateValue(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> generateRandomValue.generateValue(new Field("name", null, "integer", new FieldParams[0], null)));

        Assertions.assertThrows(GenerationValuesException.class, () -> generateRandomValue.generateValue(new Field("address", "macAddresss", "string", new FieldParams[0], null)));
        Assertions.assertThrows(GenerationValuesException.class, () -> generateRandomValue.generateValue(new Field("name", "nothing", "integer", new FieldParams[0], null)));
        Assertions.assertThrows(GenerationValuesException.class, () -> generateRandomValue.generateValue(new Field(null, "nothing", null, new FieldParams[0], null)));
        Assertions.assertThrows(GenerationValuesException.class, () -> generateRandomValueError.generateValue(new Field("address", "ERROR", "string", new FieldParams[0], null)));

    }

    @Test
    public void mustThrowExceptionWhenPathIsWrong() {
        Assertions.assertThrows(IOException.class, () -> new GenerateRandomValue(Path.of("wrong path")).generateValue(new Field("", "", "", null, null)));
    }

    @Test
    public void mustGenerateValueRelationship() throws IOException {
        GenerateRandomValue generateRandomValue = new GenerateRandomValue(path);

        List<TableValue> tableValues = new ArrayList<>();
        tableValues.add(new TableValue("installations", new RowTable[]{new RowTable(new FieldValue[]{new FieldValue("id", "33", "integer"),
                new FieldValue("ids", "teste", "string")}),
                new RowTable(new FieldValue[]{new FieldValue("id", "12", "integer"),
                        new FieldValue("ids", "teste2", "string")})}));

        String value = generateRandomValue.generateValueRelationship(
                new Field("name", "firstName", "integer", new FieldParams[0], new Relationship("installations", "id")), tableValues);

        String valueAutoID = generateRandomValue.generateValueRelationship(
                new Field("id", "number", "integer", new FieldParams[0], new Relationship("installations", true)), tableValues);

        Assertions.assertNotNull(value);
        Assertions.assertFalse(value.isBlank());
        Assertions.assertTrue(value.equals("12") || value.equals("33"));
        Assertions.assertTrue(valueAutoID.equals("1") || valueAutoID.equals("2"));
    }
}
