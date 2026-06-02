lexer grammar HTMLLexer;

@header{
    package antlr.html;
}

COMMENT
    : '<!--' .*? '-->' -> skip
    ;

// --------------------
// Jinja openings
// IMPORTANT:
// Must appear BEFORE TEXT
// --------------------

DOUBLE_OPEN_BRACE
    : '{{' -> pushMode(JINJA_EXPR)
    ;

OPEN_TAG
    : '{%' -> pushMode(JINJA_EXPR)
    ;

COMMENT_START
    : '{#' -> pushMode(JINJA_COMMENT)
    ;

// --------------------
// HTML whitespace
// --------------------

WS
    : (' ' | '\t' | '\r'? '\n')+ -> skip
    ;

// --------------------
// Script / Style
// --------------------

SCRIPT_OPEN
    : '<script' .*? '>' -> pushMode(SCRIPT)
    ;

STYLE_OPEN
    : '<style' .*? '>' -> pushMode(STYLE)
    ;

// --------------------
// HTML tag open
// --------------------

TAG_OPEN_HTML
    : '<' -> pushMode(TAG)
    ;

// --------------------
// HTML text
// IMPORTANT:
// stops on BOTH '<' and '{'
// --------------------

TEXT
    : (
        ~[<{]
        | '{' ~[{%#]
      )+
    ;

// ======================================================
// TAG MODE
// ======================================================

mode TAG;

TAG_CLOSE
    : '>' -> popMode
    ;

TAG_SLASH_CLOSE
    : '/>' -> popMode
    ;

TAG_SLASH
    : '/'
    ;

TAG_EQUALS
    : '=' -> pushMode(ATTVALUE)
    ;

TAG_ACCEPTED_NAME
    : 'html'
    | 'head'
    | 'body'
    | 'div'
    | 'span'
    | 'p'
    | 'ul'
    | 'li'
    | 'a'
    | 'img'
    | 'form'
    | 'input'
    | 'label'
    | 'h1'
    | 'h2'
    | 'h3'
    | 'meta'
    | 'title'
    | 'strong'
    | 'textarea'
    | 'link'
    | 'button'
    | 'section'
    | 'article'
    | 'nav'
    | 'footer'
    | 'header'
    | 'main'
    | 'aside'
    | 'figure'
    | 'figcaption'
    | 'video'
    | 'audio'
    | 'source'
    | 'iframe'
    | 'canvas'
    | 'svg'
    | 'path'
    | 'circle'
    | 'rect'
    | 'line'
    | 'polyline'
    | 'polygon'
    | 'g'
    | 'symbol'
    | 'use'
    | 'defs'
    | 'clipPath'
    | 'mask'
    | 'pattern'
    | 'small'
    ;

CHAR_NAME
    : NameStartChar NameChar*
    ;

TAG_WHITESPACE
    : [ \t\r\n] -> channel(HIDDEN)
    ;

// ======================================================
// SCRIPT MODE
// ======================================================

mode SCRIPT;

SCRIPT_BODY
    : .*? '</script>' -> popMode
    ;

SCRIPT_SHORT_BODY
    : .*? '</>' -> popMode
    ;

// ======================================================
// STYLE MODE
// ======================================================

mode STYLE;

STYLE_BODY
    : .*? '</style>' -> popMode
    ;

STYLE_SHORT_BODY
    : .*? '</>' -> popMode
    ;

// ======================================================
// ATTRIBUTE VALUE MODE
// ======================================================

mode ATTVALUE;

ATTVALUE_VALUE
    : ' '* ATTRIBUTE -> popMode
    ;

ATTRIBUTE
    : DOUBLE_QUOTE_STRING
    | SINGLE_QUOTE_STRING
    | ATTCHARS
    | HEXCHARS
    | DECCHARS
    ;

// ======================================================
// JINJA EXPR MODE
// ======================================================

mode JINJA_EXPR;

CLOSE_TAG
    : '%}' -> popMode
    ;

DOUBLE_CLOSE_BRACE
    : '}}' -> popMode
    ;

INCLUDE
    : 'include'
    ;

FOR
    : 'for'
    ;

IN
    : 'in'
    ;

ENDFOR
    : 'endfor'
    ;

IF
    : 'if'
    ;

ELIF
    : 'elif'
    ;

ELSE
    : 'else'
    ;

ENDIF
    : 'endif'
    ;

BLOCK
    : 'block'
    ;

ENDBLOCK
    : 'endblock'
    ;

MACRO
    : 'macro'
    ;

ENDMACRO
    : 'endmacro'
    ;

SET
    : 'set'
    ;

ENDSET
    : 'endset'
    ;

EXTENDS
    : 'extends'
    ;

RAW
    : 'raw'
    ;

ENDRAW
    : 'endraw'
    ;

IS
    : 'is'
    ;

OR
    : 'or'
    ;

AND
    : 'and'
    ;

NOT
    : 'not'
    ;

TRUE
    : 'true'
    ;

FALSE
    : 'false'
    ;

NONE
    : 'none'
    ;

PLUS
    : '+'
    ;

MINUS
    : '-'
    ;

STAR
    : '*'
    ;

SLASH
    : '/'
    ;

PERCENT
    : '%'
    ;

EQ
    : '=='
    ;

NEQ
    : '!='
    ;

LT
    : '<'
    ;

GT
    : '>'
    ;

LTE
    : '<='
    ;

GTE
    : '>='
    ;

ASSIGN
    : '='
    ;

PIPE
    : '|'
    ;

DOT
    : '.'
    ;

COMMA
    : ','
    ;

COLON
    : ':'
    ;

LPAREN
    : '('
    ;

RPAREN
    : ')'
    ;

LBRACK
    : '['
    ;

RBRACK
    : ']'
    ;

STRING
    : '"' ( ~["\\] | '\\' . )* '"'
    | '\'' ( ~['\\] | '\\' . )* '\''
    ;

NUMBER
    : [0-9]+ ('.' [0-9]+)?
    ;

ID
    : [a-zA-Z_][a-zA-Z0-9_]*
    ;

JINJA_WS
    : [ \t\r\n]+ -> skip
    ;

// ======================================================
// JINJA COMMENT MODE
// ======================================================

mode JINJA_COMMENT;

COMMENT_END
    : '#}' -> popMode
    ;

COMMENT_TEXT
    : . -> skip
    ;

// ======================================================
// FRAGMENTS
// ======================================================

fragment DIGIT
    : [0-9]
    ;

fragment HEXDIGIT
    : [a-fA-F0-9]
    ;

fragment NameChar
    : NameStartChar
    | '-'
    | '_'
    | '.'
    | DIGIT
    | '\u00B7'
    | '\u0300'..'\u036F'
    | '\u203F'..'\u2040'
    ;

fragment NameStartChar
    : [a-zA-Z:]
    | '\u2070'..'\u218F'
    | '\u2C00'..'\u2FEF'
    | '\u3001'..'\uD7FF'
    | '\uF900'..'\uFDCF'
    | '\uFDF0'..'\uFFFD'
    ;

fragment ATTCHARS
    : ATTCHAR+ ' '?
    ;

fragment ATTCHAR
    : '-'
    | '_'
    | '.'
    | '/'
    | '+'
    | ','
    | '?'
    | '='
    | ':'
    | ';'
    | '#'
    | [0-9a-zA-Z]
    ;

fragment HEXCHARS
    : '#' [0-9a-fA-F]+
    ;

fragment DECCHARS
    : [0-9]+ '%'?
    ;

fragment DOUBLE_QUOTE_STRING
    : '"' ~[<"]* '"'
    ;

fragment SINGLE_QUOTE_STRING
    : '\'' ~[<']* '\''
    ;