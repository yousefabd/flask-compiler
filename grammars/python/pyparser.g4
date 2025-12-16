parser grammar pyparser;

@header{
    package antlr.python;
}
options {
    tokenVocab = pylexer;
    // superClass = PythonParserBase;
}

/***************************** this is the root rules *****************************/
prog : (NEWLINE | stmt)* EOF;

stmt: simple_stmt | compouned_stmt;

compouned_stmt
    : IF test COLON body elif_clause* else_clause?
    | WHILE test COLON body else_clause?
    | FOR iter IN iterable COLON body else_clause?
    | decorator* (funcdef)
    ;


/******************** types of simple statment except expr_stmt ********************/
simple_stmt
    : small_stmt (SEMI_COLON small_stmt)* SEMI_COLON? (NEWLINE | EOF)
    ;

small_stmt
    : expr_stmt
    | augassign_stmt
    | pass_stmt
    | break_stmt
    | continue_stmt
    | return_stmt
    | import_stmt
    ;

augassign_stmt
    :ID (
        ADD_ASSIGN       // +=
        | SUB_ASSIGN      // -=
        | MULT_ASSIGN     // *=
        | DIV_ASSIGN      // /=
        | MOD_ASSIGN      // %=
        | IDIV_ASSIGN     // //=
        | AND_ASSIGN      // &=
        | OR_ASSIGN       // |=
        | XOR_ASSIGN      // ^=
        | LSHIFT_ASSIGN   // <<=
        | RSHIFT_ASSIGN   // >>=
        | POWER_ASSIGN    // **=
    ) expr
    ;

pass_stmt
    : PASS
    ;

break_stmt
    : BREAK
    ;

continue_stmt
    : CONTINUE
    ;

return_stmt
    : RETURN testlist?
    ;

import_stmt
    : IMPORT dotted_name
    | FROM dotted_name IMPORT import_targets
    ;

dotted_name
    : ID (DOT ID)*
    ;

import_targets
    : STAR
    | ID (COMMA ID)*
    ;

/***************************** expr_stmt and its variants *****************************/

expr_stmt
    : testlist (ASSIGN testlist)?
    ;

testlist
    : test (COMMA test)* COMMA?
    ;

test
    : comparison
    | NOT test
    | test op = AND test
    | test op = OR test
    ;

comparison
    : expr (
        LESS_THAN
        | GREATER_THAN
        | EQUALS
        | GT_EQ
        | LT_EQ
        | NOT_EQ
        | optional = NOT? IN
        | IS optional = NOT?
    ) expr
    | expr
    ;

expr
    : ID trailer*
    | atom
    | <assoc = right> expr op = POWER expr
    | op = (ADD | MINUS | NOT_OP) expr
    | expr op = (STAR | DIV | MOD | IDIV | AT) expr
    | expr op = (ADD | MINUS) expr
    | expr op = (LSHIFT | RSHIFT) expr
    | expr op = AND_OP expr
    | expr op = XOR expr
    | expr op = OR_OP expr
    ;

atom
    : OPEN_PAREN expr CLOSE_PAREN
    | OPEN_BRACKET list? CLOSE_BRACKET
    | OPEN_BRACE dicorset? CLOSE_BRACE
    | ID
    | TRUE
    | FALSE
    | MINUS? number
    | NONE
    | STRING
    ;

number
    : INTEGER
    | FLOAT
    ;

/*************************** the trailer and its variants ***************************/

trailer
    : DOT ID arguments?
    | arguments
    ;

arguments
    : OPEN_PAREN arglist? CLOSE_PAREN
    | OPEN_BRACKET subscriptlist CLOSE_BRACKET
    ;

arglist
    : argument (COMMA argument)* COMMA?
    ;
argument
    : test (ASSIGN test)?
    | STAR test
    ;

subscriptlist
    : test (COMMA test)* COMMA?
    ;

/****************************** for definition ******************************/

iter: ID (COMMA ID)*;

iterable
    : ID
    | list
    | call_expr
    ;

call_expr: ID OPEN_PAREN arglist? CLOSE_PAREN;

/****************************** function definition ******************************/

funcdef
    : DEF ID OPEN_PAREN typedargslist? CLOSE_PAREN (ARROW test)? COLON body
    ;

typedargslist
    : (def_parameters COMMA)? (args (COMMA def_parameters)? | COMMA)?
    | def_parameters COMMA?
    ;

args
    : STAR named_parameter
    ;

def_parameters
    : def_parameter (COMMA def_parameter)*
    ;

def_parameter
    : named_parameter (ASSIGN test)?
    | STAR
    ;

named_parameter
    : ID (COLON test)?
    ;

/****************************** else if else & body ******************************/

elif_clause: ELIF test COLON body;

else_clause: ELSE COLON body;

body: simple_stmt
    | NEWLINE INDENT stmt+ DEDENT
    ;

/************************** list dictionary set **************************/

list: expr (COMMA expr)*
    | expr FOR expr IN expr
    ;

dicorset
    : expr (COMMA expr)*
    | expr COLON expr (COMMA expr COLON expr)*
    ;

/****************************** decorator ******************************/

decorator
    : AT  dotted_name (OPEN_PAREN arglist? CLOSE_PAREN)? NEWLINE
    ;

















