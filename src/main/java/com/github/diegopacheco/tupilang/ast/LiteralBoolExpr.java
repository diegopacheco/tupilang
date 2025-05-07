package com.github.diegopacheco.tupilang.ast;

public class LiteralBoolExpr implements Expr {
    private final boolean value;

    public LiteralBoolExpr(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

}