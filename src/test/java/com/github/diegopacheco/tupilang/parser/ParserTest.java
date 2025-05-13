package com.github.diegopacheco.tupilang.parser;

import com.github.diegopacheco.tupilang.ast.*;
import com.github.diegopacheco.tupilang.token.Token;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void testParseValDeclaration() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.VAL, "val", null, 1));
        tokens.add(new Token(Token.Type.IDENTIFIER, "x", "x", 1));
        tokens.add(new Token(Token.Type.EQUAL, "=", null, 1));
        tokens.add(new Token(Token.Type.NUMBER, "10", 10, 1));
        tokens.add(new Token(Token.Type.SEMICOLON, ";", null, 1));
        tokens.add(new Token(Token.Type.EOF, "", null, 1));

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertTrue(statements.get(0) instanceof ValDeclaration);

        ValDeclaration valDecl = (ValDeclaration) statements.get(0);
        assertEquals("x", valDecl.getName());
        assertTrue(valDecl.getInitializer() instanceof LiteralIntExpr);
    }

    @Test
    public void testParseIfStatement() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.IF, "if", null, 1));
        tokens.add(new Token(Token.Type.LPAREN, "(", null, 1));
        tokens.add(new Token(Token.Type.IDENTIFIER, "x", "x", 1));
        tokens.add(new Token(Token.Type.EQEQ, "==", null, 1));
        tokens.add(new Token(Token.Type.NUMBER, "10", 10, 1));
        tokens.add(new Token(Token.Type.RPAREN, ")", null, 1));
        tokens.add(new Token(Token.Type.LBRACE, "{", null, 1));
        tokens.add(new Token(Token.Type.PRINT, "print", null, 1));
        tokens.add(new Token(Token.Type.LPAREN, "(", null, 1));
        tokens.add(new Token(Token.Type.STRING, "x is 10", "x is 10", 1));
        tokens.add(new Token(Token.Type.RPAREN, ")", null, 1));
        tokens.add(new Token(Token.Type.SEMICOLON, ";", null, 1));
        tokens.add(new Token(Token.Type.RBRACE, "}", null, 1));
        tokens.add(new Token(Token.Type.EOF, "", null, 1));

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertTrue(statements.get(0) instanceof IfStatement);

        IfStatement ifStmt = (IfStatement) statements.get(0);
        assertTrue(ifStmt.getCondition() instanceof BinaryExpr);
        assertEquals(1, ifStmt.getThenBranch().size());
        assertNull(ifStmt.getElseBranch());
    }

    @Test
    public void testParseIfElseStatement() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.IF, "if", null, 1));
        tokens.add(new Token(Token.Type.LPAREN, "(", null, 1));
        tokens.add(new Token(Token.Type.IDENTIFIER, "x", "x", 1));
        tokens.add(new Token(Token.Type.EQEQ, "==", null, 1));
        tokens.add(new Token(Token.Type.NUMBER, "10", 10, 1));
        tokens.add(new Token(Token.Type.RPAREN, ")", null, 1));
        tokens.add(new Token(Token.Type.LBRACE, "{", null, 1));
        tokens.add(new Token(Token.Type.PRINT, "print", null, 1));
        tokens.add(new Token(Token.Type.LPAREN, "(", null, 1));
        tokens.add(new Token(Token.Type.STRING, "x is 10", "x is 10", 1));
        tokens.add(new Token(Token.Type.RPAREN, ")", null, 1));
        tokens.add(new Token(Token.Type.SEMICOLON, ";", null, 1));
        tokens.add(new Token(Token.Type.RBRACE, "}", null, 1));
        tokens.add(new Token(Token.Type.ELSE, "else", null, 1));
        tokens.add(new Token(Token.Type.LBRACE, "{", null, 1));
        tokens.add(new Token(Token.Type.PRINT, "print", null, 1));
        tokens.add(new Token(Token.Type.LPAREN, "(", null, 1));
        tokens.add(new Token(Token.Type.STRING, "x is not 10", "x is not 10", 1));
        tokens.add(new Token(Token.Type.RPAREN, ")", null, 1));
        tokens.add(new Token(Token.Type.SEMICOLON, ";", null, 1));
        tokens.add(new Token(Token.Type.RBRACE, "}", null, 1));
        tokens.add(new Token(Token.Type.EOF, "", null, 1));

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertTrue(statements.get(0) instanceof IfStatement);

        IfStatement ifStmt = (IfStatement) statements.get(0);
        assertTrue(ifStmt.getCondition() instanceof BinaryExpr);
        assertEquals(1, ifStmt.getThenBranch().size());
        assertNotNull(ifStmt.getElseBranch());
        assertEquals(1, ifStmt.getElseBranch().size());
    }

    @Test
    public void testParsePrintStatement() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.PRINT, "print", null, 1));
        tokens.add(new Token(Token.Type.LPAREN, "(", null, 1));
        tokens.add(new Token(Token.Type.STRING, "hello", "hello", 1));
        tokens.add(new Token(Token.Type.RPAREN, ")", null, 1));
        tokens.add(new Token(Token.Type.SEMICOLON, ";", null, 1));
        tokens.add(new Token(Token.Type.EOF, "", null, 1));

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertTrue(statements.get(0) instanceof ExpressionStatement);

        ExpressionStatement exprStmt = (ExpressionStatement) statements.get(0);
        assertTrue(exprStmt.getExpression() instanceof CallExpr);

        CallExpr callExpr = (CallExpr) exprStmt.getExpression();
        assertEquals("print", callExpr.getCallee());
        assertEquals(1, callExpr.getArguments().size());
        assertTrue(callExpr.getArguments().get(0) instanceof LiteralStringExpr);
    }

    @Test
    public void testParseFunctionDefinition() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.DEF, "def", null, 1));
        tokens.add(new Token(Token.Type.IDENTIFIER, "add", "add", 1));
        tokens.add(new Token(Token.Type.LPAREN, "(", null, 1));
        tokens.add(new Token(Token.Type.IDENTIFIER, "a", "a", 1));
        tokens.add(new Token(Token.Type.COLON, ":", null, 1));
        tokens.add(new Token(Token.Type.INT_TYPE, "Int", null, 1));
        tokens.add(new Token(Token.Type.COMMA, ",", null, 1));
        tokens.add(new Token(Token.Type.IDENTIFIER, "b", "b", 1));
        tokens.add(new Token(Token.Type.COLON, ":", null, 1));
        tokens.add(new Token(Token.Type.INT_TYPE, "Int", null, 1));
        tokens.add(new Token(Token.Type.RPAREN, ")", null, 1));
        tokens.add(new Token(Token.Type.INT_TYPE, "Int", null, 1));
        tokens.add(new Token(Token.Type.LBRACE, "{", null, 1));
        tokens.add(new Token(Token.Type.RETURN, "return", null, 1));
        tokens.add(new Token(Token.Type.IDENTIFIER, "a", "a", 1));
        tokens.add(new Token(Token.Type.PLUS, "+", null, 1));
        tokens.add(new Token(Token.Type.IDENTIFIER, "b", "b", 1));
        tokens.add(new Token(Token.Type.SEMICOLON, ";", null, 1));
        tokens.add(new Token(Token.Type.RBRACE, "}", null, 1));
        tokens.add(new Token(Token.Type.EOF, "", null, 1));

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertTrue(statements.get(0) instanceof FunctionDefinition);

        FunctionDefinition funcDef = (FunctionDefinition) statements.get(0);
        assertEquals("add", funcDef.getName());
        assertEquals(2, funcDef.getParameters().size());
        assertEquals("Int", funcDef.getReturnType());
        assertEquals(1, funcDef.getBody().size());
    }

    @Test
    public void testParseReturnStatement() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.RETURN, "return", null, 1));
        tokens.add(new Token(Token.Type.NUMBER, "42", 42, 1));
        tokens.add(new Token(Token.Type.SEMICOLON, ";", null, 1));
        tokens.add(new Token(Token.Type.EOF, "", null, 1));

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertTrue(statements.get(0) instanceof ReturnStatement);

        ReturnStatement returnStmt = (ReturnStatement) statements.get(0);
        assertTrue(returnStmt.getExpression() instanceof LiteralIntExpr);
    }

    @Test
    public void testParseFunctionCall() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.IDENTIFIER, "add", "add", 1));
        tokens.add(new Token(Token.Type.LPAREN, "(", null, 1));
        tokens.add(new Token(Token.Type.NUMBER, "5", 5, 1));
        tokens.add(new Token(Token.Type.COMMA, ",", null, 1));
        tokens.add(new Token(Token.Type.NUMBER, "10", 10, 1));
        tokens.add(new Token(Token.Type.RPAREN, ")", null, 1));
        tokens.add(new Token(Token.Type.SEMICOLON, ";", null, 1));
        tokens.add(new Token(Token.Type.EOF, "", null, 1));

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertTrue(statements.get(0) instanceof ExpressionStatement);

        ExpressionStatement exprStmt = (ExpressionStatement) statements.get(0);
        assertTrue(exprStmt.getExpression() instanceof CallExpr);

        CallExpr callExpr = (CallExpr) exprStmt.getExpression();
        assertEquals("add", callExpr.getCallee());
        assertEquals(2, callExpr.getArguments().size());
    }

    @Test
    public void testParseArrayLiteral() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.LEFT_BRACKET, "[", null, 1));
        tokens.add(new Token(Token.Type.NUMBER, "1", 1, 1));
        tokens.add(new Token(Token.Type.COMMA, ",", null, 1));
        tokens.add(new Token(Token.Type.NUMBER, "2", 2, 1));
        tokens.add(new Token(Token.Type.COMMA, ",", null, 1));
        tokens.add(new Token(Token.Type.NUMBER, "3", 3, 1));
        tokens.add(new Token(Token.Type.RIGHT_BRACKET, "]", null, 1));
        tokens.add(new Token(Token.Type.SEMICOLON, ";", null, 1));
        tokens.add(new Token(Token.Type.EOF, "", null, 1));

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        assertEquals(1, statements.size());
        assertTrue(statements.get(0) instanceof ExpressionStatement);

        ExpressionStatement exprStmt = (ExpressionStatement) statements.get(0);
        assertTrue(exprStmt.getExpression() instanceof ArrayLiteralExpr);

        ArrayLiteralExpr arrayExpr = (ArrayLiteralExpr) exprStmt.getExpression();
        List<Expr> elements = arrayExpr.getElements();

        assertEquals(3, elements.size());
        assertTrue(elements.get(0) instanceof LiteralIntExpr);
        assertEquals(1, ((LiteralIntExpr) elements.get(0)).getValue());
        assertTrue(elements.get(1) instanceof LiteralIntExpr);
        assertEquals(2, ((LiteralIntExpr) elements.get(1)).getValue());
        assertTrue(elements.get(2) instanceof LiteralIntExpr);
        assertEquals(3, ((LiteralIntExpr) elements.get(2)).getValue());

        assertEquals("[1, 2, 3]", arrayExpr.toString());
    }
}