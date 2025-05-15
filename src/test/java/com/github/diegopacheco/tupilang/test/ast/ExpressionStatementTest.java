package com.github.diegopacheco.tupilang.test.ast;

import com.github.diegopacheco.tupilang.ast.Expr;
import com.github.diegopacheco.tupilang.ast.ExpressionStatement;
import com.github.diegopacheco.tupilang.ast.LiteralIntExpr;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExpressionStatementTest {

    private ExpressionStatementTest(){}

    @Test
    public void testCreation() {
        Expr expression = new LiteralIntExpr(42);
        ExpressionStatement stmt = new ExpressionStatement(expression);
        assertSame(expression, stmt.getExpression());
    }

    @Test
    public void testWithNullExpression() {
        ExpressionStatement stmt = new ExpressionStatement(null);
        assertNull(stmt.getExpression());
    }

    @Test
    public void testDirectInspection() {
        Expr expression = new LiteralIntExpr(99);
        ExpressionStatement stmt = new ExpressionStatement(expression);

        assertTrue(stmt.getExpression() instanceof LiteralIntExpr);
        LiteralIntExpr intExpr = (LiteralIntExpr) stmt.getExpression();
        assertEquals(99, intExpr.getValue());
    }

    @Test
    public void testDifferentExpressionTypes() {
        ExpressionStatement stmt1 = new ExpressionStatement(new LiteralIntExpr(123));
        assertNotNull(stmt1.getExpression());
        assertInstanceOf(LiteralIntExpr.class, stmt1.getExpression());
    }
}