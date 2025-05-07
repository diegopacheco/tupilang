package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LiteralBoolExprTest {

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
    public void testCreation() {
        LiteralBoolExpr trueExpr = new LiteralBoolExpr(true);
        LiteralBoolExpr falseExpr = new LiteralBoolExpr(false);

        assertTrue(trueExpr.getValue());
        assertFalse(falseExpr.getValue());
    }

    @Test
    public void testDirectInspection() {
        LiteralBoolExpr trueExpr = new LiteralBoolExpr(true);
        LiteralBoolExpr falseExpr = new LiteralBoolExpr(false);

        assertTrue(trueExpr.getValue());
        assertFalse(falseExpr.getValue());
    }
}