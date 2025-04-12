package com.github.diegopacheco.tupilang.lexer;

import com.github.diegopacheco.tupilang.token.Token;
import java.util.*;

public class Lexer {
    private final String input;
    private int pos = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char c = peek();
            if (Character.isWhitespace(c)) {
                advance();
            } else if (Character.isLetter(c)) {
                String word = readWhile(Character::isLetterOrDigit);
                switch (word) {
                    case "val" -> tokens.add(new Token(Token.Type.VAL, word));
                    case "def" -> tokens.add(new Token(Token.Type.DEF, word));
                    case "if" -> tokens.add(new Token(Token.Type.IF, word));
                    case "return" -> tokens.add(new Token(Token.Type.RETURN, word));
                    case "print" -> tokens.add(new Token(Token.Type.PRINT, word));
                    case "int" -> tokens.add(new Token(Token.Type.INT_TYPE, word));
                    case "void" -> tokens.add(new Token(Token.Type.VOID_TYPE, word));
                    default -> tokens.add(new Token(Token.Type.IDENTIFIER, word));
                }
            } else if (Character.isDigit(c)) {
                String num = readWhile(Character::isDigit);
                tokens.add(new Token(Token.Type.NUMBER, num, Integer.parseInt(num)));
            } else if (c == '"') {
                advance(); // skip opening "
                StringBuilder sb = new StringBuilder();
                while (pos < input.length() && peek() != '"') {
                    sb.append(peek());
                    advance();
                }
                if (pos >= input.length()) {
                    throw new RuntimeException("Unterminated string");
                }
                advance(); // skip closing "
                tokens.add(new Token(Token.Type.STRING, sb.toString(), sb.toString()));
            } else {
                switch (c) {
                    case '=' -> {
                        advance();
                        if (pos < input.length() && peek() == '=') {
                            advance();
                            tokens.add(new Token(Token.Type.EQEQ, "=="));
                        } else {
                            tokens.add(new Token(Token.Type.EQUAL, "="));
                        }
                    }
                    case '+' -> { tokens.add(new Token(Token.Type.PLUS, "+")); advance(); }
                    case '(' -> { tokens.add(new Token(Token.Type.LPAREN, "(")); advance(); }
                    case ')' -> { tokens.add(new Token(Token.Type.RPAREN, ")")); advance(); }
                    case '{' -> { tokens.add(new Token(Token.Type.LBRACE, "{")); advance(); }
                    case '}' -> { tokens.add(new Token(Token.Type.RBRACE, "}")); advance(); }
                    case ',' -> { tokens.add(new Token(Token.Type.COMMA, ",")); advance(); }
                    case ';' -> { tokens.add(new Token(Token.Type.SEMICOLON, ";")); advance(); }
                    case ':' -> { tokens.add(new Token(Token.Type.COLON, ":")); advance(); }
                    default -> throw new RuntimeException("Unexpected char: " + c);
                }
            }
        }
        tokens.add(new Token(Token.Type.EOF, ""));
        return tokens;
    }

    private char peek() {
        return input.charAt(pos);
    }

    private void advance() {
        pos++;
    }

    private String readWhile(java.util.function.Predicate<Character> predicate) {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && predicate.test(peek())) {
            sb.append(peek());
            advance();
        }
        return sb.toString();
    }
}