package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValDeclarationTest {

    @Test
    public void testValDeclarationCreation() {
        Expr initializer = new LiteralIntExpr(42);
        ValDeclaration declaration = new ValDeclaration("answer", initializer);

        assertEquals("answer", declaration.getName());
        assertSame(initializer, declaration.getInitializer());
    }

    @Test
    public void testValDeclarationWithNullInitializer() {
        ValDeclaration declaration = new ValDeclaration("uninitializedVar", null);
        assertEquals("uninitializedVar", declaration.getName());
        assertNull(declaration.getInitializer());
    }

    @Test
    public void testValDeclarationAcceptsVisitor() {
        Expr initializer = new LiteralStringExpr("hello");
        ValDeclaration declaration = new ValDeclaration("greeting", initializer);

        StatementVisitor<String> visitor = new StatementVisitor<String>() {
            @Override
            public String visitValDeclaration(ValDeclaration stmt) {
                return stmt.getName();
            }
            // Implement other required methods
            @Override
            public String visitPrintStatement(PrintStatement stmt) {
                return null;
            }
            @Override
            public String visitExpressionStatement(ExpressionStatement stmt) {
                return null;
            }
            @Override
            public String visitFunctionDefinition(FunctionDefinition stmt) {
                return "";
            }
            @Override
            public String visitReturnStatement(ReturnStatement stmt) {
                return "";
            }
            @Override
            public String visitIfStatement(IfStatement stmt) {
                return "";
            }
        };

        String result = declaration.accept(visitor);
        assertEquals("greeting", result);
    }
}