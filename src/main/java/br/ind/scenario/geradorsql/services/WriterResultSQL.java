package br.ind.scenario.geradorsql.services;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class WriterResultSQL {

    @NotNull
    private final Path resultPath;

    public WriterResultSQL() {
        this.resultPath = Path.of(System.getProperty("user.dir"), "result.sql");
    }

    public void overrideResultFile() throws IOException {
        Files.deleteIfExists(resultPath);
        Files.createFile(resultPath);
    }

    public void writeFileResultInstruction(@NotNull String sql) throws IOException {
        Files.writeString(resultPath, sql, StandardOpenOption.APPEND);
        Files.writeString(resultPath, "\n", StandardOpenOption.APPEND);
    }
}
