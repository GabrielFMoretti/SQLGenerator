package br.ind.scenario.geradorsql.services;

import br.ind.scenario.geradorsql.models.table.FieldValue;
import br.ind.scenario.geradorsql.models.table.RowTable;
import br.ind.scenario.geradorsql.models.table.TableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class GenerateSQL {

    public @Nullable String generateInsertsSQL(@NotNull TableValue tableValue) {
        validateTableValueOrThrows(tableValue);
        return String.format("INSERT INTO %s (%s) VALUES %s;", tableValue.getName(), getAllFieldsNamesSeparator(tableValue), getValuesFromTable(tableValue));
    }

    private void validateTableValueOrThrows(@NotNull TableValue tableValue) {
        String reason = "";
        if (tableValue.getRows().isEmpty())
            reason = "rows is empty";
        if (tableValue.getName().isBlank())
            reason = "name is blank";
        if (tableValue.getRows().stream().findFirst().isPresent() && tableValue.getRows().stream().findFirst().get().getValues().isEmpty())
            reason = "has no values in fields";
        if (!reason.isEmpty())
            throw new IllegalArgumentException("Table receive is invalid. Reason: " + reason);
    }

    private String getAllFieldsNamesSeparator(@NotNull TableValue tableValue) {
        StringBuilder strBuilder = new StringBuilder();
        for (String name : tableValue.getAllFieldsNames()) {
            strBuilder.append(name).append(",");
        }
        return strBuilder.deleteCharAt(strBuilder.lastIndexOf(",")).toString();
    }

    private String getValuesFromTable(@NotNull TableValue tableValue) {
        StringBuilder strBuilder = new StringBuilder();
        String[] namesFields = getAllFieldsNamesSeparator(tableValue).split(",");

        for (RowTable row : tableValue.getRows()) {
            strBuilder.append("(");
            for (FieldValue field : row.getValues()) {
                for (String nameField : namesFields) {
                    if (Objects.equals(nameField, field.getName()))
                        strBuilder.append(field.getValue()).append(",");
                }
            }
            strBuilder.deleteCharAt(strBuilder.lastIndexOf(","));
            strBuilder.append("),");
        }
        return strBuilder.deleteCharAt(strBuilder.lastIndexOf(",")).toString();
    }
}
