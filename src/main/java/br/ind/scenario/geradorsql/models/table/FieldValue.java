package br.ind.scenario.geradorsql.models.table;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FieldValue implements Comparable<FieldValue> {
    @NotNull
    private final String name;
    @NotNull
    private final String value;
    @NotNull
    private final TypeField type;

    public FieldValue(@NotNull String name, @NotNull String value) {
        validateField(name, value);
        this.name = name;
        this.value = value;
        this.type = TypeField.STRING;
    }

    public FieldValue(@NotNull String name, @NotNull String value, @NotNull String type) {
        validateField(name, value);
        this.name = name;
        this.value = value;
        this.type = TypeField.valueOf(type.toUpperCase().trim());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldValue that = (FieldValue) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(@NotNull FieldValue o) {
        return this.name.compareTo(o.getName());
    }

    @Override
    public String toString() {
        return "FieldValue{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
