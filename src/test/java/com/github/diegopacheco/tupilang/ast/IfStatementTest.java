package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IfStatementTest {

    @Test
    public void testIfStatementCreation() {
        Expr condition = new LiteralIntExpr(1); // Truthy condition
        List<Stmt> thenBranch = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(new LiteralIntExpr(42))))
        );
        List<Stmt> elseBranch = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(new LiteralIntExpr(24))))
        );

        IfStatement stmt = new IfStatement(condition, thenBranch, elseBranch);

        assertSame(condition, stmt.getCondition());
        assertSame(thenBranch, stmt.getThenBranch());
        assertSame(elseBranch, stmt.getElseBranch());
        assertTrue(stmt.hasElseBranch());
    }

    @Test
    public void testIfWithoutElse() {
        Expr condition = new LiteralIntExpr(1);
        List<Stmt> thenBranch = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(new LiteralIntExpr(42))))
        );

        IfStatement stmt = new IfStatement(condition, thenBranch, null);

        assertSame(condition, stmt.getCondition());
        assertSame(thenBranch, stmt.getThenBranch());
        assertNull(stmt.getElseBranch());
        assertFalse(stmt.hasElseBranch());
    }

    @Test
    public void testComplexBranches() {
        Expr condition = new LiteralIntExpr(1);

        List<Stmt> thenBranch = new ArrayList<>();
        thenBranch.add(new ValDeclaration("x", new LiteralIntExpr(1)));
        thenBranch.add(new ExpressionStatement(new CallExpr("print", List.of(new LiteralIntExpr(2)))));

        List<Stmt> elseBranch = new ArrayList<>();
        elseBranch.add(new ValDeclaration("y", new LiteralIntExpr(3)));
        elseBranch.add(new ExpressionStatement(new CallExpr("print", List.of(new LiteralIntExpr(4)))));

        IfStatement stmt = new IfStatement(condition, thenBranch, elseBranch);

        assertEquals(2, stmt.getThenBranch().size());
        assertEquals(2, stmt.getElseBranch().size());
    }

    @Test
    public void testDirectInspection() {
        Expr condition = new LiteralIntExpr(1);
        List<Stmt> thenBranch = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(new LiteralIntExpr(42))))
        );
        List<Stmt> elseBranch = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(new LiteralIntExpr(24))))
        );

        IfStatement stmt = new IfStatement(condition, thenBranch, elseBranch);

        assertInstanceOf(LiteralIntExpr.class, stmt.getCondition());
        assertEquals(1, ((LiteralIntExpr)stmt.getCondition()).getValue());

        assertEquals(1, stmt.getThenBranch().size());
        assertInstanceOf(ExpressionStatement.class, stmt.getThenBranch().get(0));

        ExpressionStatement thenStmt = (ExpressionStatement)stmt.getThenBranch().get(0);
        assertInstanceOf(CallExpr.class, thenStmt.getExpression());

        CallExpr callExpr = (CallExpr)thenStmt.getExpression();
        assertEquals("print", callExpr.getCallee());
        assertEquals(1, callExpr.getArguments().size());
        assertInstanceOf(LiteralIntExpr.class, callExpr.getArguments().get(0));
        assertEquals(42, ((LiteralIntExpr)callExpr.getArguments().get(0)).getValue());

        assertTrue(stmt.hasElseBranch());
    }
}