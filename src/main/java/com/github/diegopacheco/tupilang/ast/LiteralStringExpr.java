package com.github.diegopacheco.tupilang.tupilang.ast;

public class LiteralStringExpr implements Expr {
    private final String value;

    public LiteralStringExpr(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitLiteralStringExpr(this);
    }
}
