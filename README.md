# ChocoPy

> [ChocoPy](https://chocopy.org/) is a restricted subset of Python 3, which can easily be compiled to a target such as RISC-V.

The language is [fully specified](https://chocopy.org/chocopy_language_reference.pdf) using formal
grammar, typing rules, and operational semantics.

The grammar however is **left recursive** and specified in EBNF.

This repository contains the [ANTLR4](https://www.antlr.org/) analag grammar to build the lexer & grammar.

## Development

* You'll need JDK11+

```bash
mvn clean install
```

The lexer & parser are _automatically_ generated using the ANTLR maven plugin.