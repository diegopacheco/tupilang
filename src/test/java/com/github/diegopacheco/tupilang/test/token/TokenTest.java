package com.github.diegopacheco.tupilang.test.token;

import com.github.diegopacheco.tupilang.lexer.Lexer;
import com.github.diegopacheco.tupilang.token.Token;
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

    @Test
    public void testMinusToken() {
        Token token = new Token(Token.Type.MINUS, "-", null, 1);
        assertEquals(Token.Type.MINUS, token.type());
        assertEquals("-", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testPlusPlusToken() {
        Token token = new Token(Token.Type.PLUS_PLUS, "++", null, 1);
        assertEquals(Token.Type.PLUS_PLUS, token.type());
        assertEquals("++", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testMinusMinusToken() {
        Token token = new Token(Token.Type.MINUS_MINUS, "--", null, 1);
        assertEquals(Token.Type.MINUS_MINUS, token.type());
        assertEquals("--", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testModuloToken() {
        Token token = new Token(Token.Type.MODULO, "%", null, 1);
        assertEquals(Token.Type.MODULO, token.type());
        assertEquals("%", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testPipeToken() {
        Token token = new Token(Token.Type.PIPE, "|", null, 1);
        assertEquals(Token.Type.PIPE, token.type());
        assertEquals("|", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testOrToken() {
        Token token = new Token(Token.Type.OR, "||", null, 1);
        assertEquals(Token.Type.OR, token.type());
        assertEquals("||", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testAndToken() {
        Token token = new Token(Token.Type.AND, "&&", null, 1);
        assertEquals(Token.Type.AND, token.type());
        assertEquals("&&", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testLessToken() {
        Token token = new Token(Token.Type.LESS, "<", null, 1);
        assertEquals(Token.Type.LESS, token.type());
        assertEquals("<", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testGreaterToken() {
        Token token = new Token(Token.Type.GREATER, ">", null, 1);
        assertEquals(Token.Type.GREATER, token.type());
        assertEquals(">", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testLessEqualToken() {
        Token token = new Token(Token.Type.LESS_EQUAL, "<=", null, 1);
        assertEquals(Token.Type.LESS_EQUAL, token.type());
        assertEquals("<=", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testGreaterEqualToken() {
        Token token = new Token(Token.Type.GREATER_EQUAL, ">=", null, 1);
        assertEquals(Token.Type.GREATER_EQUAL, token.type());
        assertEquals(">=", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testEqualToken() {
        Token token = new Token(Token.Type.EQUAL, "=", null, 1);
        assertEquals(Token.Type.EQUAL, token.type());
        assertEquals("=", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testRangeForToken() {
        Token token = new Token(Token.Type.TO, "TO", null, 1);
        assertEquals(Token.Type.TO, token.type());
        assertEquals("TO", token.lexeme());
        assertEquals(1, token.line());
    }

    @Test
    public void testBangToken() {
        Token token = new Token(Token.Type.BANG, "!", null, 1);
        assertEquals(Token.Type.BANG, token.type());
        assertEquals("!", token.lexeme());
        assertEquals(1, token.line());
    }

}