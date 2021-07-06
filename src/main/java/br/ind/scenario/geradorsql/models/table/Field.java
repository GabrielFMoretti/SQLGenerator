package br.ind.scenario.geradorsql.models.table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jetbrains.annotations.Nullable;

@JsonSerialize
public record Field(String nameField, String faker, String type, FieldParams[] methodParams,
                    @Nullable Relationship relationship) {
}
