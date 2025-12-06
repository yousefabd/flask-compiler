parser grammar HTMLParser;
@header{
    antlr
}
options {
    tokenVocab=HTMLLexer;
}

htmlDocument
    : element* EOF
    ;

element
    : TEXT
    | normalElement
    | voidElement
    ;
normalElement
    : beginTag element* endTag
    ;
beginTag
    : TAG_OPEN TAG_NAME attribute* TAG_CLOSE //<name att=..>
    ;
endTag
    : TAG_OPEN TAG_SLASH TAG_NAME TAG_CLOSE //</name>
    ;
voidElement
    : TAG_OPEN TAG_NAME attribute* TAG_SLASH_CLOSE //<name att=.. />
    ;
attribute
    : TAG_NAME (TAG_EQUALS ATTVALUE_VALUE)?
    ;