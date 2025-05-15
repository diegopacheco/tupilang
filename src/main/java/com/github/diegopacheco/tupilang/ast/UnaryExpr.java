package com.github.diegopacheco.tupilang.ast;

public class UnaryExpr implements Expr {
    private final String operator;
    private final Expr expr;
    private final boolean isPostfix;

    public UnaryExpr(String operator, Expr expr, boolean isPostfix) {
        this.operator = operator;
        this.expr = expr;
        this.isPostfix = isPostfix;
    }

    public String getOperator() {
        return operator;
    }

    public Expr getExpr() {
        return expr;
    }

    public boolean isPostfix() {
        return isPostfix;
    }

    @Override
    public String toString() {
        return isPostfix ?
                "(" + expr + operator + ")" :
                "(" + operator + expr + ")";
    }
}