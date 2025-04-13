package com.github.diegopacheco.tupilang.tupilang;

import com.github.diegopacheco.tupilang.tupilang.repl.REPL;

import com.github.diegopacheco.tupilang.tupilang.interpreter.Interpreter;
import com.github.diegopacheco.tupilang.tupilang.lexer.Lexer;
import com.github.diegopacheco.tupilang.tupilang.parser.Parser;
import com.github.diegopacheco.tupilang.tupilang.token.Token;
import com.github.diegopacheco.tupilang.tupilang.ast.Stmt;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].endsWith(".tupi")) {
            runFile(args[0]);
        } else {
            REPL.run(args);
        }
    }

    private static void runFile(String path) {
        try {
            String source = Files.readString(Paths.get(path));
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.scanTokens();
            Parser parser = new Parser(tokens);

            List<Stmt> statements = parser.parse();
            Interpreter interpreter = new Interpreter();
            interpreter.interpret(statements);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error executing program: " + e.getMessage());
            System.exit(1);
        }
    }
}