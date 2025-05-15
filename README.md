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
   * Type code and run on the fly
   * Proper help function
   * CTRL + D to exit or type `exit;`
   * Can run samples in `samples` folder just type `run`
   * `time` function to trace execution time (ms)
6. Tupilang So far supports:
 * Types: Int, String, Void, Bool, array represented by []
 * Keywords: if, return
 * Create functions with def, comments with //
 * String concatenation, Bool concatenation
 * Built-in functions: print, len
 * Val is immutable by default if want mutate keep using val
 * Val automatically detect types.
7. Removed by Design: 
  * No Enums
  * No Abstract classes
  * No Aspects
  * No Switch
  * No While
  * No Recursion
  * No Annoations 

Tupilang binary: jar size it's only **39KB** <br/>
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
:> def sum(a:Int, b:Int) Int {
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
:> run
Available samples:
  1. array.tupi
  2. bool.tupi
  3. comments.tupi
  4. concat_bool.tupi
  5. function.tupi
  6. ifs.tupi
  7. len.tupi
  8. main.tupi
  9. string_interpolation.tupi
Enter the number of the sample to run (or 0 to cancel): 5

--- Running sample: function.tupi ---
Source code:
def sum(a:Int, b:Int) int {
  print("Running function sum ");
  val result =  a + b;
  return result;
}
print(sum(52,8));
--- Output ---
"Running function sum "
60
--- End of sample ---

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
[INFO] Tests run: 130, Failures: 0, Errors: 0, Skipped: 0
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




