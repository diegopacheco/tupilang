package com.github.diegopacheco.tupilang.std;

public class PrintSTD implements StandardFunction {

    @Override
    public String getName() {
        return "print";
    }

    @Override
    public Object execute(Object... args) {
        if (args == null || args.length == 0) {
            System.out.println();
        } else {
            System.out.println(stringify(args[0]));
        }
        return null;
    }

    private String stringify(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof Boolean) return obj.toString();
        if (obj instanceof Integer) return obj.toString();
        if (obj instanceof String) return (String) obj;
        return obj.toString();
    }
}