package br.ind.scenario.geradorsql.models.table;

import java.util.Objects;

public record FieldParams(String className, Object value) {

    public Class<?> getClassTotalPackage() {
        try {
            if (className.equals("int"))
                return int.class;
            return Class.forName(String.format("java.lang.%s", className));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public Object value() {
        if (Objects.equals(getClassTotalPackage(), int.class))
            return Integer.parseInt(value.toString());
        return value;
    }
}
