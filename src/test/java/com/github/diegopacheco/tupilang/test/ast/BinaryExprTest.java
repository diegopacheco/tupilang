package com.github.diegopacheco.tupilang.test.ast;

import com.github.diegopacheco.tupilang.ast.BinaryExpr;
import com.github.diegopacheco.tupilang.ast.Expr;
import com.github.diegopacheco.tupilang.ast.LiteralIntExpr;
import com.github.diegopacheco.tupilang.ast.LiteralStringExpr;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BinaryExprTest {

    private BinaryExprTest(){}

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
    public void testDirectInspection() {
        BinaryExpr expr = new BinaryExpr(
                new LiteralIntExpr(10),
                "+",
                new LiteralIntExpr(20)
        );

        assertTrue(expr.getLeft() instanceof LiteralIntExpr);
        assertTrue(expr.getRight() instanceof LiteralIntExpr);

        LiteralIntExpr left = (LiteralIntExpr) expr.getLeft();
        LiteralIntExpr right = (LiteralIntExpr) expr.getRight();

        assertEquals(10, left.getValue());
        assertEquals(20, right.getValue());
        assertEquals("+", expr.getOperator());
    }
}