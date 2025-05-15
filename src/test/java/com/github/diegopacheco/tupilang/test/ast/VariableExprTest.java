package com.github.diegopacheco.tupilang.test.ast;

import com.github.diegopacheco.tupilang.ast.VariableExpr;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VariableExprTest {

    private VariableExprTest(){}

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
    public void testVariableInspection() {
        String varName = "myVariable";
        VariableExpr expr = new VariableExpr(varName);
        assertEquals("myVariable", expr.getName());
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