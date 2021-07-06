package br.ind.scenario.geradorsql.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record JsonUtilModelTest(String name, int number) {
}
