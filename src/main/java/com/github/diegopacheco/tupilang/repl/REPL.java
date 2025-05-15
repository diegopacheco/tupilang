package com.github.diegopacheco.tupilang.repl;

import com.github.diegopacheco.tupilang.interpreter.Interpreter;
import com.github.diegopacheco.tupilang.parser.Parser;
import com.github.diegopacheco.tupilang.lexer.Lexer;
import com.github.diegopacheco.tupilang.token.Token;
import com.github.diegopacheco.tupilang.ast.Stmt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class REPL {
    public static void run(String[] args) {
        printLogo();

        Scanner scanner = new Scanner(System.in);
        Interpreter interpreter = new Interpreter();
        StringBuilder input = new StringBuilder();
        boolean timeExecution = false;
        int openBraces = 0;

        while (true) {
            System.out.print(input.isEmpty() ? ":> " : "... ");
            String line;
            try {
                line = scanner.nextLine();
            } catch (NoSuchElementException e) {
                // This happens on CTRL+D (EOF)
                System.out.println("\nGoodbye!");
                break;
            }

            if (line.trim().equals("exit;")) {
                System.out.println("Goodbye!");
                break;
            }

            if (line.trim().equalsIgnoreCase("help")) {
                printHelp();
                continue;
            }

            if (line.trim().equalsIgnoreCase("time")) {
                timeExecution = true;
                System.out.println("Time measurement enabled for next execution");
                continue;
            }

            if (line.trim().equalsIgnoreCase("run")) {
                runSample(scanner, interpreter, true); // Always time the run command
                continue;
            }

            // Handle empty lines
            if (line.trim().isEmpty()) {
                continue;
            }

            // Add the line to input buffer regardless of whether it's a comment
            input.append(line).append("\n");

            // If it's a comment line, just continue without further processing
            if (line.trim().startsWith("//")) {
                continue;
            }

            // Count braces for multi-line statements
            for (char c : line.toCharArray()) {
                if (c == '{') openBraces++;
                if (c == '}') openBraces--;
            }

            // Execute when statement is complete
            if (openBraces == 0 && (line.trim().endsWith(";") || line.trim().endsWith("}"))) {
                try {
                    long startTime = 0;
                    if (timeExecution) {
                        startTime = System.nanoTime();
                    }

                    Lexer lexer = new Lexer(input.toString());
                    List<Token> tokens = lexer.scanTokens();
                    Parser parser = new Parser(tokens);
                    List<Stmt> statements = parser.parse();

                    Object result = executeStatements(interpreter, statements);
                    if (result != null) {
                        System.out.println(result);
                    }

                    if (timeExecution) {
                        long endTime = System.nanoTime();
                        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
                        System.out.println(String.format("Execution time: %.3f ms", executionTimeMs));
                        timeExecution = false;  // Reset the flag after execution
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    if (timeExecution) {
                        timeExecution = false;  // Reset the flag after error
                    }
                }
                input = new StringBuilder();
            }
        }
    }

    private static Object executeStatements(Interpreter interpreter, List<Stmt> statements) {
        Object result = null;
        for (Stmt stmt : statements) {
            result = interpreter.execute(stmt);
        }
        return result;
    }

    private static void runSample(Scanner scanner, Interpreter interpreter, boolean timeExecution) {
        File samplesDir = new File("samples");
        if (!samplesDir.exists() || !samplesDir.isDirectory()) {
            System.out.println("Error: '" + samplesDir + "' directory not found or is not a directory.");
            return;
        }

        File[] sampleFilesArray = samplesDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".tupi"));
        if (sampleFilesArray == null || sampleFilesArray.length == 0) {
            System.out.println("No sample files found in '" + samplesDir + "'.");
            return;
        }

        List<File> sampleFiles = new ArrayList<>(Arrays.asList(sampleFilesArray));
        sampleFiles.sort(File::compareTo);

        System.out.println("\nAvailable samples:");
        for (int i = 0; i < sampleFiles.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + sampleFiles.get(i).getName());
        }

        System.out.print("Enter the number of the sample to run (or 0 to cancel): ");
        String choiceStr;
        try {
            choiceStr = scanner.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println("\nOperation cancelled. Returning to REPL.");
            return;
        }

        try {
            int choice = Integer.parseInt(choiceStr);
            if (choice > 0 && choice <= sampleFiles.size()) {
                File selectedFile = sampleFiles.get(choice - 1);
                System.out.println("\n--- Running sample: " + selectedFile.getName() + " ---");
                String content = new String(Files.readAllBytes(selectedFile.toPath()));
                System.out.println("Source code:\n" + content);
                System.out.println("--- Output ---");

                long startTime = 0;
                if (timeExecution) {
                    startTime = System.nanoTime();
                }

                executeCode(content, interpreter);

                if (timeExecution) {
                    long endTime = System.nanoTime();
                    double executionTimeMs = (endTime - startTime) / 1_000_000.0;
                    System.out.printf("Execution time: %.3f ms%n", executionTimeMs);
                }

                System.out.println("--- End of sample ---");
            } else if (choice == 0) {
                System.out.println("Cancelled running sample.");
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (IOException e) {
            System.out.println("Error reading sample file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error executing sample: " + e.getMessage());
        }
        System.out.println();
    }

    private static void executeCode(String code, Interpreter interpreter) throws Exception {
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Object result = executeStatements(interpreter, statements);
        if (result != null) {
            System.out.println(result);
        }
    }

    private static void printHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  help     - Show this help message");
        System.out.println("  run      - Run a sample file from the 'samples' directory");
        System.out.println("  exit;    - Exit the REPL");
        System.out.println("  time     - Measure execution time of the next command");
        System.out.println("  <code>   - Enter Tupi code (end with ';' or '}' to execute)");
        System.out.println("\nAvailable lang features:");
        System.out.println("  - Types (int, string, bool, [])");
        System.out.println("  - Variables (val)");
        System.out.println("  - Functions (def, return)");
        System.out.println("  - Control flow (if, else)");
        System.out.println("  - Comments (// this is a comment)");
        System.out.println("  - Built-in functions: print(), len()");
    }

    private static void printLogo() {
        System.out.println(" _______             _   _                      ");
        System.out.println("|__   __|           (_) | |                     ");
        System.out.println("   | | _   _ _ __    _  | |     __ _ _ __   __ _ ");
        System.out.println("   | || | | | '_ \\  | | | |    / _` | '_ \\ / _` |");
        System.out.println("   | || |_| | |_) | | | | |___| (_| | | | | (_| |");
        System.out.println("   |_|\\__,_| .__/|  |_| |______\\__,_|_| |_|\\__, |");
        System.out.println("           | |                              __/ |");
        System.out.println("           |_|                             |___/ ");
        System.out.println();
        System.out.println("    Tupi Lang by Diego Pacheco                    ");
        System.out.println("  🌿 Version 1.0-SNAPSHOT - written in Java 23 🌿 ");
        System.out.println();
    }
}