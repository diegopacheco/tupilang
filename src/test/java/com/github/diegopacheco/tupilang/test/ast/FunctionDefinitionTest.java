package com.github.diegopacheco.tupilang.test.ast;

import com.github.diegopacheco.tupilang.ast.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FunctionDefinitionTest {

    private FunctionDefinitionTest(){}

    @Test
    public void testBasicFunctionDefinition() {
        String functionName = "testFunction";
        List<Param> parameters = Arrays.asList(
                new Param("param1", "int"),
                new Param("param2", "string")
        );
        String returnType = "int";

        List<Stmt> body = Collections.singletonList(
                new ReturnStatement(new LiteralIntExpr(42))
        );

        FunctionDefinition function = new FunctionDefinition(functionName, parameters, returnType, body);

        assertEquals(functionName, function.getName());
        assertEquals(parameters, function.getParameters());
        assertEquals(returnType, function.getReturnType());
        assertEquals(body, function.getBody());
        assertEquals(1, function.getBody().size());
    }

    @Test
    public void testEmptyParameterList() {
        FunctionDefinition function = new FunctionDefinition(
                "noParamsFunction",
                Collections.emptyList(),
                "void",
                Collections.singletonList(new ReturnStatement(null))
        );

        assertTrue(function.getParameters().isEmpty());
        assertEquals(0, function.getParameters().size());
        assertEquals("void", function.getReturnType());
    }

    @Test
    public void testEmptyBody() {
        FunctionDefinition function = new FunctionDefinition(
                "emptyFunction",
                Arrays.asList(new Param("a", "int"), new Param("b", "int")),
                "void",
                Collections.emptyList()
        );

        assertTrue(function.getBody().isEmpty());
        assertEquals("void", function.getReturnType());
    }

    @Test
    public void testComplexFunctionBody() {
        List<Stmt> body = new ArrayList<>();
        body.add(new ValDeclaration("x", new LiteralIntExpr(10)));
        body.add(new ExpressionStatement(new CallExpr("print", List.of(new LiteralIntExpr(20)))));
        body.add(new ReturnStatement(new LiteralIntExpr(30)));

        FunctionDefinition function = new FunctionDefinition(
                "complexFunction",
                Collections.singletonList(new Param("input", "int")),
                "int",
                body
        );

        assertEquals(3, function.getBody().size());
        assertEquals("int", function.getReturnType());
        assertTrue(function.getBody().get(0) instanceof ValDeclaration);
        assertTrue(function.getBody().get(1) instanceof ExpressionStatement);
        ExpressionStatement printStmt = (ExpressionStatement) function.getBody().get(1);
        assertTrue(printStmt.getExpression() instanceof CallExpr);
        CallExpr callExpr = (CallExpr) printStmt.getExpression();
        assertEquals("print", callExpr.getCallee());
        assertTrue(function.getBody().get(2) instanceof ReturnStatement);
    }

    @Test
    public void testDirectInspection() {
        FunctionDefinition function = new FunctionDefinition(
                "testFunction",
                Arrays.asList(new Param("a", "int"), new Param("b", "string")),
                "boolean",
                Collections.singletonList(new ReturnStatement(new LiteralIntExpr(42)))
        );

        assertEquals("testFunction", function.getName());
        assertEquals(2, function.getParameters().size());
        assertEquals("int", function.getParameters().get(0).getType());
        assertEquals("string", function.getParameters().get(1).getType());
        assertEquals("boolean", function.getReturnType());
        assertEquals(1, function.getBody().size());
        assertTrue(function.getBody().get(0) instanceof ReturnStatement);

        ReturnStatement returnStmt = (ReturnStatement) function.getBody().getFirst();
        assertTrue(returnStmt.getExpression() instanceof LiteralIntExpr);
        assertEquals(42, ((LiteralIntExpr)returnStmt.getExpression()).getValue());
    }
}