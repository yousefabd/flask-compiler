lexer grammar CSSLexer;

/* --- Symbols --- */
LBRACE  : '{' ;
RBRACE  : '}' ;
LBRACK  : '[' ;
RBRACK  : ']' ;
LPAREN  : '(' ;
RPAREN  : ')' ;
COLON   : ':' ;
SEMI    : ';' ;
COMMA   : ',' ;
DOT     : '.' ;
HASH    : '#' ;
STAR    : '*' ;
GT      : '>' ;
PLUS    : '+' ;
TILDE   : '~' ;
EQUAL   : '=' ;
PREFIX  : '^=' ;
SUFFIX  : '$=' ;
SUBSTR  : '*=' ;
DOUBLEDASH : '--' ;

/* --- Literals --- */
STRING
    : '"' (~["\\\r\n] | '\\' .)* '"'
    | '\'' (~['\\\r\n] | '\\' .)* '\''
    ;

NUMBER
    : [0-9]+ ('.' [0-9]+)?
    ;

PERCENT
    : NUMBER '%'
    ;

DIMENSION
    : NUMBER IDENT
    ;

HEX
    : '#' [0-9a-fA-F]{3,6}
    ;

/* --- Identifiers --- */
IDENT
    : [a-zA-Z_][a-zA-Z0-9_-]*
    ;

WS
    : [ \t\r\n]+
    ;

/* --- Important --- */
IMPORTANT
    : '!' WS* 'important'
    ;