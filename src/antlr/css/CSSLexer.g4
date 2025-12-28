lexer grammar CSSLexer;

channels { ERROR }

// Punctuation
OpenBrace  : '{';
CloseBrace : '}';
Colon      : ':';
SemiColon  : ';';
Comma      : ',';
Dot        : '.';
Plus       : '+';
Minus      : '-';
Greater    : '>';
Tilde      : '~';

// Whitespace
WS : [ \t\r\n\f]+ -> skip;

// Comments
Comment : '/*' .*? '*/' -> skip;

// Keywords
Import    : '@' I M P O R T;
Media     : '@' M E D I A;
Important : '!' I M P O R T A N T;

// Identifiers
Ident : '-'? Nmstart Nmchar*;

// Numbers
Number : [0-9]+ | [0-9]* '.' [0-9]+;
Percentage : Number '%';

// Strings
String_
    : '"' (~[\n\r\f\\"])* '"'
    | '\'' (~[\n\r\f\\'])* '\''
    ;

// Fragments
fragment Nmstart : [_a-zA-Z];
fragment Nmchar  : [_a-zA-Z0-9\-];

// Letters
fragment A:'a'|'A'; fragment B:'b'|'B'; fragment C:'c'|'C';
fragment D:'d'|'D'; fragment E:'e'|'E'; fragment F:'f'|'F';
fragment G:'g'|'G'; fragment H:'h'|'H'; fragment I:'i'|'I';
fragment M:'m'|'M'; fragment N:'n'|'N'; fragment O:'o'|'O';
fragment P:'p'|'P'; fragment R:'r'|'R'; fragment T:'t'|'T';

// Catch-all
ERROR_CHAR : . -> channel(ERROR);
