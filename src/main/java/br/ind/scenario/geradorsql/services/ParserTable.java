package br.ind.scenario.geradorsql.services;

import br.ind.scenario.geradorsql.exceptions.GenerationValuesException;
import br.ind.scenario.geradorsql.models.table.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public record ParserTable(@NotNull GenerateRandomValue generateRandomValue,
                          @NotNull List<TableValue> tables) {

    public ParserTable(@NotNull GenerateRandomValue generateRandomValue, List<TableValue> tables) {
        this.generateRandomValue = generateRandomValue;
        this.tables = tables;
    }

    public void addTableValue(@NotNull TableValue tableValue) {
        tables.add(tableValue);
    }

    public Optional<TableValue> createValuesForTable(int numberRows, @NotNull Table table) {
        try {
            RowTable[] rows = generateRowsOfTable(numberRows, table);
            changePrimaryKeysValues(rows);
            return Optional.of(new TableValue(table.table(), rows));
        } catch (GenerationValuesException e) {
            return Optional.empty();
        }
    }

    private RowTable[] generateRowsOfTable(int numberRows, @NotNull Table table) throws GenerationValuesException {
        List<RowTable> rows = new ArrayList<>();
        for (int row = 0; row < numberRows; row++)
            rows.add(new RowTable(generateFieldsOfRow(table.fields())));

        return rows.toArray(RowTable[]::new);
    }

    private FieldValue[] generateFieldsOfRow(Field[] fieldsTable) throws GenerationValuesException {
        List<FieldValue> fieldValues = new ArrayList<>();
        for (Field field : fieldsTable) {
            String value = "";

            if (Objects.nonNull(field.relationship()))
                value = generateRandomValue.generateValueRelationship(field, tables);
            else
                value = generateRandomValue.generateValue(field);

            FieldValue fieldValue = new FieldValue(field.nameField(), value, field.type(), field.unique());
            fieldValues.add(fieldValue);
        }
        return fieldValues.toArray(FieldValue[]::new);
    }

    private void changePrimaryKeysValues(RowTable[] rows) throws GenerationValuesException {
        for (RowTable row : rows) {
            List<FieldValue> fieldInRowUnique = row.getValues().stream().filter(FieldValue::isUnique).collect(Collectors.toList());
            for (FieldValue fieldValue : fieldInRowUnique) {
                int attempts = 0;
                while (fieldInRowUnique.stream().anyMatch(fieldValueInList -> Objects.equals(fieldValue.getValue(), fieldValueInList.getValue())) && attempts < 3) {
                    fieldValue.complementValue(generateRandomValue.generateRandomComplement());
                    attempts++;
                }
            }
        }
    }
}
