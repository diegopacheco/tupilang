package com.github.diegopacheco.tupilang.test.ast;

import com.github.diegopacheco.tupilang.ast.LiteralIntExpr;
import com.github.diegopacheco.tupilang.ast.UnaryExpr;
import com.github.diegopacheco.tupilang.ast.VariableExpr;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnaryExprTest {

    @Test
    public void testCreatePrefixUnaryExpr() {
        // Create a prefix unary expression: -x
        VariableExpr variable = new VariableExpr("x");
        UnaryExpr unaryExpr = new UnaryExpr("-", variable, false);

        assertFalse(unaryExpr.isPostfix());
        assertEquals("-", unaryExpr.getOperator());
        assertSame(variable, unaryExpr.getExpr());
    }

    @Test
    public void testCreatePostfixUnaryExpr() {
        // Create a postfix unary expression: x++
        VariableExpr variable = new VariableExpr("x");
        UnaryExpr unaryExpr = new UnaryExpr("++", variable, true);

        assertTrue(unaryExpr.isPostfix());
        assertEquals("++", unaryExpr.getOperator());
        assertSame(variable, unaryExpr.getExpr());
    }

    @Test
    public void testPrefixUnaryToString() {
        // Test string representation of prefix operations
        UnaryExpr negation = new UnaryExpr("-", new LiteralIntExpr(5), false);
        UnaryExpr not = new UnaryExpr("!", new VariableExpr("isValid"), false);

        assertEquals("(-5)", negation.toString());
        assertEquals("(!isValid)", not.toString());
    }

    @Test
    public void testPostfixUnaryToString() {
        // Test string representation of postfix operations
        UnaryExpr increment = new UnaryExpr("++", new VariableExpr("counter"), true);
        UnaryExpr decrement = new UnaryExpr("--", new VariableExpr("index"), true);

        assertEquals("(counter++)", increment.toString());
        assertEquals("(index--)", decrement.toString());
    }

    @Test
    public void testNestedUnaryExpressions() {
        // Create nested unary expressions: -(!x)
        VariableExpr variable = new VariableExpr("x");
        UnaryExpr inner = new UnaryExpr("!", variable, false);
        UnaryExpr outer = new UnaryExpr("-", inner, false);

        assertEquals("-", outer.getOperator());
        assertFalse(outer.isPostfix());
        assertTrue(outer.getExpr() instanceof UnaryExpr);

        UnaryExpr innerExpr = (UnaryExpr) outer.getExpr();
        assertEquals("!", innerExpr.getOperator());
        assertFalse(innerExpr.isPostfix());
        assertEquals("x", ((VariableExpr) innerExpr.getExpr()).getName());

        // Test string representation of nested expression
        assertEquals("(-(!x))", outer.toString());
    }

    @Test
    public void testMixedPrefixAndPostfixNesting() {
        // Test a mix of prefix and postfix: -(x++)
        VariableExpr variable = new VariableExpr("x");
        UnaryExpr postfix = new UnaryExpr("++", variable, true);
        UnaryExpr prefix = new UnaryExpr("-", postfix, false);

        assertFalse(prefix.isPostfix());
        assertEquals("-", prefix.getOperator());

        UnaryExpr inner = (UnaryExpr) prefix.getExpr();
        assertTrue(inner.isPostfix());
        assertEquals("++", inner.getOperator());

        assertEquals("(-(x++))", prefix.toString());
    }
}
