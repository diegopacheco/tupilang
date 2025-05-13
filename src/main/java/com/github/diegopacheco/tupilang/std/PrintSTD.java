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
        if (obj instanceof Boolean || obj instanceof Integer || obj instanceof String) {
            return obj.toString();
        }
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