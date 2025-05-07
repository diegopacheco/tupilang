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
    public void testDirectInspection() {
        Expr expression = new LiteralIntExpr(42);
        ReturnStatement stmt = new ReturnStatement(expression);

        assertInstanceOf(LiteralIntExpr.class, stmt.getExpression());
        assertEquals(42, ((LiteralIntExpr)stmt.getExpression()).getValue());
    }

    @Test
    public void testVoidReturn() {
        ReturnStatement stmt = new ReturnStatement(null);
        assertNull(stmt.getExpression());
    }

    @Test
    public void testWithComplexExpression() {
        BinaryExpr expr = new BinaryExpr(
                new LiteralIntExpr(10),
                "*",
                new LiteralIntExpr(5)
        );

        ReturnStatement stmt = new ReturnStatement(expr);
        assertInstanceOf(BinaryExpr.class, stmt.getExpression());

        BinaryExpr binaryExpr = (BinaryExpr) stmt.getExpression();
        assertEquals("*", binaryExpr.getOperator());
        assertEquals(10, ((LiteralIntExpr)binaryExpr.getLeft()).getValue());
        assertEquals(5, ((LiteralIntExpr)binaryExpr.getRight()).getValue());
    }
}