package com.github.diegopacheco.tupilang.ast;

public interface Stmt {
    <T> T accept(StatementVisitor<T> visitor);
}