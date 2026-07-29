parser grammar HTMLParser;

@header{
    package antlr.html;
}

options {
    tokenVocab=HTMLLexer;
}
template
    : tag* EOF
    ;

// ======================================================
// MIXED CONTENT
// ======================================================

tag
    : variable          #VariableStatement
    | stmt              #Statement
    | inline_stmt       #InlineStatement
    | htmlElement       #HtmlStatement
    | TEXT              #Text
    ;

// ======================================================
// HTML
// ======================================================

htmlElement
    : normalElement
    | voidElement
    ;

normalElement
    : beginTag tag* endTag
    ;

beginTag
    : TAG_OPEN_HTML TAG_ACCEPTED_NAME attribute* TAG_CLOSE
    ;

endTag
    : TAG_OPEN_HTML TAG_SLASH TAG_ACCEPTED_NAME TAG_CLOSE
    ;

voidElement
    : TAG_OPEN_HTML TAG_ACCEPTED_NAME attribute* TAG_SLASH_CLOSE
    ;

attribute
    : attributeName (TAG_EQUALS ATTVALUE_VALUE)?
    ;

attributeName
    : CHAR_NAME
    | TAG_ACCEPTED_NAME
    ;

// ======================================================
// VARIABLES
// ======================================================

variable
    : DOUBLE_OPEN_BRACE expr DOUBLE_CLOSE_BRACE
    ;

// ======================================================
// STATEMENTS
// ======================================================

stmt
    : for_block     #ForStatement
    | if_block      #IfStatement
    | set_block     #SetStatement
    | macro_block   #MacroStatement
    | block_block   #BlockStatement
    ;

inline_stmt
    : extends_stmt      #InlineExtendsStatement
    | include_stmt      #InlineIncludeStatement
    | set_inline        #InlineSetStatement
    ;

// ======================================================
// FOR
// ======================================================

for_block
    : OPEN_TAG FOR (ID (COMMA ID)*) IN expr CLOSE_TAG
      body
      OPEN_TAG ENDFOR CLOSE_TAG
    ;

// ======================================================
// IF / ELIF / ELSE
// ======================================================

if_block
    : OPEN_TAG IF expr CLOSE_TAG body
      (OPEN_TAG ELIF expr CLOSE_TAG body)*
      (OPEN_TAG ELSE CLOSE_TAG body)?
      OPEN_TAG ENDIF CLOSE_TAG
    ;

body
    : tag*
    ;

// ======================================================
// SET
// ======================================================

set_inline
    : OPEN_TAG SET (ID (COMMA ID)*) ASSIGN expr CLOSE_TAG
    ;

set_block
    : OPEN_TAG SET (ID (COMMA ID)*) CLOSE_TAG
      body
      OPEN_TAG ENDSET CLOSE_TAG
    ;

// ======================================================
// MACRO
// ======================================================

macro_block
    : OPEN_TAG MACRO ID LPAREN parameters? RPAREN CLOSE_TAG
      body
      OPEN_TAG ENDMACRO CLOSE_TAG
    ;

parameters
    : parameter (COMMA parameter)*
    ;

parameter
    : ID (ASSIGN expr)?
    ;

// ======================================================
// BLOCK
// ======================================================

block_block
    : OPEN_TAG BLOCK ID CLOSE_TAG
      body
      OPEN_TAG ENDBLOCK CLOSE_TAG
    ;

extends_stmt
    : OPEN_TAG EXTENDS STRING CLOSE_TAG
    ;

include_stmt
    : OPEN_TAG INCLUDE expr CLOSE_TAG
    ;

// ======================================================
// EXPRESSIONS
// ======================================================

expr
    : primary (trailer | filter)*                         #IDTrFlExpression
    | primary                                            #PrimaryExpression
    | op=(PLUS | MINUS | NOT) expr                       #UnaryExpression
    | expr op=(STAR | SLASH | PERCENT) expr              #BinaryExpression
    | expr op=(PLUS | MINUS) expr                        #BinaryExpression
    | expr op=(LT | GT | LTE | GTE | EQ | NEQ | IN) expr #BinaryExpression
    | expr IS NOT? testInvocation                        #TestExpression
    | expr op=AND expr                                   #BinaryExpression
    | expr op=OR expr                                    #BinaryExpression
    ;

testInvocation
    : testName testArguments?
    ;

testName
    : ID
    | NONE
    | TRUE
    | FALSE
    | IN
    ;

testArguments
    : LPAREN arguments? RPAREN
    | primary
    ;
trailer
    : LPAREN arguments? RPAREN
    | DOT ID
    | LBRACK expr RBRACK
    ;

filter
    : PIPE ID (LPAREN arguments? RPAREN)?
    ;

arguments
    : argument (COMMA argument)*
    ;

argument
    : expr (ASSIGN expr)?
    ;

primary
    : LPAREN expr RPAREN                             #ParenExpression
    | ID                                             #ID
    | TRUE                                           #Boolean
    | FALSE                                          #Boolean
    | MINUS? NUMBER                                  #Number
    | NONE                                           #None
    | STRING                                         #String
    | listdef                                        #List
    | dictdef                                        #Dictionary
    ;

listdef
    : LBRACK (expr (COMMA expr)*)? RBRACK
    ;

dictdef
    : LBRACE ((expr COLON expr) (COMMA (expr COLON expr))*)? RBRACE
    ;