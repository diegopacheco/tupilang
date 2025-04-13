package com.github.diegopacheco.tupilang.interpreter;

import com.github.diegopacheco.tupilang.ast.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {

    private Interpreter interpreter;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setup() {
        interpreter = new Interpreter();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testValDeclaration() {
        List<Stmt> program = Collections.singletonList(
            new ValDeclaration("x", new LiteralIntExpr(42))
        );

        interpreter.interpret(program);
    }

    @Test
    public void testPrintStatement() {
        List<Stmt> program = Collections.singletonList(
            new PrintStatement(new LiteralIntExpr(42))
        );

        interpreter.interpret(program);
        assertEquals("42", outputStream.toString().trim());
    }

    @Test
    public void testStringLiteral() {
        List<Stmt> program = Collections.singletonList(
            new PrintStatement(new LiteralStringExpr("hello"))
        );

        interpreter.interpret(program);
        assertEquals("hello", outputStream.toString().trim());
    }

    @Test
    public void testVariableAccess() {
        List<Stmt> program = Arrays.asList(
            new ValDeclaration("x", new LiteralIntExpr(42)),
            new PrintStatement(new VariableExpr("x"))
        );

        interpreter.interpret(program);
        assertEquals("42", outputStream.toString().trim());
    }

    @Test
    public void testBinaryExpression() {
        List<Stmt> program = Collections.singletonList(
            new PrintStatement(new BinaryExpr(
                new LiteralIntExpr(40),
                "+",
                new LiteralIntExpr(2)
            ))
        );

        interpreter.interpret(program);
        assertEquals("42", outputStream.toString().trim());
    }

    @Test
    public void testStringConcatenation() {
        List<Stmt> program = Collections.singletonList(
            new PrintStatement(new BinaryExpr(
                new LiteralStringExpr("hello "),
                "+",
                new LiteralStringExpr("world")
            ))
        );

        interpreter.interpret(program);
        assertEquals("hello world", outputStream.toString().trim());
    }

    @Test
    public void testIfStatement() {
        // val x = 10;
        // if (x == 10) {
        //   print "true branch";
        // } else {
        //   print "false branch";
        // }
        List<Stmt> program = new ArrayList<>();
        program.add(new ValDeclaration("x", new LiteralIntExpr(10)));

        List<Stmt> thenBranch = Collections.singletonList(
            new PrintStatement(new LiteralStringExpr("true branch"))
        );

        List<Stmt> elseBranch = Collections.singletonList(
            new PrintStatement(new LiteralStringExpr("false branch"))
        );

        program.add(new IfStatement(
            new BinaryExpr(
                new VariableExpr("x"),
                "==",
                new LiteralIntExpr(10)
            ),
            thenBranch,
            elseBranch
        ));

        interpreter.interpret(program);
        assertEquals("true branch", outputStream.toString().trim());
    }

    @Test
    public void testIfElseStatement() {
        // val x = 5;
        // if (x == 10) {
        //   print "true branch";
        // } else {
        //   print "false branch";
        // }
        List<Stmt> program = new ArrayList<>();
        program.add(new ValDeclaration("x", new LiteralIntExpr(5)));

        List<Stmt> thenBranch = Collections.singletonList(
            new PrintStatement(new LiteralStringExpr("true branch"))
        );

        List<Stmt> elseBranch = Collections.singletonList(
            new PrintStatement(new LiteralStringExpr("false branch"))
        );

        program.add(new IfStatement(
            new BinaryExpr(
                new VariableExpr("x"),
                "==",
                new LiteralIntExpr(10)
            ),
            thenBranch,
            elseBranch
        ));

        interpreter.interpret(program);
        assertEquals("false branch", outputStream.toString().trim());
    }

    @Test
    public void testFunction() {
        // fn add(a, b) -> int {
        //   return a + b;
        // }
        // print add(5, 3);
        List<Stmt> program = new ArrayList<>();

        // Function parameters
        List<Param> params = Arrays.asList(
            new Param("a", "int"),
            new Param("b", "int")
        );

        // Function body
        List<Stmt> body = Collections.singletonList(
            new ReturnStatement(new BinaryExpr(
                new VariableExpr("a"),
                "+",
                new VariableExpr("b")
            ))
        );

        // Function definition
        program.add(new FunctionDefinition("add", params, "int", body));

        // Function call
        List<Expr> arguments = Arrays.asList(
            new LiteralIntExpr(5),
            new LiteralIntExpr(3)
        );

        program.add(new PrintStatement(new CallExpr("add", arguments)));

        interpreter.interpret(program);
        assertEquals("8", outputStream.toString().trim());
    }

    @Test
    public void testNestedFunctionCall() {
        // fn add(a, b) -> int {
        //   return a + b;
        // }
        // fn multiply(x, y) -> int {
        //   return x * y;  // Assuming multiplication is supported
        // }
        // print add(2, multiply(3, 4));  // Should print 14

        // For this test, we'll simulate the result without multiplication
        // by directly returning 12 from the multiply function

        List<Stmt> program = new ArrayList<>();

        // add function
        List<Param> addParams = Arrays.asList(
            new Param("a", "int"),
            new Param("b", "int")
        );
        List<Stmt> addBody = Collections.singletonList(
            new ReturnStatement(new BinaryExpr(
                new VariableExpr("a"),
                "+",
                new VariableExpr("b")
            ))
        );
        program.add(new FunctionDefinition("add", addParams, "int", addBody));

        // multiply function - simplified to just return 12
        List<Param> multiplyParams = Arrays.asList(
            new Param("x", "int"),
            new Param("y", "int")
        );
        List<Stmt> multiplyBody = Collections.singletonList(
            new ReturnStatement(new LiteralIntExpr(12))
        );
        program.add(new FunctionDefinition("multiply", multiplyParams, "int", multiplyBody));

        // Call add(2, multiply(3, 4))
        List<Expr> multiplyArgs = Arrays.asList(
            new LiteralIntExpr(3),
            new LiteralIntExpr(4)
        );

        List<Expr> addArgs = Arrays.asList(
            new LiteralIntExpr(2),
            new CallExpr("multiply", multiplyArgs)
        );

        program.add(new PrintStatement(new CallExpr("add", addArgs)));

        interpreter.interpret(program);
        assertEquals("14", outputStream.toString().trim());
    }
}