parser grammar Jinja2Parser;

@header{
    package antlr.jinja2;
}
options { tokenVocab=Jinja2Lexer; }

// ===== Top-level =====

template
    : (variable | block_stmt | inline_stmt | COMMENT)* | EOF
    ;

// ===== Variables =====

variable
    : DOUBLE_OPEN_BRACE MINUS? expr MINUS? DOUBLE_CLOSE_BRACE
    ;

// ===== Statements (tags) =====

block_stmt
    : for_block
    | if_block
    | set_block
    | macro_block
    | block_block
    ;

inline_stmt
    : extends_stmt
    | include_stmt
    | set_inline
    ;

// ----- For -----
for_block
    : OPEN_TAG MINUS? FOR target IN expr MINUS? CLOSE_TAG
      template
      end_for
    ;
end_for :  OPEN_TAG MINUS? ENDFOR MINUS? CLOSE_TAG ;

target
    : ID (COMMA ID)*
    ;

// ----- If / Elif / Else -----
if_block
    : OPEN_TAG MINUS? IF expr MINUS? CLOSE_TAG
      template
      (OPEN_TAG MINUS? ELIF expr MINUS? CLOSE_TAG template)*
      (OPEN_TAG MINUS? ELSE MINUS? CLOSE_TAG template)?
      OPEN_TAG MINUS? ENDIF MINUS? CLOSE_TAG
    ;

// ----- Set -----
set_inline
    : OPEN_TAG MINUS? SET set_targets ASSIGN expr MINUS? CLOSE_TAG
    ;

set_block
    : OPEN_TAG MINUS? SET set_targets MINUS? CLOSE_TAG
      template
      OPEN_TAG MINUS? ENDSET MINUS? CLOSE_TAG
    ;

set_targets
    : ID (COMMA ID)*
    ;

// ----- Macro -----
macro_block
    : OPEN_TAG MINUS? MACRO ID LPAREN call_params? RPAREN MINUS? CLOSE_TAG
      template
      OPEN_TAG MINUS? ENDMACRO MINUS? CLOSE_TAG
    ;

call_params
    : param (COMMA param)*
    ;

param
    : ID (ASSIGN expr)?
    ;

// ----- Block / Inheritance -----
block_block
    : OPEN_TAG MINUS? BLOCK ID MINUS? CLOSE_TAG
      template
      OPEN_TAG MINUS? ENDBLOCK MINUS? CLOSE_TAG
    ;

extends_stmt
    : OPEN_TAG MINUS? EXTENDS STRING MINUS? CLOSE_TAG
    ;

include_stmt
    : OPEN_TAG MINUS? INCLUDE expr MINUS? CLOSE_TAG
    ;
// ===== Raw =====
//raw_block
//    : OPEN_TAG MINUS? RAW MINUS? CLOSE_TAG
//      RAW_TEXT*
//      OPEN_TAG MINUS? ENDRAW MINUS? CLOSE_TAG
//    ;
// ===== Expressions =====
expr
    : primary
    | (PLUS | MINUS | NOT) expr
    | expr (STAR | SLASH | PERCENT) expr
    | expr (PLUS | MINUS) expr
    | expr (LT | GT | LTE | GTE | EQ | NEQ | IN | IS) expr
    | expr AND expr
    | expr OR expr
    ;

primary
    : NUMBER
    | STRING
    | TRUE
    | FALSE
    | NONE
    | ID
    | LPAREN expr RPAREN
    | primary DOT ID        // obj.attr
    | primary LBRACK expr RBRACK  // list[0]
    | primary LPAREN (expr (COMMA expr)*)? RPAREN // func(args)
    | primary PIPE ID (LPAREN (expr (COMMA expr)*)? RPAREN)? // value|filter(args)
    | primary IS ID (LPAREN (expr (COMMA expr)*)? RPAREN)?   // value is test(args)
    ;



