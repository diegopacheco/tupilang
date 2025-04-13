package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LiteralBoolExprTest {

    @Test
    public void testConstructorAndValue() {
        LiteralBoolExpr trueExpr = new LiteralBoolExpr(true);
        LiteralBoolExpr falseExpr = new LiteralBoolExpr(false);

        assertTrue(trueExpr.value);
        assertFalse(falseExpr.value);
    }

    @Test
    public void testToStringTrue() {
        LiteralBoolExpr expr = new LiteralBoolExpr(true);
        assertEquals("true", expr.toString());
    }

    @Test
    public void testToStringFalse() {
        LiteralBoolExpr expr = new LiteralBoolExpr(false);
        assertEquals("false", expr.toString());
    }

    @Test
    public void testEquality() {
        LiteralBoolExpr expr1 = new LiteralBoolExpr(true);
        LiteralBoolExpr expr2 = new LiteralBoolExpr(true);
        LiteralBoolExpr expr3 = new LiteralBoolExpr(false);

        // Test equality using the "value" field (not equals method)
        assertEquals(expr1.value, expr2.value);
        assertNotEquals(expr1.value, expr3.value);
    }
}