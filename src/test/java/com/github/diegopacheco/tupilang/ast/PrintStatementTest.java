package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrintStatementTest {

    @Test
    public void testCreation() {
        // Create a simple test expression
        Expr expression = new LiteralStringExpr("Hello, world!");
        PrintStatement stmt = new PrintStatement(expression);

        // Test that the expression is correctly stored
        assertSame(expression, stmt.getExpression());
    }

    @Test
    public void testWithNullExpression() {
        // Test with null expression - should not throw exception
        PrintStatement stmt = new PrintStatement(null);
        assertNull(stmt.getExpression());
    }

    @Test
    public void testAcceptVisitor() {
        Expr expression = new LiteralIntExpr(42);
        PrintStatement stmt = new PrintStatement(expression);

        StatementVisitor<String> visitor = new StatementVisitor<String>() {
            @Override
            public String visitValDeclaration(ValDeclaration stmt) {
                return "";
            }

            @Override
            public String visitPrintStatement(PrintStatement stmt) {
                return "Print: " + stmt.getExpression().toString();
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
                return "";
            }

            @Override
            public String visitIfStatement(IfStatement stmt) {
                return "";
            }
        };

        String result = stmt.accept(visitor);
        assertTrue(result.startsWith("Print:"));
    }

    @Test
    public void testComplexExpression() {
        // Test with a more complex expression
        // For example, a binary expression if available
        // This is a placeholder using a Literal as we don't have BinaryExpr in the context
        Expr expression = new LiteralStringExpr("Complex expression");
        PrintStatement stmt = new PrintStatement(expression);
        assertNotNull(stmt.getExpression());
    }
}