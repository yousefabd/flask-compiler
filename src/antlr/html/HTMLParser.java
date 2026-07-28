// Generated from C:/Users/Yousef.Abdulmonaem/Documents/GitHub/flask-compiler/grammars/html/HTMLParser.g4 by ANTLR 4.13.2

    package antlr.html;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class HTMLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		COMMENT=1, DOUBLE_OPEN_BRACE=2, OPEN_TAG=3, COMMENT_START=4, SCRIPT_OPEN=5, 
		STYLE_OPEN=6, TAG_OPEN_HTML=7, TEXT=8, TAG_CLOSE=9, TAG_SLASH_CLOSE=10, 
		TAG_SLASH=11, TAG_EQUALS=12, TAG_ACCEPTED_NAME=13, CHAR_NAME=14, TAG_WHITESPACE=15, 
		SCRIPT_BODY=16, SCRIPT_SHORT_BODY=17, STYLE_BODY=18, STYLE_SHORT_BODY=19, 
		ATTVALUE_VALUE=20, ATTRIBUTE=21, CLOSE_TAG=22, DOUBLE_CLOSE_BRACE=23, 
		INCLUDE=24, FOR=25, IN=26, ENDFOR=27, IF=28, ELIF=29, ELSE=30, ENDIF=31, 
		BLOCK=32, ENDBLOCK=33, MACRO=34, ENDMACRO=35, SET=36, ENDSET=37, EXTENDS=38, 
		RAW=39, ENDRAW=40, IS=41, OR=42, AND=43, NOT=44, TRUE=45, FALSE=46, NONE=47, 
		PLUS=48, MINUS=49, STAR=50, SLASH=51, PERCENT=52, EQ=53, NEQ=54, LT=55, 
		GT=56, LTE=57, GTE=58, ASSIGN=59, PIPE=60, DOT=61, COMMA=62, COLON=63, 
		LPAREN=64, RPAREN=65, LBRACK=66, RBRACK=67, LBRACE=68, RBRACE=69, STRING=70, 
		NUMBER=71, ID=72, JINJA_WS=73, COMMENT_END=74, COMMENT_TEXT=75;
	public static final int
		RULE_template = 0, RULE_tag = 1, RULE_htmlElement = 2, RULE_normalElement = 3, 
		RULE_beginTag = 4, RULE_endTag = 5, RULE_voidElement = 6, RULE_attribute = 7, 
		RULE_attributeName = 8, RULE_variable = 9, RULE_stmt = 10, RULE_inline_stmt = 11, 
		RULE_for_block = 12, RULE_if_block = 13, RULE_body = 14, RULE_set_inline = 15, 
		RULE_set_block = 16, RULE_macro_block = 17, RULE_parameters = 18, RULE_parameter = 19, 
		RULE_block_block = 20, RULE_extends_stmt = 21, RULE_include_stmt = 22, 
		RULE_expr = 23, RULE_trailer = 24, RULE_filter = 25, RULE_arguments = 26, 
		RULE_argument = 27, RULE_primary = 28, RULE_listdef = 29, RULE_dictdef = 30;
	private static String[] makeRuleNames() {
		return new String[] {
			"template", "tag", "htmlElement", "normalElement", "beginTag", "endTag", 
			"voidElement", "attribute", "attributeName", "variable", "stmt", "inline_stmt", 
			"for_block", "if_block", "body", "set_inline", "set_block", "macro_block", 
			"parameters", "parameter", "block_block", "extends_stmt", "include_stmt", 
			"expr", "trailer", "filter", "arguments", "argument", "primary", "listdef", 
			"dictdef"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'{#'", null, null, null, null, null, "'/>'", 
			null, null, null, null, null, null, null, null, null, null, null, "'%}'", 
			null, "'include'", "'for'", "'in'", "'endfor'", "'if'", "'elif'", "'else'", 
			"'endif'", "'block'", "'endblock'", "'macro'", "'endmacro'", "'set'", 
			"'endset'", "'extends'", "'raw'", "'endraw'", "'is'", "'or'", "'and'", 
			"'not'", "'true'", "'false'", "'none'", "'+'", "'-'", "'*'", null, "'%'", 
			"'=='", "'!='", null, null, "'<='", "'>='", null, "'|'", "'.'", "','", 
			"':'", "'('", "')'", "'['", "']'", "'{'", null, null, null, null, null, 
			"'#}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "COMMENT", "DOUBLE_OPEN_BRACE", "OPEN_TAG", "COMMENT_START", "SCRIPT_OPEN", 
			"STYLE_OPEN", "TAG_OPEN_HTML", "TEXT", "TAG_CLOSE", "TAG_SLASH_CLOSE", 
			"TAG_SLASH", "TAG_EQUALS", "TAG_ACCEPTED_NAME", "CHAR_NAME", "TAG_WHITESPACE", 
			"SCRIPT_BODY", "SCRIPT_SHORT_BODY", "STYLE_BODY", "STYLE_SHORT_BODY", 
			"ATTVALUE_VALUE", "ATTRIBUTE", "CLOSE_TAG", "DOUBLE_CLOSE_BRACE", "INCLUDE", 
			"FOR", "IN", "ENDFOR", "IF", "ELIF", "ELSE", "ENDIF", "BLOCK", "ENDBLOCK", 
			"MACRO", "ENDMACRO", "SET", "ENDSET", "EXTENDS", "RAW", "ENDRAW", "IS", 
			"OR", "AND", "NOT", "TRUE", "FALSE", "NONE", "PLUS", "MINUS", "STAR", 
			"SLASH", "PERCENT", "EQ", "NEQ", "LT", "GT", "LTE", "GTE", "ASSIGN", 
			"PIPE", "DOT", "COMMA", "COLON", "LPAREN", "RPAREN", "LBRACK", "RBRACK", 
			"LBRACE", "RBRACE", "STRING", "NUMBER", "ID", "JINJA_WS", "COMMENT_END", 
			"COMMENT_TEXT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "HTMLParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public HTMLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(HTMLParser.EOF, 0); }
		public List<TagContext> tag() {
			return getRuleContexts(TagContext.class);
		}
		public TagContext tag(int i) {
			return getRuleContext(TagContext.class,i);
		}
		public TemplateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_template; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterTemplate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitTemplate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitTemplate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateContext template() throws RecognitionException {
		TemplateContext _localctx = new TemplateContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_template);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 396L) != 0)) {
				{
				{
				setState(62);
				tag();
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(68);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TagContext extends ParserRuleContext {
		public TagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tag; }
	 
		public TagContext() { }
		public void copyFrom(TagContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VariableStatementContext extends TagContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public VariableStatementContext(TagContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterVariableStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitVariableStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitVariableStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends TagContext {
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public StatementContext(TagContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class HtmlStatementContext extends TagContext {
		public HtmlElementContext htmlElement() {
			return getRuleContext(HtmlElementContext.class,0);
		}
		public HtmlStatementContext(TagContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterHtmlStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitHtmlStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitHtmlStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TextContext extends TagContext {
		public TerminalNode TEXT() { return getToken(HTMLParser.TEXT, 0); }
		public TextContext(TagContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitText(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitText(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InlineStatementContext extends TagContext {
		public Inline_stmtContext inline_stmt() {
			return getRuleContext(Inline_stmtContext.class,0);
		}
		public InlineStatementContext(TagContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterInlineStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitInlineStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitInlineStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagContext tag() throws RecognitionException {
		TagContext _localctx = new TagContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_tag);
		try {
			setState(75);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new VariableStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(70);
				variable();
				}
				break;
			case 2:
				_localctx = new StatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				stmt();
				}
				break;
			case 3:
				_localctx = new InlineStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(72);
				inline_stmt();
				}
				break;
			case 4:
				_localctx = new HtmlStatementContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(73);
				htmlElement();
				}
				break;
			case 5:
				_localctx = new TextContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(74);
				match(TEXT);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HtmlElementContext extends ParserRuleContext {
		public NormalElementContext normalElement() {
			return getRuleContext(NormalElementContext.class,0);
		}
		public VoidElementContext voidElement() {
			return getRuleContext(VoidElementContext.class,0);
		}
		public HtmlElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterHtmlElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitHtmlElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitHtmlElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtmlElementContext htmlElement() throws RecognitionException {
		HtmlElementContext _localctx = new HtmlElementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_htmlElement);
		try {
			setState(79);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(77);
				normalElement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(78);
				voidElement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NormalElementContext extends ParserRuleContext {
		public BeginTagContext beginTag() {
			return getRuleContext(BeginTagContext.class,0);
		}
		public EndTagContext endTag() {
			return getRuleContext(EndTagContext.class,0);
		}
		public List<TagContext> tag() {
			return getRuleContexts(TagContext.class);
		}
		public TagContext tag(int i) {
			return getRuleContext(TagContext.class,i);
		}
		public NormalElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_normalElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterNormalElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitNormalElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitNormalElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NormalElementContext normalElement() throws RecognitionException {
		NormalElementContext _localctx = new NormalElementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_normalElement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			beginTag();
			setState(85);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(82);
					tag();
					}
					} 
				}
				setState(87);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(88);
			endTag();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BeginTagContext extends ParserRuleContext {
		public TerminalNode TAG_OPEN_HTML() { return getToken(HTMLParser.TAG_OPEN_HTML, 0); }
		public TerminalNode TAG_ACCEPTED_NAME() { return getToken(HTMLParser.TAG_ACCEPTED_NAME, 0); }
		public TerminalNode TAG_CLOSE() { return getToken(HTMLParser.TAG_CLOSE, 0); }
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public BeginTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_beginTag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterBeginTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitBeginTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitBeginTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BeginTagContext beginTag() throws RecognitionException {
		BeginTagContext _localctx = new BeginTagContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_beginTag);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(TAG_OPEN_HTML);
			setState(91);
			match(TAG_ACCEPTED_NAME);
			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TAG_ACCEPTED_NAME || _la==CHAR_NAME) {
				{
				{
				setState(92);
				attribute();
				}
				}
				setState(97);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(98);
			match(TAG_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EndTagContext extends ParserRuleContext {
		public TerminalNode TAG_OPEN_HTML() { return getToken(HTMLParser.TAG_OPEN_HTML, 0); }
		public TerminalNode TAG_SLASH() { return getToken(HTMLParser.TAG_SLASH, 0); }
		public TerminalNode TAG_ACCEPTED_NAME() { return getToken(HTMLParser.TAG_ACCEPTED_NAME, 0); }
		public TerminalNode TAG_CLOSE() { return getToken(HTMLParser.TAG_CLOSE, 0); }
		public EndTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endTag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterEndTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitEndTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitEndTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EndTagContext endTag() throws RecognitionException {
		EndTagContext _localctx = new EndTagContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_endTag);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(TAG_OPEN_HTML);
			setState(101);
			match(TAG_SLASH);
			setState(102);
			match(TAG_ACCEPTED_NAME);
			setState(103);
			match(TAG_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VoidElementContext extends ParserRuleContext {
		public TerminalNode TAG_OPEN_HTML() { return getToken(HTMLParser.TAG_OPEN_HTML, 0); }
		public TerminalNode TAG_ACCEPTED_NAME() { return getToken(HTMLParser.TAG_ACCEPTED_NAME, 0); }
		public TerminalNode TAG_SLASH_CLOSE() { return getToken(HTMLParser.TAG_SLASH_CLOSE, 0); }
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public VoidElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_voidElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterVoidElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitVoidElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitVoidElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VoidElementContext voidElement() throws RecognitionException {
		VoidElementContext _localctx = new VoidElementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_voidElement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(TAG_OPEN_HTML);
			setState(106);
			match(TAG_ACCEPTED_NAME);
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TAG_ACCEPTED_NAME || _la==CHAR_NAME) {
				{
				{
				setState(107);
				attribute();
				}
				}
				setState(112);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(113);
			match(TAG_SLASH_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeContext extends ParserRuleContext {
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode TAG_EQUALS() { return getToken(HTMLParser.TAG_EQUALS, 0); }
		public TerminalNode ATTVALUE_VALUE() { return getToken(HTMLParser.ATTVALUE_VALUE, 0); }
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_attribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			attributeName();
			setState(118);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TAG_EQUALS) {
				{
				setState(116);
				match(TAG_EQUALS);
				setState(117);
				match(ATTVALUE_VALUE);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeNameContext extends ParserRuleContext {
		public TerminalNode CHAR_NAME() { return getToken(HTMLParser.CHAR_NAME, 0); }
		public TerminalNode TAG_ACCEPTED_NAME() { return getToken(HTMLParser.TAG_ACCEPTED_NAME, 0); }
		public AttributeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterAttributeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitAttributeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitAttributeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeNameContext attributeName() throws RecognitionException {
		AttributeNameContext _localctx = new AttributeNameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_attributeName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			_la = _input.LA(1);
			if ( !(_la==TAG_ACCEPTED_NAME || _la==CHAR_NAME) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableContext extends ParserRuleContext {
		public TerminalNode DOUBLE_OPEN_BRACE() { return getToken(HTMLParser.DOUBLE_OPEN_BRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DOUBLE_CLOSE_BRACE() { return getToken(HTMLParser.DOUBLE_CLOSE_BRACE, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			match(DOUBLE_OPEN_BRACE);
			setState(123);
			expr(0);
			setState(124);
			match(DOUBLE_CLOSE_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StmtContext extends ParserRuleContext {
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
	 
		public StmtContext() { }
		public void copyFrom(StmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfStatementContext extends StmtContext {
		public If_blockContext if_block() {
			return getRuleContext(If_blockContext.class,0);
		}
		public IfStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MacroStatementContext extends StmtContext {
		public Macro_blockContext macro_block() {
			return getRuleContext(Macro_blockContext.class,0);
		}
		public MacroStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterMacroStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitMacroStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitMacroStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BlockStatementContext extends StmtContext {
		public Block_blockContext block_block() {
			return getRuleContext(Block_blockContext.class,0);
		}
		public BlockStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterBlockStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitBlockStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitBlockStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ForStatementContext extends StmtContext {
		public For_blockContext for_block() {
			return getRuleContext(For_blockContext.class,0);
		}
		public ForStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterForStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitForStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitForStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SetStatementContext extends StmtContext {
		public Set_blockContext set_block() {
			return getRuleContext(Set_blockContext.class,0);
		}
		public SetStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterSetStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitSetStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitSetStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_stmt);
		try {
			setState(131);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				_localctx = new ForStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(126);
				for_block();
				}
				break;
			case 2:
				_localctx = new IfStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(127);
				if_block();
				}
				break;
			case 3:
				_localctx = new SetStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(128);
				set_block();
				}
				break;
			case 4:
				_localctx = new MacroStatementContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(129);
				macro_block();
				}
				break;
			case 5:
				_localctx = new BlockStatementContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(130);
				block_block();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Inline_stmtContext extends ParserRuleContext {
		public Inline_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inline_stmt; }
	 
		public Inline_stmtContext() { }
		public void copyFrom(Inline_stmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InlineSetStatementContext extends Inline_stmtContext {
		public Set_inlineContext set_inline() {
			return getRuleContext(Set_inlineContext.class,0);
		}
		public InlineSetStatementContext(Inline_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterInlineSetStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitInlineSetStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitInlineSetStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InlineExtendsStatementContext extends Inline_stmtContext {
		public Extends_stmtContext extends_stmt() {
			return getRuleContext(Extends_stmtContext.class,0);
		}
		public InlineExtendsStatementContext(Inline_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterInlineExtendsStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitInlineExtendsStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitInlineExtendsStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class InlineIncludeStatementContext extends Inline_stmtContext {
		public Include_stmtContext include_stmt() {
			return getRuleContext(Include_stmtContext.class,0);
		}
		public InlineIncludeStatementContext(Inline_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterInlineIncludeStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitInlineIncludeStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitInlineIncludeStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Inline_stmtContext inline_stmt() throws RecognitionException {
		Inline_stmtContext _localctx = new Inline_stmtContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_inline_stmt);
		try {
			setState(136);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				_localctx = new InlineExtendsStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(133);
				extends_stmt();
				}
				break;
			case 2:
				_localctx = new InlineIncludeStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(134);
				include_stmt();
				}
				break;
			case 3:
				_localctx = new InlineSetStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(135);
				set_inline();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class For_blockContext extends ParserRuleContext {
		public List<TerminalNode> OPEN_TAG() { return getTokens(HTMLParser.OPEN_TAG); }
		public TerminalNode OPEN_TAG(int i) {
			return getToken(HTMLParser.OPEN_TAG, i);
		}
		public TerminalNode FOR() { return getToken(HTMLParser.FOR, 0); }
		public TerminalNode IN() { return getToken(HTMLParser.IN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<TerminalNode> CLOSE_TAG() { return getTokens(HTMLParser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(HTMLParser.CLOSE_TAG, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode ENDFOR() { return getToken(HTMLParser.ENDFOR, 0); }
		public List<TerminalNode> ID() { return getTokens(HTMLParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(HTMLParser.ID, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(HTMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(HTMLParser.COMMA, i);
		}
		public For_blockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterFor_block(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitFor_block(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitFor_block(this);
			else return visitor.visitChildren(this);
		}
	}

	public final For_blockContext for_block() throws RecognitionException {
		For_blockContext _localctx = new For_blockContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_for_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			match(OPEN_TAG);
			setState(139);
			match(FOR);
			{
			setState(140);
			match(ID);
			setState(145);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(141);
				match(COMMA);
				setState(142);
				match(ID);
				}
				}
				setState(147);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(148);
			match(IN);
			setState(149);
			expr(0);
			setState(150);
			match(CLOSE_TAG);
			setState(151);
			body();
			setState(152);
			match(OPEN_TAG);
			setState(153);
			match(ENDFOR);
			setState(154);
			match(CLOSE_TAG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class If_blockContext extends ParserRuleContext {
		public List<TerminalNode> OPEN_TAG() { return getTokens(HTMLParser.OPEN_TAG); }
		public TerminalNode OPEN_TAG(int i) {
			return getToken(HTMLParser.OPEN_TAG, i);
		}
		public TerminalNode IF() { return getToken(HTMLParser.IF, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> CLOSE_TAG() { return getTokens(HTMLParser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(HTMLParser.CLOSE_TAG, i);
		}
		public List<BodyContext> body() {
			return getRuleContexts(BodyContext.class);
		}
		public BodyContext body(int i) {
			return getRuleContext(BodyContext.class,i);
		}
		public TerminalNode ENDIF() { return getToken(HTMLParser.ENDIF, 0); }
		public List<TerminalNode> ELIF() { return getTokens(HTMLParser.ELIF); }
		public TerminalNode ELIF(int i) {
			return getToken(HTMLParser.ELIF, i);
		}
		public TerminalNode ELSE() { return getToken(HTMLParser.ELSE, 0); }
		public If_blockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterIf_block(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitIf_block(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitIf_block(this);
			else return visitor.visitChildren(this);
		}
	}

	public final If_blockContext if_block() throws RecognitionException {
		If_blockContext _localctx = new If_blockContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_if_block);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			match(OPEN_TAG);
			setState(157);
			match(IF);
			setState(158);
			expr(0);
			setState(159);
			match(CLOSE_TAG);
			setState(160);
			body();
			setState(169);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(161);
					match(OPEN_TAG);
					setState(162);
					match(ELIF);
					setState(163);
					expr(0);
					setState(164);
					match(CLOSE_TAG);
					setState(165);
					body();
					}
					} 
				}
				setState(171);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			setState(176);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(172);
				match(OPEN_TAG);
				setState(173);
				match(ELSE);
				setState(174);
				match(CLOSE_TAG);
				setState(175);
				body();
				}
				break;
			}
			setState(178);
			match(OPEN_TAG);
			setState(179);
			match(ENDIF);
			setState(180);
			match(CLOSE_TAG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BodyContext extends ParserRuleContext {
		public List<TagContext> tag() {
			return getRuleContexts(TagContext.class);
		}
		public TagContext tag(int i) {
			return getRuleContext(TagContext.class,i);
		}
		public BodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyContext body() throws RecognitionException {
		BodyContext _localctx = new BodyContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_body);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(182);
					tag();
					}
					} 
				}
				setState(187);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Set_inlineContext extends ParserRuleContext {
		public TerminalNode OPEN_TAG() { return getToken(HTMLParser.OPEN_TAG, 0); }
		public TerminalNode SET() { return getToken(HTMLParser.SET, 0); }
		public TerminalNode ASSIGN() { return getToken(HTMLParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode CLOSE_TAG() { return getToken(HTMLParser.CLOSE_TAG, 0); }
		public List<TerminalNode> ID() { return getTokens(HTMLParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(HTMLParser.ID, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(HTMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(HTMLParser.COMMA, i);
		}
		public Set_inlineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_set_inline; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterSet_inline(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitSet_inline(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitSet_inline(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Set_inlineContext set_inline() throws RecognitionException {
		Set_inlineContext _localctx = new Set_inlineContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_set_inline);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			match(OPEN_TAG);
			setState(189);
			match(SET);
			{
			setState(190);
			match(ID);
			setState(195);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(191);
				match(COMMA);
				setState(192);
				match(ID);
				}
				}
				setState(197);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(198);
			match(ASSIGN);
			setState(199);
			expr(0);
			setState(200);
			match(CLOSE_TAG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Set_blockContext extends ParserRuleContext {
		public List<TerminalNode> OPEN_TAG() { return getTokens(HTMLParser.OPEN_TAG); }
		public TerminalNode OPEN_TAG(int i) {
			return getToken(HTMLParser.OPEN_TAG, i);
		}
		public TerminalNode SET() { return getToken(HTMLParser.SET, 0); }
		public List<TerminalNode> CLOSE_TAG() { return getTokens(HTMLParser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(HTMLParser.CLOSE_TAG, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode ENDSET() { return getToken(HTMLParser.ENDSET, 0); }
		public List<TerminalNode> ID() { return getTokens(HTMLParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(HTMLParser.ID, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(HTMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(HTMLParser.COMMA, i);
		}
		public Set_blockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_set_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterSet_block(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitSet_block(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitSet_block(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Set_blockContext set_block() throws RecognitionException {
		Set_blockContext _localctx = new Set_blockContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_set_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(202);
			match(OPEN_TAG);
			setState(203);
			match(SET);
			{
			setState(204);
			match(ID);
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(205);
				match(COMMA);
				setState(206);
				match(ID);
				}
				}
				setState(211);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(212);
			match(CLOSE_TAG);
			setState(213);
			body();
			setState(214);
			match(OPEN_TAG);
			setState(215);
			match(ENDSET);
			setState(216);
			match(CLOSE_TAG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Macro_blockContext extends ParserRuleContext {
		public List<TerminalNode> OPEN_TAG() { return getTokens(HTMLParser.OPEN_TAG); }
		public TerminalNode OPEN_TAG(int i) {
			return getToken(HTMLParser.OPEN_TAG, i);
		}
		public TerminalNode MACRO() { return getToken(HTMLParser.MACRO, 0); }
		public TerminalNode ID() { return getToken(HTMLParser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(HTMLParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(HTMLParser.RPAREN, 0); }
		public List<TerminalNode> CLOSE_TAG() { return getTokens(HTMLParser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(HTMLParser.CLOSE_TAG, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode ENDMACRO() { return getToken(HTMLParser.ENDMACRO, 0); }
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public Macro_blockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_macro_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterMacro_block(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitMacro_block(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitMacro_block(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Macro_blockContext macro_block() throws RecognitionException {
		Macro_blockContext _localctx = new Macro_blockContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_macro_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			match(OPEN_TAG);
			setState(219);
			match(MACRO);
			setState(220);
			match(ID);
			setState(221);
			match(LPAREN);
			setState(223);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(222);
				parameters();
				}
			}

			setState(225);
			match(RPAREN);
			setState(226);
			match(CLOSE_TAG);
			setState(227);
			body();
			setState(228);
			match(OPEN_TAG);
			setState(229);
			match(ENDMACRO);
			setState(230);
			match(CLOSE_TAG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParametersContext extends ParserRuleContext {
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(HTMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(HTMLParser.COMMA, i);
		}
		public ParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametersContext parameters() throws RecognitionException {
		ParametersContext _localctx = new ParametersContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_parameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			parameter();
			setState(237);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(233);
				match(COMMA);
				setState(234);
				parameter();
				}
				}
				setState(239);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(HTMLParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(HTMLParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_parameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			match(ID);
			setState(243);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(241);
				match(ASSIGN);
				setState(242);
				expr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Block_blockContext extends ParserRuleContext {
		public List<TerminalNode> OPEN_TAG() { return getTokens(HTMLParser.OPEN_TAG); }
		public TerminalNode OPEN_TAG(int i) {
			return getToken(HTMLParser.OPEN_TAG, i);
		}
		public TerminalNode BLOCK() { return getToken(HTMLParser.BLOCK, 0); }
		public TerminalNode ID() { return getToken(HTMLParser.ID, 0); }
		public List<TerminalNode> CLOSE_TAG() { return getTokens(HTMLParser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(HTMLParser.CLOSE_TAG, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode ENDBLOCK() { return getToken(HTMLParser.ENDBLOCK, 0); }
		public Block_blockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterBlock_block(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitBlock_block(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitBlock_block(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Block_blockContext block_block() throws RecognitionException {
		Block_blockContext _localctx = new Block_blockContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_block_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			match(OPEN_TAG);
			setState(246);
			match(BLOCK);
			setState(247);
			match(ID);
			setState(248);
			match(CLOSE_TAG);
			setState(249);
			body();
			setState(250);
			match(OPEN_TAG);
			setState(251);
			match(ENDBLOCK);
			setState(252);
			match(CLOSE_TAG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Extends_stmtContext extends ParserRuleContext {
		public TerminalNode OPEN_TAG() { return getToken(HTMLParser.OPEN_TAG, 0); }
		public TerminalNode EXTENDS() { return getToken(HTMLParser.EXTENDS, 0); }
		public TerminalNode STRING() { return getToken(HTMLParser.STRING, 0); }
		public TerminalNode CLOSE_TAG() { return getToken(HTMLParser.CLOSE_TAG, 0); }
		public Extends_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extends_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterExtends_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitExtends_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitExtends_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Extends_stmtContext extends_stmt() throws RecognitionException {
		Extends_stmtContext _localctx = new Extends_stmtContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_extends_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			match(OPEN_TAG);
			setState(255);
			match(EXTENDS);
			setState(256);
			match(STRING);
			setState(257);
			match(CLOSE_TAG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Include_stmtContext extends ParserRuleContext {
		public TerminalNode OPEN_TAG() { return getToken(HTMLParser.OPEN_TAG, 0); }
		public TerminalNode INCLUDE() { return getToken(HTMLParser.INCLUDE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode CLOSE_TAG() { return getToken(HTMLParser.CLOSE_TAG, 0); }
		public Include_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_include_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterInclude_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitInclude_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitInclude_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Include_stmtContext include_stmt() throws RecognitionException {
		Include_stmtContext _localctx = new Include_stmtContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_include_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(259);
			match(OPEN_TAG);
			setState(260);
			match(INCLUDE);
			setState(261);
			expr(0);
			setState(262);
			match(CLOSE_TAG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryExpressionContext extends ExprContext {
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public PrimaryExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BinaryExpressionContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode STAR() { return getToken(HTMLParser.STAR, 0); }
		public TerminalNode SLASH() { return getToken(HTMLParser.SLASH, 0); }
		public TerminalNode PERCENT() { return getToken(HTMLParser.PERCENT, 0); }
		public TerminalNode PLUS() { return getToken(HTMLParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(HTMLParser.MINUS, 0); }
		public TerminalNode LT() { return getToken(HTMLParser.LT, 0); }
		public TerminalNode GT() { return getToken(HTMLParser.GT, 0); }
		public TerminalNode LTE() { return getToken(HTMLParser.LTE, 0); }
		public TerminalNode GTE() { return getToken(HTMLParser.GTE, 0); }
		public TerminalNode EQ() { return getToken(HTMLParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(HTMLParser.NEQ, 0); }
		public TerminalNode IN() { return getToken(HTMLParser.IN, 0); }
		public TerminalNode IS() { return getToken(HTMLParser.IS, 0); }
		public TerminalNode AND() { return getToken(HTMLParser.AND, 0); }
		public TerminalNode OR() { return getToken(HTMLParser.OR, 0); }
		public BinaryExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterBinaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitBinaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitBinaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryExpressionContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(HTMLParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(HTMLParser.MINUS, 0); }
		public TerminalNode NOT() { return getToken(HTMLParser.NOT, 0); }
		public UnaryExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterUnaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitUnaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitUnaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IDTrFlExpressionContext extends ExprContext {
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public List<TrailerContext> trailer() {
			return getRuleContexts(TrailerContext.class);
		}
		public TrailerContext trailer(int i) {
			return getRuleContext(TrailerContext.class,i);
		}
		public List<FilterContext> filter() {
			return getRuleContexts(FilterContext.class);
		}
		public FilterContext filter(int i) {
			return getRuleContext(FilterContext.class,i);
		}
		public IDTrFlExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterIDTrFlExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitIDTrFlExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitIDTrFlExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 46;
		enterRecursionRule(_localctx, 46, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				_localctx = new IDTrFlExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(265);
				primary();
				setState(270);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						setState(268);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case DOT:
						case LPAREN:
						case LBRACK:
							{
							setState(266);
							trailer();
							}
							break;
						case PIPE:
							{
							setState(267);
							filter();
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						} 
					}
					setState(272);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				}
				}
				break;
			case 2:
				{
				_localctx = new PrimaryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(273);
				primary();
				}
				break;
			case 3:
				{
				_localctx = new UnaryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(274);
				((UnaryExpressionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 862017116176384L) != 0)) ) {
					((UnaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(275);
				expr(6);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(295);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(293);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(278);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(279);
						((BinaryExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 7881299347898368L) != 0)) ) {
							((BinaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(280);
						expr(6);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(281);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(282);
						((BinaryExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((BinaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(283);
						expr(5);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(284);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(285);
						((BinaryExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 567455752139046912L) != 0)) ) {
							((BinaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(286);
						expr(4);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(287);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(288);
						((BinaryExpressionContext)_localctx).op = match(AND);
						setState(289);
						expr(3);
						}
						break;
					case 5:
						{
						_localctx = new BinaryExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(290);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(291);
						((BinaryExpressionContext)_localctx).op = match(OR);
						setState(292);
						expr(2);
						}
						break;
					}
					} 
				}
				setState(297);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TrailerContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(HTMLParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(HTMLParser.RPAREN, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public TerminalNode DOT() { return getToken(HTMLParser.DOT, 0); }
		public TerminalNode ID() { return getToken(HTMLParser.ID, 0); }
		public TerminalNode LBRACK() { return getToken(HTMLParser.LBRACK, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACK() { return getToken(HTMLParser.RBRACK, 0); }
		public TrailerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trailer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterTrailer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitTrailer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitTrailer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TrailerContext trailer() throws RecognitionException {
		TrailerContext _localctx = new TrailerContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_trailer);
		int _la;
		try {
			setState(309);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(298);
				match(LPAREN);
				setState(300);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & 491782207L) != 0)) {
					{
					setState(299);
					arguments();
					}
				}

				setState(302);
				match(RPAREN);
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(303);
				match(DOT);
				setState(304);
				match(ID);
				}
				break;
			case LBRACK:
				enterOuterAlt(_localctx, 3);
				{
				setState(305);
				match(LBRACK);
				setState(306);
				expr(0);
				setState(307);
				match(RBRACK);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FilterContext extends ParserRuleContext {
		public TerminalNode PIPE() { return getToken(HTMLParser.PIPE, 0); }
		public TerminalNode ID() { return getToken(HTMLParser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(HTMLParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(HTMLParser.RPAREN, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public FilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitFilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterContext filter() throws RecognitionException {
		FilterContext _localctx = new FilterContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_filter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			match(PIPE);
			setState(312);
			match(ID);
			setState(318);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(313);
				match(LPAREN);
				setState(315);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & 491782207L) != 0)) {
					{
					setState(314);
					arguments();
					}
				}

				setState(317);
				match(RPAREN);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentsContext extends ParserRuleContext {
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(HTMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(HTMLParser.COMMA, i);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(320);
			argument();
			setState(325);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(321);
				match(COMMA);
				setState(322);
				argument();
				}
				}
				setState(327);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ASSIGN() { return getToken(HTMLParser.ASSIGN, 0); }
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_argument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			expr(0);
			setState(331);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(329);
				match(ASSIGN);
				setState(330);
				expr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryContext extends ParserRuleContext {
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
	 
		public PrimaryContext() { }
		public void copyFrom(PrimaryContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DictionaryContext extends PrimaryContext {
		public DictdefContext dictdef() {
			return getRuleContext(DictdefContext.class,0);
		}
		public DictionaryContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterDictionary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitDictionary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitDictionary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumberContext extends PrimaryContext {
		public TerminalNode NUMBER() { return getToken(HTMLParser.NUMBER, 0); }
		public TerminalNode MINUS() { return getToken(HTMLParser.MINUS, 0); }
		public NumberContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenExpressionContext extends PrimaryContext {
		public TerminalNode LPAREN() { return getToken(HTMLParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(HTMLParser.RPAREN, 0); }
		public ParenExpressionContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterParenExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitParenExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitParenExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ListContext extends PrimaryContext {
		public ListdefContext listdef() {
			return getRuleContext(ListdefContext.class,0);
		}
		public ListContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitList(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IDContext extends PrimaryContext {
		public TerminalNode ID() { return getToken(HTMLParser.ID, 0); }
		public IDContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitID(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringContext extends PrimaryContext {
		public TerminalNode STRING() { return getToken(HTMLParser.STRING, 0); }
		public StringContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BooleanContext extends PrimaryContext {
		public TerminalNode TRUE() { return getToken(HTMLParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(HTMLParser.FALSE, 0); }
		public BooleanContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitBoolean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitBoolean(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NoneContext extends PrimaryContext {
		public TerminalNode NONE() { return getToken(HTMLParser.NONE, 0); }
		public NoneContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterNone(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitNone(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitNone(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_primary);
		int _la;
		try {
			setState(348);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				_localctx = new ParenExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(333);
				match(LPAREN);
				setState(334);
				expr(0);
				setState(335);
				match(RPAREN);
				}
				break;
			case ID:
				_localctx = new IDContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(337);
				match(ID);
				}
				break;
			case TRUE:
				_localctx = new BooleanContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(338);
				match(TRUE);
				}
				break;
			case FALSE:
				_localctx = new BooleanContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(339);
				match(FALSE);
				}
				break;
			case MINUS:
			case NUMBER:
				_localctx = new NumberContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(341);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(340);
					match(MINUS);
					}
				}

				setState(343);
				match(NUMBER);
				}
				break;
			case NONE:
				_localctx = new NoneContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(344);
				match(NONE);
				}
				break;
			case STRING:
				_localctx = new StringContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(345);
				match(STRING);
				}
				break;
			case LBRACK:
				_localctx = new ListContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(346);
				listdef();
				}
				break;
			case LBRACE:
				_localctx = new DictionaryContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(347);
				dictdef();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListdefContext extends ParserRuleContext {
		public TerminalNode LBRACK() { return getToken(HTMLParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(HTMLParser.RBRACK, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(HTMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(HTMLParser.COMMA, i);
		}
		public ListdefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listdef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterListdef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitListdef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitListdef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListdefContext listdef() throws RecognitionException {
		ListdefContext _localctx = new ListdefContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_listdef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			match(LBRACK);
			setState(359);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & 491782207L) != 0)) {
				{
				setState(351);
				expr(0);
				setState(356);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(352);
					match(COMMA);
					setState(353);
					expr(0);
					}
					}
					setState(358);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(361);
			match(RBRACK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DictdefContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(HTMLParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(HTMLParser.RBRACE, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COLON() { return getTokens(HTMLParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(HTMLParser.COLON, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(HTMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(HTMLParser.COMMA, i);
		}
		public DictdefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictdef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).enterDictdef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HTMLParserListener ) ((HTMLParserListener)listener).exitDictdef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof HTMLParserVisitor ) return ((HTMLParserVisitor<? extends T>)visitor).visitDictdef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictdefContext dictdef() throws RecognitionException {
		DictdefContext _localctx = new DictdefContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_dictdef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			match(LBRACE);
			setState(378);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & 491782207L) != 0)) {
				{
				{
				setState(364);
				expr(0);
				setState(365);
				match(COLON);
				setState(366);
				expr(0);
				}
				setState(375);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(368);
					match(COMMA);
					{
					setState(369);
					expr(0);
					setState(370);
					match(COLON);
					setState(371);
					expr(0);
					}
					}
					}
					setState(377);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(380);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 23:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 5);
		case 1:
			return precpred(_ctx, 4);
		case 2:
			return precpred(_ctx, 3);
		case 3:
			return precpred(_ctx, 2);
		case 4:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001K\u017f\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0001\u0000\u0005\u0000@\b\u0000\n\u0000\f\u0000C\t\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0003\u0001L\b\u0001\u0001\u0002\u0001\u0002\u0003\u0002P\b\u0002\u0001"+
		"\u0003\u0001\u0003\u0005\u0003T\b\u0003\n\u0003\f\u0003W\t\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004^\b"+
		"\u0004\n\u0004\f\u0004a\t\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0005\u0006m\b\u0006\n\u0006\f\u0006p\t\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007w\b\u0007\u0001"+
		"\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0003\n\u0084\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0003"+
		"\u000b\u0089\b\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u0090"+
		"\b\f\n\f\f\f\u0093\t\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005\r\u00a8\b\r\n\r\f\r\u00ab\t\r"+
		"\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u00b1\b\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\u000e\u0005\u000e\u00b8\b\u000e\n\u000e\f\u000e\u00bb"+
		"\t\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0005"+
		"\u000f\u00c2\b\u000f\n\u000f\f\u000f\u00c5\t\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0005\u0010\u00d0\b\u0010\n\u0010\f\u0010\u00d3\t\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00e0"+
		"\b\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u00ec"+
		"\b\u0012\n\u0012\f\u0012\u00ef\t\u0012\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0003\u0013\u00f4\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0005\u0017\u010d\b\u0017\n\u0017\f\u0017\u0110\t\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u0115\b\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0005\u0017\u0126\b\u0017\n\u0017\f\u0017\u0129\t\u0017"+
		"\u0001\u0018\u0001\u0018\u0003\u0018\u012d\b\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018"+
		"\u0136\b\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019"+
		"\u013c\b\u0019\u0001\u0019\u0003\u0019\u013f\b\u0019\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0005\u001a\u0144\b\u001a\n\u001a\f\u001a\u0147\t\u001a"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0003\u001b\u014c\b\u001b\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0003\u001c\u0156\b\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0003\u001c\u015d\b\u001c\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0005\u001d\u0163\b\u001d\n\u001d\f\u001d\u0166"+
		"\t\u001d\u0003\u001d\u0168\b\u001d\u0001\u001d\u0001\u001d\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0005\u001e\u0176\b\u001e\n\u001e"+
		"\f\u001e\u0179\t\u001e\u0003\u001e\u017b\b\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0000\u0001.\u001f\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<\u0000\u0005"+
		"\u0001\u0000\r\u000e\u0002\u0000,,01\u0001\u000024\u0001\u000001\u0003"+
		"\u0000\u001a\u001a))5:\u0195\u0000A\u0001\u0000\u0000\u0000\u0002K\u0001"+
		"\u0000\u0000\u0000\u0004O\u0001\u0000\u0000\u0000\u0006Q\u0001\u0000\u0000"+
		"\u0000\bZ\u0001\u0000\u0000\u0000\nd\u0001\u0000\u0000\u0000\fi\u0001"+
		"\u0000\u0000\u0000\u000es\u0001\u0000\u0000\u0000\u0010x\u0001\u0000\u0000"+
		"\u0000\u0012z\u0001\u0000\u0000\u0000\u0014\u0083\u0001\u0000\u0000\u0000"+
		"\u0016\u0088\u0001\u0000\u0000\u0000\u0018\u008a\u0001\u0000\u0000\u0000"+
		"\u001a\u009c\u0001\u0000\u0000\u0000\u001c\u00b9\u0001\u0000\u0000\u0000"+
		"\u001e\u00bc\u0001\u0000\u0000\u0000 \u00ca\u0001\u0000\u0000\u0000\""+
		"\u00da\u0001\u0000\u0000\u0000$\u00e8\u0001\u0000\u0000\u0000&\u00f0\u0001"+
		"\u0000\u0000\u0000(\u00f5\u0001\u0000\u0000\u0000*\u00fe\u0001\u0000\u0000"+
		"\u0000,\u0103\u0001\u0000\u0000\u0000.\u0114\u0001\u0000\u0000\u00000"+
		"\u0135\u0001\u0000\u0000\u00002\u0137\u0001\u0000\u0000\u00004\u0140\u0001"+
		"\u0000\u0000\u00006\u0148\u0001\u0000\u0000\u00008\u015c\u0001\u0000\u0000"+
		"\u0000:\u015e\u0001\u0000\u0000\u0000<\u016b\u0001\u0000\u0000\u0000>"+
		"@\u0003\u0002\u0001\u0000?>\u0001\u0000\u0000\u0000@C\u0001\u0000\u0000"+
		"\u0000A?\u0001\u0000\u0000\u0000AB\u0001\u0000\u0000\u0000BD\u0001\u0000"+
		"\u0000\u0000CA\u0001\u0000\u0000\u0000DE\u0005\u0000\u0000\u0001E\u0001"+
		"\u0001\u0000\u0000\u0000FL\u0003\u0012\t\u0000GL\u0003\u0014\n\u0000H"+
		"L\u0003\u0016\u000b\u0000IL\u0003\u0004\u0002\u0000JL\u0005\b\u0000\u0000"+
		"KF\u0001\u0000\u0000\u0000KG\u0001\u0000\u0000\u0000KH\u0001\u0000\u0000"+
		"\u0000KI\u0001\u0000\u0000\u0000KJ\u0001\u0000\u0000\u0000L\u0003\u0001"+
		"\u0000\u0000\u0000MP\u0003\u0006\u0003\u0000NP\u0003\f\u0006\u0000OM\u0001"+
		"\u0000\u0000\u0000ON\u0001\u0000\u0000\u0000P\u0005\u0001\u0000\u0000"+
		"\u0000QU\u0003\b\u0004\u0000RT\u0003\u0002\u0001\u0000SR\u0001\u0000\u0000"+
		"\u0000TW\u0001\u0000\u0000\u0000US\u0001\u0000\u0000\u0000UV\u0001\u0000"+
		"\u0000\u0000VX\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000\u0000XY\u0003"+
		"\n\u0005\u0000Y\u0007\u0001\u0000\u0000\u0000Z[\u0005\u0007\u0000\u0000"+
		"[_\u0005\r\u0000\u0000\\^\u0003\u000e\u0007\u0000]\\\u0001\u0000\u0000"+
		"\u0000^a\u0001\u0000\u0000\u0000_]\u0001\u0000\u0000\u0000_`\u0001\u0000"+
		"\u0000\u0000`b\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000bc\u0005"+
		"\t\u0000\u0000c\t\u0001\u0000\u0000\u0000de\u0005\u0007\u0000\u0000ef"+
		"\u0005\u000b\u0000\u0000fg\u0005\r\u0000\u0000gh\u0005\t\u0000\u0000h"+
		"\u000b\u0001\u0000\u0000\u0000ij\u0005\u0007\u0000\u0000jn\u0005\r\u0000"+
		"\u0000km\u0003\u000e\u0007\u0000lk\u0001\u0000\u0000\u0000mp\u0001\u0000"+
		"\u0000\u0000nl\u0001\u0000\u0000\u0000no\u0001\u0000\u0000\u0000oq\u0001"+
		"\u0000\u0000\u0000pn\u0001\u0000\u0000\u0000qr\u0005\n\u0000\u0000r\r"+
		"\u0001\u0000\u0000\u0000sv\u0003\u0010\b\u0000tu\u0005\f\u0000\u0000u"+
		"w\u0005\u0014\u0000\u0000vt\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000"+
		"\u0000w\u000f\u0001\u0000\u0000\u0000xy\u0007\u0000\u0000\u0000y\u0011"+
		"\u0001\u0000\u0000\u0000z{\u0005\u0002\u0000\u0000{|\u0003.\u0017\u0000"+
		"|}\u0005\u0017\u0000\u0000}\u0013\u0001\u0000\u0000\u0000~\u0084\u0003"+
		"\u0018\f\u0000\u007f\u0084\u0003\u001a\r\u0000\u0080\u0084\u0003 \u0010"+
		"\u0000\u0081\u0084\u0003\"\u0011\u0000\u0082\u0084\u0003(\u0014\u0000"+
		"\u0083~\u0001\u0000\u0000\u0000\u0083\u007f\u0001\u0000\u0000\u0000\u0083"+
		"\u0080\u0001\u0000\u0000\u0000\u0083\u0081\u0001\u0000\u0000\u0000\u0083"+
		"\u0082\u0001\u0000\u0000\u0000\u0084\u0015\u0001\u0000\u0000\u0000\u0085"+
		"\u0089\u0003*\u0015\u0000\u0086\u0089\u0003,\u0016\u0000\u0087\u0089\u0003"+
		"\u001e\u000f\u0000\u0088\u0085\u0001\u0000\u0000\u0000\u0088\u0086\u0001"+
		"\u0000\u0000\u0000\u0088\u0087\u0001\u0000\u0000\u0000\u0089\u0017\u0001"+
		"\u0000\u0000\u0000\u008a\u008b\u0005\u0003\u0000\u0000\u008b\u008c\u0005"+
		"\u0019\u0000\u0000\u008c\u0091\u0005H\u0000\u0000\u008d\u008e\u0005>\u0000"+
		"\u0000\u008e\u0090\u0005H\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000"+
		"\u0090\u0093\u0001\u0000\u0000\u0000\u0091\u008f\u0001\u0000\u0000\u0000"+
		"\u0091\u0092\u0001\u0000\u0000\u0000\u0092\u0094\u0001\u0000\u0000\u0000"+
		"\u0093\u0091\u0001\u0000\u0000\u0000\u0094\u0095\u0005\u001a\u0000\u0000"+
		"\u0095\u0096\u0003.\u0017\u0000\u0096\u0097\u0005\u0016\u0000\u0000\u0097"+
		"\u0098\u0003\u001c\u000e\u0000\u0098\u0099\u0005\u0003\u0000\u0000\u0099"+
		"\u009a\u0005\u001b\u0000\u0000\u009a\u009b\u0005\u0016\u0000\u0000\u009b"+
		"\u0019\u0001\u0000\u0000\u0000\u009c\u009d\u0005\u0003\u0000\u0000\u009d"+
		"\u009e\u0005\u001c\u0000\u0000\u009e\u009f\u0003.\u0017\u0000\u009f\u00a0"+
		"\u0005\u0016\u0000\u0000\u00a0\u00a9\u0003\u001c\u000e\u0000\u00a1\u00a2"+
		"\u0005\u0003\u0000\u0000\u00a2\u00a3\u0005\u001d\u0000\u0000\u00a3\u00a4"+
		"\u0003.\u0017\u0000\u00a4\u00a5\u0005\u0016\u0000\u0000\u00a5\u00a6\u0003"+
		"\u001c\u000e\u0000\u00a6\u00a8\u0001\u0000\u0000\u0000\u00a7\u00a1\u0001"+
		"\u0000\u0000\u0000\u00a8\u00ab\u0001\u0000\u0000\u0000\u00a9\u00a7\u0001"+
		"\u0000\u0000\u0000\u00a9\u00aa\u0001\u0000\u0000\u0000\u00aa\u00b0\u0001"+
		"\u0000\u0000\u0000\u00ab\u00a9\u0001\u0000\u0000\u0000\u00ac\u00ad\u0005"+
		"\u0003\u0000\u0000\u00ad\u00ae\u0005\u001e\u0000\u0000\u00ae\u00af\u0005"+
		"\u0016\u0000\u0000\u00af\u00b1\u0003\u001c\u000e\u0000\u00b0\u00ac\u0001"+
		"\u0000\u0000\u0000\u00b0\u00b1\u0001\u0000\u0000\u0000\u00b1\u00b2\u0001"+
		"\u0000\u0000\u0000\u00b2\u00b3\u0005\u0003\u0000\u0000\u00b3\u00b4\u0005"+
		"\u001f\u0000\u0000\u00b4\u00b5\u0005\u0016\u0000\u0000\u00b5\u001b\u0001"+
		"\u0000\u0000\u0000\u00b6\u00b8\u0003\u0002\u0001\u0000\u00b7\u00b6\u0001"+
		"\u0000\u0000\u0000\u00b8\u00bb\u0001\u0000\u0000\u0000\u00b9\u00b7\u0001"+
		"\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000\u0000\u00ba\u001d\u0001"+
		"\u0000\u0000\u0000\u00bb\u00b9\u0001\u0000\u0000\u0000\u00bc\u00bd\u0005"+
		"\u0003\u0000\u0000\u00bd\u00be\u0005$\u0000\u0000\u00be\u00c3\u0005H\u0000"+
		"\u0000\u00bf\u00c0\u0005>\u0000\u0000\u00c0\u00c2\u0005H\u0000\u0000\u00c1"+
		"\u00bf\u0001\u0000\u0000\u0000\u00c2\u00c5\u0001\u0000\u0000\u0000\u00c3"+
		"\u00c1\u0001\u0000\u0000\u0000\u00c3\u00c4\u0001\u0000\u0000\u0000\u00c4"+
		"\u00c6\u0001\u0000\u0000\u0000\u00c5\u00c3\u0001\u0000\u0000\u0000\u00c6"+
		"\u00c7\u0005;\u0000\u0000\u00c7\u00c8\u0003.\u0017\u0000\u00c8\u00c9\u0005"+
		"\u0016\u0000\u0000\u00c9\u001f\u0001\u0000\u0000\u0000\u00ca\u00cb\u0005"+
		"\u0003\u0000\u0000\u00cb\u00cc\u0005$\u0000\u0000\u00cc\u00d1\u0005H\u0000"+
		"\u0000\u00cd\u00ce\u0005>\u0000\u0000\u00ce\u00d0\u0005H\u0000\u0000\u00cf"+
		"\u00cd\u0001\u0000\u0000\u0000\u00d0\u00d3\u0001\u0000\u0000\u0000\u00d1"+
		"\u00cf\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2"+
		"\u00d4\u0001\u0000\u0000\u0000\u00d3\u00d1\u0001\u0000\u0000\u0000\u00d4"+
		"\u00d5\u0005\u0016\u0000\u0000\u00d5\u00d6\u0003\u001c\u000e\u0000\u00d6"+
		"\u00d7\u0005\u0003\u0000\u0000\u00d7\u00d8\u0005%\u0000\u0000\u00d8\u00d9"+
		"\u0005\u0016\u0000\u0000\u00d9!\u0001\u0000\u0000\u0000\u00da\u00db\u0005"+
		"\u0003\u0000\u0000\u00db\u00dc\u0005\"\u0000\u0000\u00dc\u00dd\u0005H"+
		"\u0000\u0000\u00dd\u00df\u0005@\u0000\u0000\u00de\u00e0\u0003$\u0012\u0000"+
		"\u00df\u00de\u0001\u0000\u0000\u0000\u00df\u00e0\u0001\u0000\u0000\u0000"+
		"\u00e0\u00e1\u0001\u0000\u0000\u0000\u00e1\u00e2\u0005A\u0000\u0000\u00e2"+
		"\u00e3\u0005\u0016\u0000\u0000\u00e3\u00e4\u0003\u001c\u000e\u0000\u00e4"+
		"\u00e5\u0005\u0003\u0000\u0000\u00e5\u00e6\u0005#\u0000\u0000\u00e6\u00e7"+
		"\u0005\u0016\u0000\u0000\u00e7#\u0001\u0000\u0000\u0000\u00e8\u00ed\u0003"+
		"&\u0013\u0000\u00e9\u00ea\u0005>\u0000\u0000\u00ea\u00ec\u0003&\u0013"+
		"\u0000\u00eb\u00e9\u0001\u0000\u0000\u0000\u00ec\u00ef\u0001\u0000\u0000"+
		"\u0000\u00ed\u00eb\u0001\u0000\u0000\u0000\u00ed\u00ee\u0001\u0000\u0000"+
		"\u0000\u00ee%\u0001\u0000\u0000\u0000\u00ef\u00ed\u0001\u0000\u0000\u0000"+
		"\u00f0\u00f3\u0005H\u0000\u0000\u00f1\u00f2\u0005;\u0000\u0000\u00f2\u00f4"+
		"\u0003.\u0017\u0000\u00f3\u00f1\u0001\u0000\u0000\u0000\u00f3\u00f4\u0001"+
		"\u0000\u0000\u0000\u00f4\'\u0001\u0000\u0000\u0000\u00f5\u00f6\u0005\u0003"+
		"\u0000\u0000\u00f6\u00f7\u0005 \u0000\u0000\u00f7\u00f8\u0005H\u0000\u0000"+
		"\u00f8\u00f9\u0005\u0016\u0000\u0000\u00f9\u00fa\u0003\u001c\u000e\u0000"+
		"\u00fa\u00fb\u0005\u0003\u0000\u0000\u00fb\u00fc\u0005!\u0000\u0000\u00fc"+
		"\u00fd\u0005\u0016\u0000\u0000\u00fd)\u0001\u0000\u0000\u0000\u00fe\u00ff"+
		"\u0005\u0003\u0000\u0000\u00ff\u0100\u0005&\u0000\u0000\u0100\u0101\u0005"+
		"F\u0000\u0000\u0101\u0102\u0005\u0016\u0000\u0000\u0102+\u0001\u0000\u0000"+
		"\u0000\u0103\u0104\u0005\u0003\u0000\u0000\u0104\u0105\u0005\u0018\u0000"+
		"\u0000\u0105\u0106\u0003.\u0017\u0000\u0106\u0107\u0005\u0016\u0000\u0000"+
		"\u0107-\u0001\u0000\u0000\u0000\u0108\u0109\u0006\u0017\uffff\uffff\u0000"+
		"\u0109\u010e\u00038\u001c\u0000\u010a\u010d\u00030\u0018\u0000\u010b\u010d"+
		"\u00032\u0019\u0000\u010c\u010a\u0001\u0000\u0000\u0000\u010c\u010b\u0001"+
		"\u0000\u0000\u0000\u010d\u0110\u0001\u0000\u0000\u0000\u010e\u010c\u0001"+
		"\u0000\u0000\u0000\u010e\u010f\u0001\u0000\u0000\u0000\u010f\u0115\u0001"+
		"\u0000\u0000\u0000\u0110\u010e\u0001\u0000\u0000\u0000\u0111\u0115\u0003"+
		"8\u001c\u0000\u0112\u0113\u0007\u0001\u0000\u0000\u0113\u0115\u0003.\u0017"+
		"\u0006\u0114\u0108\u0001\u0000\u0000\u0000\u0114\u0111\u0001\u0000\u0000"+
		"\u0000\u0114\u0112\u0001\u0000\u0000\u0000\u0115\u0127\u0001\u0000\u0000"+
		"\u0000\u0116\u0117\n\u0005\u0000\u0000\u0117\u0118\u0007\u0002\u0000\u0000"+
		"\u0118\u0126\u0003.\u0017\u0006\u0119\u011a\n\u0004\u0000\u0000\u011a"+
		"\u011b\u0007\u0003\u0000\u0000\u011b\u0126\u0003.\u0017\u0005\u011c\u011d"+
		"\n\u0003\u0000\u0000\u011d\u011e\u0007\u0004\u0000\u0000\u011e\u0126\u0003"+
		".\u0017\u0004\u011f\u0120\n\u0002\u0000\u0000\u0120\u0121\u0005+\u0000"+
		"\u0000\u0121\u0126\u0003.\u0017\u0003\u0122\u0123\n\u0001\u0000\u0000"+
		"\u0123\u0124\u0005*\u0000\u0000\u0124\u0126\u0003.\u0017\u0002\u0125\u0116"+
		"\u0001\u0000\u0000\u0000\u0125\u0119\u0001\u0000\u0000\u0000\u0125\u011c"+
		"\u0001\u0000\u0000\u0000\u0125\u011f\u0001\u0000\u0000\u0000\u0125\u0122"+
		"\u0001\u0000\u0000\u0000\u0126\u0129\u0001\u0000\u0000\u0000\u0127\u0125"+
		"\u0001\u0000\u0000\u0000\u0127\u0128\u0001\u0000\u0000\u0000\u0128/\u0001"+
		"\u0000\u0000\u0000\u0129\u0127\u0001\u0000\u0000\u0000\u012a\u012c\u0005"+
		"@\u0000\u0000\u012b\u012d\u00034\u001a\u0000\u012c\u012b\u0001\u0000\u0000"+
		"\u0000\u012c\u012d\u0001\u0000\u0000\u0000\u012d\u012e\u0001\u0000\u0000"+
		"\u0000\u012e\u0136\u0005A\u0000\u0000\u012f\u0130\u0005=\u0000\u0000\u0130"+
		"\u0136\u0005H\u0000\u0000\u0131\u0132\u0005B\u0000\u0000\u0132\u0133\u0003"+
		".\u0017\u0000\u0133\u0134\u0005C\u0000\u0000\u0134\u0136\u0001\u0000\u0000"+
		"\u0000\u0135\u012a\u0001\u0000\u0000\u0000\u0135\u012f\u0001\u0000\u0000"+
		"\u0000\u0135\u0131\u0001\u0000\u0000\u0000\u01361\u0001\u0000\u0000\u0000"+
		"\u0137\u0138\u0005<\u0000\u0000\u0138\u013e\u0005H\u0000\u0000\u0139\u013b"+
		"\u0005@\u0000\u0000\u013a\u013c\u00034\u001a\u0000\u013b\u013a\u0001\u0000"+
		"\u0000\u0000\u013b\u013c\u0001\u0000\u0000\u0000\u013c\u013d\u0001\u0000"+
		"\u0000\u0000\u013d\u013f\u0005A\u0000\u0000\u013e\u0139\u0001\u0000\u0000"+
		"\u0000\u013e\u013f\u0001\u0000\u0000\u0000\u013f3\u0001\u0000\u0000\u0000"+
		"\u0140\u0145\u00036\u001b\u0000\u0141\u0142\u0005>\u0000\u0000\u0142\u0144"+
		"\u00036\u001b\u0000\u0143\u0141\u0001\u0000\u0000\u0000\u0144\u0147\u0001"+
		"\u0000\u0000\u0000\u0145\u0143\u0001\u0000\u0000\u0000\u0145\u0146\u0001"+
		"\u0000\u0000\u0000\u01465\u0001\u0000\u0000\u0000\u0147\u0145\u0001\u0000"+
		"\u0000\u0000\u0148\u014b\u0003.\u0017\u0000\u0149\u014a\u0005;\u0000\u0000"+
		"\u014a\u014c\u0003.\u0017\u0000\u014b\u0149\u0001\u0000\u0000\u0000\u014b"+
		"\u014c\u0001\u0000\u0000\u0000\u014c7\u0001\u0000\u0000\u0000\u014d\u014e"+
		"\u0005@\u0000\u0000\u014e\u014f\u0003.\u0017\u0000\u014f\u0150\u0005A"+
		"\u0000\u0000\u0150\u015d\u0001\u0000\u0000\u0000\u0151\u015d\u0005H\u0000"+
		"\u0000\u0152\u015d\u0005-\u0000\u0000\u0153\u015d\u0005.\u0000\u0000\u0154"+
		"\u0156\u00051\u0000\u0000\u0155\u0154\u0001\u0000\u0000\u0000\u0155\u0156"+
		"\u0001\u0000\u0000\u0000\u0156\u0157\u0001\u0000\u0000\u0000\u0157\u015d"+
		"\u0005G\u0000\u0000\u0158\u015d\u0005/\u0000\u0000\u0159\u015d\u0005F"+
		"\u0000\u0000\u015a\u015d\u0003:\u001d\u0000\u015b\u015d\u0003<\u001e\u0000"+
		"\u015c\u014d\u0001\u0000\u0000\u0000\u015c\u0151\u0001\u0000\u0000\u0000"+
		"\u015c\u0152\u0001\u0000\u0000\u0000\u015c\u0153\u0001\u0000\u0000\u0000"+
		"\u015c\u0155\u0001\u0000\u0000\u0000\u015c\u0158\u0001\u0000\u0000\u0000"+
		"\u015c\u0159\u0001\u0000\u0000\u0000\u015c\u015a\u0001\u0000\u0000\u0000"+
		"\u015c\u015b\u0001\u0000\u0000\u0000\u015d9\u0001\u0000\u0000\u0000\u015e"+
		"\u0167\u0005B\u0000\u0000\u015f\u0164\u0003.\u0017\u0000\u0160\u0161\u0005"+
		">\u0000\u0000\u0161\u0163\u0003.\u0017\u0000\u0162\u0160\u0001\u0000\u0000"+
		"\u0000\u0163\u0166\u0001\u0000\u0000\u0000\u0164\u0162\u0001\u0000\u0000"+
		"\u0000\u0164\u0165\u0001\u0000\u0000\u0000\u0165\u0168\u0001\u0000\u0000"+
		"\u0000\u0166\u0164\u0001\u0000\u0000\u0000\u0167\u015f\u0001\u0000\u0000"+
		"\u0000\u0167\u0168\u0001\u0000\u0000\u0000\u0168\u0169\u0001\u0000\u0000"+
		"\u0000\u0169\u016a\u0005C\u0000\u0000\u016a;\u0001\u0000\u0000\u0000\u016b"+
		"\u017a\u0005D\u0000\u0000\u016c\u016d\u0003.\u0017\u0000\u016d\u016e\u0005"+
		"?\u0000\u0000\u016e\u016f\u0003.\u0017\u0000\u016f\u0177\u0001\u0000\u0000"+
		"\u0000\u0170\u0171\u0005>\u0000\u0000\u0171\u0172\u0003.\u0017\u0000\u0172"+
		"\u0173\u0005?\u0000\u0000\u0173\u0174\u0003.\u0017\u0000\u0174\u0176\u0001"+
		"\u0000\u0000\u0000\u0175\u0170\u0001\u0000\u0000\u0000\u0176\u0179\u0001"+
		"\u0000\u0000\u0000\u0177\u0175\u0001\u0000\u0000\u0000\u0177\u0178\u0001"+
		"\u0000\u0000\u0000\u0178\u017b\u0001\u0000\u0000\u0000\u0179\u0177\u0001"+
		"\u0000\u0000\u0000\u017a\u016c\u0001\u0000\u0000\u0000\u017a\u017b\u0001"+
		"\u0000\u0000\u0000\u017b\u017c\u0001\u0000\u0000\u0000\u017c\u017d\u0005"+
		"E\u0000\u0000\u017d=\u0001\u0000\u0000\u0000#AKOU_nv\u0083\u0088\u0091"+
		"\u00a9\u00b0\u00b9\u00c3\u00d1\u00df\u00ed\u00f3\u010c\u010e\u0114\u0125"+
		"\u0127\u012c\u0135\u013b\u013e\u0145\u014b\u0155\u015c\u0164\u0167\u0177"+
		"\u017a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}