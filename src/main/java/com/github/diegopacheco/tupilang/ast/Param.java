package com.github.diegopacheco.tupilang.ast;

public class Param {
    private final String name;
    private final String type;

    public Param(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}