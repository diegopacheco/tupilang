package com.github.diegopacheco.tupilang.interpreter;

import com.github.diegopacheco.tupilang.ast.*;

import java.util.*;

public class Interpreter implements ExpressionVisitor<Object>, StatementVisitor<Void> {
    private final Map<String, Object> environment = new HashMap<>();
    private final Map<String, FunctionDefinition> functions = new HashMap<>();

    public void interpret(List<Stmt> statements) {
        for (Stmt statement : statements) {
            execute(statement);
        }
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    @Override
    public Void visitValDeclaration(ValDeclaration stmt) {
        Object value = null;
        if (stmt.getInitializer() != null) {
            value = evaluate(stmt.getInitializer());
        }
        environment.put(stmt.getName(), value);
        return null;
    }

    @Override
    public Void visitIfStatement(IfStatement stmt) {
        Object condition = evaluate(stmt.getCondition());

        if (isTruthy(condition)) {
            for (Stmt statement : stmt.getThenBranch()) {
                execute(statement);
            }
        } else if (stmt.hasElseBranch()) {
            for (Stmt statement : stmt.getElseBranch()) {
                execute(statement);
            }
        }
        return null;
    }

    private boolean isTruthy(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Boolean) return (Boolean) obj;
        if (obj instanceof Integer) return (Integer) obj != 0;
        return true;
    }

    @Override
    public Void visitPrintStatement(PrintStatement stmt) {
        Object value = evaluate(stmt.getExpression());
        System.out.println(stringify(value));
        return null;
    }

    private String stringify(Object obj) {
        return switch (obj) {
            case null -> "null";
            case Boolean b -> obj.toString();
            case Integer i -> obj.toString();
            case String s -> s;
            default -> obj.toString();
        };

    }

    @Override
    public Void visitReturnStatement(ReturnStatement stmt) {
        Object returnValue = evaluate(stmt.getExpression());
        return null;
    }

    @Override
    public Void visitFunctionDefinition(FunctionDefinition stmt) {
        functions.put(stmt.getName(), stmt);
        return null;
    }

    @Override
    public Object visitBinaryExpr(BinaryExpr expr) {
        Object left = evaluate(expr.getLeft());
        Object right = evaluate(expr.getRight());

        switch (expr.getOperator()) {
            case "+" -> {
                if (left instanceof Integer && right instanceof Integer) {
                    return (Integer) left + (Integer) right;
                }
                if (left instanceof String || right instanceof String) {
                    return left.toString() + right.toString();
                }
            }
            case "-" -> {
                if (left instanceof Integer && right instanceof Integer) {
                    return (Integer) left - (Integer) right;
                }
            }
            case "*" -> {
                if (left instanceof Integer && right instanceof Integer) {
                    return (Integer) left * (Integer) right;
                }
            }
            case "/" -> {
                if (left instanceof Integer && right instanceof Integer) {
                    return (Integer) left / (Integer) right;
                }
            }
        }

        if (expr.getOperator().equals("==")) {
            return Objects.equals(left, right);
        }

        throw new RuntimeException("Unsupported operation: " + expr.getOperator());
    }

    @Override
    public Object visitVariableExpr(VariableExpr expr) {
        if (!environment.containsKey(expr.getName())) {
            throw new RuntimeException("Undefined variable: " + expr.getName());
        }
        return environment.get(expr.getName());
    }

    @Override
    public Object visitLiteralIntExpr(LiteralIntExpr expr) {
        return expr.getValue();
    }

    @Override
    public Object visitLiteralStringExpr(LiteralStringExpr expr) {
        return expr.getValue();
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatement stmt) {
        evaluate(stmt.getExpression());
        return null;
    }

    @Override
    public Object visitCallExpr(CallExpr expr) {
        String callee = expr.getCallee();
        FunctionDefinition function = functions.get(callee);

        if (null==function) {
            throw new RuntimeException("Undefined function: " + callee);
        }

        List<Object> arguments = new ArrayList<>();
        for (Expr argument : expr.getArguments()) {
            arguments.add(evaluate(argument));
        }

        // Check argument count
        if (arguments.size() != function.getParameters().size()) {
            throw new RuntimeException("Expected " + function.getParameters().size() +
                    " arguments but got " + arguments.size());
        }

        // Create a new environment for the function execution
        Map<String, Object> oldEnvironment = new HashMap<>(environment);

        // Bind arguments to parameters
        for (int i = 0; i < arguments.size(); i++) {
            environment.put(function.getParameters().get(i).getName(), arguments.get(i));
        }

        // Execute function body
        Object returnValue = null;
        try {
            for (Stmt stmt : function.getBody()) {
                if (stmt instanceof ReturnStatement) {
                    returnValue = evaluate(((ReturnStatement) stmt).getExpression());
                    break;
                }
                execute(stmt);
            }
        } finally {
            // Restore environment
            environment.clear();
            environment.putAll(oldEnvironment);
        }

        return returnValue;
    }

    @Override
    public Object acceptLiteralBoolExpr(LiteralBoolExpr literalBoolExpr) {
        return literalBoolExpr.value;
    }
}