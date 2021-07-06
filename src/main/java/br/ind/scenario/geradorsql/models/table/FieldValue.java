package br.ind.scenario.geradorsql.models.table;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FieldValue implements Comparable<FieldValue> {
    @NotNull
    private final String name;
    @NotNull
    private final TypeField type;
    private final boolean unique;
    @NotNull
    private String value;

    public FieldValue(@NotNull String name, @NotNull String value) {
        validateField(name, value);
        this.name = name;
        this.value = value;
        this.type = TypeField.STRING;
        this.unique = false;
    }

    public FieldValue(@NotNull String name, @NotNull String value, @NotNull String type) {
        validateField(name, value);
        this.name = name;
        this.value = value;
        this.type = TypeField.valueOf(type.toUpperCase().trim());
        this.unique = false;
    }

    public FieldValue(@NotNull String name, @NotNull String value, @NotNull String type, boolean unique) {
        validateField(name, value);
        this.name = name;
        this.value = value;
        this.type = TypeField.valueOf(type.toUpperCase().trim());
        this.unique = unique;
    }

    public void validateField(@NotNull String name, @NotNull String value) {
        if (name.isBlank() || value.isBlank())
            throw new IllegalArgumentException("Field Invalid");
    }

    @NotNull
    public String getName() {
        return name;
    }

    public String getValue() {
        return type.convert(value);
    }

    public void complementValue(@NotNull String complement) {
        this.value = complement + this.value;
    }

    public boolean isUnique() {
        return unique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldValue that = (FieldValue) o;
        return name.equals(that.name) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public int compareTo(@NotNull FieldValue o) {
        return this.name.compareTo(o.getName());
    }
}
