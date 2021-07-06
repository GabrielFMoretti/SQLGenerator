package br.ind.scenario.geradorsql.models.table;

public record Relationship(String table, String field, boolean isAutoGenerateID) {
    public Relationship(String table, String field) {
        this(table, field, false);
    }

    public Relationship(String table, boolean isAutoGenerateID) {
        this(table, "null", isAutoGenerateID);
    }
}
