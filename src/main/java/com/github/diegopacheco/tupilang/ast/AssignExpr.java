package com.github.diegopacheco.tupilang.ast;

public class AssignExpr implements Expr {
    private final String name;
    private final Expr value;

    public AssignExpr(String name, Expr value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Expr getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" = ");
        if (value instanceof AssignExpr) {
            sb.append("(").append(value.toString()).append(")");
        } else {
            sb.append(value.toString());
        }
        return sb.toString();
    }

}