package com.github.diegopacheco.tupilang.test.std;

import com.github.diegopacheco.tupilang.std.LenSTD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LenSTDTest {

    private LenSTD lenSTD;

    @BeforeEach
    public void setUp() {
        lenSTD = new LenSTD();
    }

    @Test
    public void testGetName() {
        assertEquals("len", lenSTD.getName());
    }

    @Test
    public void testLenWithEmptyString() {
        Object result = lenSTD.execute("");
        assertEquals(0, result);
    }

    @Test
    public void testLenWithNonEmptyString() {
        Object result = lenSTD.execute("Hello");
        assertEquals(5, result);
    }

    @Test
    public void testLenWithSpecialCharacters() {
        Object result = lenSTD.execute("!@#$%^&*()");
        assertEquals(10, result);
    }

    @Test
    public void testLenWithUnicodeCharacters() {
        Object result = lenSTD.execute("こんにちは");
        assertEquals(5, result);
    }

    @Test
    public void testLenWithNull() {
        assertThrows(RuntimeException.class, () -> lenSTD.execute((Object[])null));
    }

    @Test
    public void testLenWithNoArguments() {
        assertThrows(RuntimeException.class, () -> lenSTD.execute());
    }

    @Test
    public void testLenWithNonStringArgument() {
        assertThrows(RuntimeException.class, () -> lenSTD.execute(123));
    }
}