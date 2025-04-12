package com.github.diegopacheco.tupilang.ast;

import java.util.List;

public class FunctionDefinition implements Stmt {
    private final String name;
    private final List<Param> params;
    private final String returnType;
    private final List<Stmt> body;

    public FunctionDefinition(String name, List<Param> params, String returnType, List<Stmt> body) {
        this.name = name;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public List<Param> getParameters() {
        return params;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<Stmt> getBody() {
        return body;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitFunctionDefinition(this);
    }
}