# Tupilang

<img src="tupilang-logo.png" />

### Rationale

This is a simple POC on how to create a toy programing language.
Interesting things here:
1. Uses Java 23
2. No libraries, No external dependencies
3. Heavily inspired by Scala 3
4. However, much more simple
5. REPL
6. So far supports:
 * Types: int, string, void, bool, array
 * Keywords: if, return
 * Create functions with def, comments with //
 * Built-in functions: print, len

Tupilang binary: jar size it's only **37KB** <br/>
Created by Diego Pacheco in APRIL/2025.

I wanted a simple version of Scala. But also simple and different. 

### Build 

```bash
./mvnw clean install package
```

### Run REPL

```
./repl.sh
```

### Result

```
❯ ./repl.sh
 _______             _   _
|__   __|           (_) | |
   | | _   _ _ __    _  | |     __ _ _ __   __ _
   | || | | | '_ \  | | | |    / _` | '_ \ / _` |
   | || |_| | |_) | | | | |___| (_| | | | | (_| |
   |_|\__,_| .__/|  |_| |______\__,_|_| |_|\__, |
           | |                              __/ |
           |_|                             |___/

    Tupi Lang by Diego Pacheco
  🌿 Version 1.0-SNAPSHOT - written in Java 23 🌿

:> val x = 10;
:> if (x == 10) {
  print(x);
}... ...
10
:> val xxx = "test";
:> print(xxx);
test
:> def sum(a:Int, b:Int) int {
  return a + b;
}... ...
:> print(sum(100,200));
300
:>
:> val x = 10;
:> val y = 20;
:> val name = "Diego";
:> def printAll() void {
... print(x);
... print(y);
... print(name);
... }
:> printAll();
10
20
Diego
:>

```

### Running .tupi files

```bash
./repl.sh samples/main.tupi
```

samples/main.tupi
```scala
val x = 10;
if (x == 10) {
  print(x);
}

val xxx = "test";
print(xxx);

def sum(a:Int, b:Int) int {
  return a + b;
}
print(sum(x,x));
```

result
```scala
10
test
20
```

### Tests

```bash
./mvnw clean test
```

```
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 127, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO]
```

### How it works?

<img src="tupi-how-it-worrks.png" />

The execution pipeline starts with the REPL, which reads code either typed by the user or from a 
file, and passes the raw source code to the lexer. The lexer scans the code and produces a 
list of tokens, which are stored in a token stream. This stream is then processed by the parser,
which builds an Abstract Syntax Tree (AST) that represents the structural and semantic 
hierarchy of the program. The AST is handed to the interpreter, which walks through it and
executes the logic, producing runtime effects like printed output, variable assignments, or 
function calls.

## Note on Visitor Pattern / Experiment

The current code is not using Visitor pattern, that's is by design and desire.

##### Centralization on Parser

All ASTs are handled from there. Could be all inside `parser` or `interpreter` classes.
It's keep in there for simplicity and also for debugability and is easier to follow the flow.
This is the current approach.

##### Distribution with Visitor Pattern

The victor pattern helps to add more logic on the AST side of the story.
However, is always the same `acept` method with no much logic. 
The issue IMHO is that the parser flow or options getting hidden across multiple classes
which IMHO gets harder to understand and even to maintain.




