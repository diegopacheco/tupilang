package com.github.diegopacheco.tupilang.ast;
import java.util.List;

public class ForStatement implements Stmt {
    private final String varName;
    private final Expr initializer;
    private final Expr condition;
    private final Expr increment;
    private final List<Stmt> body;
    private final boolean isTraditional;

    // Traditional for loop: for (int i=0; i<=10; i++)
    public ForStatement(String varName, Expr initializer, Expr condition,
                        Expr increment, List<Stmt> body) {
        this.varName = varName;
        this.initializer = initializer;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
        this.isTraditional = true;
    }

    // Range-based for loop: for (int i : 1 to 10)
    public ForStatement(String varName, Expr start, Expr end, List<Stmt> body) {
        this.varName = varName;
        this.initializer = start;
        this.condition = end;
        // Not used in range-based version
        this.increment = null;
        this.body = body;
        this.isTraditional = false;
    }

    public String getVarName() {
        return varName;
    }

    public Expr getInitializer() {
        return initializer;
    }

    public Expr getCondition() {
        return condition;
    }

    public Expr getIncrement() {
        return increment;
    }

    public List<Stmt> getBody() {
        return body;
    }

    public boolean isTraditional() {
        return isTraditional;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isTraditional) {
            sb.append("for (").append(varName).append(" = ").append(initializer).append("; ")
              .append(condition).append("; ").append(increment).append(") {\n");
        } else {
            sb.append("for (").append(varName).append(" : ").append(initializer)
              .append(" to ").append(condition).append(") {\n");
        }
        for (Stmt stmt : body) {
            sb.append("\t").append(stmt.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}