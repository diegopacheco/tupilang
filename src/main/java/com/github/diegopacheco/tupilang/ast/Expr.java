package com.github.diegopacheco.tupilang.ast;

public interface Expr {
    <T> T accept(ExpressionVisitor<T> visitor);
}