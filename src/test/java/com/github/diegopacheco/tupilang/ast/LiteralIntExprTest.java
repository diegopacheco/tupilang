package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LiteralIntExprTest {

    @Test
    public void testCreation() {
        int value = 42;
        LiteralIntExpr expr = new LiteralIntExpr(value);
        assertEquals(value, expr.getValue());
    }

    @Test
    public void testZeroValue() {
        LiteralIntExpr expr = new LiteralIntExpr(0);
        assertEquals(0, expr.getValue());
    }

    @Test
    public void testNegativeValue() {
        int value = -100;
        LiteralIntExpr expr = new LiteralIntExpr(value);
        assertEquals(value, expr.getValue());
    }

    @Test
    public void testMaxIntValue() {
        LiteralIntExpr expr = new LiteralIntExpr(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, expr.getValue());
    }

    @Test
    public void testMinIntValue() {
        LiteralIntExpr expr = new LiteralIntExpr(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, expr.getValue());
    }

    @Test
    public void testAcceptVisitor() {
        LiteralIntExpr expr = new LiteralIntExpr(123);

        ExpressionVisitor<String> visitor = new ExpressionVisitor<String>() {
            @Override
            public String visitBinaryExpr(BinaryExpr expr) {
                return "";
            }

            @Override
            public String visitCallExpr(CallExpr expr) {
                return "";
            }

            @Override
            public String visitLiteralIntExpr(LiteralIntExpr expr) {
                return "Int: " + expr.getValue();
            }

            @Override
            public String visitLiteralStringExpr(LiteralStringExpr expr) {
                return "";
            }

            @Override
            public String visitVariableExpr(VariableExpr expr) {
                return "";
            }
        };

        String result = expr.accept(visitor);
        assertEquals("Int: 123", result);
    }
}