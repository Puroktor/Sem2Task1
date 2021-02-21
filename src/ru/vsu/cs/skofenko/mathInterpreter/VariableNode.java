package ru.vsu.cs.skofenko.mathInterpreter;

public class VariableNode extends Node {
    private final String NAME;

    public VariableNode(String NAME) {
        this.NAME = NAME;
    }

    public String getNAME() {
        return NAME;
    }
}
