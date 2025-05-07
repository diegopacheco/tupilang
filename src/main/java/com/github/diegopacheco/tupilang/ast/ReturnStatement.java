package com.github.diegopacheco.tupilang.ast;

public class ReturnStatement implements Stmt {
    private final Expr expression;

    public ReturnStatement(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() {
        return expression;
    }

}