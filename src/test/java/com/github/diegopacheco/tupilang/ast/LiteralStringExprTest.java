package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LiteralStringExprTest {

    @Test
    public void testCreation() {
        String value = "test string";
        LiteralStringExpr expr = new LiteralStringExpr(value);
        assertEquals(value, expr.getValue());
    }

    @Test
    public void testEmptyString() {
        LiteralStringExpr expr = new LiteralStringExpr("");
        assertEquals("", expr.getValue());
    }

    @Test
    public void testNullString() {
        LiteralStringExpr expr = new LiteralStringExpr(null);
        assertNull(expr.getValue());
    }

    @Test
    public void testAcceptVisitor() {
        LiteralStringExpr expr = new LiteralStringExpr("hello world");

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
                return "";
            }

            @Override
            public String visitLiteralStringExpr(LiteralStringExpr expr) {
                return "String: " + expr.getValue();
            }

            @Override
            public String visitVariableExpr(VariableExpr expr) {
                return "";
            }
        };

        String result = expr.accept(visitor);
        assertEquals("String: hello world", result);
    }

    @Test
    public void testSpecialCharacters() {
        String special = "Line1\nLine2\t\"quoted\"";
        LiteralStringExpr expr = new LiteralStringExpr(special);
        assertEquals(special, expr.getValue());
    }
}