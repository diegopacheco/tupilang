package com.github.diegopacheco.tupilang.ast;
import java.util.List;

public class BlockStmt implements Stmt {
    private final List<Stmt> statements;

    public BlockStmt(List<Stmt> statements) {
        this.statements = statements;
    }

    public List<Stmt> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (Stmt stmt : statements) {
            sb.append("  ").append(stmt.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}