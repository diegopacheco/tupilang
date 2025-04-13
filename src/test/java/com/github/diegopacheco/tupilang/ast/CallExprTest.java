package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CallExprTest {

    @Test
    public void testCallExprCreation() {
        String callee = "testFunction";
        List<Expr> arguments = Arrays.asList(
                new LiteralIntExpr(42),
                new LiteralStringExpr("hello")
        );

        CallExpr callExpr = new CallExpr(callee, arguments);

        assertEquals(callee, callExpr.getCallee());
        assertEquals(arguments, callExpr.getArguments());
        assertEquals(2, callExpr.getArguments().size());
    }

    @Test
    public void testNoArguments() {
        String callee = "noArgsFunction";
        List<Expr> arguments = Collections.emptyList();

        CallExpr callExpr = new CallExpr(callee, arguments);

        assertEquals(callee, callExpr.getCallee());
        assertTrue(callExpr.getArguments().isEmpty());
    }

    @Test
    public void testNestedArguments() {
        String outerCallee = "outer";
        String innerCallee = "inner";

        List<Expr> innerArgs = Collections.singletonList(new LiteralIntExpr(100));
        CallExpr innerCall = new CallExpr(innerCallee, innerArgs);

        List<Expr> outerArgs = Arrays.asList(
                new LiteralIntExpr(1),
                innerCall,
                new LiteralStringExpr("test")
        );

        CallExpr callExpr = new CallExpr(outerCallee, outerArgs);

        assertEquals(outerCallee, callExpr.getCallee());
        assertEquals(3, callExpr.getArguments().size());
        assertInstanceOf(CallExpr.class, callExpr.getArguments().get(1));

        CallExpr nestedCall = (CallExpr) callExpr.getArguments().get(1);
        assertEquals(innerCallee, nestedCall.getCallee());
        assertEquals(1, nestedCall.getArguments().size());
    }

    @Test
    public void testAcceptVisitor() {
        String callee = "visitedFunction";
        List<Expr> arguments = Arrays.asList(
                new LiteralIntExpr(1),
                new LiteralIntExpr(2)
        );

        CallExpr callExpr = new CallExpr(callee, arguments);

        ExpressionVisitor<String> visitor = new ExpressionVisitor<String>() {
            @Override
            public String visitBinaryExpr(BinaryExpr expr) {
                return "";
            }

            @Override
            public String visitCallExpr(CallExpr expr) {
                return "Call to: " + expr.getCallee() + " with " +
                        expr.getArguments().size() + " args";
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
                return "";
            }
        };

        String result = callExpr.accept(visitor);
        assertEquals("Call to: visitedFunction with 2 args", result);
    }

    @Test
    public void testArgumentModification() {
        String callee = "testFunction";
        List<Expr> arguments = new ArrayList<>();
        arguments.add(new LiteralIntExpr(1));

        CallExpr callExpr = new CallExpr(callee, arguments);
        assertEquals(1, callExpr.getArguments().size());

        arguments.add(new LiteralIntExpr(2));
        assertEquals(2, callExpr.getArguments().size());
    }
}