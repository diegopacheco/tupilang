package com.github.diegopacheco.tupilang.ast;

public class ArrayAccessExpr implements Expr {
    private final Expr array;
    private final Expr index;

    public ArrayAccessExpr(Expr array, Expr index) {
        this.array = array;
        this.index = index;
    }

    public Expr getArray() {
        return array;
    }

    public Expr getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return array + "[" + index + "]";
    }
}