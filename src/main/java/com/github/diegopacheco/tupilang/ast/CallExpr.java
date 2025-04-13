package com.github.diegopacheco.tupilang.tupilang.ast;

import java.util.List;

public class CallExpr implements Expr {
    private final String callee;
    private final List<Expr> arguments;

    public CallExpr(String callee, List<Expr> arguments) {
        this.callee = callee;
        this.arguments = arguments;
    }

    public String getCallee() {
        return callee;
    }

    public List<Expr> getArguments() {
        return arguments;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitCallExpr(this);
    }
}