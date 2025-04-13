package com.github.diegopacheco.tupilang.tupilang.repl;

import com.github.diegopacheco.tupilang.tupilang.interpreter.Interpreter;
import com.github.diegopacheco.tupilang.tupilang.parser.Parser;
import com.github.diegopacheco.tupilang.tupilang.lexer.Lexer;
import com.github.diegopacheco.tupilang.tupilang.token.Token;

import java.util.List;
import java.util.Scanner;

public class REPL {
    public static void run(String[] args) {
        printLogo();

        Scanner scanner = new Scanner(System.in);
        Interpreter interpreter = new Interpreter();
        StringBuilder input = new StringBuilder();
        int openBraces = 0;

        while (true) {
            System.out.print(input.isEmpty() ? ":> " : "... ");
            String line = scanner.nextLine().trim();

            if (line.equals("exit;")) {
                break;
            }

            // Count braces to track multi-line blocks
            for (char c : line.toCharArray()) {
                if (c == '{') openBraces++;
                if (c == '}') openBraces--;
            }

            input.append(line).append("\n");

            // Process input when all braces are balanced and we have a complete statement
            if (openBraces == 0 && (line.endsWith(";") || line.endsWith("}"))) {
                try {
                    Lexer lexer = new Lexer(input.toString());
                    List<Token> tokens = lexer.scanTokens();

                    Parser parser = new Parser(tokens);
                    interpreter.interpret(parser.parse());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                input = new StringBuilder();
            }
        }

        System.out.println("Goodbye!");
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
        System.out.println("");
        System.out.println("    Tupi Lang by Diego Pacheco                    ");
        System.out.println("  🌿 Version 1.0-SNAPSHOT - written in Java 23 🌿 ");
        System.out.println("");
    }
}