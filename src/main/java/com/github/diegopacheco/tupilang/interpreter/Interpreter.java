package com.github.diegopacheco.tupilang.interpreter;

import com.github.diegopacheco.tupilang.ast.*;

import java.util.*;

public class Interpreter {
    private final Map<String, Object> environment = new HashMap<>();
    private final Map<String, FunctionDefinition> functions = new HashMap<>();

    public Object interpret(List<Stmt> statements) {
        Object lastValue = null;
        for (Stmt statement : statements) {
            lastValue = execute(statement);
        }
        return lastValue;
    }

    public Object execute(Stmt stmt) {
        if (stmt instanceof ValDeclaration val) {
            Object value = null;
            if (val.getInitializer() != null) {
                value = evaluate(val.getInitializer());
            }
            environment.put(val.getName(), value);
            return null;
        } else if (stmt instanceof PrintStatement print) {
            Object value = evaluate(print.getExpression());
            System.out.println(stringify(value));
            return null;
        } else if (stmt instanceof ExpressionStatement expr) {
            Object result = evaluate(expr.getExpression());
            if (expr.getExpression() instanceof VariableExpr) {
                System.out.println(stringify(result));
            }
            return result;
        } else if (stmt instanceof FunctionDefinition func) {
            functions.put(func.getName(), func);
            return null;
        } else if (stmt instanceof ReturnStatement ret) {
            return evaluate(ret.getExpression());
        } else if (stmt instanceof IfStatement ifStmt) {
            Object condition = evaluate(ifStmt.getCondition());

            if (isTruthy(condition)) {
                for (Stmt statement : ifStmt.getThenBranch()) {
                    execute(statement);
                }
            } else if (ifStmt.getElseBranch() != null) {
                for (Stmt statement : ifStmt.getElseBranch()) {
                    execute(statement);
                }
            }
            return null;
        }
        throw new RuntimeException("Unknown statement type: " + stmt.getClass().getName());
    }

    public Object evaluate(Expr expr) {
        if (expr instanceof LiteralIntExpr lit) {
            return lit.getValue();
        } else if (expr instanceof LiteralStringExpr lit) {
            return lit.getValue();
        } else if (expr instanceof LiteralBoolExpr lit) {
            return lit.value;
        } else if (expr instanceof VariableExpr var) {
            if (!environment.containsKey(var.getName())) {
                throw new RuntimeException("Undefined variable: " + var.getName());
            }
            return environment.get(var.getName());
        } else if (expr instanceof BinaryExpr binary) {
            return evaluateBinary(binary);
        } else if (expr instanceof CallExpr call) {
            return evaluateCall(call);
        }
        throw new RuntimeException("Unknown expression type: " + expr.getClass().getName());
    }

    private Object evaluateBinary(BinaryExpr expr) {
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
            case "==" -> {
                return Objects.equals(left, right);
            }
        }
        throw new RuntimeException("Unsupported operation: " + expr.getOperator());
    }

    private Object evaluateCall(CallExpr expr) {
        String callee = expr.getCallee();
        FunctionDefinition function = functions.get(callee);

        if (function == null) {
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

    private boolean isTruthy(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Boolean) return (Boolean) obj;
        if (obj instanceof Integer) return (Integer) obj != 0;
        return true;
    }

    private String stringify(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof Boolean) return obj.toString();
        if (obj instanceof Integer) return obj.toString();
        if (obj instanceof String) return (String) obj;
        return obj.toString();
    }
}