package com.github.diegopacheco.tupilang.ast;

public class VariableExpr implements Expr {
    private final String name;

    public VariableExpr(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
