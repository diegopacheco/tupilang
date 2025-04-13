package com.github.diegopacheco.tupilang.token;

public class Token {

    public enum Type {
        // Single-character tokens
        LEFT_PAREN, RIGHT_PAREN, LPAREN, RPAREN, LBRACKET,
        LEFT_BRACE, RIGHT_BRACE, LBRACE, RBRACE, RBRACKET,
        COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR, COLON,

        // One or two character tokens
        BANG, BANG_EQUAL,
        EQUAL, EQUAL_EQUAL, EQEQ,
        GREATER, GREATER_EQUAL,
        LESS, LESS_EQUAL,

        // Literals
        IDENTIFIER, STRING, NUMBER,

        // Keywords
        IF, ELSE, VAL, DEF, RETURN, PRINT, TRUE, FALSE, COMMENT,

        // Types
        INT_TYPE, VOID_TYPE, BOOL_TYPE,

        ERROR, EOF
    }

    public final Type type;
    public final String text;
    public final Object literal;
    public int line = 1;

    public Token(Type type, String text) {
        this(type, text, null);
    }

    public Token(Type type, String text, Object literal) {
        this.type = type;
        this.text = text;
        this.literal = literal;
    }

    public Token(Type type, String anInt, Object o, int i) {
        this.type = type;
        this.text = anInt;
        this.literal = o;
        this.line = i;
    }

    public Type type() {
        return type;
    }

    public String lexeme() {
        return text;
    }

    public Object literal() {
        return literal;
    }

    public int line() {
        return line;
    }

    @Override
    public String toString() {
        return type + " " + text + (literal != null ? " " + literal : "");
    }
}