lexer grammar ChocoPyLexer;

options { superClass=ChocoPyLexerBase; }

// Artificial tokens only for parser purposes
tokens { INDENT, DEDENT, LINE_BREAK }


DEF                : 'def';
RETURN             : 'return';
AS                 : 'as';
IF                 : 'if';
ELIF               : 'elif';
ELSE               : 'else';
WHILE              : 'while';
FOR                : 'for';
IN                 : 'in';
TRY                : 'try';
NONE               : 'None';
FINALLY            : 'finally';
WITH               : 'with';
EXCEPT             : 'except';
LAMBDA             : 'lambda';
OR                 : 'or';
AND                : 'and';
NOT                : 'not';
IS                 : 'is';
CLASS              : 'class';
PASS               : 'pass';
CONTINUE           : 'continue';
BREAK              : 'break';
TRUE               : 'True';
FALSE              : 'False';
NONLOCAL           : 'nonlocal';
GLOBAL             : 'global';

PLUS                : '+';
MINUS              : '-';
DOT                : '.';
COMMA              : ',';
ARROW              : '->';
BANG_EQUAL         : '!=';
EQUALS             : '==';
ASSIGN             : '=';
GREATER_THAN_EQUAL : '>=';
LESS_THAN_EQUAL    : '<=';
GREATER_THAN       : '>';
LESS_THAN          : '<';
MOD                : '%';
STAR               : '*';
IDIV               : '//';
COLON              : ':';

OPEN_PAREN         : '(';
CLOSE_PAREN        : ')';
OPEN_BRACKET       : '[';
CLOSE_BRACKET      : ']';

IDENTIFIER         : ID_START ID_CONTINUE*;

// By default, tokens are placed on the default channel (Token.DEFAULT_CHANNEL),
// but may be reassigned by using the ->channel(HIDDEN)
NEWLINE            : RN                {HandleNewLine();}  -> channel(HIDDEN);
WHITESPACE         : [ \t]+            {HandleSpaces();}   -> channel(HIDDEN);

INTEGER            : NON_ZERO_DIGIT DIGIT* | '0';

STRING
    : '"' ( ~["] )* '"'
    ;

IDSTRING
    : STRING
    ;

fragment RN
    : '\r'? '\n'
    ;

fragment NON_ZERO_DIGIT
    : [1-9]
    ;

fragment DIGIT
    : [0-9]
    ;

fragment ZERO
    : '0'
    ;

fragment ID_CONTINUE
    : ID_START
    | [0-9]
    ;

fragment ID_START
    : '_'
    | [A-Z]
    | [a-z]
    ;