package ru.vsu.cs.skofenko.mathInterpreter;

public class NumNode extends Node {
    private final double VALUE;

    public NumNode(double VALUE) {
        this.VALUE = VALUE;
    }

    public double getVALUE() {
        return VALUE;
    }
}
