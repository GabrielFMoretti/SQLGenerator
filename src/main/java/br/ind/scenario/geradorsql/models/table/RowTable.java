package br.ind.scenario.geradorsql.models.table;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class RowTable {
    @NotNull
    private final Set<FieldValue> values;

    public RowTable(@NotNull FieldValue[] fields) {
        values = new TreeSet<>();
        Collections.addAll(values, fields);
    }

    public Set<FieldValue> getValues() {
        return Collections.unmodifiableSet(values);
    }

    @Override
    public String toString() {
        return "RowTable{" +
                "values=" + values +
                '}';
    }
}
