package com.github.diegopacheco.tupilang.tupilang.ast;

public class PrintStatement implements Stmt {
    private final Expr expression;

    public PrintStatement(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() {
        return expression;
    }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitPrintStatement(this);
    }
}