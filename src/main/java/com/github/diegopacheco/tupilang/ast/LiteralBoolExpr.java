package com.github.diegopacheco.tupilang.ast;

public class LiteralBoolExpr implements Expr {
    public final boolean value;

    public LiteralBoolExpr(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.acceptLiteralBoolExpr(this);
    }
}