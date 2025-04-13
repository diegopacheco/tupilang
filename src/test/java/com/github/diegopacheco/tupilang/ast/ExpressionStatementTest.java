package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExpressionStatementTest {

    @Test
    public void testCreation() {
        Expr expression = new LiteralIntExpr(42);
        ExpressionStatement stmt = new ExpressionStatement(expression);
        assertSame(expression, stmt.getExpression());
    }

    @Test
    public void testWithNullExpression() {
        ExpressionStatement stmt = new ExpressionStatement(null);
        assertNull(stmt.getExpression());
    }

    @Test
    public void testAcceptVisitor() {
        Expr expression = new LiteralIntExpr(99);
        ExpressionStatement stmt = new ExpressionStatement(expression);

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
                return "Expression: " + (stmt.getExpression() != null ?
                        stmt.getExpression().toString() : "null");
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

        String result = stmt.accept(visitor);
        assertTrue(result.startsWith("Expression:"));
    }

    @Test
    public void testDifferentExpressionTypes() {
        ExpressionStatement stmt1 = new ExpressionStatement(new LiteralIntExpr(123));
        assertNotNull(stmt1.getExpression());
        assertInstanceOf(LiteralIntExpr.class, stmt1.getExpression());
    }
}