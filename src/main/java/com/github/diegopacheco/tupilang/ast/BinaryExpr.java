package com.github.diegopacheco.tupilang.tupilang.ast;

public class BinaryExpr implements Expr {
    private final Expr left;
    private final String operator;
    private final Expr right;

    public BinaryExpr(Expr left, String operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public Expr getRight() {
        return right;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitBinaryExpr(this);
    }
}
