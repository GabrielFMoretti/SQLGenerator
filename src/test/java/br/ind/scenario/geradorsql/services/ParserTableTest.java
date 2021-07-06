package br.ind.scenario.geradorsql.services;

import br.ind.scenario.geradorsql.models.table.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class ParserTableTest {

    @Test
    public void mustParserTableToTableValueObject() throws IOException, URISyntaxException {
        final int NUMBER_ROWS = 50;
        Table table = new Table("instalacoes", new Field[]{
                new Field("name", "firstName", "string", new FieldParams[0], null),
                new Field("address", "macAddress", "string", new FieldParams[0], null),
        });
        Path pathConfiguration = Path.of(Objects.requireNonNull(this.getClass().getResource("/configuration.json")).toURI());
        ParserTable parserTable = new ParserTable(new GenerateRandomValue(pathConfiguration), new ArrayList<>());
        Optional<TableValue> optionalTableValue = parserTable.createValuesForTable(NUMBER_ROWS, table);

        Assertions.assertTrue(optionalTableValue.isPresent());
        Assertions.assertEquals(NUMBER_ROWS, optionalTableValue.get().getRows().size());

        for (RowTable rowTable : optionalTableValue.get().getRows()) {
            boolean foundAllFields = rowTable.getValues().stream().allMatch(fieldValue -> fieldValue.getName().equals("name") || fieldValue.getName().equals("address"));
            Assertions.assertTrue(foundAllFields);
        }
    }

    @Test
    @SuppressWarnings("all")
    public void mustFailGeneration() throws IOException, URISyntaxException {
        Table table = new Table("instalacoes", new Field[]{
                new Field("name", "firstName", "string", new FieldParams[0], null),
                new Field("address", "error", "string", new FieldParams[0], null),
        });
        Path pathConfiguration = Path.of(Objects.requireNonNull(this.getClass().getResource("/configuration.json")).toURI());
        ParserTable parserTable = new ParserTable(new GenerateRandomValue(pathConfiguration), new ArrayList<>());
        Optional<TableValue> optionalTableValue = parserTable.createValuesForTable(50, table);
        Assertions.assertTrue(optionalTableValue.isEmpty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> parserTable.createValuesForTable(50, null));
    }


    @Test
    public void mustParserTableWithRelationship() throws IOException, URISyntaxException {
        Table tableInstallation = new Table("installations", new Field[]{
                new Field("name", "firstName", "string", new FieldParams[0], null),
                new Field("address", "macAddress", "string", new FieldParams[0], null),
        });
        Table tableUser = new Table("users", new Field[]{
                new Field("name", "firstName", "string", new FieldParams[0], null),
                new Field("installation_mac", "macAddress", "string", new FieldParams[0], new Relationship("installations", "address")),
        });

        Path pathConfiguration = Path.of(Objects.requireNonNull(this.getClass().getResource("/configuration.json")).toURI());
        ParserTable parserTable = new ParserTable(new GenerateRandomValue(pathConfiguration), new ArrayList<>());

        Optional<TableValue> optionalTableInstallation = parserTable.createValuesForTable(50, tableInstallation);
        Assertions.assertTrue(optionalTableInstallation.isPresent());
        parserTable.addTableValue(optionalTableInstallation.get());
        Optional<TableValue> optionalTableUser = parserTable.createValuesForTable(50, tableUser);

        Assertions.assertTrue(optionalTableUser.isPresent());

        for (RowTable rowTable : optionalTableUser.get().getRows()) {
            boolean foundAllFields = rowTable.getValues().stream().allMatch(fieldValue -> fieldValue.getName().equals("name") || fieldValue.getName().equals("installation_mac"));
            String valueRelation = rowTable.getValues().stream().filter(fieldValue -> fieldValue.getName().equals("installation_mac")).findAny().orElseThrow().getValue();
            boolean foundRelationship = false;
            for (RowTable rowTableInstallation : optionalTableInstallation.get().getRows()) {
                foundRelationship = rowTableInstallation.getValues().stream().anyMatch(fieldValue -> Objects.equals(fieldValue.getValue(), valueRelation));
                if (foundRelationship)
                    break;
            }

            Assertions.assertTrue(foundRelationship);
            Assertions.assertTrue(foundAllFields);
        }
    }
}
