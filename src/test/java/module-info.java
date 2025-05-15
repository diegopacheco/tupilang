module com.github.diegopacheco.tupilang.test {
    requires com.github.diegopacheco.tupilang;
    requires org.junit.jupiter.api;
    requires org.junit.platform.commons;

    // Change these to match your actual package structure
    opens com.github.diegopacheco.tupilang.test.ast to org.junit.jupiter.api;
    opens com.github.diegopacheco.tupilang.test.lexer to org.junit.jupiter.api;
    opens com.github.diegopacheco.tupilang.test.parser to org.junit.jupiter.api;
    opens com.github.diegopacheco.tupilang.test.repl to org.junit.jupiter.api;
    opens com.github.diegopacheco.tupilang.test.test.token to org.junit.jupiter.api;
    opens com.github.diegopacheco.tupilang.test.interpreter to org.junit.jupiter.api;
}