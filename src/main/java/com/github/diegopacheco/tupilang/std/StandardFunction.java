package com.github.diegopacheco.tupilang.std;

public interface StandardFunction {
    String getName();
    Object execute(Object... args);
}
