package com.github.diegopacheco.tupilang.token;

import com.github.diegopacheco.tupilang.lexer.Lexer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TokenTest {

    @Test
    public void testIntTypeToken() {
        Token token = new Token(Token.Type.INT_TYPE, "Int", null, 1);
        assertEquals(Token.Type.INT_TYPE, token.type());
        assertEquals("Int", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testVoidTypeToken() {
        Token token = new Token(Token.Type.VOID_TYPE, "Void", null, 1);
        assertEquals(Token.Type.VOID_TYPE, token.type());
        assertEquals("Void", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testIfToken() {
        Token token = new Token(Token.Type.IF, "if", null, 1);
        assertEquals(Token.Type.IF, token.type());
        assertEquals("if", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testElseToken() {
        Token token = new Token(Token.Type.ELSE, "else", null, 1);
        assertEquals(Token.Type.ELSE, token.type());
        assertEquals("else", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testValToken() {
        Token token = new Token(Token.Type.VAL, "val", null, 1);
        assertEquals(Token.Type.VAL, token.type());
        assertEquals("val", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testDefToken() {
        Token token = new Token(Token.Type.DEF, "def", null, 1);
        assertEquals(Token.Type.DEF, token.type());
        assertEquals("def", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testReturnToken() {
        Token token = new Token(Token.Type.RETURN, "return", null, 1);
        assertEquals(Token.Type.RETURN, token.type());
        assertEquals("return", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testPrintToken() {
        Token token = new Token(Token.Type.PRINT, "print", null, 1);
        assertEquals(Token.Type.PRINT, token.type());
        assertEquals("print", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testCommaToken() {
        Token token = new Token(Token.Type.COMMA, ",", null, 1);
        assertEquals(Token.Type.COMMA, token.type());
        assertEquals(",", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testLeftParenToken() {
        Token token = new Token(Token.Type.LPAREN, "(", null, 1);
        assertEquals(Token.Type.LPAREN, token.type());
        assertEquals("(", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testRightParenToken() {
        Token token = new Token(Token.Type.RPAREN, ")", null, 1);
        assertEquals(Token.Type.RPAREN, token.type());
        assertEquals(")", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testLeftBraceToken() {
        Token token = new Token(Token.Type.LBRACE, "{", null, 1);
        assertEquals(Token.Type.LBRACE, token.type());
        assertEquals("{", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testRightBraceToken() {
        Token token = new Token(Token.Type.RBRACE, "}", null, 1);
        assertEquals(Token.Type.RBRACE, token.type());
        assertEquals("}", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testSemicolonToken() {
        Token token = new Token(Token.Type.SEMICOLON, ";", null, 1);
        assertEquals(Token.Type.SEMICOLON, token.type());
        assertEquals(";", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testPlusToken() {
        Token token = new Token(Token.Type.PLUS, "+", null, 1);
        assertEquals(Token.Type.PLUS, token.type());
        assertEquals("+", token.lexeme());
        assertEquals(1, token.line());
    }

}