package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReturnStatementTest {

    @Test
    public void testCreation() {
        Expr expression = new LiteralIntExpr(42);
        ReturnStatement stmt = new ReturnStatement(expression);

        assertSame(expression, stmt.getExpression());
    }

    @Test
    public void testNullExpression() {
        ReturnStatement stmt = new ReturnStatement(null);
        assertNull(stmt.getExpression());
    }

    @Test
    public void testAcceptVisitor() {
        Expr expression = new LiteralIntExpr(42);
        ReturnStatement stmt = new ReturnStatement(expression);

        StatementVisitor<String> visitor = new StatementVisitor<String>() {
            @Override
            public String visitValDeclaration(ValDeclaration stmt) {
                return "";
            }

            @Override
            public String visitPrintStatement(PrintStatement stmt) {
                return "";
            }

            @Override
            public String visitExpressionStatement(ExpressionStatement stmt) {
                return "";
            }

            @Override
            public String visitFunctionDefinition(FunctionDefinition stmt) {
                return "";
            }

            @Override
            public String visitReturnStatement(ReturnStatement stmt) {
                return "Return: " + (stmt.getExpression() != null ?
                        stmt.getExpression().toString() : "void");
            }

            @Override
            public String visitIfStatement(IfStatement stmt) {
                return "";
            }
        };

        String result = stmt.accept(visitor);
        assertTrue(result.startsWith("Return:"));
    }

    @Test
    public void testVoidReturnVisitor() {
        ReturnStatement stmt = new ReturnStatement(null);

        StatementVisitor<Boolean> visitor = new StatementVisitor<Boolean>() {
            @Override
            public Boolean visitValDeclaration(ValDeclaration stmt) {
                return false;
            }

            @Override
            public Boolean visitPrintStatement(PrintStatement stmt) {
                return false;
            }

            @Override
            public Boolean visitExpressionStatement(ExpressionStatement stmt) {
                return false;
            }

            @Override
            public Boolean visitFunctionDefinition(FunctionDefinition stmt) {
                return false;
            }

            @Override
            public Boolean visitReturnStatement(ReturnStatement stmt) {
                return stmt.getExpression() == null;
            }

            @Override
            public Boolean visitIfStatement(IfStatement stmt) {
                return false;
            }
        };

        assertTrue(stmt.accept(visitor));
    }
}