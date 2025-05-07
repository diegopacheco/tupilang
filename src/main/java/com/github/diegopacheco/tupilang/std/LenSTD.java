package com.github.diegopacheco.tupilang.std;

public class LenSTD implements StandardFunction {

    @Override
    public String getName() {
        return "len";
    }


    @Override
    public Object execute(Object... args) {
        if (args.length == 0) {
            throw new RuntimeException("len() requires an argument");
        }

        Object arg = args[0];
        if (arg == null) {
            return 0;
        }

        if (arg instanceof String str) {
            // Remove quotes if the string still has them
            if (str.startsWith("\"") && str.endsWith("\"")) {
                return str.length() - 2; // Subtract the two quote characters
            }
            return str.length();
        }

        if (arg instanceof Object[] array) {
            return array.length;
        }

        if (!(arg instanceof String)) {
            throw new RuntimeException("len() requires a string argument");
        }

        return arg.toString().length();
    }
}