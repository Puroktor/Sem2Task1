package ru.vsu.cs.skofenko.mathInterpreter;

class Node {
    private Node leftChild;
    private Node RightChild;

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return RightChild;
    }

    public void setRightChild(Node rightChild) {
        RightChild = rightChild;
    }
}
