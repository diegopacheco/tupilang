package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayLiteralExprTest {

    @Test
    public void testArrayLiteralExpr() {
        List<Expr> elements = Arrays.asList(
                new LiteralIntExpr(1),
                new LiteralIntExpr(2),
                new LiteralIntExpr(3)
        );
        ArrayLiteralExpr arrayLiteralExpr = new ArrayLiteralExpr(elements);
        assertEquals("[1, 2, 3]", arrayLiteralExpr.toString());
    }

}
