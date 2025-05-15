package com.github.diegopacheco.tupilang.lexer;

import com.github.diegopacheco.tupilang.token.Token;
import java.util.*;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, Token.Type> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("val", Token.Type.VAL);
        keywords.put("print", Token.Type.PRINT);
        keywords.put("if", Token.Type.IF);
        keywords.put("else", Token.Type.ELSE);
        keywords.put("def", Token.Type.DEF);
        keywords.put("return", Token.Type.RETURN);
        keywords.put("Int", Token.Type.INT_TYPE);
        keywords.put("Void", Token.Type.VOID_TYPE);
        keywords.put("Bool", Token.Type.BOOL_TYPE);
        keywords.put("String", Token.Type.STRING);
        keywords.put("true", Token.Type.TRUE);
        keywords.put("false", Token.Type.FALSE);
        keywords.put("for", Token.Type.FOR);
        keywords.put("to", Token.Type.TO);
    }

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(Token.Type.EOF, ""));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(Token.Type.LPAREN); break;
            case ')': addToken(Token.Type.RPAREN); break;
            case '{': addToken(Token.Type.LBRACE); break;
            case '}': addToken(Token.Type.RBRACE); break;
            case '[': addToken(Token.Type.LEFT_BRACKET); break;
            case ']': addToken(Token.Type.RIGHT_BRACKET); break;
            case ',': addToken(Token.Type.COMMA); break;
            case '.': addToken(Token.Type.DOT); break;
            case '+':
                if (peek() == '+') {
                    advance();
                    addToken(Token.Type.PLUS_PLUS);
                } else {
                    addToken(Token.Type.PLUS);
                }
                break;
            case '%':
                addToken(Token.Type.MODULO);
                break;
            case '|':
                if (match('|')) {
                    addToken(Token.Type.OR);
                } else {
                    addToken(Token.Type.PIPE);
                }
                break;
            case '*': addToken(Token.Type.STAR); break;
            case '-': addToken(Token.Type.MINUS); break;
            case ';': addToken(Token.Type.SEMICOLON); break;
            case ':': addToken(Token.Type.COLON); break;
            case '<':
                addToken(match('=') ? Token.Type.LESS_EQUAL : Token.Type.LESS);
                break;
            case '>':
                addToken(match('=') ? Token.Type.GREATER_EQUAL : Token.Type.GREATER);
                break;
            case '!':
                addToken(match('=') ? Token.Type.NOT_EQUAL : Token.Type.BANG);
                break;
            case '/':
                if (match('/')) {
                    // Comment goes until the end of the line
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                    // No token is added for comments
                } else {
                    addToken(Token.Type.SLASH);
                }
                break;
            case '=':
                addToken(match('=') ? Token.Type.EQEQ : Token.Type.EQUAL);
                break;
            // Handle whitespace
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace
                break;
            case '\n':
                line++;
                break;
            // Handle string literals
            case '"': string(); break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    // Unexpected character
                    System.err.println("Unexpected character: " + c);
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        Token.Type type = keywords.getOrDefault(text, Token.Type.IDENTIFIER);
        addToken(type);
    }

    private void number() {
        while (isDigit(peek())) advance();

        // Look for a decimal part
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the '.'
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(Token.Type.NUMBER,
                Integer.parseInt(source.substring(start, current)));
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        // Unterminated string
        if (isAtEnd()) {
            System.err.println("Unterminated string.");
            return;
        }

        // The closing "
        advance();

        // Trim the surrounding quotes
        String value = source.substring(start + 1, current - 1);
        addToken(Token.Type.STRING, value);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(Token.Type type) {
        addToken(type, null);
    }

    private void addToken(Token.Type type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal));
    }
}