package com.github.diegopacheco.tupilang.ast;

public interface ExpressionVisitor<T> {
    T visitBinaryExpr(BinaryExpr expr);
    T visitVariableExpr(VariableExpr expr);
    T visitLiteralIntExpr(LiteralIntExpr expr);
    T visitLiteralStringExpr(LiteralStringExpr expr);
    T visitCallExpr(CallExpr expr);
}