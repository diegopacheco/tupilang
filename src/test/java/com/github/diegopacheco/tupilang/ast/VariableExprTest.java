package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VariableExprTest {

    @Test
    public void testCreation() {
        String name = "testVar";
        VariableExpr expr = new VariableExpr(name);
        assertEquals(name, expr.getName());
    }

    @Test
    public void testEmptyName() {
        VariableExpr expr = new VariableExpr("");
        assertEquals("", expr.getName());
    }

    @Test
    public void testAcceptVisitor() {
        String varName = "myVariable";
        VariableExpr expr = new VariableExpr(varName);

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
                return "";
            }

            @Override
            public String visitVariableExpr(VariableExpr expr) {
                return "Variable: " + expr.getName();
            }
        };

        String result = expr.accept(visitor);
        assertEquals("Variable: myVariable", result);
    }

    @Test
    public void testSpecialCharactersInName() {
        String name = "test_var$123";
        VariableExpr expr = new VariableExpr(name);
        assertEquals(name, expr.getName());
    }

    @Test
    public void testNullName() {
        VariableExpr expr = new VariableExpr(null);
        assertNull(expr.getName());
    }
}