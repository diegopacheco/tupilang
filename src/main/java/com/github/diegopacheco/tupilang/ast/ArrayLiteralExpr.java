package com.github.diegopacheco.tupilang.ast;

import java.util.List;

public class ArrayLiteralExpr implements Expr {
    private final List<Expr> elements;

    public ArrayLiteralExpr(List<Expr> elements) {
        this.elements = elements;
    }

    public List<Expr> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).toString());
            if (i < elements.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}