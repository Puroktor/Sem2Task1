package ru.vsu.cs.skofenko.mathInterpreter;

class OperationNode extends Node {
    private final Operations OPERATION;

    public OperationNode(Operations OPERATION) {
        this.OPERATION = OPERATION;
    }

    public Operations getOPERATION() {
        return OPERATION;
    }
}
