package com.github.diegopacheco.tupilang.ast;

public interface StatementVisitor<T> {
    T visitValDeclaration(ValDeclaration stmt);
    T visitPrintStatement(PrintStatement stmt);
    T visitExpressionStatement(ExpressionStatement stmt);
    T visitFunctionDefinition(FunctionDefinition stmt);
    T visitReturnStatement(ReturnStatement stmt);
    T visitIfStatement(IfStatement stmt);
}