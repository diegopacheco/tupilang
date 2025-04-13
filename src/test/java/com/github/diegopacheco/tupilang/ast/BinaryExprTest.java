package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BinaryExprTest {

    @Test
    public void testCreation() {
        Expr left = new LiteralIntExpr(5);
        String operator = "+";
        Expr right = new LiteralIntExpr(3);

        BinaryExpr expr = new BinaryExpr(left, operator, right);

        assertSame(left, expr.getLeft());
        assertEquals(operator, expr.getOperator());
        assertSame(right, expr.getRight());
    }

    @Test
    public void testDifferentOperators() {
        testWithOperator("+");
        testWithOperator("-");
        testWithOperator("*");
        testWithOperator("/");
        testWithOperator("==");
        testWithOperator("!=");
        testWithOperator("<");
        testWithOperator(">");
    }

    private void testWithOperator(String operator) {
        Expr left = new LiteralIntExpr(10);
        Expr right = new LiteralIntExpr(20);

        BinaryExpr expr = new BinaryExpr(left, operator, right);
        assertEquals(operator, expr.getOperator());
    }

    @Test
    public void testNestedExpressions() {
        BinaryExpr inner = new BinaryExpr(
                new LiteralIntExpr(5),
                "+",
                new LiteralIntExpr(3)
        );

        BinaryExpr expr = new BinaryExpr(
                inner,
                "*",
                new LiteralIntExpr(2)
        );

        assertInstanceOf(BinaryExpr.class, expr.getLeft());
        assertEquals("*", expr.getOperator());
        assertInstanceOf(LiteralIntExpr.class, expr.getRight());
    }

    @Test
    public void testMixedExpressionTypes() {
        BinaryExpr expr = new BinaryExpr(
                new LiteralStringExpr("hello"),
                "+",
                new LiteralIntExpr(123)
        );

        assertInstanceOf(LiteralStringExpr.class, expr.getLeft());
        assertEquals("+", expr.getOperator());
        assertInstanceOf(LiteralIntExpr.class, expr.getRight());
    }

    @Test
    public void testAcceptVisitor() {
        BinaryExpr expr = new BinaryExpr(
                new LiteralIntExpr(10),
                "+",
                new LiteralIntExpr(20)
        );

        ExpressionVisitor<String> visitor = new ExpressionVisitor<String>() {
            @Override
            public String visitBinaryExpr(BinaryExpr expr) {
                return expr.getLeft().accept(this) +
                        " " + expr.getOperator() + " " +
                        expr.getRight().accept(this);
            }

            @Override
            public String visitCallExpr(CallExpr expr) {
                return "";
            }

            @Override
            public String visitLiteralIntExpr(LiteralIntExpr expr) {
                return Integer.toString(expr.getValue());
            }

            @Override
            public String visitLiteralStringExpr(LiteralStringExpr expr) {
                return expr.getValue();
            }

            @Override
            public String visitVariableExpr(VariableExpr expr) {
                return "";
            }
        };

        String result = expr.accept(visitor);
        assertEquals("10 + 20", result);
    }
}