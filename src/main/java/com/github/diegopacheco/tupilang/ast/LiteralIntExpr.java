package com.github.diegopacheco.tupilang.ast;

public class LiteralIntExpr implements Expr {
    private final int value;

    public LiteralIntExpr(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
