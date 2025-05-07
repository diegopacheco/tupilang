package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrintStatementTest {

    @Test
    public void testCreation() {
        Expr expression = new LiteralStringExpr("Hello, world!");
        PrintStatement stmt = new PrintStatement(expression);
        assertSame(expression, stmt.getExpression());
    }

    @Test
    public void testWithNullExpression() {
        PrintStatement stmt = new PrintStatement(null);
        assertNull(stmt.getExpression());
    }

    @Test
    public void testDirectInspection() {
        Expr expression = new LiteralIntExpr(42);
        PrintStatement stmt = new PrintStatement(expression);

        assertTrue(stmt.getExpression() instanceof LiteralIntExpr);
        assertEquals(42, ((LiteralIntExpr)stmt.getExpression()).getValue());
    }

    @Test
    public void testComplexExpression() {
        BinaryExpr binaryExpr = new BinaryExpr(
                new LiteralStringExpr("Hello"),
                "+",
                new LiteralStringExpr(" World")
        );

        PrintStatement stmt = new PrintStatement(binaryExpr);
        assertTrue(stmt.getExpression() instanceof BinaryExpr);

        BinaryExpr expr = (BinaryExpr) stmt.getExpression();
        assertEquals("+", expr.getOperator());
        assertEquals("Hello", ((LiteralStringExpr)expr.getLeft()).getValue());
        assertEquals(" World", ((LiteralStringExpr)expr.getRight()).getValue());
    }
}