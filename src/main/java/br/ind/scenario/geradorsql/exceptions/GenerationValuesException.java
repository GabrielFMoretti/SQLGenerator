package br.ind.scenario.geradorsql.exceptions;

public class GenerationValuesException extends Exception {
    public GenerationValuesException() {
        super("Can not generate generic value");
    }

    public GenerationValuesException(String reason) {
        super("Can not generate generic value. Reason: " + reason);
    }
}
