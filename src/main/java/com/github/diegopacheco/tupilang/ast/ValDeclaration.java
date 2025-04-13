package com.github.diegopacheco.tupilang.ast;

public class ValDeclaration implements Stmt {
    private final String name;
    private final Expr initializer;

    public ValDeclaration(String name, Expr initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    public String getName() {
        return name;
    }
    public Expr getInitializer() {
        return initializer;
    }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitValDeclaration(this);
    }
}