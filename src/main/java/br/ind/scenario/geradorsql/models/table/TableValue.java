package br.ind.scenario.geradorsql.models.table;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TableValue {
    @NotNull
    private final String name;
    @NotNull
    private final Set<RowTable> rows;

    public TableValue(@NotNull String name, @NotNull RowTable[] rowsTable) {
        this.name = name;
        this.rows = new HashSet<>();
        Collections.addAll(rows, rowsTable);
    }

    @NotNull
    public String getName() {
        return name;
    }

    public Set<RowTable> getRows() {
        return Collections.unmodifiableSet(rows);
    }

    public Set<String> getAllFieldsNames() {
        Set<String> names = new TreeSet<>();
        for (RowTable row : rows) {
            for (FieldValue value : row.getValues()) {
                names.add(value.getName());
            }
        }
        return names;
    }

    @Override
    public String toString() {
        return "TableValue{" +
                "name='" + name + '\'' +
                ", rows=" + rows +
                '}';
    }
}
