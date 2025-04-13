package com.github.diegopacheco.tupilang.tupilang.ast;

public interface Expr {
    <T> T accept(ExpressionVisitor<T> visitor);
}