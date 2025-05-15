package com.github.diegopacheco.tupilang.test.std;

import com.github.diegopacheco.tupilang.std.PrintSTD;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PrintSTDTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private PrintSTD printSTD;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        printSTD = new PrintSTD();
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testGetName() {
        assertEquals("print", printSTD.getName());
    }

    @Test
    public void testPrintWithNoArguments() {
        printSTD.execute();
        assertEquals(System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testPrintWithNullArgument() {
        printSTD.execute(new Object[]{null});
        assertEquals("null" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testPrintWithStringArgument() {
        printSTD.execute("Hello, world!");
        assertEquals("Hello, world!" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testPrintWithIntegerArgument() {
        printSTD.execute(42);
        assertEquals("42" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testPrintWithBooleanArgument() {
        printSTD.execute(true);
        assertEquals("true" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testReturnValue() {
        Object result = printSTD.execute("test");
        assertNull(result);
    }
}