package com.github.diegopacheco.tupilang.test.repl;

import com.github.diegopacheco.tupilang.repl.REPL;
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
    public void testCommentsTupiFile() throws Exception {
        String fileContent = Files.readString(Path.of("samples/comments.tupi"));
        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("42"), "Output should contain the value 42");
    }

    @Test
    public void testBoolTupiFile() throws Exception {
        String fileContent = Files.readString(Path.of("samples/bool.tupi"));
        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("works"), "Output should contain the value works");
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

    @Test
    public void testLenStdfunc() throws Exception {
        String fileContent = Files.readString(Path.of("samples/len.tupi"));
        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("26"), "Output should contain '26'");
    }

    @Test
    public void testArrayTupiFile() throws Exception {
        String fileContent = Files.readString(Path.of("samples/array.tupi"));
        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("[1, 2, 3, 4, 5]"), "Output should contain the array representation");
        assertTrue(output.contains("1"), "Output should contain the first element of the array");
    }

    @Test
    public void testStringInterpolationTupiFile() throws Exception {
        String fileContent = Files.readString(Path.of("samples/string_interpolation.tupi"));
        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("Diego Pacheco"), "Output should contain 'Diego Pacheco'");
        assertTrue(output.contains("Tupi Lang"), "Output should contain 'Tupi  Lang'");
    }

    @Test
    public void testBoolConcatTupiFile() throws Exception {
        String fileContent = Files.readString(Path.of("samples/concat_bool.tupi"));
        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("true"), "Output should contain 'true'");
        assertTrue(output.contains("false"), "Output should contain 'false'");
    }

    @Test
    public void testControlFlowForTupiFile() throws Exception {
        String fileContent = Files.readString(Path.of("samples/control_flow_for.tupi"));
        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();

        // Modern Range-based for loop
        assertTrue(output.contains("Even number: 0"), "Output should contain 'Even number: 0'");
        assertTrue(output.contains("Odd number: 1"), "Output should contain 'Odd number: 1'");
        assertTrue(output.contains("Even number: 2"), "Output should contain 'Even number: 2'");
        assertTrue(output.contains("Odd number: 3"), "Output should contain 'Odd number: 3'");
        assertTrue(output.contains("Even number: 4"), "Output should contain 'Even number: 4'");
        assertTrue(output.contains("Odd number: 5"), "Output should contain 'Odd number: 5'");
        assertTrue(output.contains("Even number: 6"), "Output should contain 'Even number: 6'");
        assertTrue(output.contains("Odd number: 7"), "Output should contain 'Odd number: 7'");
        assertTrue(output.contains("Even number: 8"), "Output should contain 'Even number: 8'");
        assertTrue(output.contains("Odd number: 9"), "Output should contain 'Odd number: 9'");
        assertTrue(output.contains("Even number: 10"), "Output should contain 'Even number: 10'");

        // Classical traditional C-Style for loop
        assertTrue(output.contains("Number j: 0"), "Output should contain 'Number j: 0'");
        assertTrue(output.contains("Number j: 1"), "Output should contain 'Number j: 1'");
        assertTrue(output.contains("Number j: 2"), "Output should contain 'Number j: 2'");
        assertTrue(output.contains("Number j: 3"), "Output should contain 'Number j: 3'");
    }

    @Test
    public void testModuloTupiFile() throws Exception {
        String fileContent = Files.readString(Path.of("samples/modulo.tupi"));
        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("it's even number"), "Output should contain 'it's even number'");
        assertFalse(output.contains("it's odd number"), "Output should not contain 'it's odd number'");
    }

    @Test
    public void testPlusPlusMinusMinusTupiFile() throws Exception {
        String fileContent = Files.readString(Path.of("samples/plus_plus_minus_minus.tupi"));
        ByteArrayInputStream testInput = new ByteArrayInputStream((fileContent + "\nexit;\n").getBytes());
        System.setIn(testInput);

        REPL.run(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("10"), "Output should contain initial value '10'");
        assertTrue(output.contains("11"), "Output should contain incremented value '11'");
        assertTrue(output.contains("9"), "Output should contain decremented value '9'");
        assertTrue(output.contains("9"), "Output should contain decremented value '9'");
    }

}