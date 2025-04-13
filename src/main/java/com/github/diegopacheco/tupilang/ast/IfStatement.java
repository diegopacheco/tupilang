package com.github.diegopacheco.tupilang.ast;

import java.util.List;

public class IfStatement implements Stmt {
    private final Expr condition;
    private final List<Stmt> thenBranch;
    private final List<Stmt> elseBranch;

    public IfStatement(Expr condition, List<Stmt> thenBranch, List<Stmt> elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expr getCondition() {
        return condition;
    }

    public List<Stmt> getThenBranch() {
        return thenBranch;
    }

    public List<Stmt> getElseBranch() {
        return elseBranch;
    }

    public boolean hasElseBranch() {
        return elseBranch != null;
    }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitIfStatement(this);
    }
}