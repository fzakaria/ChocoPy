parser grammar ChocoPyParser;

options { tokenVocab=ChocoPyLexer; }

program
    : (var_def | func_def | class_def)* stmt* EOF
    ;

class_def
    : CLASS IDENTIFIER OPEN_PAREN IDENTIFIER CLOSE_PAREN COLON LINE_BREAK INDENT class_body DEDENT
    ;

class_body
    : PASS LINE_BREAK
    | (var_def | func_def)+
    ;

func_def
    : DEF IDENTIFIER OPEN_PAREN (typed_var (COMMA typed_var)*)? CLOSE_PAREN (ARROW type)? COLON LINE_BREAK INDENT func_body DEDENT
    ;

func_body
    : (global_decl | nonlocal_decl | var_def | func_def)* stmt+
    ;

typed_var
    : IDENTIFIER COLON type
    ;

type
    : IDENTIFIER | IDSTRING | OPEN_BRACKET type CLOSE_BRACKET
    ;

global_decl
    : GLOBAL IDENTIFIER LINE_BREAK
    ;

nonlocal_decl
    : NONLOCAL IDENTIFIER LINE_BREAK
    ;

var_def
    : typed_var ASSIGN literal LINE_BREAK
    ;

stmt
    : simple_stmt LINE_BREAK
    | IF expr COLON block (ELIF expr COLON block)* (ELSE COLON block)?
    | WHILE expr COLON block
    | FOR IDENTIFIER IN expr COLON block
    ;

simple_stmt
    : PASS
    | expr
    | RETURN (expr)?
    | (target ASSIGN)+ expr
    ;

block
    : LINE_BREAK INDENT stmt+ DEDENT
    ;

literal
    : NONE
    | TRUE
    | FALSE
    | INTEGER
    | IDSTRING
    | STRING
    ;

expr
    : cexpr
    | NOT expr
    | expr (AND | OR) expr
    | expr IF expr ELSE expr
    ;

cexpr
    : IDENTIFIER
    | literal
    | OPEN_BRACKET (expr (COMMA expr)*)? CLOSE_BRACKET
    | OPEN_PAREN expr CLOSE_PAREN
    | cexpr DOT IDENTIFIER
    | cexpr OPEN_BRACKET expr CLOSE_BRACKET
    | IDENTIFIER OPEN_PAREN (expr (COMMA expr)*)? CLOSE_PAREN
    | cexpr DOT IDENTIFIER OPEN_PAREN (expr (COMMA expr)*)? CLOSE_PAREN
    | cexpr bin_op cexpr
    | MINUS cexpr
    ;

bin_op
    : PLUS | MINUS | STAR | IDIV | MOD | EQUALS
    | BANG_EQUAL | LESS_THAN_EQUAL | GREATER_THAN_EQUAL | LESS_THAN | GREATER_THAN
    | IS
    ;


target
    : IDENTIFIER
    | cexpr DOT IDENTIFIER
    | cexpr OPEN_BRACKET expr CLOSE_BRACKET
    ;