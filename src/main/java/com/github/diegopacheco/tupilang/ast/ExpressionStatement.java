package com.github.diegopacheco.tupilang.ast;

public class ExpressionStatement implements Stmt {
    private final Expr expression;

    public ExpressionStatement(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() {
        return expression;
    }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitExpressionStatement(this);
    }
}