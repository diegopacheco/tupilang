package com.github.diegopacheco.tupilang.ast;

public interface Stmt {
    void accept(StatementVisitor visitor);
}
