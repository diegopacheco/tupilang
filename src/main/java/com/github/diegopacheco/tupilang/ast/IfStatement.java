package com.github.diegopacheco.tupilang.ast;

import java.util.List;

public class IfStatement implements Stmt {
    private final Expr condition;
    private final List<Stmt> body;

    public IfStatement(Expr condition, List<Stmt> body) {
        this.condition = condition;
        this.body = body;
    }

    public Expr getCondition() {
        return condition;
    }

    public List<Stmt> getBody() {
        return body;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitIfStatement(this);
    }
}