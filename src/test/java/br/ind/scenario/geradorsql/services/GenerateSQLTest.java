package br.ind.scenario.geradorsql.services;

import br.ind.scenario.geradorsql.models.table.FieldValue;
import br.ind.scenario.geradorsql.models.table.RowTable;
import br.ind.scenario.geradorsql.models.table.TableValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class GenerateSQLTest {

    //because the order isn't important so has a variation of answers correct.
    private static final String SQL_INSERT_TEST = "INSERT INTO instalacoes (age,name) VALUES (18,\"gabriel\"),(22,\"felipe\");";
    private static final String SQL_INSERT_TEST_ALTERNATIVE = "INSERT INTO instalacoes (age,name) VALUES (22,\"felipe\"),(18,\"gabriel\");";

    @Test
    public void mustGenerateSQL() {
        TableValue tableValue = new TableValue("instalacoes", generateRows());
        GenerateSQL generateSQL = new GenerateSQL();
        String sql = generateSQL.generateInsertsSQL(tableValue);
        Assertions.assertTrue(Objects.nonNull(sql));
        System.out.println(sql);
        Assertions.assertTrue(Objects.equals(SQL_INSERT_TEST, sql) || Objects.equals(SQL_INSERT_TEST_ALTERNATIVE, sql));
    }

    @Test
    @SuppressWarnings("all")
    public void mustLaunchIllegalArgumentException() {
        GenerateSQL generateSQL = new GenerateSQL();

        Assertions.assertThrows(IllegalArgumentException.class, () -> generateSQL.generateInsertsSQL(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> generateSQL.generateInsertsSQL(new TableValue(null, generateRows())));
        Assertions.assertThrows(IllegalArgumentException.class, () -> generateSQL.generateInsertsSQL(new TableValue("", generateRows())));
        Assertions.assertThrows(IllegalArgumentException.class, () -> generateSQL.generateInsertsSQL(new TableValue("instalacoes", null)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> generateSQL.generateInsertsSQL(new TableValue("instalacoes", new RowTable[0])));
        Assertions.assertThrows(IllegalArgumentException.class, () -> generateSQL.generateInsertsSQL(new TableValue("instalacoes", new RowTable[]{new RowTable(new FieldValue[0])})));
        Assertions.assertThrows(IllegalArgumentException.class, () -> generateSQL.generateInsertsSQL(new TableValue("instalacoes", new RowTable[]{new RowTable(new FieldValue[]{new FieldValue("name","123","")})})));
        Assertions.assertThrows(IllegalArgumentException.class, () -> generateSQL.generateInsertsSQL(new TableValue("instalacoes", new RowTable[]{new RowTable(new FieldValue[]{new FieldValue("name","","integer")})})));
        Assertions.assertThrows(IllegalArgumentException.class, () -> generateSQL.generateInsertsSQL(new TableValue("instalacoes", new RowTable[]{new RowTable(new FieldValue[]{new FieldValue("","123","integer")})})));

        Assertions.assertDoesNotThrow(() -> generateSQL.generateInsertsSQL(new TableValue("instalacoes", new RowTable[]{new RowTable(new FieldValue[]{new FieldValue("number","123","integer")})})));
    }


    private RowTable[] generateRows() {
        return new RowTable[]{
                new RowTable(new FieldValue[]{
                        new FieldValue("age", "18", "INTEGER"),
                        new FieldValue("name", "gabriel"),
                        new FieldValue("name", "18", "INTEGER"),
                }),
                new RowTable(new FieldValue[]{
                        new FieldValue("name", "felipe"),
                        new FieldValue("age", "22", "integer"),
                })
        };
    }
}
