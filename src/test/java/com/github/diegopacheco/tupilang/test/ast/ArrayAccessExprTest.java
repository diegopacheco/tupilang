package com.github.diegopacheco.tupilang.test.ast;

import com.github.diegopacheco.tupilang.ast.ArrayAccessExpr;
import com.github.diegopacheco.tupilang.ast.Expr;
import com.github.diegopacheco.tupilang.ast.LiteralIntExpr;
import com.github.diegopacheco.tupilang.ast.VariableExpr;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayAccessExprTest {

    private ArrayAccessExprTest(){}

    @Test
    public void testArrayAccessExpr(){
        Expr array = new VariableExpr("myArray");
        Expr index = new LiteralIntExpr(2);
        ArrayAccessExpr arrayAccess = new ArrayAccessExpr(array, index);

        assertEquals("myArray[2]", arrayAccess.toString());
        assertEquals(array, arrayAccess.getArray());
        assertEquals(index, arrayAccess.getIndex());
    }

}