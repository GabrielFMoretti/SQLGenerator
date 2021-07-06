package br.ind.scenario.geradorsql.models.table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record Table(String table, Field[] fields) {
}

