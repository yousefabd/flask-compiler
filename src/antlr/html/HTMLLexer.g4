lexer grammar HTMLLexer;

// Comments
COMMENT: '<!--' .*? '-->'  -> skip;

// Normal text outside tags
WS: (' ' | '\t' | '\r'? '\n')+ -> skip;

SCRIPT_OPEN: '<script' .*? '>' -> pushMode(SCRIPT);
STYLE_OPEN:  '<style'  .*? '>' -> pushMode(STYLE);
TAG_OPEN: '<' -> pushMode(TAG);

TEXT: ~'<' + ;

mode TAG;

TAG_CLOSE: '>' -> popMode;
TAG_SLASH_CLOSE: '/>' -> popMode;
TAG_SLASH: '/';

TAG_EQUALS: '=' -> pushMode(ATTVALUE);

TAG_NAME: TAG_NameStartChar TAG_NameChar*;

TAG_WHITESPACE: [ \t\r\n] -> channel(HIDDEN);

fragment DIGIT: [0-9];
fragment HEXDIGIT: [a-fA-F0-9];

fragment TAG_NameChar:
      TAG_NameStartChar
    | '-'
    | '_'
    | '.'
    | DIGIT
    | '\u00B7'
    | '\u0300'..'\u036F'
    | '\u203F'..'\u2040'
;

fragment TAG_NameStartChar:
      [a-zA-Z:]
    | '\u2070'..'\u218F'
    | '\u2C00'..'\u2FEF'
    | '\u3001'..'\uD7FF'
    | '\uF900'..'\uFDCF'
    | '\uFDF0'..'\uFFFD'
;
mode SCRIPT;

SCRIPT_BODY: .*? '</script>' -> popMode;

SCRIPT_SHORT_BODY: .*? '</>' -> popMode;

mode STYLE;

STYLE_BODY: .*? '</style>' -> popMode;

STYLE_SHORT_BODY: .*? '</>' -> popMode;

mode ATTVALUE;

ATTR_VALUE_DOUBLE:
      '"' ~[<"]* '"'
    -> popMode
;

ATTR_VALUE_SINGLE:
      '\'' ~[<']* '\''
    -> popMode
;

ATTR_VALUE_UNQUOTED:
      [^\t\r\n >]+
    -> popMode
;
