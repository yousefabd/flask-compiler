// Generated from C:/Users/yahia/IdeaProjects/flaskcomp/grammars/jinja2/Jinja2Parser.g4 by ANTLR 4.13.2

    package antlr.jinja2;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class Jinja2Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DOUBLE_OPEN_BRACE=1, OPEN_TAG=2, COMMENT_START=3, TEXT=4, CLOSE_TAG=5, 
		DOUBLE_CLOSE_BRACE=6, INCLUDE=7, FOR=8, IN=9, ENDFOR=10, IF=11, ELIF=12, 
		ELSE=13, ENDIF=14, BLOCK=15, ENDBLOCK=16, MACRO=17, ENDMACRO=18, SET=19, 
		ENDSET=20, EXTENDS=21, RAW=22, ENDRAW=23, IS=24, OR=25, AND=26, NOT=27, 
		TRUE=28, FALSE=29, NONE=30, PLUS=31, MINUS=32, STAR=33, SLASH=34, PERCENT=35, 
		EQ=36, NEQ=37, LT=38, GT=39, LTE=40, GTE=41, ASSIGN=42, PIPE=43, DOT=44, 
		COMMA=45, COLON=46, LPAREN=47, RPAREN=48, LBRACK=49, RBRACK=50, STRING=51, 
		NUMBER=52, ID=53, WS=54, COMMENT_END=55, COMMENT_TEXT=56;
	public static final int
		RULE_template = 0, RULE_tag = 1, RULE_variable = 2, RULE_stmt = 3, RULE_inline_stmt = 4, 
		RULE_for_block = 5, RULE_if_block = 6, RULE_body = 7, RULE_set_inline = 8, 
		RULE_set_block = 9, RULE_macro_block = 10, RULE_parameters = 11, RULE_parameter = 12, 
		RULE_block_block = 13, RULE_extends_stmt = 14, RULE_include_stmt = 15, 
		RULE_expr = 16, RULE_trailer = 17, RULE_filter = 18, RULE_arguments = 19, 
		RULE_argument = 20, RULE_primary = 21, RULE_listdef = 22, RULE_dictdef = 23;
	private static String[] makeRuleNames() {
		return new String[] {
			"template", "tag", "variable", "stmt", "inline_stmt", "for_block", "if_block", 
			"body", "set_inline", "set_block", "macro_block", "parameters", "parameter", 
			"block_block", "extends_stmt", "include_stmt", "expr", "trailer", "filter", 
			"arguments", "argument", "primary", "listdef", "dictdef"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{{'", "'{%'", "'{#'", null, "'%}'", "'}}'", "'include'", "'for'", 
			"'in'", "'endfor'", "'if'", "'elif'", "'else'", "'endif'", "'block'", 
			"'endblock'", "'macro'", "'endmacro'", "'set'", "'endset'", "'extends'", 
			"'raw'", "'endraw'", "'is'", "'or'", "'and'", "'not'", "'true'", "'false'", 
			"'none'", "'+'", "'-'", "'*'", "'/'", "'%'", "'=='", "'!='", "'<'", "'>'", 
			"'<='", "'>='", "'='", "'|'", "'.'", "','", "':'", "'('", "')'", "'['", 
			"']'", null, null, null, null, "'#}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "DOUBLE_OPEN_BRACE", "OPEN_TAG", "COMMENT_START", "TEXT", "CLOSE_TAG", 
			"DOUBLE_CLOSE_BRACE", "INCLUDE", "FOR", "IN", "ENDFOR", "IF", "ELIF", 
			"ELSE", "ENDIF", "BLOCK", "ENDBLOCK", "MACRO", "ENDMACRO", "SET", "ENDSET", 
			"EXTENDS", "RAW", "ENDRAW", "IS", "OR", "AND", "NOT", "TRUE", "FALSE", 
			"NONE", "PLUS", "MINUS", "STAR", "SLASH", "PERCENT", "EQ", "NEQ", "LT", 
			"GT", "LTE", "GTE", "ASSIGN", "PIPE", "DOT", "COMMA", "COLON", "LPAREN", 
			"RPAREN", "LBRACK", "RBRACK", "STRING", "NUMBER", "ID", "WS", "COMMENT_END", 
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
	public String getGrammarFileName() { return "Jinja2Parser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public Jinja2Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(Jinja2Parser.EOF, 0); }
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterTemplate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitTemplate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitTemplate(this);
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
			setState(51);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 22L) != 0)) {
				{
				{
				setState(48);
				tag();
				}
				}
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(54);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterVariableStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitVariableStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitVariableStatement(this);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TextContext extends TagContext {
		public TerminalNode TEXT() { return getToken(Jinja2Parser.TEXT, 0); }
		public TextContext(TagContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitText(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitText(this);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterInlineStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitInlineStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitInlineStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagContext tag() throws RecognitionException {
		TagContext _localctx = new TagContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_tag);
		try {
			setState(60);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new VariableStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(56);
				variable();
				}
				break;
			case 2:
				_localctx = new StatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(57);
				stmt();
				}
				break;
			case 3:
				_localctx = new InlineStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(58);
				inline_stmt();
				}
				break;
			case 4:
				_localctx = new TextContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(59);
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
	public static class VariableContext extends ParserRuleContext {
		public TerminalNode DOUBLE_OPEN_BRACE() { return getToken(Jinja2Parser.DOUBLE_OPEN_BRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DOUBLE_CLOSE_BRACE() { return getToken(Jinja2Parser.DOUBLE_CLOSE_BRACE, 0); }
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
		}
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_variable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			match(DOUBLE_OPEN_BRACE);
			setState(64);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(63);
				match(MINUS);
				}
				break;
			}
			setState(66);
			expr(0);
			setState(68);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(67);
				match(MINUS);
				}
			}

			setState(70);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitIfStatement(this);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterMacroStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitMacroStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitMacroStatement(this);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterBlockStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitBlockStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitBlockStatement(this);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterForStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitForStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitForStatement(this);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterSetStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitSetStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitSetStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_stmt);
		try {
			setState(77);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				_localctx = new ForStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(72);
				for_block();
				}
				break;
			case 2:
				_localctx = new IfStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(73);
				if_block();
				}
				break;
			case 3:
				_localctx = new SetStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(74);
				set_block();
				}
				break;
			case 4:
				_localctx = new MacroStatementContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(75);
				macro_block();
				}
				break;
			case 5:
				_localctx = new BlockStatementContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(76);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterInlineSetStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitInlineSetStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitInlineSetStatement(this);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterInlineExtendsStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitInlineExtendsStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitInlineExtendsStatement(this);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterInlineIncludeStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitInlineIncludeStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitInlineIncludeStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Inline_stmtContext inline_stmt() throws RecognitionException {
		Inline_stmtContext _localctx = new Inline_stmtContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_inline_stmt);
		try {
			setState(82);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				_localctx = new InlineExtendsStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(79);
				extends_stmt();
				}
				break;
			case 2:
				_localctx = new InlineIncludeStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(80);
				include_stmt();
				}
				break;
			case 3:
				_localctx = new InlineSetStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(81);
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
		public List<TerminalNode> OPEN_TAG() { return getTokens(Jinja2Parser.OPEN_TAG); }
		public TerminalNode OPEN_TAG(int i) {
			return getToken(Jinja2Parser.OPEN_TAG, i);
		}
		public TerminalNode FOR() { return getToken(Jinja2Parser.FOR, 0); }
		public TerminalNode IN() { return getToken(Jinja2Parser.IN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<TerminalNode> CLOSE_TAG() { return getTokens(Jinja2Parser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(Jinja2Parser.CLOSE_TAG, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode ENDFOR() { return getToken(Jinja2Parser.ENDFOR, 0); }
		public List<TerminalNode> ID() { return getTokens(Jinja2Parser.ID); }
		public TerminalNode ID(int i) {
			return getToken(Jinja2Parser.ID, i);
		}
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Jinja2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Jinja2Parser.COMMA, i);
		}
		public For_blockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterFor_block(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitFor_block(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitFor_block(this);
			else return visitor.visitChildren(this);
		}
	}

	public final For_blockContext for_block() throws RecognitionException {
		For_blockContext _localctx = new For_blockContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_for_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(OPEN_TAG);
			setState(86);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(85);
				match(MINUS);
				}
			}

			setState(88);
			match(FOR);
			{
			setState(89);
			match(ID);
			setState(94);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(90);
				match(COMMA);
				setState(91);
				match(ID);
				}
				}
				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(97);
			match(IN);
			setState(98);
			expr(0);
			setState(100);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(99);
				match(MINUS);
				}
			}

			setState(102);
			match(CLOSE_TAG);
			setState(103);
			body();
			setState(104);
			match(OPEN_TAG);
			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(105);
				match(MINUS);
				}
			}

			setState(108);
			match(ENDFOR);
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(109);
				match(MINUS);
				}
			}

			setState(112);
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
		public List<TerminalNode> OPEN_TAG() { return getTokens(Jinja2Parser.OPEN_TAG); }
		public TerminalNode OPEN_TAG(int i) {
			return getToken(Jinja2Parser.OPEN_TAG, i);
		}
		public TerminalNode IF() { return getToken(Jinja2Parser.IF, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> CLOSE_TAG() { return getTokens(Jinja2Parser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(Jinja2Parser.CLOSE_TAG, i);
		}
		public List<BodyContext> body() {
			return getRuleContexts(BodyContext.class);
		}
		public BodyContext body(int i) {
			return getRuleContext(BodyContext.class,i);
		}
		public TerminalNode ENDIF() { return getToken(Jinja2Parser.ENDIF, 0); }
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
		}
		public List<TerminalNode> ELIF() { return getTokens(Jinja2Parser.ELIF); }
		public TerminalNode ELIF(int i) {
			return getToken(Jinja2Parser.ELIF, i);
		}
		public TerminalNode ELSE() { return getToken(Jinja2Parser.ELSE, 0); }
		public If_blockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterIf_block(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitIf_block(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitIf_block(this);
			else return visitor.visitChildren(this);
		}
	}

	public final If_blockContext if_block() throws RecognitionException {
		If_blockContext _localctx = new If_blockContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_if_block);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			match(OPEN_TAG);
			setState(116);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(115);
				match(MINUS);
				}
			}

			setState(118);
			match(IF);
			setState(119);
			expr(0);
			setState(121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(120);
				match(MINUS);
				}
			}

			setState(123);
			match(CLOSE_TAG);
			setState(124);
			body();
			setState(139);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(125);
					match(OPEN_TAG);
					setState(127);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==MINUS) {
						{
						setState(126);
						match(MINUS);
						}
					}

					setState(129);
					match(ELIF);
					setState(130);
					expr(0);
					setState(132);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==MINUS) {
						{
						setState(131);
						match(MINUS);
						}
					}

					setState(134);
					match(CLOSE_TAG);
					setState(135);
					body();
					}
					} 
				}
				setState(141);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			setState(152);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(142);
				match(OPEN_TAG);
				setState(144);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(143);
					match(MINUS);
					}
				}

				setState(146);
				match(ELSE);
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(147);
					match(MINUS);
					}
				}

				setState(150);
				match(CLOSE_TAG);
				setState(151);
				body();
				}
				break;
			}
			setState(154);
			match(OPEN_TAG);
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(155);
				match(MINUS);
				}
			}

			setState(158);
			match(ENDIF);
			setState(160);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(159);
				match(MINUS);
				}
			}

			setState(162);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyContext body() throws RecognitionException {
		BodyContext _localctx = new BodyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_body);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(164);
					tag();
					}
					} 
				}
				setState(169);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
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
		public TerminalNode OPEN_TAG() { return getToken(Jinja2Parser.OPEN_TAG, 0); }
		public TerminalNode SET() { return getToken(Jinja2Parser.SET, 0); }
		public TerminalNode ASSIGN() { return getToken(Jinja2Parser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode CLOSE_TAG() { return getToken(Jinja2Parser.CLOSE_TAG, 0); }
		public List<TerminalNode> ID() { return getTokens(Jinja2Parser.ID); }
		public TerminalNode ID(int i) {
			return getToken(Jinja2Parser.ID, i);
		}
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Jinja2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Jinja2Parser.COMMA, i);
		}
		public Set_inlineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_set_inline; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterSet_inline(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitSet_inline(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitSet_inline(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Set_inlineContext set_inline() throws RecognitionException {
		Set_inlineContext _localctx = new Set_inlineContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_set_inline);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			match(OPEN_TAG);
			setState(172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(171);
				match(MINUS);
				}
			}

			setState(174);
			match(SET);
			{
			setState(175);
			match(ID);
			setState(180);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(176);
				match(COMMA);
				setState(177);
				match(ID);
				}
				}
				setState(182);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(183);
			match(ASSIGN);
			setState(184);
			expr(0);
			setState(186);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(185);
				match(MINUS);
				}
			}

			setState(188);
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
		public List<TerminalNode> OPEN_TAG() { return getTokens(Jinja2Parser.OPEN_TAG); }
		public TerminalNode OPEN_TAG(int i) {
			return getToken(Jinja2Parser.OPEN_TAG, i);
		}
		public TerminalNode SET() { return getToken(Jinja2Parser.SET, 0); }
		public List<TerminalNode> CLOSE_TAG() { return getTokens(Jinja2Parser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(Jinja2Parser.CLOSE_TAG, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode ENDSET() { return getToken(Jinja2Parser.ENDSET, 0); }
		public List<TerminalNode> ID() { return getTokens(Jinja2Parser.ID); }
		public TerminalNode ID(int i) {
			return getToken(Jinja2Parser.ID, i);
		}
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Jinja2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Jinja2Parser.COMMA, i);
		}
		public Set_blockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_set_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterSet_block(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitSet_block(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitSet_block(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Set_blockContext set_block() throws RecognitionException {
		Set_blockContext _localctx = new Set_blockContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_set_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			match(OPEN_TAG);
			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(191);
				match(MINUS);
				}
			}

			setState(194);
			match(SET);
			{
			setState(195);
			match(ID);
			setState(200);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(196);
				match(COMMA);
				setState(197);
				match(ID);
				}
				}
				setState(202);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(204);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(203);
				match(MINUS);
				}
			}

			setState(206);
			match(CLOSE_TAG);
			setState(207);
			body();
			setState(208);
			match(OPEN_TAG);
			setState(210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(209);
				match(MINUS);
				}
			}

			setState(212);
			match(ENDSET);
			setState(214);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(213);
				match(MINUS);
				}
			}

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
		public List<TerminalNode> OPEN_TAG() { return getTokens(Jinja2Parser.OPEN_TAG); }
		public TerminalNode OPEN_TAG(int i) {
			return getToken(Jinja2Parser.OPEN_TAG, i);
		}
		public TerminalNode MACRO() { return getToken(Jinja2Parser.MACRO, 0); }
		public TerminalNode ID() { return getToken(Jinja2Parser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(Jinja2Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(Jinja2Parser.RPAREN, 0); }
		public List<TerminalNode> CLOSE_TAG() { return getTokens(Jinja2Parser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(Jinja2Parser.CLOSE_TAG, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode ENDMACRO() { return getToken(Jinja2Parser.ENDMACRO, 0); }
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
		}
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public Macro_blockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_macro_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterMacro_block(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitMacro_block(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitMacro_block(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Macro_blockContext macro_block() throws RecognitionException {
		Macro_blockContext _localctx = new Macro_blockContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_macro_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			match(OPEN_TAG);
			setState(220);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(219);
				match(MINUS);
				}
			}

			setState(222);
			match(MACRO);
			setState(223);
			match(ID);
			setState(224);
			match(LPAREN);
			setState(226);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(225);
				parameters();
				}
			}

			setState(228);
			match(RPAREN);
			setState(230);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(229);
				match(MINUS);
				}
			}

			setState(232);
			match(CLOSE_TAG);
			setState(233);
			body();
			setState(234);
			match(OPEN_TAG);
			setState(236);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(235);
				match(MINUS);
				}
			}

			setState(238);
			match(ENDMACRO);
			setState(240);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(239);
				match(MINUS);
				}
			}

			setState(242);
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
		public List<TerminalNode> COMMA() { return getTokens(Jinja2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Jinja2Parser.COMMA, i);
		}
		public ParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametersContext parameters() throws RecognitionException {
		ParametersContext _localctx = new ParametersContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_parameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			parameter();
			setState(249);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(245);
				match(COMMA);
				setState(246);
				parameter();
				}
				}
				setState(251);
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
		public TerminalNode ID() { return getToken(Jinja2Parser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(Jinja2Parser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_parameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			match(ID);
			setState(255);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(253);
				match(ASSIGN);
				setState(254);
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
		public List<TerminalNode> OPEN_TAG() { return getTokens(Jinja2Parser.OPEN_TAG); }
		public TerminalNode OPEN_TAG(int i) {
			return getToken(Jinja2Parser.OPEN_TAG, i);
		}
		public TerminalNode BLOCK() { return getToken(Jinja2Parser.BLOCK, 0); }
		public TerminalNode ID() { return getToken(Jinja2Parser.ID, 0); }
		public List<TerminalNode> CLOSE_TAG() { return getTokens(Jinja2Parser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(Jinja2Parser.CLOSE_TAG, i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode ENDBLOCK() { return getToken(Jinja2Parser.ENDBLOCK, 0); }
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
		}
		public Block_blockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterBlock_block(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitBlock_block(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitBlock_block(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Block_blockContext block_block() throws RecognitionException {
		Block_blockContext _localctx = new Block_blockContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_block_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			match(OPEN_TAG);
			setState(259);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(258);
				match(MINUS);
				}
			}

			setState(261);
			match(BLOCK);
			setState(262);
			match(ID);
			setState(264);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(263);
				match(MINUS);
				}
			}

			setState(266);
			match(CLOSE_TAG);
			setState(267);
			body();
			setState(268);
			match(OPEN_TAG);
			setState(270);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(269);
				match(MINUS);
				}
			}

			setState(272);
			match(ENDBLOCK);
			setState(274);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(273);
				match(MINUS);
				}
			}

			setState(276);
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
		public TerminalNode OPEN_TAG() { return getToken(Jinja2Parser.OPEN_TAG, 0); }
		public TerminalNode EXTENDS() { return getToken(Jinja2Parser.EXTENDS, 0); }
		public TerminalNode STRING() { return getToken(Jinja2Parser.STRING, 0); }
		public TerminalNode CLOSE_TAG() { return getToken(Jinja2Parser.CLOSE_TAG, 0); }
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
		}
		public Extends_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extends_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterExtends_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitExtends_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitExtends_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Extends_stmtContext extends_stmt() throws RecognitionException {
		Extends_stmtContext _localctx = new Extends_stmtContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_extends_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(278);
			match(OPEN_TAG);
			setState(280);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(279);
				match(MINUS);
				}
			}

			setState(282);
			match(EXTENDS);
			setState(283);
			match(STRING);
			setState(285);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(284);
				match(MINUS);
				}
			}

			setState(287);
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
		public TerminalNode OPEN_TAG() { return getToken(Jinja2Parser.OPEN_TAG, 0); }
		public TerminalNode INCLUDE() { return getToken(Jinja2Parser.INCLUDE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode CLOSE_TAG() { return getToken(Jinja2Parser.CLOSE_TAG, 0); }
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
		}
		public Include_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_include_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterInclude_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitInclude_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitInclude_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Include_stmtContext include_stmt() throws RecognitionException {
		Include_stmtContext _localctx = new Include_stmtContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_include_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(289);
			match(OPEN_TAG);
			setState(291);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(290);
				match(MINUS);
				}
			}

			setState(293);
			match(INCLUDE);
			setState(294);
			expr(0);
			setState(296);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(295);
				match(MINUS);
				}
			}

			setState(298);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitPrimaryExpression(this);
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
		public TerminalNode STAR() { return getToken(Jinja2Parser.STAR, 0); }
		public TerminalNode SLASH() { return getToken(Jinja2Parser.SLASH, 0); }
		public TerminalNode PERCENT() { return getToken(Jinja2Parser.PERCENT, 0); }
		public TerminalNode PLUS() { return getToken(Jinja2Parser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(Jinja2Parser.MINUS, 0); }
		public TerminalNode LT() { return getToken(Jinja2Parser.LT, 0); }
		public TerminalNode GT() { return getToken(Jinja2Parser.GT, 0); }
		public TerminalNode LTE() { return getToken(Jinja2Parser.LTE, 0); }
		public TerminalNode GTE() { return getToken(Jinja2Parser.GTE, 0); }
		public TerminalNode EQ() { return getToken(Jinja2Parser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(Jinja2Parser.NEQ, 0); }
		public TerminalNode IN() { return getToken(Jinja2Parser.IN, 0); }
		public TerminalNode IS() { return getToken(Jinja2Parser.IS, 0); }
		public TerminalNode AND() { return getToken(Jinja2Parser.AND, 0); }
		public TerminalNode OR() { return getToken(Jinja2Parser.OR, 0); }
		public BinaryExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterBinaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitBinaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitBinaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryExpressionContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(Jinja2Parser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(Jinja2Parser.MINUS, 0); }
		public TerminalNode NOT() { return getToken(Jinja2Parser.NOT, 0); }
		public UnaryExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterUnaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitUnaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitUnaryExpression(this);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterIDTrFlExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitIDTrFlExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitIDTrFlExpression(this);
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
		int _startState = 32;
		enterRecursionRule(_localctx, 32, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(312);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				_localctx = new IDTrFlExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(301);
				primary();
				setState(306);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						setState(304);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case DOT:
						case LPAREN:
						case LBRACK:
							{
							setState(302);
							trailer();
							}
							break;
						case PIPE:
							{
							setState(303);
							filter();
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						} 
					}
					setState(308);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,46,_ctx);
				}
				}
				break;
			case 2:
				{
				_localctx = new PrimaryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(309);
				primary();
				}
				break;
			case 3:
				{
				_localctx = new UnaryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(310);
				((UnaryExpressionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 6576668672L) != 0)) ) {
					((UnaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(311);
				expr(6);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(331);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(329);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(314);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(315);
						((BinaryExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 60129542144L) != 0)) ) {
							((BinaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(316);
						expr(6);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(317);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(318);
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
						setState(319);
						expr(5);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(320);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(321);
						((BinaryExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 4329343812096L) != 0)) ) {
							((BinaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(322);
						expr(4);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(323);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(324);
						((BinaryExpressionContext)_localctx).op = match(AND);
						setState(325);
						expr(3);
						}
						break;
					case 5:
						{
						_localctx = new BinaryExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(326);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(327);
						((BinaryExpressionContext)_localctx).op = match(OR);
						setState(328);
						expr(2);
						}
						break;
					}
					} 
				}
				setState(333);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
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
		public TerminalNode LPAREN() { return getToken(Jinja2Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(Jinja2Parser.RPAREN, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public TerminalNode DOT() { return getToken(Jinja2Parser.DOT, 0); }
		public TerminalNode ID() { return getToken(Jinja2Parser.ID, 0); }
		public TerminalNode LBRACK() { return getToken(Jinja2Parser.LBRACK, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACK() { return getToken(Jinja2Parser.RBRACK, 0); }
		public TrailerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trailer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterTrailer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitTrailer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitTrailer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TrailerContext trailer() throws RecognitionException {
		TrailerContext _localctx = new TrailerContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_trailer);
		int _la;
		try {
			setState(345);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(334);
				match(LPAREN);
				setState(336);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16466294593290240L) != 0)) {
					{
					setState(335);
					arguments();
					}
				}

				setState(338);
				match(RPAREN);
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(339);
				match(DOT);
				setState(340);
				match(ID);
				}
				break;
			case LBRACK:
				enterOuterAlt(_localctx, 3);
				{
				setState(341);
				match(LBRACK);
				setState(342);
				expr(0);
				setState(343);
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
		public TerminalNode PIPE() { return getToken(Jinja2Parser.PIPE, 0); }
		public TerminalNode ID() { return getToken(Jinja2Parser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(Jinja2Parser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(Jinja2Parser.RPAREN, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public FilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitFilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterContext filter() throws RecognitionException {
		FilterContext _localctx = new FilterContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_filter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			match(PIPE);
			setState(348);
			match(ID);
			setState(354);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				{
				setState(349);
				match(LPAREN);
				setState(351);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16466294593290240L) != 0)) {
					{
					setState(350);
					arguments();
					}
				}

				setState(353);
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
		public List<TerminalNode> COMMA() { return getTokens(Jinja2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Jinja2Parser.COMMA, i);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(356);
			argument();
			setState(361);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(357);
				match(COMMA);
				setState(358);
				argument();
				}
				}
				setState(363);
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
		public TerminalNode ASSIGN() { return getToken(Jinja2Parser.ASSIGN, 0); }
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_argument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			expr(0);
			setState(367);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(365);
				match(ASSIGN);
				setState(366);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterDictionary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitDictionary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitDictionary(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumberContext extends PrimaryContext {
		public TerminalNode NUMBER() { return getToken(Jinja2Parser.NUMBER, 0); }
		public TerminalNode MINUS() { return getToken(Jinja2Parser.MINUS, 0); }
		public NumberContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenExpressionContext extends PrimaryContext {
		public TerminalNode LPAREN() { return getToken(Jinja2Parser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(Jinja2Parser.RPAREN, 0); }
		public ParenExpressionContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterParenExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitParenExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitParenExpression(this);
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
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitList(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IDContext extends PrimaryContext {
		public TerminalNode ID() { return getToken(Jinja2Parser.ID, 0); }
		public IDContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitID(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringContext extends PrimaryContext {
		public TerminalNode STRING() { return getToken(Jinja2Parser.STRING, 0); }
		public StringContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BooleanContext extends PrimaryContext {
		public TerminalNode TRUE() { return getToken(Jinja2Parser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(Jinja2Parser.FALSE, 0); }
		public BooleanContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitBoolean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitBoolean(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NoneContext extends PrimaryContext {
		public TerminalNode NONE() { return getToken(Jinja2Parser.NONE, 0); }
		public NoneContext(PrimaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterNone(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitNone(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitNone(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_primary);
		int _la;
		try {
			setState(384);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				_localctx = new ParenExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(369);
				match(LPAREN);
				setState(370);
				expr(0);
				setState(371);
				match(RPAREN);
				}
				break;
			case 2:
				_localctx = new IDContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(373);
				match(ID);
				}
				break;
			case 3:
				_localctx = new BooleanContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(374);
				match(TRUE);
				}
				break;
			case 4:
				_localctx = new BooleanContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(375);
				match(FALSE);
				}
				break;
			case 5:
				_localctx = new NumberContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(377);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(376);
					match(MINUS);
					}
				}

				setState(379);
				match(NUMBER);
				}
				break;
			case 6:
				_localctx = new NoneContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(380);
				match(NONE);
				}
				break;
			case 7:
				_localctx = new StringContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(381);
				match(STRING);
				}
				break;
			case 8:
				_localctx = new ListContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(382);
				listdef();
				}
				break;
			case 9:
				_localctx = new DictionaryContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(383);
				dictdef();
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
	public static class ListdefContext extends ParserRuleContext {
		public TerminalNode LBRACK() { return getToken(Jinja2Parser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(Jinja2Parser.RBRACK, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Jinja2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Jinja2Parser.COMMA, i);
		}
		public ListdefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listdef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterListdef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitListdef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitListdef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListdefContext listdef() throws RecognitionException {
		ListdefContext _localctx = new ListdefContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_listdef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(386);
			match(LBRACK);
			setState(395);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16466294593290240L) != 0)) {
				{
				setState(387);
				expr(0);
				setState(392);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(388);
					match(COMMA);
					setState(389);
					expr(0);
					}
					}
					setState(394);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(397);
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
		public TerminalNode LBRACK() { return getToken(Jinja2Parser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(Jinja2Parser.RBRACK, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COLON() { return getTokens(Jinja2Parser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(Jinja2Parser.COLON, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Jinja2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Jinja2Parser.COMMA, i);
		}
		public DictdefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dictdef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterDictdef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitDictdef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitDictdef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DictdefContext dictdef() throws RecognitionException {
		DictdefContext _localctx = new DictdefContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_dictdef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
			match(LBRACK);
			setState(414);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16466294593290240L) != 0)) {
				{
				{
				setState(400);
				expr(0);
				setState(401);
				match(COLON);
				setState(402);
				expr(0);
				}
				setState(411);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(404);
					match(COMMA);
					{
					setState(405);
					expr(0);
					setState(406);
					match(COLON);
					setState(407);
					expr(0);
					}
					}
					}
					setState(413);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(416);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 16:
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
		"\u0004\u00018\u01a3\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0001\u0000\u0005\u0000"+
		"2\b\u0000\n\u0000\f\u00005\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001=\b\u0001\u0001\u0002"+
		"\u0001\u0002\u0003\u0002A\b\u0002\u0001\u0002\u0001\u0002\u0003\u0002"+
		"E\b\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0003\u0003N\b\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0003\u0004S\b\u0004\u0001\u0005\u0001\u0005\u0003\u0005"+
		"W\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005"+
		"]\b\u0005\n\u0005\f\u0005`\t\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0003\u0005e\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0003\u0005k\b\u0005\u0001\u0005\u0001\u0005\u0003\u0005o\b\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0003\u0006u\b\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0003\u0006z\b\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u0080\b\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0003\u0006\u0085\b\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0005\u0006\u008a\b\u0006\n\u0006\f\u0006\u008d\t\u0006\u0001\u0006"+
		"\u0001\u0006\u0003\u0006\u0091\b\u0006\u0001\u0006\u0001\u0006\u0003\u0006"+
		"\u0095\b\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u0099\b\u0006\u0001"+
		"\u0006\u0001\u0006\u0003\u0006\u009d\b\u0006\u0001\u0006\u0001\u0006\u0003"+
		"\u0006\u00a1\b\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0005\u0007\u00a6"+
		"\b\u0007\n\u0007\f\u0007\u00a9\t\u0007\u0001\b\u0001\b\u0003\b\u00ad\b"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0005\b\u00b3\b\b\n\b\f\b\u00b6\t\b"+
		"\u0001\b\u0001\b\u0001\b\u0003\b\u00bb\b\b\u0001\b\u0001\b\u0001\t\u0001"+
		"\t\u0003\t\u00c1\b\t\u0001\t\u0001\t\u0001\t\u0001\t\u0005\t\u00c7\b\t"+
		"\n\t\f\t\u00ca\t\t\u0001\t\u0003\t\u00cd\b\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0003\t\u00d3\b\t\u0001\t\u0001\t\u0003\t\u00d7\b\t\u0001\t\u0001\t"+
		"\u0001\n\u0001\n\u0003\n\u00dd\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003"+
		"\n\u00e3\b\n\u0001\n\u0001\n\u0003\n\u00e7\b\n\u0001\n\u0001\n\u0001\n"+
		"\u0001\n\u0003\n\u00ed\b\n\u0001\n\u0001\n\u0003\n\u00f1\b\n\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u00f8\b\u000b\n\u000b"+
		"\f\u000b\u00fb\t\u000b\u0001\f\u0001\f\u0001\f\u0003\f\u0100\b\f\u0001"+
		"\r\u0001\r\u0003\r\u0104\b\r\u0001\r\u0001\r\u0001\r\u0003\r\u0109\b\r"+
		"\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u010f\b\r\u0001\r\u0001\r\u0003"+
		"\r\u0113\b\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0003\u000e\u0119"+
		"\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u011e\b\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0003\u000f\u0124\b\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0129\b\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0005\u0010"+
		"\u0131\b\u0010\n\u0010\f\u0010\u0134\t\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0003\u0010\u0139\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0005"+
		"\u0010\u014a\b\u0010\n\u0010\f\u0010\u014d\t\u0010\u0001\u0011\u0001\u0011"+
		"\u0003\u0011\u0151\b\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u015a\b\u0011\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0160\b\u0012\u0001\u0012"+
		"\u0003\u0012\u0163\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0005\u0013"+
		"\u0168\b\u0013\n\u0013\f\u0013\u016b\t\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0003\u0014\u0170\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u017a"+
		"\b\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003"+
		"\u0015\u0181\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0005"+
		"\u0016\u0187\b\u0016\n\u0016\f\u0016\u018a\t\u0016\u0003\u0016\u018c\b"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0005\u0017\u019a\b\u0017\n\u0017\f\u0017\u019d\t\u0017\u0003\u0017"+
		"\u019f\b\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0000\u0001 \u0018"+
		"\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$&(*,.\u0000\u0004\u0002\u0000\u001b\u001b\u001f \u0001"+
		"\u0000!#\u0001\u0000\u001f \u0003\u0000\t\t\u0018\u0018$)\u01da\u0000"+
		"3\u0001\u0000\u0000\u0000\u0002<\u0001\u0000\u0000\u0000\u0004>\u0001"+
		"\u0000\u0000\u0000\u0006M\u0001\u0000\u0000\u0000\bR\u0001\u0000\u0000"+
		"\u0000\nT\u0001\u0000\u0000\u0000\fr\u0001\u0000\u0000\u0000\u000e\u00a7"+
		"\u0001\u0000\u0000\u0000\u0010\u00aa\u0001\u0000\u0000\u0000\u0012\u00be"+
		"\u0001\u0000\u0000\u0000\u0014\u00da\u0001\u0000\u0000\u0000\u0016\u00f4"+
		"\u0001\u0000\u0000\u0000\u0018\u00fc\u0001\u0000\u0000\u0000\u001a\u0101"+
		"\u0001\u0000\u0000\u0000\u001c\u0116\u0001\u0000\u0000\u0000\u001e\u0121"+
		"\u0001\u0000\u0000\u0000 \u0138\u0001\u0000\u0000\u0000\"\u0159\u0001"+
		"\u0000\u0000\u0000$\u015b\u0001\u0000\u0000\u0000&\u0164\u0001\u0000\u0000"+
		"\u0000(\u016c\u0001\u0000\u0000\u0000*\u0180\u0001\u0000\u0000\u0000,"+
		"\u0182\u0001\u0000\u0000\u0000.\u018f\u0001\u0000\u0000\u000002\u0003"+
		"\u0002\u0001\u000010\u0001\u0000\u0000\u000025\u0001\u0000\u0000\u0000"+
		"31\u0001\u0000\u0000\u000034\u0001\u0000\u0000\u000046\u0001\u0000\u0000"+
		"\u000053\u0001\u0000\u0000\u000067\u0005\u0000\u0000\u00017\u0001\u0001"+
		"\u0000\u0000\u00008=\u0003\u0004\u0002\u00009=\u0003\u0006\u0003\u0000"+
		":=\u0003\b\u0004\u0000;=\u0005\u0004\u0000\u0000<8\u0001\u0000\u0000\u0000"+
		"<9\u0001\u0000\u0000\u0000<:\u0001\u0000\u0000\u0000<;\u0001\u0000\u0000"+
		"\u0000=\u0003\u0001\u0000\u0000\u0000>@\u0005\u0001\u0000\u0000?A\u0005"+
		" \u0000\u0000@?\u0001\u0000\u0000\u0000@A\u0001\u0000\u0000\u0000AB\u0001"+
		"\u0000\u0000\u0000BD\u0003 \u0010\u0000CE\u0005 \u0000\u0000DC\u0001\u0000"+
		"\u0000\u0000DE\u0001\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000FG\u0005"+
		"\u0006\u0000\u0000G\u0005\u0001\u0000\u0000\u0000HN\u0003\n\u0005\u0000"+
		"IN\u0003\f\u0006\u0000JN\u0003\u0012\t\u0000KN\u0003\u0014\n\u0000LN\u0003"+
		"\u001a\r\u0000MH\u0001\u0000\u0000\u0000MI\u0001\u0000\u0000\u0000MJ\u0001"+
		"\u0000\u0000\u0000MK\u0001\u0000\u0000\u0000ML\u0001\u0000\u0000\u0000"+
		"N\u0007\u0001\u0000\u0000\u0000OS\u0003\u001c\u000e\u0000PS\u0003\u001e"+
		"\u000f\u0000QS\u0003\u0010\b\u0000RO\u0001\u0000\u0000\u0000RP\u0001\u0000"+
		"\u0000\u0000RQ\u0001\u0000\u0000\u0000S\t\u0001\u0000\u0000\u0000TV\u0005"+
		"\u0002\u0000\u0000UW\u0005 \u0000\u0000VU\u0001\u0000\u0000\u0000VW\u0001"+
		"\u0000\u0000\u0000WX\u0001\u0000\u0000\u0000XY\u0005\b\u0000\u0000Y^\u0005"+
		"5\u0000\u0000Z[\u0005-\u0000\u0000[]\u00055\u0000\u0000\\Z\u0001\u0000"+
		"\u0000\u0000]`\u0001\u0000\u0000\u0000^\\\u0001\u0000\u0000\u0000^_\u0001"+
		"\u0000\u0000\u0000_a\u0001\u0000\u0000\u0000`^\u0001\u0000\u0000\u0000"+
		"ab\u0005\t\u0000\u0000bd\u0003 \u0010\u0000ce\u0005 \u0000\u0000dc\u0001"+
		"\u0000\u0000\u0000de\u0001\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000"+
		"fg\u0005\u0005\u0000\u0000gh\u0003\u000e\u0007\u0000hj\u0005\u0002\u0000"+
		"\u0000ik\u0005 \u0000\u0000ji\u0001\u0000\u0000\u0000jk\u0001\u0000\u0000"+
		"\u0000kl\u0001\u0000\u0000\u0000ln\u0005\n\u0000\u0000mo\u0005 \u0000"+
		"\u0000nm\u0001\u0000\u0000\u0000no\u0001\u0000\u0000\u0000op\u0001\u0000"+
		"\u0000\u0000pq\u0005\u0005\u0000\u0000q\u000b\u0001\u0000\u0000\u0000"+
		"rt\u0005\u0002\u0000\u0000su\u0005 \u0000\u0000ts\u0001\u0000\u0000\u0000"+
		"tu\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000vw\u0005\u000b\u0000"+
		"\u0000wy\u0003 \u0010\u0000xz\u0005 \u0000\u0000yx\u0001\u0000\u0000\u0000"+
		"yz\u0001\u0000\u0000\u0000z{\u0001\u0000\u0000\u0000{|\u0005\u0005\u0000"+
		"\u0000|\u008b\u0003\u000e\u0007\u0000}\u007f\u0005\u0002\u0000\u0000~"+
		"\u0080\u0005 \u0000\u0000\u007f~\u0001\u0000\u0000\u0000\u007f\u0080\u0001"+
		"\u0000\u0000\u0000\u0080\u0081\u0001\u0000\u0000\u0000\u0081\u0082\u0005"+
		"\f\u0000\u0000\u0082\u0084\u0003 \u0010\u0000\u0083\u0085\u0005 \u0000"+
		"\u0000\u0084\u0083\u0001\u0000\u0000\u0000\u0084\u0085\u0001\u0000\u0000"+
		"\u0000\u0085\u0086\u0001\u0000\u0000\u0000\u0086\u0087\u0005\u0005\u0000"+
		"\u0000\u0087\u0088\u0003\u000e\u0007\u0000\u0088\u008a\u0001\u0000\u0000"+
		"\u0000\u0089}\u0001\u0000\u0000\u0000\u008a\u008d\u0001\u0000\u0000\u0000"+
		"\u008b\u0089\u0001\u0000\u0000\u0000\u008b\u008c\u0001\u0000\u0000\u0000"+
		"\u008c\u0098\u0001\u0000\u0000\u0000\u008d\u008b\u0001\u0000\u0000\u0000"+
		"\u008e\u0090\u0005\u0002\u0000\u0000\u008f\u0091\u0005 \u0000\u0000\u0090"+
		"\u008f\u0001\u0000\u0000\u0000\u0090\u0091\u0001\u0000\u0000\u0000\u0091"+
		"\u0092\u0001\u0000\u0000\u0000\u0092\u0094\u0005\r\u0000\u0000\u0093\u0095"+
		"\u0005 \u0000\u0000\u0094\u0093\u0001\u0000\u0000\u0000\u0094\u0095\u0001"+
		"\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000\u0096\u0097\u0005"+
		"\u0005\u0000\u0000\u0097\u0099\u0003\u000e\u0007\u0000\u0098\u008e\u0001"+
		"\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009a\u0001"+
		"\u0000\u0000\u0000\u009a\u009c\u0005\u0002\u0000\u0000\u009b\u009d\u0005"+
		" \u0000\u0000\u009c\u009b\u0001\u0000\u0000\u0000\u009c\u009d\u0001\u0000"+
		"\u0000\u0000\u009d\u009e\u0001\u0000\u0000\u0000\u009e\u00a0\u0005\u000e"+
		"\u0000\u0000\u009f\u00a1\u0005 \u0000\u0000\u00a0\u009f\u0001\u0000\u0000"+
		"\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000\u00a1\u00a2\u0001\u0000\u0000"+
		"\u0000\u00a2\u00a3\u0005\u0005\u0000\u0000\u00a3\r\u0001\u0000\u0000\u0000"+
		"\u00a4\u00a6\u0003\u0002\u0001\u0000\u00a5\u00a4\u0001\u0000\u0000\u0000"+
		"\u00a6\u00a9\u0001\u0000\u0000\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000"+
		"\u00a7\u00a8\u0001\u0000\u0000\u0000\u00a8\u000f\u0001\u0000\u0000\u0000"+
		"\u00a9\u00a7\u0001\u0000\u0000\u0000\u00aa\u00ac\u0005\u0002\u0000\u0000"+
		"\u00ab\u00ad\u0005 \u0000\u0000\u00ac\u00ab\u0001\u0000\u0000\u0000\u00ac"+
		"\u00ad\u0001\u0000\u0000\u0000\u00ad\u00ae\u0001\u0000\u0000\u0000\u00ae"+
		"\u00af\u0005\u0013\u0000\u0000\u00af\u00b4\u00055\u0000\u0000\u00b0\u00b1"+
		"\u0005-\u0000\u0000\u00b1\u00b3\u00055\u0000\u0000\u00b2\u00b0\u0001\u0000"+
		"\u0000\u0000\u00b3\u00b6\u0001\u0000\u0000\u0000\u00b4\u00b2\u0001\u0000"+
		"\u0000\u0000\u00b4\u00b5\u0001\u0000\u0000\u0000\u00b5\u00b7\u0001\u0000"+
		"\u0000\u0000\u00b6\u00b4\u0001\u0000\u0000\u0000\u00b7\u00b8\u0005*\u0000"+
		"\u0000\u00b8\u00ba\u0003 \u0010\u0000\u00b9\u00bb\u0005 \u0000\u0000\u00ba"+
		"\u00b9\u0001\u0000\u0000\u0000\u00ba\u00bb\u0001\u0000\u0000\u0000\u00bb"+
		"\u00bc\u0001\u0000\u0000\u0000\u00bc\u00bd\u0005\u0005\u0000\u0000\u00bd"+
		"\u0011\u0001\u0000\u0000\u0000\u00be\u00c0\u0005\u0002\u0000\u0000\u00bf"+
		"\u00c1\u0005 \u0000\u0000\u00c0\u00bf\u0001\u0000\u0000\u0000\u00c0\u00c1"+
		"\u0001\u0000\u0000\u0000\u00c1\u00c2\u0001\u0000\u0000\u0000\u00c2\u00c3"+
		"\u0005\u0013\u0000\u0000\u00c3\u00c8\u00055\u0000\u0000\u00c4\u00c5\u0005"+
		"-\u0000\u0000\u00c5\u00c7\u00055\u0000\u0000\u00c6\u00c4\u0001\u0000\u0000"+
		"\u0000\u00c7\u00ca\u0001\u0000\u0000\u0000\u00c8\u00c6\u0001\u0000\u0000"+
		"\u0000\u00c8\u00c9\u0001\u0000\u0000\u0000\u00c9\u00cc\u0001\u0000\u0000"+
		"\u0000\u00ca\u00c8\u0001\u0000\u0000\u0000\u00cb\u00cd\u0005 \u0000\u0000"+
		"\u00cc\u00cb\u0001\u0000\u0000\u0000\u00cc\u00cd\u0001\u0000\u0000\u0000"+
		"\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u00cf\u0005\u0005\u0000\u0000"+
		"\u00cf\u00d0\u0003\u000e\u0007\u0000\u00d0\u00d2\u0005\u0002\u0000\u0000"+
		"\u00d1\u00d3\u0005 \u0000\u0000\u00d2\u00d1\u0001\u0000\u0000\u0000\u00d2"+
		"\u00d3\u0001\u0000\u0000\u0000\u00d3\u00d4\u0001\u0000\u0000\u0000\u00d4"+
		"\u00d6\u0005\u0014\u0000\u0000\u00d5\u00d7\u0005 \u0000\u0000\u00d6\u00d5"+
		"\u0001\u0000\u0000\u0000\u00d6\u00d7\u0001\u0000\u0000\u0000\u00d7\u00d8"+
		"\u0001\u0000\u0000\u0000\u00d8\u00d9\u0005\u0005\u0000\u0000\u00d9\u0013"+
		"\u0001\u0000\u0000\u0000\u00da\u00dc\u0005\u0002\u0000\u0000\u00db\u00dd"+
		"\u0005 \u0000\u0000\u00dc\u00db\u0001\u0000\u0000\u0000\u00dc\u00dd\u0001"+
		"\u0000\u0000\u0000\u00dd\u00de\u0001\u0000\u0000\u0000\u00de\u00df\u0005"+
		"\u0011\u0000\u0000\u00df\u00e0\u00055\u0000\u0000\u00e0\u00e2\u0005/\u0000"+
		"\u0000\u00e1\u00e3\u0003\u0016\u000b\u0000\u00e2\u00e1\u0001\u0000\u0000"+
		"\u0000\u00e2\u00e3\u0001\u0000\u0000\u0000\u00e3\u00e4\u0001\u0000\u0000"+
		"\u0000\u00e4\u00e6\u00050\u0000\u0000\u00e5\u00e7\u0005 \u0000\u0000\u00e6"+
		"\u00e5\u0001\u0000\u0000\u0000\u00e6\u00e7\u0001\u0000\u0000\u0000\u00e7"+
		"\u00e8\u0001\u0000\u0000\u0000\u00e8\u00e9\u0005\u0005\u0000\u0000\u00e9"+
		"\u00ea\u0003\u000e\u0007\u0000\u00ea\u00ec\u0005\u0002\u0000\u0000\u00eb"+
		"\u00ed\u0005 \u0000\u0000\u00ec\u00eb\u0001\u0000\u0000\u0000\u00ec\u00ed"+
		"\u0001\u0000\u0000\u0000\u00ed\u00ee\u0001\u0000\u0000\u0000\u00ee\u00f0"+
		"\u0005\u0012\u0000\u0000\u00ef\u00f1\u0005 \u0000\u0000\u00f0\u00ef\u0001"+
		"\u0000\u0000\u0000\u00f0\u00f1\u0001\u0000\u0000\u0000\u00f1\u00f2\u0001"+
		"\u0000\u0000\u0000\u00f2\u00f3\u0005\u0005\u0000\u0000\u00f3\u0015\u0001"+
		"\u0000\u0000\u0000\u00f4\u00f9\u0003\u0018\f\u0000\u00f5\u00f6\u0005-"+
		"\u0000\u0000\u00f6\u00f8\u0003\u0018\f\u0000\u00f7\u00f5\u0001\u0000\u0000"+
		"\u0000\u00f8\u00fb\u0001\u0000\u0000\u0000\u00f9\u00f7\u0001\u0000\u0000"+
		"\u0000\u00f9\u00fa\u0001\u0000\u0000\u0000\u00fa\u0017\u0001\u0000\u0000"+
		"\u0000\u00fb\u00f9\u0001\u0000\u0000\u0000\u00fc\u00ff\u00055\u0000\u0000"+
		"\u00fd\u00fe\u0005*\u0000\u0000\u00fe\u0100\u0003 \u0010\u0000\u00ff\u00fd"+
		"\u0001\u0000\u0000\u0000\u00ff\u0100\u0001\u0000\u0000\u0000\u0100\u0019"+
		"\u0001\u0000\u0000\u0000\u0101\u0103\u0005\u0002\u0000\u0000\u0102\u0104"+
		"\u0005 \u0000\u0000\u0103\u0102\u0001\u0000\u0000\u0000\u0103\u0104\u0001"+
		"\u0000\u0000\u0000\u0104\u0105\u0001\u0000\u0000\u0000\u0105\u0106\u0005"+
		"\u000f\u0000\u0000\u0106\u0108\u00055\u0000\u0000\u0107\u0109\u0005 \u0000"+
		"\u0000\u0108\u0107\u0001\u0000\u0000\u0000\u0108\u0109\u0001\u0000\u0000"+
		"\u0000\u0109\u010a\u0001\u0000\u0000\u0000\u010a\u010b\u0005\u0005\u0000"+
		"\u0000\u010b\u010c\u0003\u000e\u0007\u0000\u010c\u010e\u0005\u0002\u0000"+
		"\u0000\u010d\u010f\u0005 \u0000\u0000\u010e\u010d\u0001\u0000\u0000\u0000"+
		"\u010e\u010f\u0001\u0000\u0000\u0000\u010f\u0110\u0001\u0000\u0000\u0000"+
		"\u0110\u0112\u0005\u0010\u0000\u0000\u0111\u0113\u0005 \u0000\u0000\u0112"+
		"\u0111\u0001\u0000\u0000\u0000\u0112\u0113\u0001\u0000\u0000\u0000\u0113"+
		"\u0114\u0001\u0000\u0000\u0000\u0114\u0115\u0005\u0005\u0000\u0000\u0115"+
		"\u001b\u0001\u0000\u0000\u0000\u0116\u0118\u0005\u0002\u0000\u0000\u0117"+
		"\u0119\u0005 \u0000\u0000\u0118\u0117\u0001\u0000\u0000\u0000\u0118\u0119"+
		"\u0001\u0000\u0000\u0000\u0119\u011a\u0001\u0000\u0000\u0000\u011a\u011b"+
		"\u0005\u0015\u0000\u0000\u011b\u011d\u00053\u0000\u0000\u011c\u011e\u0005"+
		" \u0000\u0000\u011d\u011c\u0001\u0000\u0000\u0000\u011d\u011e\u0001\u0000"+
		"\u0000\u0000\u011e\u011f\u0001\u0000\u0000\u0000\u011f\u0120\u0005\u0005"+
		"\u0000\u0000\u0120\u001d\u0001\u0000\u0000\u0000\u0121\u0123\u0005\u0002"+
		"\u0000\u0000\u0122\u0124\u0005 \u0000\u0000\u0123\u0122\u0001\u0000\u0000"+
		"\u0000\u0123\u0124\u0001\u0000\u0000\u0000\u0124\u0125\u0001\u0000\u0000"+
		"\u0000\u0125\u0126\u0005\u0007\u0000\u0000\u0126\u0128\u0003 \u0010\u0000"+
		"\u0127\u0129\u0005 \u0000\u0000\u0128\u0127\u0001\u0000\u0000\u0000\u0128"+
		"\u0129\u0001\u0000\u0000\u0000\u0129\u012a\u0001\u0000\u0000\u0000\u012a"+
		"\u012b\u0005\u0005\u0000\u0000\u012b\u001f\u0001\u0000\u0000\u0000\u012c"+
		"\u012d\u0006\u0010\uffff\uffff\u0000\u012d\u0132\u0003*\u0015\u0000\u012e"+
		"\u0131\u0003\"\u0011\u0000\u012f\u0131\u0003$\u0012\u0000\u0130\u012e"+
		"\u0001\u0000\u0000\u0000\u0130\u012f\u0001\u0000\u0000\u0000\u0131\u0134"+
		"\u0001\u0000\u0000\u0000\u0132\u0130\u0001\u0000\u0000\u0000\u0132\u0133"+
		"\u0001\u0000\u0000\u0000\u0133\u0139\u0001\u0000\u0000\u0000\u0134\u0132"+
		"\u0001\u0000\u0000\u0000\u0135\u0139\u0003*\u0015\u0000\u0136\u0137\u0007"+
		"\u0000\u0000\u0000\u0137\u0139\u0003 \u0010\u0006\u0138\u012c\u0001\u0000"+
		"\u0000\u0000\u0138\u0135\u0001\u0000\u0000\u0000\u0138\u0136\u0001\u0000"+
		"\u0000\u0000\u0139\u014b\u0001\u0000\u0000\u0000\u013a\u013b\n\u0005\u0000"+
		"\u0000\u013b\u013c\u0007\u0001\u0000\u0000\u013c\u014a\u0003 \u0010\u0006"+
		"\u013d\u013e\n\u0004\u0000\u0000\u013e\u013f\u0007\u0002\u0000\u0000\u013f"+
		"\u014a\u0003 \u0010\u0005\u0140\u0141\n\u0003\u0000\u0000\u0141\u0142"+
		"\u0007\u0003\u0000\u0000\u0142\u014a\u0003 \u0010\u0004\u0143\u0144\n"+
		"\u0002\u0000\u0000\u0144\u0145\u0005\u001a\u0000\u0000\u0145\u014a\u0003"+
		" \u0010\u0003\u0146\u0147\n\u0001\u0000\u0000\u0147\u0148\u0005\u0019"+
		"\u0000\u0000\u0148\u014a\u0003 \u0010\u0002\u0149\u013a\u0001\u0000\u0000"+
		"\u0000\u0149\u013d\u0001\u0000\u0000\u0000\u0149\u0140\u0001\u0000\u0000"+
		"\u0000\u0149\u0143\u0001\u0000\u0000\u0000\u0149\u0146\u0001\u0000\u0000"+
		"\u0000\u014a\u014d\u0001\u0000\u0000\u0000\u014b\u0149\u0001\u0000\u0000"+
		"\u0000\u014b\u014c\u0001\u0000\u0000\u0000\u014c!\u0001\u0000\u0000\u0000"+
		"\u014d\u014b\u0001\u0000\u0000\u0000\u014e\u0150\u0005/\u0000\u0000\u014f"+
		"\u0151\u0003&\u0013\u0000\u0150\u014f\u0001\u0000\u0000\u0000\u0150\u0151"+
		"\u0001\u0000\u0000\u0000\u0151\u0152\u0001\u0000\u0000\u0000\u0152\u015a"+
		"\u00050\u0000\u0000\u0153\u0154\u0005,\u0000\u0000\u0154\u015a\u00055"+
		"\u0000\u0000\u0155\u0156\u00051\u0000\u0000\u0156\u0157\u0003 \u0010\u0000"+
		"\u0157\u0158\u00052\u0000\u0000\u0158\u015a\u0001\u0000\u0000\u0000\u0159"+
		"\u014e\u0001\u0000\u0000\u0000\u0159\u0153\u0001\u0000\u0000\u0000\u0159"+
		"\u0155\u0001\u0000\u0000\u0000\u015a#\u0001\u0000\u0000\u0000\u015b\u015c"+
		"\u0005+\u0000\u0000\u015c\u0162\u00055\u0000\u0000\u015d\u015f\u0005/"+
		"\u0000\u0000\u015e\u0160\u0003&\u0013\u0000\u015f\u015e\u0001\u0000\u0000"+
		"\u0000\u015f\u0160\u0001\u0000\u0000\u0000\u0160\u0161\u0001\u0000\u0000"+
		"\u0000\u0161\u0163\u00050\u0000\u0000\u0162\u015d\u0001\u0000\u0000\u0000"+
		"\u0162\u0163\u0001\u0000\u0000\u0000\u0163%\u0001\u0000\u0000\u0000\u0164"+
		"\u0169\u0003(\u0014\u0000\u0165\u0166\u0005-\u0000\u0000\u0166\u0168\u0003"+
		"(\u0014\u0000\u0167\u0165\u0001\u0000\u0000\u0000\u0168\u016b\u0001\u0000"+
		"\u0000\u0000\u0169\u0167\u0001\u0000\u0000\u0000\u0169\u016a\u0001\u0000"+
		"\u0000\u0000\u016a\'\u0001\u0000\u0000\u0000\u016b\u0169\u0001\u0000\u0000"+
		"\u0000\u016c\u016f\u0003 \u0010\u0000\u016d\u016e\u0005*\u0000\u0000\u016e"+
		"\u0170\u0003 \u0010\u0000\u016f\u016d\u0001\u0000\u0000\u0000\u016f\u0170"+
		"\u0001\u0000\u0000\u0000\u0170)\u0001\u0000\u0000\u0000\u0171\u0172\u0005"+
		"/\u0000\u0000\u0172\u0173\u0003 \u0010\u0000\u0173\u0174\u00050\u0000"+
		"\u0000\u0174\u0181\u0001\u0000\u0000\u0000\u0175\u0181\u00055\u0000\u0000"+
		"\u0176\u0181\u0005\u001c\u0000\u0000\u0177\u0181\u0005\u001d\u0000\u0000"+
		"\u0178\u017a\u0005 \u0000\u0000\u0179\u0178\u0001\u0000\u0000\u0000\u0179"+
		"\u017a\u0001\u0000\u0000\u0000\u017a\u017b\u0001\u0000\u0000\u0000\u017b"+
		"\u0181\u00054\u0000\u0000\u017c\u0181\u0005\u001e\u0000\u0000\u017d\u0181"+
		"\u00053\u0000\u0000\u017e\u0181\u0003,\u0016\u0000\u017f\u0181\u0003."+
		"\u0017\u0000\u0180\u0171\u0001\u0000\u0000\u0000\u0180\u0175\u0001\u0000"+
		"\u0000\u0000\u0180\u0176\u0001\u0000\u0000\u0000\u0180\u0177\u0001\u0000"+
		"\u0000\u0000\u0180\u0179\u0001\u0000\u0000\u0000\u0180\u017c\u0001\u0000"+
		"\u0000\u0000\u0180\u017d\u0001\u0000\u0000\u0000\u0180\u017e\u0001\u0000"+
		"\u0000\u0000\u0180\u017f\u0001\u0000\u0000\u0000\u0181+\u0001\u0000\u0000"+
		"\u0000\u0182\u018b\u00051\u0000\u0000\u0183\u0188\u0003 \u0010\u0000\u0184"+
		"\u0185\u0005-\u0000\u0000\u0185\u0187\u0003 \u0010\u0000\u0186\u0184\u0001"+
		"\u0000\u0000\u0000\u0187\u018a\u0001\u0000\u0000\u0000\u0188\u0186\u0001"+
		"\u0000\u0000\u0000\u0188\u0189\u0001\u0000\u0000\u0000\u0189\u018c\u0001"+
		"\u0000\u0000\u0000\u018a\u0188\u0001\u0000\u0000\u0000\u018b\u0183\u0001"+
		"\u0000\u0000\u0000\u018b\u018c\u0001\u0000\u0000\u0000\u018c\u018d\u0001"+
		"\u0000\u0000\u0000\u018d\u018e\u00052\u0000\u0000\u018e-\u0001\u0000\u0000"+
		"\u0000\u018f\u019e\u00051\u0000\u0000\u0190\u0191\u0003 \u0010\u0000\u0191"+
		"\u0192\u0005.\u0000\u0000\u0192\u0193\u0003 \u0010\u0000\u0193\u019b\u0001"+
		"\u0000\u0000\u0000\u0194\u0195\u0005-\u0000\u0000\u0195\u0196\u0003 \u0010"+
		"\u0000\u0196\u0197\u0005.\u0000\u0000\u0197\u0198\u0003 \u0010\u0000\u0198"+
		"\u019a\u0001\u0000\u0000\u0000\u0199\u0194\u0001\u0000\u0000\u0000\u019a"+
		"\u019d\u0001\u0000\u0000\u0000\u019b\u0199\u0001\u0000\u0000\u0000\u019b"+
		"\u019c\u0001\u0000\u0000\u0000\u019c\u019f\u0001\u0000\u0000\u0000\u019d"+
		"\u019b\u0001\u0000\u0000\u0000\u019e\u0190\u0001\u0000\u0000\u0000\u019e"+
		"\u019f\u0001\u0000\u0000\u0000\u019f\u01a0\u0001\u0000\u0000\u0000\u01a0"+
		"\u01a1\u00052\u0000\u0000\u01a1/\u0001\u0000\u0000\u0000>3<@DMRV^djnt"+
		"y\u007f\u0084\u008b\u0090\u0094\u0098\u009c\u00a0\u00a7\u00ac\u00b4\u00ba"+
		"\u00c0\u00c8\u00cc\u00d2\u00d6\u00dc\u00e2\u00e6\u00ec\u00f0\u00f9\u00ff"+
		"\u0103\u0108\u010e\u0112\u0118\u011d\u0123\u0128\u0130\u0132\u0138\u0149"+
		"\u014b\u0150\u0159\u015f\u0162\u0169\u016f\u0179\u0180\u0188\u018b\u019b"+
		"\u019e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}