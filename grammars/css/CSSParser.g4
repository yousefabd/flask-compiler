parser grammar CSSParser;
@header{
    package antlr.css;
}
options { tokenVocab=CSSLexer; }

stylesheet
    : (ws ruleset ws)* EOF
    ;

ruleset
    : selectorGroup ws LBRACE ws declarationList? ws RBRACE
    ;

selectorGroup
    : selector (COMMA ws selector)*
    ;

selector
    : selectorSequence ws (combinator selectorSequence ws)*
    ;

combinator
    : WS                # descendant
    | GT ws             # child
    | PLUS ws           # adjacent
    | TILDE ws          # sibling
    ;

selectorSequence
    : typeSelector ws selectorSuffix*
    | selectorSuffix+
    ;

typeSelector
    : IDENT
    | STAR
    ;

selectorSuffix
    : idSelector
    | classSelector
    | attributeSelector
    | pseudo
    ;

idSelector
    : HASH IDENT
    ;

classSelector
    : DOT IDENT
    ;

attributeSelector
    : LBRACK WS? IDENT WS?
      (
        (EQUAL | PREFIX | SUFFIX | SUBSTR)
        WS? (IDENT | STRING)
      )?
      WS? RBRACK
    ;

pseudo
    : COLON IDENT                           #PseudoClass
    | COLON IDENT LPAREN pseudoArgument RPAREN #PseudoClassWithArgs
    | COLON COLON IDENT                    #PseudoElement
    ;

pseudoArgument
    : IDENT
    | NUMBER
    | STRING
    | selector
    ;

declarationList
    : declaration (ws SEMI ws declaration)* ws SEMI?
    ;

declaration
    : property ws COLON ws value ws IMPORTANT?
    ;

property
    : IDENT
    | DOUBLEDASH IDENT
    ;

value
    : valuePart ((ws | ws COMMA ws) valuePart)*
    ;

valuePart
    : IDENT
    | DASH? NUMBER
    | PERCENT
    | DASH? DIMENSION
    | STRING
    | HEX
    | variable
    | function_
    ;

function_
    : IDENT ws LPAREN ws value? ws RPAREN ws
    ;
variable
    : DOUBLEDASH IDENT
    ;

ws
    : WS*
    ;
