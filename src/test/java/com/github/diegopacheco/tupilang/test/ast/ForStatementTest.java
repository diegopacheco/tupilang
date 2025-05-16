package com.github.diegopacheco.tupilang.test.ast;

import com.github.diegopacheco.tupilang.ast.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ForStatementTest {

    private ForStatementTest() {}

    @Test
    public void testTraditionalForLoop() {
        // For loop: for (i = 0; i < 10; i++)
        String varName = "i";
        LiteralIntExpr initializer = new LiteralIntExpr(0);
        VariableExpr conditionVar = new VariableExpr("i");
        LiteralIntExpr conditionVal = new LiteralIntExpr(10);
        BinaryExpr condition = new BinaryExpr(conditionVar, "<", conditionVal);
        UnaryExpr increment = new UnaryExpr("++", new VariableExpr("i"), true);

        List<Stmt> body = new ArrayList<>();
        body.add(new ExpressionStatement(new VariableExpr("x")));

        ForStatement forStmt = new ForStatement(varName, initializer, condition, increment, body);

        assertTrue(forStmt.isTraditional());
        assertEquals("i", forStmt.getVarName());
        assertSame(initializer, forStmt.getInitializer());
        assertSame(condition, forStmt.getCondition());
        assertSame(increment, forStmt.getIncrement());
        assertEquals(1, forStmt.getBody().size());
    }

    @Test
    public void testRangeBasedForLoop() {
        // For loop: for (i : 1 to 10)
        String varName = "i";
        LiteralIntExpr start = new LiteralIntExpr(1);
        LiteralIntExpr end = new LiteralIntExpr(10);

        List<Stmt> body = new ArrayList<>();
        body.add(new ExpressionStatement(new VariableExpr("x")));

        ForStatement forStmt = new ForStatement(varName, start, end, body);

        assertFalse(forStmt.isTraditional());
        assertEquals("i", forStmt.getVarName());
        assertSame(start, forStmt.getInitializer());
        assertSame(end, forStmt.getCondition());
        assertNull(forStmt.getIncrement());
        assertEquals(1, forStmt.getBody().size());
    }

    @Test
    public void testTraditionalForLoopToString() {
        String varName = "i";
        LiteralIntExpr initializer = new LiteralIntExpr(0);
        LiteralIntExpr condition = new LiteralIntExpr(10);
        LiteralIntExpr increment = new LiteralIntExpr(1);

        List<Stmt> body = new ArrayList<>();
        body.add(new ExpressionStatement(new VariableExpr("print")));

        ForStatement forStmt = new ForStatement(varName, initializer, condition, increment, body);

        String result = forStmt.toString();
        assertTrue(result.contains("for (i = 0; 10; 1)"));
        assertTrue(result.contains("{"));
        assertTrue(result.contains("}"));
    }

    @Test
    public void testRangeBasedForLoopToString() {
        String varName = "i";
        LiteralIntExpr start = new LiteralIntExpr(1);
        LiteralIntExpr end = new LiteralIntExpr(10);

        List<Stmt> body = new ArrayList<>();
        body.add(new ExpressionStatement(new VariableExpr("print")));

        ForStatement forStmt = new ForStatement(varName, start, end, body);

        String result = forStmt.toString();
        assertTrue(result.contains("for (i : 1 to 10)"));
        assertTrue(result.contains("{"));
        assertTrue(result.contains("}"));
    }

    @Test
    public void testEmptyBodyForLoop() {
        String varName = "i";
        LiteralIntExpr initializer = new LiteralIntExpr(0);
        LiteralIntExpr condition = new LiteralIntExpr(10);
        LiteralIntExpr increment = new LiteralIntExpr(1);

        List<Stmt> body = new ArrayList<>();
        ForStatement forStmt = new ForStatement(varName, initializer, condition, increment, body);
        assertEquals(0, forStmt.getBody().size());
    }
}