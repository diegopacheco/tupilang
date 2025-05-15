package com.github.diegopacheco.tupilang.test.ast;

import com.github.diegopacheco.tupilang.ast.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValDeclarationTest {

    private ValDeclarationTest(){}

    @Test
    public void testValDeclarationCreation() {
        Expr initializer = new LiteralIntExpr(42);
        ValDeclaration declaration = new ValDeclaration("answer", initializer);

        assertEquals("answer", declaration.getName());
        assertSame(initializer, declaration.getInitializer());
    }

    @Test
    public void testValDeclarationWithNullInitializer() {
        ValDeclaration declaration = new ValDeclaration("uninitializedVar", null);
        assertEquals("uninitializedVar", declaration.getName());
        assertNull(declaration.getInitializer());
    }

    @Test
    public void testDirectInspection() {
        // Replace visitor test with direct inspection
        Expr initializer = new LiteralStringExpr("hello");
        ValDeclaration declaration = new ValDeclaration("greeting", initializer);

        assertEquals("greeting", declaration.getName());
        assertTrue(declaration.getInitializer() instanceof LiteralStringExpr);
        assertEquals("hello", ((LiteralStringExpr)declaration.getInitializer()).getValue());
    }

    @Test
    public void testWithComplexInitializer() {
        BinaryExpr binaryExpr = new BinaryExpr(
                new LiteralIntExpr(5),
                "+",
                new LiteralIntExpr(3)
        );

        ValDeclaration declaration = new ValDeclaration("sum", binaryExpr);

        assertEquals("sum", declaration.getName());
        assertTrue(declaration.getInitializer() instanceof BinaryExpr);

        BinaryExpr expr = (BinaryExpr) declaration.getInitializer();
        assertEquals("+", expr.getOperator());
        assertEquals(5, ((LiteralIntExpr)expr.getLeft()).getValue());
        assertEquals(3, ((LiteralIntExpr)expr.getRight()).getValue());
    }
}