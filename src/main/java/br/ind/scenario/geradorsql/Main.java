package br.ind.scenario.geradorsql;

import br.ind.scenario.geradorsql.models.table.Table;
import br.ind.scenario.geradorsql.models.table.TableValue;
import br.ind.scenario.geradorsql.services.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        var numberRows = getNumberRows(args);
        var pathTable = getPathTable(args);
        
        WriterResultSQL writerResult = new WriterResultSQL();
        writerResult.overrideResultFile();

        Path pathConfiguration = Path.of(Objects.requireNonNull(Main.class.getResource("/configuration.json")).toURI());
        Serializer.deserializer(pathTable, Table[].class).ifPresent(tables -> {
            List<TableValue> allTablesValues = new ArrayList<>();
            for (Table table : tables) {
                try {
                    TableValue tableValue = new ParserTable(new GenerateRandomValue(pathConfiguration), allTablesValues).createValuesForTable(numberRows, table).orElseThrow();
                    allTablesValues.add(tableValue);
                    String insertSQL = new GenerateSQL().generateInsertsSQL(tableValue);
                    String sqlFormatted = FormatterSQL.formartInsertSQL(insertSQL);
                    System.out.println(sqlFormatted);
                    writerResult.writeFileResultInstruction(sqlFormatted);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static int getNumberRows(String[] args) {
        if (args.length < 1)
            throw new IllegalArgumentException("Por favor informe a quantidade de registro que deseja.");
        return Integer.parseInt(args[0]);
    }

    private static Path getPathTable(String[] args) {
        var pathTables = Path.of(System.getProperty("user.dir"), "tables.json");
        if (args.length > 1)
            pathTables = Path.of(args[1]);
        return pathTables;
    }
}
