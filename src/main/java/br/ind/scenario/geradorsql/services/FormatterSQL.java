package br.ind.scenario.geradorsql.services;

import org.jetbrains.annotations.NotNull;

public class FormatterSQL {

    public static String formartInsertSQL(@NotNull String insertSQL) {
        return insertSQL.replaceAll("VALUES ", "VALUES \n").replaceAll("\\),", "),\n");
    }
}
