// cSpell: disable
parser grammar PythonParser;

@header{
    package antlr.python;
}
options {
    tokenVocab = PythonLexer;
    // superClass = PythonParserBase;
}

/***************************** this is the root rules *****************************/
prog : (NEWLINE | stmt)* EOF;

stmt
    : simple_stmt # SimpleStatement
    | compound_stmt # CompoundStatment
    ;

compound_stmt
    : IF cond COLON body elif_clause* else_clause? # IfStatment
    | WHILE cond COLON body else_clause?           # WhileStatment
    | FOR iter IN expr COLON body else_clause?     # ForStatment
    | decorator* (funcdef)                         # DecoratorStatement
    ;


/******************** types of simple statment except expr_stmt ********************/
simple_stmt
    : small_stmt (SEMI_COLON small_stmt)* SEMI_COLON? (NEWLINE | EOF)
    ;

small_stmt
    : expr_stmt         #ExpressionStatement
    | augassign_stmt    #AugAssignStatement
    | pass_stmt         #PassStatement
    | break_stmt        #BreakStatement
    | continue_stmt     #ContinueStatement
    | return_stmt       #ReturnStatement
    | import_stmt       #ImportStatement
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
    : RETURN condlist?
    ;

import_stmt
    : IMPORT dotted_name                        #SimpleImport
    | FROM dotted_name IMPORT import_targets    #FromImport
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
    : condlist (ASSIGN condlist)?
    ;

condlist
    : cond (COMMA cond)* COMMA?
    ;

cond
    : comparison            #SimpleCondition
    | NOT cond              #NotCondition
    | cond op = AND cond    #AndCondition
    | cond op = OR cond     #OrCondition
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
    ) expr                      #RelationalComparison
    | expr                      #ExpressionComparison
    ;

expr
    : ID trailer*                                   #IDExpression
    | atom                                          #AtomExpression
    | <assoc = right> expr op = POWER expr          #PowerExpression
    | op = (ADD | MINUS | NOT_OP) expr              #UnaryExpression
    | expr op = (STAR | DIV | MOD | IDIV | AT) expr #MulDivExpression
    | expr op = (ADD | MINUS) expr                  #AddSubExpression
    | expr op = (LSHIFT | RSHIFT) expr              #ShiftExpression
    | expr op = AND_OP expr                         #AndExpression
    | expr op = XOR expr                            #XorExpression
    | expr op = OR_OP expr                          #OrExpression
    ;

atom
    : OPEN_PAREN expr CLOSE_PAREN       #ParenAtom
    | OPEN_BRACKET list? CLOSE_BRACKET  #ListAtom
    | OPEN_BRACE dicorset? CLOSE_BRACE  #DicOrSetAtom
    | ID                                #IDAtom
    | TRUE                              #BoolAtom
    | FALSE                             #BoolAtom
    | MINUS? number                     #NumberAtom
    | NONE                              #NoneAtom
    | STRING                            #StringAtom
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
    : OPEN_PAREN arglist? CLOSE_PAREN           #CallArguments
    | OPEN_BRACKET subscriptlist CLOSE_BRACKET  #SubscriptArguments
    ;

arglist
    : argument (COMMA argument)* COMMA?
    ;

argument
    : cond (ASSIGN cond)?
    ;

subscriptlist
    : cond (COMMA cond)* COMMA?
    ;

/****************************** for definition ******************************/

iter: ID (COMMA ID)*;

// call_expr: ID OPEN_PAREN arglist? CLOSE_PAREN;

/****************************** function definition ******************************/

funcdef
    : DEF ID OPEN_PAREN def_parameters COMMA? CLOSE_PAREN (ARROW cond)? COLON body
    ;

def_parameters
    : def_parameter (COMMA def_parameter)*
    ;

def_parameter
    : ID (COLON cond)? (ASSIGN cond)?
    ;

/****************************** else if else & body ******************************/

elif_clause: ELIF cond COLON body;

else_clause: ELSE COLON body;

body: simple_stmt
    | NEWLINE INDENT stmt+ DEDENT
    ;

/************************** list dictionary set **************************/

list
    : expr (COMMA expr)*
    ;

dicorset
    : expr (COMMA expr)*
    | expr COLON expr (COMMA expr COLON expr)*
    ;

/****************************** decorator ******************************/

decorator
    : AT  dotted_name (OPEN_PAREN arglist? CLOSE_PAREN)? NEWLINE
    ;

















