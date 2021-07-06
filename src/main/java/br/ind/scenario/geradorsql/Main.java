package br.ind.scenario.geradorsql;

import br.ind.scenario.geradorsql.models.table.Table;
import br.ind.scenario.geradorsql.models.table.TableValue;
import br.ind.scenario.geradorsql.services.GenerateRandomValue;
import br.ind.scenario.geradorsql.services.GenerateSQL;
import br.ind.scenario.geradorsql.services.ParserTable;
import br.ind.scenario.geradorsql.services.Serializer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length < 1)
            throw new IllegalArgumentException("Por favor informe a quantidade de registro que deseja.");
        var numberRows = Integer.parseInt(args[0]);
        var path = Path.of(System.getProperty("user.dir"), "tables.json");
        if (args.length > 1)
            path = Path.of(args[1]);

        Path pathConfiguration = Path.of(Objects.requireNonNull(Main.class.getResource("/configuration.json")).toURI());
        Serializer.deserializer(path, Table[].class).ifPresent(tables -> {
            List<TableValue> allTablesValues = new ArrayList<>();
            for (Table table : tables) {
                try {
                    TableValue tableValue = new ParserTable(new GenerateRandomValue(pathConfiguration), allTablesValues).createValuesForTable(numberRows, table).orElseThrow();
                    allTablesValues.add(tableValue);
                    System.out.println(new GenerateSQL().generateInsertsSQL(tableValue));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}