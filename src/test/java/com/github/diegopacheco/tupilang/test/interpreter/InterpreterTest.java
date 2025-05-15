package com.github.diegopacheco.tupilang.test.interpreter;

import com.github.diegopacheco.tupilang.ast.*;
import com.github.diegopacheco.tupilang.interpreter.Interpreter;
import com.github.diegopacheco.tupilang.parser.Parser;
import com.github.diegopacheco.tupilang.token.Token;
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
    void testParsePrintStatement() {
        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.PRINT, "print", 1),
                new Token(Token.Type.STRING, "Hello, World!", 1),
                new Token(Token.Type.SEMICOLON, ";", 1),
                new Token(Token.Type.EOF, "", 1)
        );

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertTrue(statements.size() == 1);
        assertTrue(statements.get(0) instanceof ExpressionStatement);

        ExpressionStatement exprStmt = (ExpressionStatement)statements.get(0);
        assertTrue(exprStmt.getExpression() instanceof CallExpr);

        CallExpr callExpr = (CallExpr)exprStmt.getExpression();
        assertEquals("print", callExpr.getCallee());
        assertTrue(callExpr.getArguments().size() == 1);
        assertTrue(callExpr.getArguments().get(0) instanceof LiteralStringExpr);
    }

    @Test
    public void testStringLiteral() {
        List<Stmt> program = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(new LiteralStringExpr("hello"))))
        );

        interpreter.interpret(program);
        assertEquals("hello", outputStream.toString().trim());
    }

    @Test
    public void testVariableAccess() {
        List<Stmt> program = Arrays.asList(
                new ValDeclaration("x", new LiteralIntExpr(42)),
                new ExpressionStatement(new CallExpr("print", List.of(new VariableExpr("x"))))
        );

        interpreter.interpret(program);
        assertEquals("42", outputStream.toString().trim());
    }

    @Test
    public void testBinaryExpression() {
        List<Stmt> program = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(
                        new BinaryExpr(
                                new LiteralIntExpr(40),
                                "+",
                                new LiteralIntExpr(2)
                        )
                )))
        );

        interpreter.interpret(program);
        assertEquals("42", outputStream.toString().trim());
    }

    @Test
    public void testStringConcatenation() {
        List<Stmt> program = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(
                        new BinaryExpr(
                                new LiteralStringExpr("hello "),
                                "+",
                                new LiteralStringExpr("world")
                        )
                )))
        );

        interpreter.interpret(program);
        assertEquals("hello world", outputStream.toString().trim());
    }

    @Test
    public void testIfStatement() {
        // val x = 10;
        // if (x == 10) {
        //   print("true branch");
        // } else {
        //   print("false branch");
        // }
        List<Stmt> program = new ArrayList<>();
        program.add(new ValDeclaration("x", new LiteralIntExpr(10)));

        List<Stmt> thenBranch = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(
                        new LiteralStringExpr("true branch")
                )))
        );

        List<Stmt> elseBranch = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(
                        new LiteralStringExpr("false branch")
                )))
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
        //   print("true branch");
        // } else {
        //   print("false branch");
        // }
        List<Stmt> program = new ArrayList<>();
        program.add(new ValDeclaration("x", new LiteralIntExpr(5)));

        List<Stmt> thenBranch = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(
                        new LiteralStringExpr("true branch")
                )))
        );

        List<Stmt> elseBranch = Collections.singletonList(
                new ExpressionStatement(new CallExpr("print", List.of(
                        new LiteralStringExpr("false branch")
                )))
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
        // print(add(5, 3));
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

        program.add(new ExpressionStatement(new CallExpr("print", List.of(
                new CallExpr("add", arguments)
        ))));

        interpreter.interpret(program);
        assertEquals("8", outputStream.toString().trim());
    }

    @Test
    public void testNestedFunctionCall() {
        // fn add(a, b) -> int {
        //   return a + b;
        // }
        // fn multiply(x, y) -> int {
        //   return x * y;
        // }
        // print(add(2, multiply(3, 4)));  // Should print 14

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

        List<Param> multiplyParams = Arrays.asList(
                new Param("x", "int"),
                new Param("y", "int")
        );
        List<Stmt> multiplyBody = Collections.singletonList(
                new ReturnStatement(new LiteralIntExpr(12))
        );
        program.add(new FunctionDefinition("multiply", multiplyParams, "int", multiplyBody));

        List<Expr> multiplyArgs = Arrays.asList(
                new LiteralIntExpr(3),
                new LiteralIntExpr(4)
        );

        List<Expr> addArgs = Arrays.asList(
                new LiteralIntExpr(2),
                new CallExpr("multiply", multiplyArgs)
        );

        program.add(new ExpressionStatement(new CallExpr("print", List.of(
                new CallExpr("add", addArgs)
        ))));

        interpreter.interpret(program);
        assertEquals("14", outputStream.toString().trim());
    }

    @Test
    public void testBooleanAddition() {
        // Test for:
        // def concat(a: bool, b: bool) bool {
        //     return a + b;
        // }
        // print(concat(true, false));
        // print(concat(true, true));

        List<Stmt> program = new ArrayList<>();
        List<Param> params = Arrays.asList(
                new Param("a", "bool"),
                new Param("b", "bool")
        );

        List<Stmt> body = Collections.singletonList(
                new ReturnStatement(new BinaryExpr(
                        new VariableExpr("a"),
                        "+",
                        new VariableExpr("b")
                ))
        );

        program.add(new FunctionDefinition("concat", params, "bool", body));

        List<Expr> args1 = Arrays.asList(
                new LiteralBoolExpr(true),
                new LiteralBoolExpr(false)
        );
        program.add(new ExpressionStatement(new CallExpr("print", List.of(
                new CallExpr("concat", args1)
        ))));

        List<Expr> args2 = Arrays.asList(
                new LiteralBoolExpr(true),
                new LiteralBoolExpr(true)
        );
        program.add(new ExpressionStatement(new CallExpr("print", List.of(
                new CallExpr("concat", args2)
        ))));

        interpreter.interpret(program);

        String output = outputStream.toString().trim();
        assertTrue(output.contains("true"));
        assertTrue(output.contains("false"));

        String[] lines = output.split("\\R");
        assertEquals("true", lines[0]);
        assertEquals("false", lines[1]);
    }
}