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
        List<Stmt> thenBranch = Collections.singletonList(new PrintStatement(new LiteralIntExpr(42)));
        List<Stmt> elseBranch = Collections.singletonList(new PrintStatement(new LiteralIntExpr(24)));

        IfStatement stmt = new IfStatement(condition, thenBranch, elseBranch);

        assertSame(condition, stmt.getCondition());
        assertSame(thenBranch, stmt.getThenBranch());
        assertSame(elseBranch, stmt.getElseBranch());
        assertTrue(stmt.hasElseBranch());
    }

    @Test
    public void testIfWithoutElse() {
        Expr condition = new LiteralIntExpr(1);
        List<Stmt> thenBranch = Collections.singletonList(new PrintStatement(new LiteralIntExpr(42)));

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
        thenBranch.add(new PrintStatement(new LiteralIntExpr(2)));

        List<Stmt> elseBranch = new ArrayList<>();
        elseBranch.add(new ValDeclaration("y", new LiteralIntExpr(3)));
        elseBranch.add(new PrintStatement(new LiteralIntExpr(4)));

        IfStatement stmt = new IfStatement(condition, thenBranch, elseBranch);

        assertEquals(2, stmt.getThenBranch().size());
        assertEquals(2, stmt.getElseBranch().size());
    }

    @Test
    public void testAcceptVisitor() {
        Expr condition = new LiteralIntExpr(1);
        List<Stmt> thenBranch = Collections.singletonList(new PrintStatement(new LiteralIntExpr(42)));
        List<Stmt> elseBranch = Collections.singletonList(new PrintStatement(new LiteralIntExpr(24)));

        IfStatement stmt = new IfStatement(condition, thenBranch, elseBranch);

        StatementVisitor<String> visitor = new StatementVisitor<String>() {
            @Override
            public String visitValDeclaration(ValDeclaration stmt) {
                return "";
            }

            @Override
            public String visitPrintStatement(PrintStatement stmt) {
                return "";
            }

            @Override
            public String visitExpressionStatement(ExpressionStatement stmt) {
                return "";
            }

            @Override
            public String visitFunctionDefinition(FunctionDefinition stmt) {
                return "";
            }

            @Override
            public String visitReturnStatement(ReturnStatement stmt) {
                return "";
            }

            @Override
            public String visitIfStatement(IfStatement stmt) {
                return "If statement with " +
                        (stmt.hasElseBranch() ? "else branch" : "no else");
            }
        };

        String result = stmt.accept(visitor);
        assertEquals("If statement with else branch", result);
    }
}