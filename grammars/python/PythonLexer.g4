lexer grammar PythonLexer;

@header{
    package antlr.python;
}
options {
    superClass = MidLexBase;
}
tokens {
    INDENT,
    DEDENT
}


GLOBAL : 'global';
DEF : 'def';
RETURN: 'return';
IMPORT: 'import';
IF: 'if';
ELIF: 'elif';
ELSE: 'else';
WHILE: 'while';
FOR: 'for';
TRY: 'try';
FINALLY: 'finally';
OR: 'or';
NOT: 'not';
AND: 'and';
TRUE: 'true';
PASS: 'pass';
FALSE: 'false';
CONTINUE: 'continue';
BREAK: 'break';
IN: 'in';
IS: 'is';
FROM: 'from';
NONE: 'None';

ARROW: '->';
POWER_ASSIGN: '**=';
ADD_ASSIGN: '+=';
SUB_ASSIGN: '-=';
MULT_ASSIGN: '*=';
DIV_ASSIGN: '/=';
MOD_ASSIGN: '%=';
AND_ASSIGN: '&=';
OR_ASSIGN: '|=';
XOR_ASSIGN: '^=';
LSHIFT_ASSIGN: '<<=';
RSHIFT_ASSIGN: '>>=';
IDIV_ASSIGN: '//=';
DOT: '.';
STAR: '*';
COMMA: ',';
COLON: ':';
SEMI_COLON: ';';
ASSIGN : '=';
OR_OP: '|';
XOR: '^';
AND_OP: '&';
LSHIFT: '<<';
RSHIFT: '>>';
POWER: '**';
ADD: '+';
MINUS: '-';
DIV: '/';
MOD: '%';
IDIV: '//';
NOT_OP: '~';
LESS_THAN: '<';
GREATER_THAN: '>';
EQUALS: '==';
GT_EQ: '>=';
LT_EQ: '<=';
NOT_EQ: '!=';
AT: '@';


STRING: SHORT_STRING | LONG_STRING;

INTEGER: [1-9][0-9]* | '0'+;
FLOAT: [0-9]+ '.' [0-9]+;

OPEN_PAREN    : '(' {this.openBrace();};
CLOSE_PAREN   : ')' {this.closeBrace();};
OPEN_BRACE    : '{' {this.openBrace();};
CLOSE_BRACE   : '}' {this.closeBrace();};
OPEN_BRACKET  : '[' {this.openBrace();};
CLOSE_BRACKET : ']' {this.closeBrace();};

ID : ID_START ID_CONTINUE*;

NEWLINE: ('\r'? '\n' | '\r' | '\f') SPACES?  {this.onNewLine();} ;
WS        : [ \t]+          -> channel(HIDDEN);
COMMENT   : '#' ~[\r\n\f]* -> channel(HIDDEN);

fragment SHORT_STRING:
    '\'' ('\\' (RN | .) | ~[\\\r\n'])* '\''
    | '"' ('\\' (RN | .) | ~[\\\r\n"])* '"'
;

fragment LONG_STRING: '\'\'\'' LONG_STRING_ITEM*? '\'\'\'' | '"""' LONG_STRING_ITEM*? '"""';

fragment LONG_STRING_ITEM: ~'\\' | '\\' (RN | .);

fragment RN: '\r'? '\n';
fragment ID_START : [a-zA-Z_];
fragment ID_CONTINUE : [a-zA-Z0-9_];
fragment SPACES:  [ \t]+;




