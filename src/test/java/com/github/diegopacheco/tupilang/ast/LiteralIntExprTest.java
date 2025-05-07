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
    public void testDirectInspection() {
        LiteralIntExpr expr = new LiteralIntExpr(123);
        assertEquals(123, expr.getValue());
    }
}