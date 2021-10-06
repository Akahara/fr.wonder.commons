# fr.wonder.commons

This project contains a set of Utilities I use in most of my projects.

### Loggers

The `fr.wonder.commons.loggers` package contains a simple logger implementations, the one I use the most is the `AnsiLogger` that prints using the ansi codes to give color to the process output. Also loggers can be easily converted to streams using `Logger#asStream(int logLevel)`.

### Exceptions

Under `fr.wonder.commons.exceptions` are some exception classes and the `ErrorWrapper` class.

```java
// checked exceptions
class AssertionException extends Exception {...}
class CompilationException extends Exception {...}
class GenerationException extends Exception {...}
class ParsingException extends Exception {...}
class SerializationException extends IOException {...}

// unchecked exceptions
class RecursionThresholdExceededException extends Error {...}
class UnreachableException extends Error {...}
class UnimplementedException extends Error {...}
```

The `ErrorWrapper` class can be used to collect exceptions that should not interrupt the whole program but only a part of it, then all the repported errors can be collected at once:
```java
void main(String[] args) {
  ErrorWrapper errors = new ErrorWrapper("Some error occured");
  try {
    foo(errors.subErrors("Could not process #foo"));
    bar(errors.subErrors("Count not process #bar"));
    errors.assertNoErrors(); // will interrupt if #foo or #bar repported an error
    baz(errors.subErrors("Could not process #baz"));
    errors.subErrors();
  } catch (WrappedException e) {
    e.printStackTrace(); // or e.errors.dump(), e.errors.dump(logger); ...
  }
}
```
This utility is extensively used in my compiler (see the [AHKTranpiler](https://github.com/Akahara/AHKTranspiler)), it allows for one error not to stop others from being logged.
The `WrappedException` error is checked and contains a reference of the wrapper that caused the exception.

### Types

The `fr.wonder.commons.types` package contains some useful types:

```java
// basically a pointer
class Unit<T> {...}
// basically a tuple
class Tuple<A, B> {...}

// basically a runnable that can throw an error
class ERunnable<E> {
  	public void run() throws E;
}

// see the package for the full list
```

### Annotations

The `fr.wonder.commons.annotations` package contains annotations that are not retained at runtime, they can be used for readability purposes instead of comments.

```java
public @interface NonNull {}
public @interface Nullable {}
public @interface Constant {}
public @interface PseudoFinal {}
public @interface Overrideable {}
```

## Utilities

The `fr.wonder.commons.utils` package contains a set of utilities that can be used in many cases.

* ArrayOperator
  - A very handy class that contains methods to modify arrays without having to write loops everywhere:
  - `map`, `add`, `remove`, `addAt`, `removeIf`, `contains`, `indexOf`...
* MapOperator
  - Similar to `ArrayOperator`, less about removing loops and more about doing complex-ish computations.
* Predicates
  - A list of predicate functions, see the class for the full list.
* SortingUtils
  - Contains sorting functions for integers as well as arbitrary objects.
* StringUtils
  - Contains functions to make strings (`join`, `deepToString`, `stripContent`...), character sets (`alphabet`, `numbers`...), patterns (`word`, `string`...) and more complex functions (`splitWithQuotes`...).

## More & about

This library is currently built using java 9+, it should work with java 8 for the most part, you can safely use it as long as you include the project's licence in yours.
You can find more utilities in my common libraries ([math](https://github.com/Akahara/fr.wonder.commons.math) and [systems](https://github.com/Akahara/fr.wonder.commons.systems)).
