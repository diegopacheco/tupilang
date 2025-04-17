package com.github.diegopacheco.tupilang.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParamTest {

    @Test
    public void testParamCreation() {
        Param param = new Param("count", "Int");

        assertEquals("count", param.getName());
        assertEquals("Int", param.getType());
    }

    @Test
    public void testMultipleParams() {
        Param intParam = new Param("count", "Int");
        Param boolParam = new Param("enabled", "bool");
        Param stringParam = new Param("name", "string");

        assertEquals("count", intParam.getName());
        assertEquals("Int", intParam.getType());

        assertEquals("enabled", boolParam.getName());
        assertEquals("bool", boolParam.getType());

        assertEquals("name", stringParam.getName());
        assertEquals("string", stringParam.getType());
    }

    @Test
    public void testEmptyValues() {
        Param emptyName = new Param("", "Int");
        Param emptyType = new Param("param", "");
        Param bothEmpty = new Param("", "");

        assertEquals("", emptyName.getName());
        assertEquals("Int", emptyName.getType());

        assertEquals("param", emptyType.getName());
        assertEquals("", emptyType.getType());

        assertEquals("", bothEmpty.getName());
        assertEquals("", bothEmpty.getType());
    }

    @Test
    public void testNullHandling() {
        Param nullName = new Param(null, "Int");
        Param nullType = new Param("param", null);

        assertNull(nullName.getName());
        assertEquals("Int", nullName.getType());

        assertEquals("param", nullType.getName());
        assertNull(nullType.getType());
    }
}