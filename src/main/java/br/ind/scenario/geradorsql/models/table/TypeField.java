package br.ind.scenario.geradorsql.models.table;

public enum TypeField {
    STRING,
    INTEGER;

    public String convert(String value) {
        if (this.equals(TypeField.STRING))
            return "\"" + value + "\"";
        return value;
    }
}
