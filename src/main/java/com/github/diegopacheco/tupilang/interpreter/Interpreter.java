package com.github.diegopacheco.tupilang.interpreter;

import com.github.diegopacheco.tupilang.ast.*;
import com.github.diegopacheco.tupilang.std.LenSTD;
import com.github.diegopacheco.tupilang.std.PrintSTD;
import com.github.diegopacheco.tupilang.std.StandardFunction;

import java.util.*;

public class Interpreter {
    private final Map<String, Object> environment = new HashMap<>();
    private final Map<String, FunctionDefinition> functions = new HashMap<>();
    private final Map<String, StandardFunction> stdFunctions = new HashMap<>();

    public Interpreter() {
        registerStandardFunction(new PrintSTD());
        registerStandardFunction(new LenSTD());
    }

    private void registerStandardFunction(StandardFunction function) {
        stdFunctions.put(function.getName(), function);
    }

    public void interpret(List<Stmt> statements) {
        Object lastValue = null;
        for (Stmt statement : statements) {
            lastValue = execute(statement);
        }
    }

    public Object execute(Stmt stmt) {
        if (stmt instanceof ValDeclaration val) {
            Object value = null;
            if (val.getInitializer() != null) {
                value = evaluate(val.getInitializer());
            }
            environment.put(val.getName(), value);
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
        else if (stmt instanceof ForStatement forStmt) {
            if (forStmt.isTraditional()) {
                // Traditional for loop
                // Initialize variable
                String varName = forStmt.getVarName();
                Object initialValue = evaluate(forStmt.getInitializer());
                environment.put(varName, initialValue);

                // Check condition, execute body, increment, repeat
                while (isTruthy(evaluate(forStmt.getCondition()))) {
                    // Execute all statements in the body
                    for (Stmt bodyStmt : forStmt.getBody()) {
                        execute(bodyStmt);
                    }

                    // Apply increment
                    if (forStmt.getIncrement() instanceof BinaryExpr increment) {
                        if (increment.getOperator().equals("+")) {
                            // i++ case
                            String incrementVarName = ((VariableExpr)increment.getLeft()).getName();
                            if (incrementVarName.equals(varName)) {
                                Object currentValue = environment.get(varName);
                                if (currentValue instanceof Integer) {
                                    environment.put(varName, (Integer)currentValue + 1);
                                } else {
                                    throw new RuntimeException("Can only increment integer variables in for loop");
                                }
                            }
                        } else if (increment.getOperator().equals("=")) {
                            // i = i + 1 case
                            String incrementVarName = ((VariableExpr)increment.getLeft()).getName();
                            if (incrementVarName.equals(varName)) {
                                environment.put(varName, evaluate(increment.getRight()));
                            }
                        }
                    }
                }
            } else {
                // Range-based for loop
                String varName = forStmt.getVarName();
                Object startObj = evaluate(forStmt.getInitializer());
                Object endObj = evaluate(forStmt.getCondition());

                if (!(startObj instanceof Integer) || !(endObj instanceof Integer)) {
                    throw new RuntimeException("Range-based for loop requires integer bounds");
                }

                int start = (Integer)startObj;
                int end = (Integer)endObj;

                for (int i = start; i <= end; i++) {
                    // Set loop variable
                    environment.put(varName, i);

                    // Execute all statements in the body
                    for (Stmt bodyStmt : forStmt.getBody()) {
                        execute(bodyStmt);
                    }
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
            return lit.getValue();
        } else if (expr instanceof VariableExpr var) {
            if (!environment.containsKey(var.getName())) {
                throw new RuntimeException("Undefined variable: " + var.getName());
            }
            return environment.get(var.getName());
        } else if (expr instanceof BinaryExpr binary) {
            return evaluateBinary(binary);
        } else if (expr instanceof CallExpr call) {
            return evaluateCall(call);
        } else if (expr instanceof ArrayLiteralExpr arrayLiteral) {
            List<Object> elements = new ArrayList<>();
            for (Expr element : arrayLiteral.getElements()) {
                elements.add(evaluate(element));
            }
            return elements.toArray();
        } else if (expr instanceof ArrayAccessExpr arrayAccess) {
            Object array = evaluate(arrayAccess.getArray());
            Object indexObj = evaluate(arrayAccess.getIndex());

            if (!(array instanceof Object[])) {
                throw new RuntimeException("Cannot use array access on non-array type");
            }

            if (!(indexObj instanceof Integer)) {
                throw new RuntimeException("Array index must be an integer");
            }

            int index = (Integer) indexObj;
            Object[] arrayValue = (Object[]) array;
            if (index < 0 || index >= arrayValue.length) {
                throw new RuntimeException("Array index out of bounds: " + index);
            }
            return arrayValue[index];
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
                    String leftStr = left.toString();
                    String rightStr = right.toString();

                    if (leftStr.startsWith("\"") && leftStr.endsWith("\"")) {
                        leftStr = leftStr.substring(1, leftStr.length() - 1);
                    }
                    if (rightStr.startsWith("\"") && rightStr.endsWith("\"")) {
                        rightStr = rightStr.substring(1, rightStr.length() - 1);
                    }
                    return leftStr + rightStr;
                }
                if (left instanceof Boolean && right instanceof Boolean) {
                    return (Boolean) left ^ (Boolean) right;
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
        StandardFunction stdFunc = stdFunctions.get(callee);
        if (stdFunc != null) {
            List<Object> arguments = new ArrayList<>();
            for (Expr argument : expr.getArguments()) {
                arguments.add(evaluate(argument));
            }
            return stdFunc.execute(arguments.toArray());
        }

        FunctionDefinition function = functions.get(callee);
        if (function == null) {
            throw new RuntimeException("Undefined function: " + callee);
        }

        List<Object> arguments = new ArrayList<>();
        for (Expr argument : expr.getArguments()) {
            arguments.add(evaluate(argument));
        }

        if (arguments.size() != function.getParameters().size()) {
            throw new RuntimeException("Expected " + function.getParameters().size() +
                    " arguments but got " + arguments.size());
        }

        Map<String, Object> oldEnvironment = new HashMap<>(environment);
        for (int i = 0; i < arguments.size(); i++) {
            environment.put(function.getParameters().get(i).getName(), arguments.get(i));
        }

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
        if (obj instanceof Object[] array) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < array.length; i++) {
                sb.append(stringify(array[i]));
                if (i < array.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            return sb.toString();
        }
        return obj.toString();
    }
}