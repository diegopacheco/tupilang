package com.github.diegopacheco.tupilang.repl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class REPLTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final ByteArrayInputStream inStream = new ByteArrayInputStream("exit;\n".getBytes());
    private final java.io.InputStream originalIn = System.in;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
        System.setIn(inStream);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testMainTupiFile() throws Exception {
        String fileContent = Files.readString(Path.of("samples/main.tupi"));
        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("10"), "Output should contain the value 10");
        assertTrue(output.contains("20"), "Output should contain the result of sum operation");
    }

    @Test
    public void testIfsTupiFile() throws Exception {
        String fileContent = Files.readString(Path.of("samples/ifs.tupi"));
        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Output should not be empty");
    }

    @Test
    public void testFunctionTupiFile() throws Exception {
        String fileContent = Files.readString(Path.of("samples/function.tupi"));

        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("60"), "Output should contain the result of function call");
        assertTrue(output.contains("Running function sum "), "Output should contain print message");
    }

    @Test
    public void testSimpleHelloWorld() {
        ByteArrayInputStream testInput = new ByteArrayInputStream("print(\"hello world tupi\");\nexit;\n".getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("hello world tupi"), "Output should contain 'hello world tupi'");
    }
}