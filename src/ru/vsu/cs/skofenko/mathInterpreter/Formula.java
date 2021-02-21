package ru.vsu.cs.skofenko.mathInterpreter;

import java.util.*;

public class Formula {
    private List<String> variables;
    private Set<String> variablesSet;
    private Node root;

    public void prepare(String str) {
        List<String> formula = parseStr(str);

        variables = new ArrayList<>();
        variablesSet = new HashSet<>();
        root = buildTree(formula, 0, formula.size());
    }

    private Node buildTree(List<String> formula, int start, int end) {
        if (end - start == 1) {
            try {
                return new NumNode(Double.parseDouble(formula.get(start)));
            } catch (NumberFormatException e) {
                String s = formula.get(start);
                if (!variablesSet.contains(s)) {
                    variables.add(s);
                    variablesSet.add(s);
                }
                return new VariableNode(formula.get(start));
            }
        }

        String sign1 = "+", sign2 = "-";
        Operations op1 = Operations.ADD, op2 = Operations.SUBTRACT;
        for (int i = 0; i < 2; i++) {
            for (int j = end - 1; j >= start; j--) {
                if (formula.get(j).equals(sign1) || formula.get(j).equals(sign2)) {
                    OperationNode node;
                    if (formula.get(j).equals(sign1)) {
                        node = new OperationNode(op1);
                    } else {
                        node = new OperationNode(op2);
                    }
                    node.setLeftChild(buildTree(formula, start, j));
                    node.setRightChild(buildTree(formula, j + 1, end));
                    return node;
                }
            }
            sign1 = "*";
            sign2 = "/";
            op1 = Operations.MULTYPLY;
            op2 = Operations.DIVIDE;
        }

        throw new UnsupportedOperationException();
    }

    private List<String> parseStr(String str) {
        List<String> formula = new ArrayList<>();
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isLetterOrDigit(ch) || ch == '.' || (i == 0 && ch == '-')) {
                tmp.append(ch); // O(1)
            } else {
                if (tmp.length() != 0) {
                    String s = tmp.toString();
                    formula.add(s); // O(1)
                    tmp = new StringBuilder();
                }
                if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                    formula.add(String.valueOf(ch)); // O(1)
                } else if (ch != ' ') {
                    throw new UnsupportedOperationException();
                }
            }
        }
        if (tmp.length() != 0) {
            String s = tmp.toString();
            formula.add(s);
        }
        return formula;
    }//Итого: по времени: O(n), по памяти O(n)

    public double execute(double... x) {
        if (x.length != variables.size()) {
            throw new IllegalArgumentException();
        }
        Map<String, Double> map = new HashMap<>();
        for (int i = 0; i < x.length; i++) {
            map.put(variables.get(i), x[i]); //O(1)
        }
        return count(map, root);
    }

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
            return map.get(((VariableNode) node).getNAME());
        }
    }
}
