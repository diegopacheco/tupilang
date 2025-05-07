package com.github.diegopacheco.tupilang.std;

import java.util.List;

public interface StandardFunction {
    String getName();
    Object execute(Object... args);
}
