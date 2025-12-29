lexer grammar Jinja2Lexer;
@header{
    package antlr.jinja2;
}

DOUBLE_OPEN_BRACE  : '{{';
DOUBLE_CLOSE_BRACE : '}}';

OPEN_TAG           : '{%';
CLOSE_TAG          : '%}';

COMMENT: '{#' .*? '#}' -> skip;

//RAW_TEXT
//  : ( ~[{]
//    | '{' ~[{%#]
//    )+
//  ;



INCLUDE  : 'include';
FOR      : 'for';
IN       : 'in';
ENDFOR   : 'endfor';
IF       : 'if';
ELIF     : 'elif';
ELSE     : 'else';
ENDIF    : 'endif';
BLOCK    : 'block';
ENDBLOCK : 'endblock';
MACRO    : 'macro';
ENDMACRO : 'endmacro';
SET      : 'set';
ENDSET   : 'endset';
EXTENDS  : 'extends';
RAW      : 'raw';
ENDRAW   : 'endraw';
IS       : 'is';
OR       : 'or';
AND      : 'and';
NOT      : 'not';
TRUE     : 'true';
FALSE    : 'false';
NONE     : 'none';


PLUS     : '+';
MINUS    : '-';
STAR     : '*';
SLASH    : '/';
PERCENT  : '%';
EQ       : '==';
NEQ      : '!=';
LT       : '<';
GT       : '>';
LTE      : '<=';
GTE      : '>=';

ASSIGN   : '=';
PIPE     : '|';
DOT      : '.';
COMMA    : ',';
COLON    : ':';
LPAREN   : '(';
RPAREN   : ')';
LBRACK   : '[';
RBRACK   : ']';


STRING   : '"' ( ~["\\] | '\\' . )* '"'
         | '\'' ( ~['\\] | '\\' . )* '\'';

NUMBER   : [0-9]+ ('.' [0-9]+)?;


ID       : [a-zA-Z_][a-zA-Z0-9_]*;


WS       : [ \t\r\n]+ -> skip;


TEXT     : .+? ;