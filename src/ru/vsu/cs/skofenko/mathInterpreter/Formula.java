package ru.vsu.cs.skofenko.mathInterpreter;

import java.util.*;

public class Formula {
    private List<String> variables;
    private Node root;

    public void prepare(String str) {
        List<String> tokenList = parseStr(str); // O(n)

        root = buildTree(tokenList); // O(n)
    } // O(n) по времени, O(n) по памяти

    private Node buildTree(List<String> tokenList) {
        if (tokenList.size() == 0) {
            throw new IllegalArgumentException();
        }
        variables = new ArrayList<>();
        Set<String> variablesSet = new HashSet<>();
        Node root = null, multiRoot = null, node = null;
        for (String token : tokenList) {
            switch (token) {
                case "+":
                case "-":
                    if (node == null)
                        throw new IllegalArgumentException();
                    if (root == null) {
                        root = new OperationNode(token.equals("+") ? Operations.ADD : Operations.SUBTRACT);
                        if (multiRoot == null) {
                            root.setLeftChild(node);
                        } else {
                            multiRoot.setRightChild(node);
                            root.setLeftChild(multiRoot);
                        }
                    } else {
                        if (multiRoot == null) {
                            root.setRightChild(node);
                        } else {
                            multiRoot.setRightChild(node);
                            root.setRightChild(multiRoot);
                        }
                        Node temp = root;
                        root = new OperationNode(token.equals("+") ? Operations.ADD : Operations.SUBTRACT);
                        root.setLeftChild(temp);
                    }
                    multiRoot = null;
                    break;
                case "*":
                case "/":
                    if (node == null)
                        throw new IllegalArgumentException();
                    if (multiRoot == null) {
                        multiRoot = new OperationNode(token.equals("*") ? Operations.MULTYPLY : Operations.DIVIDE);
                        multiRoot.setLeftChild(node);
                    } else {
                        multiRoot.setRightChild(node);
                        Node temp = multiRoot;
                        multiRoot = new OperationNode(token.equals("*") ? Operations.MULTYPLY : Operations.DIVIDE);
                        multiRoot.setLeftChild(temp);
                    }
                    break;
                default:
                    try {
                        node = new NumNode(Double.parseDouble(token));
                    } catch (NumberFormatException e) {
                        node = new VariableNode(token);
                        if (!variablesSet.contains(token)) {
                            variables.add(token);
                            variablesSet.add(token);
                        }
                    }
                    break;
            }
        }

        if (root != null) {
            if (multiRoot == null) {
                root.setRightChild(node);
            } else {
                multiRoot.setRightChild(node);
                root.setRightChild(multiRoot);
            }
            return root;
        } else if (multiRoot != null) {
            multiRoot.setRightChild(node);
            return multiRoot;
        } else {
            return node;
        }
    }//Итого: O(n) по времени, O(n) по памяти

    private List<String> parseStr(String str) {
        List<String> tokenList = new ArrayList<>();
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isLetterOrDigit(ch) || ch == '.' || (i == 0 && ch == '-')) {
                tmp.append(ch); // O(1)
            } else {
                if (tmp.length() != 0) {
                    String s = tmp.toString();
                    tokenList.add(s); // O(1)
                    tmp = new StringBuilder();
                }
                if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                    tokenList.add(String.valueOf(ch)); // O(1)
                } else if (ch != ' ') {
                    throw new UnsupportedOperationException();
                }
            }
        }
        if (tmp.length() != 0) {
            String s = tmp.toString();
            tokenList.add(s);
        }
        return tokenList;
    }//Итого: по времени: O(n), по памяти O(n)

    public double execute(double... x) {
        if (x.length != variables.size()) {
            throw new IllegalArgumentException();
        }
        Map<String, Double> map = new HashMap<>();
        for (int i = 0; i < x.length; i++) {
            map.put(variables.get(i), x[i]); //O(1)
        }
        return count(map, root); // O(n)
    } // O(n) по времени, O(n) по памяти

    private double count(Map<String, Double> map, Node node) {
        if (node instanceof OperationNode) {
            return switch (((OperationNode) node).getOPERATION()) {
                case ADD -> count(map, node.getLeftChild()) + count(map, node.getRightChild());
                case SUBTRACT -> count(map, node.getLeftChild()) - count(map, node.getRightChild());
                case MULTYPLY -> count(map, node.getLeftChild()) * count(map, node.getRightChild());
                case DIVIDE -> count(map, node.getLeftChild()) / count(map, node.getRightChild());
            };
        } else if (node instanceof NumNode) {
            return ((NumNode) node).getVALUE();
        } else {
            return map.get(((VariableNode) node).getNAME()); //O(1)
        }
    } // O(n) по времени, O(n) по памяти
}
