package com.github.diegopacheco.tupilang.tupilang.ast;

public class VariableExpr implements Expr {
    private final String name;

    public VariableExpr(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitVariableExpr(this);
    }
}
