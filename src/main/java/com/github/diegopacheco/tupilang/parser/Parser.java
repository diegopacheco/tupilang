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

            Expr expr = null;
            if (match(Token.Type.EQUAL)) {
                expr = parseExpression();
            }

            consume(Token.Type.SEMICOLON, "Expected ';'");
            return new ValDeclaration(name, expr);
        }

        if (match(Token.Type.FOR)) {
            return parseForStatement();
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
                    if (match(Token.Type.INT_TYPE)) {
                        type = "Int";
                    } else if (match(Token.Type.BOOL_TYPE)) {
                        type = "Bool";
                    } else if (match(Token.Type.VOID_TYPE)) {
                        type = "Void";
                    } else if (match(Token.Type.STRING)) {
                        type = "String";
                    } else if (match(Token.Type.IDENTIFIER)) {
                        type = previous().text;
                    } else {
                        throw new RuntimeException("Expected type");
                    }

                    params.add(new Param(paramName, type));
                } while (match(Token.Type.COMMA));
            }

            consume(Token.Type.RPAREN, "Expected ')'");

            String returnType;
            if (match(Token.Type.VOID_TYPE)) {
                returnType = "Void";
            } else if (match(Token.Type.INT_TYPE)) {
                returnType = "Int";
            } else if (match(Token.Type.BOOL_TYPE)) {
                returnType = "Bool";
            } else if (match(Token.Type.STRING)) {
                returnType = "String";
            } else if (match(Token.Type.IDENTIFIER)) {
                returnType = previous().text;
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
            List<Expr> arguments = new ArrayList<>();
            if (!check(Token.Type.SEMICOLON)) {
                arguments.add(parseExpression());
            }
            consume(Token.Type.SEMICOLON, "Expected ';' after value.");
            return new ExpressionStatement(new CallExpr("print", arguments));
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

    private Stmt parseForStatement() {
        consume(Token.Type.LPAREN, "Expected '(' after 'for'");

        if (match(Token.Type.INT_TYPE)) {
            String varName = consume(Token.Type.IDENTIFIER, "Expected variable name after 'Int'").text;

            if (match(Token.Type.COLON)) {
                // Range-based for loop: for (Int i: 0 to 10)
                Expr start = parseExpression();
                consume(Token.Type.TO, "Expected 'to' in range-based for loop");
                Expr end = parseExpression();
                consume(Token.Type.RPAREN, "Expected ')' after for loop condition");

                Stmt body = parseBlock();
                List<Stmt> statements = new ArrayList<>();
                if (body instanceof BlockStmt) {
                    statements = ((BlockStmt) body).getStatements();
                } else {
                    statements.add(body);
                }

                return new ForStatement(varName, start, end, statements);

            } else if (match(Token.Type.EQUAL)) {
                // Traditional C-style for loop: for (Int i=0; i<10; i++)
                Expr initializer = parseExpression();
                consume(Token.Type.SEMICOLON, "Expected ';' after for loop initializer");

                Expr condition = parseExpression();
                consume(Token.Type.SEMICOLON, "Expected ';' after for loop condition");

                // Handle both variable++ and more complex increments
                Expr increment;
                if (check(Token.Type.IDENTIFIER) && peekNext().type == Token.Type.PLUS_PLUS) {
                    varName = consume(Token.Type.IDENTIFIER, "Expected identifier").text;
                    consume(Token.Type.PLUS_PLUS, "Expected '++'");
                    increment = new UnaryExpr("++", new VariableExpr(varName), true);
                } else {
                    increment = parseExpression();
                }

                consume(Token.Type.RPAREN, "Expected ')' after for loop increment");

                Stmt body = parseBlock();
                List<Stmt> statements = new ArrayList<>();
                if (body instanceof BlockStmt) {
                    statements = ((BlockStmt) body).getStatements();
                } else {
                    statements.add(body);
                }

                return new ForStatement(varName, initializer, condition, increment, statements);
            }
            else {
                throw new RuntimeException("Expected ':' or '=' after variable name in for loop");
            }
        }

        throw new RuntimeException("Invalid for loop syntax");
    }

    private Stmt parseBlock() {
        List<Stmt> statements = new ArrayList<>();
        consume(Token.Type.LBRACE, "Expected '{' before block");

        while (!check(Token.Type.RBRACE) && !isAtEnd()) {
            statements.add(parseStatement());
        }

        consume(Token.Type.RBRACE, "Expected '}' after block");
        return new BlockStmt(statements);
    }

    private Expr parseExpression() {
        Expr expr = parseAssignment();

        // Handle post-increment/post-decrement
        if (match(Token.Type.PLUS_PLUS, Token.Type.MINUS_MINUS)) {
            String operator = previous().text;
            if (expr instanceof VariableExpr) {
                return new UnaryExpr(operator, expr, true); // true indicates postfix
            }
            throw new RuntimeException("Invalid increment/decrement target");
        }
        return expr;
    }

    private Expr parseAssignment() {
        Expr expr = parseOr();
        if (match(Token.Type.EQUAL)) {
            Expr value = parseAssignment();
            if (expr instanceof VariableExpr) {
                String name = ((VariableExpr)expr).getName(); // Use getter instead of direct access
                return new AssignExpr(name, value);
            }

            throw new RuntimeException("Invalid assignment target");
        }
        return expr;
    }

    private Expr parseOr() {
        Expr expr = parseAnd();
        while (match(Token.Type.OR)) {
            String operator = previous().text;
            Expr right = parseAnd();
            expr = new BinaryExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr parseAnd() {
        Expr expr = parseEquality();
        while (match(Token.Type.AND)) {
            String operator = previous().text;
            Expr right = parseEquality();
            expr = new BinaryExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr parseEquality() {
        Expr expr = parseComparison();
        while (match(Token.Type.EQEQ, Token.Type.NOT_EQUAL)) {
            String operator = previous().text;
            Expr right = parseComparison();
            expr = new BinaryExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr parseComparison() {
        Expr expr = parseAddition();
        while (match(Token.Type.LESS, Token.Type.LESS_EQUAL,
                Token.Type.GREATER, Token.Type.GREATER_EQUAL)) {
            String operator = previous().text;
            Expr right = parseAddition();
            expr = new BinaryExpr(expr, operator, right);
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
        while (match(Token.Type.STAR, Token.Type.SLASH, Token.Type.MODULO)) {
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
        if (match(Token.Type.LEFT_BRACKET)) {
            return parseArrayLiteral();
        }
        if (match(Token.Type.IDENTIFIER)) {
            String name = previous().text;

            // Check if this is a function call
            if (check(Token.Type.LPAREN)) {
                return parseCall(name);
            }

            // Create the variable expression
            Expr expr = new VariableExpr(name);

            // Handle array access after variable
            while (match(Token.Type.LEFT_BRACKET)) {
                Expr index = parseExpression();
                consume(Token.Type.RIGHT_BRACKET, "Expected ']' after array index");
                expr = new ArrayAccessExpr(expr, index);
            }

            return expr;
        }

        // Handle parenthesized expressions
        if (match(Token.Type.LPAREN)) {
            Expr expr = parseExpression();
            consume(Token.Type.RPAREN, "Expected ')'");

            // Handle array access after parenthesized expression
            while (match(Token.Type.LEFT_BRACKET)) {
                Expr index = parseExpression();
                consume(Token.Type.RIGHT_BRACKET, "Expected ']' after array index");
                expr = new ArrayAccessExpr(expr, index);
            }

            return expr;
        }

        throw new RuntimeException("Unexpected expression: " + peek().type + " " + peek().text);
    }

    private Expr parseArrayLiteral() {
        List<Expr> elements = new ArrayList<>();
        if (!check(Token.Type.RIGHT_BRACKET)) {
            do {
                elements.add(parseExpression());
            } while (match(Token.Type.COMMA));
        }
        consume(Token.Type.RIGHT_BRACKET, "Expect ']' after array elements.");
        return new ArrayLiteralExpr(elements);
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

    private Token peekNext() {
        if (pos + 1 >= tokens.size()) return tokens.get(tokens.size() - 1);
        return tokens.get(pos + 1);
    }
}