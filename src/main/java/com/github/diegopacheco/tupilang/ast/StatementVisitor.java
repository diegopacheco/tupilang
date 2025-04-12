package com.github.diegopacheco.tupilang.ast;

public interface StatementVisitor {
    void visitValDeclaration(ValDeclaration stmt);
    void visitPrintStatement(PrintStatement stmt);
    void visitIfStatement(IfStatement stmt);
    void visitFunctionDefinition(FunctionDefinition stmt);
    void visitReturnStatement(ReturnStatement stmt);
    void visitExpressionStatement(ExpressionStatement stmt);
}