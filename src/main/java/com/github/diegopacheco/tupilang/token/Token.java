package com.github.diegopacheco.tupilang.token;

public class Token {
    public enum Type {
        // Single-character tokens
        LEFT_PAREN, RIGHT_PAREN, LPAREN, RPAREN,
        LEFT_BRACE, RIGHT_BRACE, LBRACE, RBRACE,
        COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR, COLON,

        // One or two character tokens
        BANG, BANG_EQUAL,
        EQUAL, EQUAL_EQUAL, EQEQ,
        GREATER, GREATER_EQUAL,
        LESS, LESS_EQUAL,

        // Literals
        IDENTIFIER, STRING, NUMBER,

        // Keywords
        IF, ELSE, VAL, DEF, RETURN, PRINT, TRUE, FALSE,

        // Types
        INT_TYPE, VOID_TYPE,

        EOF
    }

    public final Type type;
    public final String text;
    public final Object literal;

    public Token(Type type, String text) {
        this(type, text, null);
    }

    public Token(Type type, String text, Object literal) {
        this.type = type;
        this.text = text;
        this.literal = literal;
    }

    @Override
    public String toString() {
        return type + " " + text + (literal != null ? " " + literal : "");
    }
}