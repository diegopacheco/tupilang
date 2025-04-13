package com.github.diegopacheco.tupilang.parser;

import com.github.diegopacheco.tupilang.ast.*;
import com.github.diegopacheco.tupilang.token.Token;
import java.util.*;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            try {
                // Skip comments
                if (match(Token.Type.COMMENT)) {
                    continue;
                }
                statements.add(parseStatement());
            } catch (Exception e) {
                // Skip to next statement on error
                synchronize();
                if (!isAtEnd()) {
                    System.err.println("Error: " + e.getMessage());
                } else {
                    throw e;
                }
            }
        }
        return statements;
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == Token.Type.SEMICOLON) return;
            switch (peek().type) {
                case VAL, PRINT, IF, DEF, RETURN:
                    return;
            }
            advance();
        }
    }

    private Stmt parseStatement() {
        if (match(Token.Type.VAL)) {
            String name = consume(Token.Type.IDENTIFIER, "Expected identifier").text;

            // Support optional initialization
            Expr expr = null;
            if (match(Token.Type.EQUAL)) {
                expr = parseExpression();
            }

            consume(Token.Type.SEMICOLON, "Expected ';'");
            return new ValDeclaration(name, expr);
        }

        if (match(Token.Type.IF)) {
            consume(Token.Type.LPAREN, "Expected '('");
            Expr condition = parseExpression();
            consume(Token.Type.RPAREN, "Expected ')'");

            // Parse then branch
            List<Stmt> thenBody = new ArrayList<>();
            if (match(Token.Type.LBRACE)) {
                // Block of statements
                while (!check(Token.Type.RBRACE) && !isAtEnd()) {
                    thenBody.add(parseStatement());
                }
                consume(Token.Type.RBRACE, "Expected '}'");
            } else {
                // Single statement
                thenBody.add(parseStatement());
            }

            // Parse optional else branch
            List<Stmt> elseBody = null;
            if (match(Token.Type.ELSE)) {
                elseBody = new ArrayList<>();
                if (match(Token.Type.LBRACE)) {
                    // Block of statements
                    while (!check(Token.Type.RBRACE) && !isAtEnd()) {
                        elseBody.add(parseStatement());
                    }
                    consume(Token.Type.RBRACE, "Expected '}'");
                } else {
                    // Single statement
                    elseBody.add(parseStatement());
                }
            }

            return new IfStatement(condition, thenBody, elseBody);
        }

        if (match(Token.Type.PRINT)) {
            consume(Token.Type.LPAREN, "Expected '('");
            Expr expr = parseExpression();
            consume(Token.Type.RPAREN, "Expected ')'");
            consume(Token.Type.SEMICOLON, "Expected ';'");
            return new PrintStatement(expr);
        }

        if (match(Token.Type.DEF)) {
            String name = consume(Token.Type.IDENTIFIER, "Expected function name").text;
            consume(Token.Type.LPAREN, "Expected '('");

            List<Param> params = new ArrayList<>();
            if (!check(Token.Type.RPAREN)) {
                do {
                    String paramName = consume(Token.Type.IDENTIFIER, "Expected param name").text;
                    consume(Token.Type.COLON, "Expected ':'");

                    // Parse parameter type
                    String type;
                    if (check(Token.Type.INT_TYPE) ||
                            (check(Token.Type.IDENTIFIER) && peek().text.equalsIgnoreCase("Int"))) {
                        type = advance().text;
                    } else {
                        throw new RuntimeException("Expected type");
                    }

                    params.add(new Param(paramName, type));
                } while (match(Token.Type.COMMA));
            }

            consume(Token.Type.RPAREN, "Expected ')'");

            // Parse return type - support void type
            String returnType;
            if (check(Token.Type.VOID_TYPE)) {
                returnType = advance().text;
            } else if (check(Token.Type.INT_TYPE)) {
                returnType = advance().text;
            } else if (check(Token.Type.BOOL_TYPE)) {
                returnType = advance().text;
            } else if (check(Token.Type.IDENTIFIER) && peek().text.equalsIgnoreCase("Int")) {
                returnType = advance().text;
            } else {
                throw new RuntimeException("Expected return type");
            }

            consume(Token.Type.LBRACE, "Expected '{'");

            List<Stmt> body = new ArrayList<>();
            while (!check(Token.Type.RBRACE) && !isAtEnd()) {
                body.add(parseStatement());
            }

            consume(Token.Type.RBRACE, "Expected '}'");
            return new FunctionDefinition(name, params, returnType, body);
        }

        if (match(Token.Type.RETURN)) {
            Expr expr = parseExpression();
            consume(Token.Type.SEMICOLON, "Expected ';'");
            return new ReturnStatement(expr);
        }

        // Handle expressions as statements (like function calls)
        Expr expr = parseExpression();
        consume(Token.Type.SEMICOLON, "Expected ';' after expression");
        return new ExpressionStatement(expr);
    }

    private Expr parseExpression() {
        return parseEquality();
    }

    private Expr parseEquality() {
        Expr expr = parseAddition();

        if (match(Token.Type.EQEQ)) {
            String op = previous().text;
            Expr right = parseAddition();
            expr = new BinaryExpr(expr, op, right);
        }

        return expr;
    }

    private Expr parseAddition() {
        Expr expr = parseMultiplication();

        while (match(Token.Type.PLUS, Token.Type.MINUS)) {
            String op = previous().text;
            Expr right = parseMultiplication();
            expr = new BinaryExpr(expr, op, right);
        }

        return expr;
    }

    private Expr parseMultiplication() {
        Expr expr = parsePrimary();

        while (match(Token.Type.STAR, Token.Type.SLASH)) {
            String op = previous().text;
            Expr right = parsePrimary();
            expr = new BinaryExpr(expr, op, right);
        }

        return expr;
    }

    private Expr parsePrimary() {
        if (match(Token.Type.NUMBER)) {
            String numberText = previous().text;
            return new LiteralIntExpr(Integer.parseInt(numberText));
        }
        if (match(Token.Type.STRING)) {
            return new LiteralStringExpr(previous().text);
        }
        if (match(Token.Type.TRUE)) {
            return new LiteralBoolExpr(true);
        }
        if (match(Token.Type.FALSE)) {
            return new LiteralBoolExpr(false);
        }
        if (match(Token.Type.IDENTIFIER)) {
            String name = previous().text;

            // Check if this is a function call
            if (check(Token.Type.LPAREN)) {
                return parseCall(name);
            }
            return new VariableExpr(name);
        }
        throw new RuntimeException("Unexpected expression: " + peek());
    }

    private Expr parseCall(String callee) {
        consume(Token.Type.LPAREN, "Expected '(' after function name");
        List<Expr> arguments = new ArrayList<>();

        if (!check(Token.Type.RPAREN)) {
            do {
                arguments.add(parseExpression());
            } while (match(Token.Type.COMMA));
        }

        consume(Token.Type.RPAREN, "Expected ')' after arguments");
        return new CallExpr(callee, arguments);
    }

    private boolean match(Token.Type... types) {
        for (Token.Type type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(Token.Type type) {
        return !isAtEnd() && peek().type == type;
    }

    private Token consume(Token.Type type, String message) {
        if (check(type)) return advance();
        throw new RuntimeException(message + ", found: " + peek().type + " " + peek().text);
    }

    private boolean isAtEnd() {
        return peek().type == Token.Type.EOF;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token previous() {
        return tokens.get(pos - 1);
    }

    private Token advance() {
        if (!isAtEnd()) pos++;
        return previous();
    }
}