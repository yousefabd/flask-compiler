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
		DOUBLE_OPEN_BRACE=1, DOUBLE_CLOSE_BRACE=2, OPEN_TAG=3, CLOSE_TAG=4, COMMENT=5, 
		INCLUDE=6, FOR=7, IN=8, ENDFOR=9, IF=10, ELIF=11, ELSE=12, ENDIF=13, BLOCK=14, 
		ENDBLOCK=15, MACRO=16, ENDMACRO=17, SET=18, ENDSET=19, EXTENDS=20, RAW=21, 
		ENDRAW=22, IS=23, OR=24, AND=25, NOT=26, TRUE=27, FALSE=28, NONE=29, PLUS=30, 
		MINUS=31, STAR=32, SLASH=33, PERCENT=34, EQ=35, NEQ=36, LT=37, GT=38, 
		LTE=39, GTE=40, ASSIGN=41, PIPE=42, DOT=43, COMMA=44, COLON=45, LPAREN=46, 
		RPAREN=47, LBRACK=48, RBRACK=49, STRING=50, NUMBER=51, ID=52, WS=53, TEXT=54;
	public static final int
		RULE_template = 0, RULE_variable = 1, RULE_block_stmt = 2, RULE_inline_stmt = 3, 
		RULE_for_block = 4, RULE_end_for = 5, RULE_target = 6, RULE_if_block = 7, 
		RULE_set_inline = 8, RULE_set_block = 9, RULE_set_targets = 10, RULE_macro_block = 11, 
		RULE_call_params = 12, RULE_param = 13, RULE_block_block = 14, RULE_extends_stmt = 15, 
		RULE_include_stmt = 16, RULE_expr = 17, RULE_primary = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"template", "variable", "block_stmt", "inline_stmt", "for_block", "end_for", 
			"target", "if_block", "set_inline", "set_block", "set_targets", "macro_block", 
			"call_params", "param", "block_block", "extends_stmt", "include_stmt", 
			"expr", "primary"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{{'", "'}}'", "'{%'", "'%}'", null, "'include'", "'for'", "'in'", 
			"'endfor'", "'if'", "'elif'", "'else'", "'endif'", "'block'", "'endblock'", 
			"'macro'", "'endmacro'", "'set'", "'endset'", "'extends'", "'raw'", "'endraw'", 
			"'is'", "'or'", "'and'", "'not'", "'true'", "'false'", "'none'", "'+'", 
			"'-'", "'*'", "'/'", "'%'", "'=='", "'!='", "'<'", "'>'", "'<='", "'>='", 
			"'='", "'|'", "'.'", "','", "':'", "'('", "')'", "'['", "']'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "DOUBLE_OPEN_BRACE", "DOUBLE_CLOSE_BRACE", "OPEN_TAG", "CLOSE_TAG", 
			"COMMENT", "INCLUDE", "FOR", "IN", "ENDFOR", "IF", "ELIF", "ELSE", "ENDIF", 
			"BLOCK", "ENDBLOCK", "MACRO", "ENDMACRO", "SET", "ENDSET", "EXTENDS", 
			"RAW", "ENDRAW", "IS", "OR", "AND", "NOT", "TRUE", "FALSE", "NONE", "PLUS", 
			"MINUS", "STAR", "SLASH", "PERCENT", "EQ", "NEQ", "LT", "GT", "LTE", 
			"GTE", "ASSIGN", "PIPE", "DOT", "COMMA", "COLON", "LPAREN", "RPAREN", 
			"LBRACK", "RBRACK", "STRING", "NUMBER", "ID", "WS", "TEXT"
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
		public List<TerminalNode> TEXT() { return getTokens(Jinja2Parser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(Jinja2Parser.TEXT, i);
		}
		public List<VariableContext> variable() {
			return getRuleContexts(VariableContext.class);
		}
		public VariableContext variable(int i) {
			return getRuleContext(VariableContext.class,i);
		}
		public List<Block_stmtContext> block_stmt() {
			return getRuleContexts(Block_stmtContext.class);
		}
		public Block_stmtContext block_stmt(int i) {
			return getRuleContext(Block_stmtContext.class,i);
		}
		public List<Inline_stmtContext> inline_stmt() {
			return getRuleContexts(Inline_stmtContext.class);
		}
		public Inline_stmtContext inline_stmt(int i) {
			return getRuleContext(Inline_stmtContext.class,i);
		}
		public List<TerminalNode> COMMENT() { return getTokens(Jinja2Parser.COMMENT); }
		public TerminalNode COMMENT(int i) {
			return getToken(Jinja2Parser.COMMENT, i);
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
			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 18014398509482026L) != 0)) {
				{
				setState(43);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(38);
					match(TEXT);
					}
					break;
				case 2:
					{
					setState(39);
					variable();
					}
					break;
				case 3:
					{
					setState(40);
					block_stmt();
					}
					break;
				case 4:
					{
					setState(41);
					inline_stmt();
					}
					break;
				case 5:
					{
					setState(42);
					match(COMMENT);
					}
					break;
				}
				}
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(48);
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
		enterRule(_localctx, 2, RULE_variable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			match(DOUBLE_OPEN_BRACE);
			setState(52);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(51);
				match(MINUS);
				}
				break;
			}
			setState(54);
			expr(0);
			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(55);
				match(MINUS);
				}
			}

			setState(58);
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
	public static class Block_stmtContext extends ParserRuleContext {
		public For_blockContext for_block() {
			return getRuleContext(For_blockContext.class,0);
		}
		public If_blockContext if_block() {
			return getRuleContext(If_blockContext.class,0);
		}
		public Set_blockContext set_block() {
			return getRuleContext(Set_blockContext.class,0);
		}
		public Macro_blockContext macro_block() {
			return getRuleContext(Macro_blockContext.class,0);
		}
		public Block_blockContext block_block() {
			return getRuleContext(Block_blockContext.class,0);
		}
		public Block_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterBlock_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitBlock_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitBlock_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Block_stmtContext block_stmt() throws RecognitionException {
		Block_stmtContext _localctx = new Block_stmtContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_block_stmt);
		try {
			setState(65);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(60);
				for_block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(61);
				if_block();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(62);
				set_block();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(63);
				macro_block();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(64);
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
		public Extends_stmtContext extends_stmt() {
			return getRuleContext(Extends_stmtContext.class,0);
		}
		public Include_stmtContext include_stmt() {
			return getRuleContext(Include_stmtContext.class,0);
		}
		public Set_inlineContext set_inline() {
			return getRuleContext(Set_inlineContext.class,0);
		}
		public Inline_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inline_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterInline_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitInline_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitInline_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Inline_stmtContext inline_stmt() throws RecognitionException {
		Inline_stmtContext _localctx = new Inline_stmtContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_inline_stmt);
		try {
			setState(70);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(67);
				extends_stmt();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(68);
				include_stmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(69);
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
		public TerminalNode OPEN_TAG() { return getToken(Jinja2Parser.OPEN_TAG, 0); }
		public TerminalNode FOR() { return getToken(Jinja2Parser.FOR, 0); }
		public TargetContext target() {
			return getRuleContext(TargetContext.class,0);
		}
		public TerminalNode IN() { return getToken(Jinja2Parser.IN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode CLOSE_TAG() { return getToken(Jinja2Parser.CLOSE_TAG, 0); }
		public TemplateContext template() {
			return getRuleContext(TemplateContext.class,0);
		}
		public End_forContext end_for() {
			return getRuleContext(End_forContext.class,0);
		}
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
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
		enterRule(_localctx, 8, RULE_for_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(OPEN_TAG);
			setState(74);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(73);
				match(MINUS);
				}
			}

			setState(76);
			match(FOR);
			setState(77);
			target();
			setState(78);
			match(IN);
			setState(79);
			expr(0);
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(80);
				match(MINUS);
				}
			}

			setState(83);
			match(CLOSE_TAG);
			setState(84);
			template();
			setState(85);
			end_for();
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
	public static class End_forContext extends ParserRuleContext {
		public TerminalNode OPEN_TAG() { return getToken(Jinja2Parser.OPEN_TAG, 0); }
		public TerminalNode ENDFOR() { return getToken(Jinja2Parser.ENDFOR, 0); }
		public TerminalNode CLOSE_TAG() { return getToken(Jinja2Parser.CLOSE_TAG, 0); }
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
		}
		public End_forContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_end_for; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterEnd_for(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitEnd_for(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitEnd_for(this);
			else return visitor.visitChildren(this);
		}
	}

	public final End_forContext end_for() throws RecognitionException {
		End_forContext _localctx = new End_forContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_end_for);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			match(OPEN_TAG);
			setState(89);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(88);
				match(MINUS);
				}
			}

			setState(91);
			match(ENDFOR);
			setState(93);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(92);
				match(MINUS);
				}
			}

			setState(95);
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
	public static class TargetContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(Jinja2Parser.ID); }
		public TerminalNode ID(int i) {
			return getToken(Jinja2Parser.ID, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Jinja2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Jinja2Parser.COMMA, i);
		}
		public TargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_target; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterTarget(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitTarget(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitTarget(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TargetContext target() throws RecognitionException {
		TargetContext _localctx = new TargetContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_target);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			match(ID);
			setState(102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(98);
				match(COMMA);
				setState(99);
				match(ID);
				}
				}
				setState(104);
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
		public List<TemplateContext> template() {
			return getRuleContexts(TemplateContext.class);
		}
		public TemplateContext template(int i) {
			return getRuleContext(TemplateContext.class,i);
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
		enterRule(_localctx, 14, RULE_if_block);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(OPEN_TAG);
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(106);
				match(MINUS);
				}
			}

			setState(109);
			match(IF);
			setState(110);
			expr(0);
			setState(112);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(111);
				match(MINUS);
				}
			}

			setState(114);
			match(CLOSE_TAG);
			setState(115);
			template();
			setState(130);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(116);
					match(OPEN_TAG);
					setState(118);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==MINUS) {
						{
						setState(117);
						match(MINUS);
						}
					}

					setState(120);
					match(ELIF);
					setState(121);
					expr(0);
					setState(123);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==MINUS) {
						{
						setState(122);
						match(MINUS);
						}
					}

					setState(125);
					match(CLOSE_TAG);
					setState(126);
					template();
					}
					} 
				}
				setState(132);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			setState(143);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(133);
				match(OPEN_TAG);
				setState(135);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(134);
					match(MINUS);
					}
				}

				setState(137);
				match(ELSE);
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(138);
					match(MINUS);
					}
				}

				setState(141);
				match(CLOSE_TAG);
				setState(142);
				template();
				}
				break;
			}
			setState(145);
			match(OPEN_TAG);
			setState(147);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(146);
				match(MINUS);
				}
			}

			setState(149);
			match(ENDIF);
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(150);
				match(MINUS);
				}
			}

			setState(153);
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
	public static class Set_inlineContext extends ParserRuleContext {
		public TerminalNode OPEN_TAG() { return getToken(Jinja2Parser.OPEN_TAG, 0); }
		public TerminalNode SET() { return getToken(Jinja2Parser.SET, 0); }
		public Set_targetsContext set_targets() {
			return getRuleContext(Set_targetsContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(Jinja2Parser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode CLOSE_TAG() { return getToken(Jinja2Parser.CLOSE_TAG, 0); }
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
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
			setState(155);
			match(OPEN_TAG);
			setState(157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(156);
				match(MINUS);
				}
			}

			setState(159);
			match(SET);
			setState(160);
			set_targets();
			setState(161);
			match(ASSIGN);
			setState(162);
			expr(0);
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(163);
				match(MINUS);
				}
			}

			setState(166);
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
		public Set_targetsContext set_targets() {
			return getRuleContext(Set_targetsContext.class,0);
		}
		public List<TerminalNode> CLOSE_TAG() { return getTokens(Jinja2Parser.CLOSE_TAG); }
		public TerminalNode CLOSE_TAG(int i) {
			return getToken(Jinja2Parser.CLOSE_TAG, i);
		}
		public TemplateContext template() {
			return getRuleContext(TemplateContext.class,0);
		}
		public TerminalNode ENDSET() { return getToken(Jinja2Parser.ENDSET, 0); }
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
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
			setState(168);
			match(OPEN_TAG);
			setState(170);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(169);
				match(MINUS);
				}
			}

			setState(172);
			match(SET);
			setState(173);
			set_targets();
			setState(175);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(174);
				match(MINUS);
				}
			}

			setState(177);
			match(CLOSE_TAG);
			setState(178);
			template();
			setState(179);
			match(OPEN_TAG);
			setState(181);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(180);
				match(MINUS);
				}
			}

			setState(183);
			match(ENDSET);
			setState(185);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(184);
				match(MINUS);
				}
			}

			setState(187);
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
	public static class Set_targetsContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(Jinja2Parser.ID); }
		public TerminalNode ID(int i) {
			return getToken(Jinja2Parser.ID, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Jinja2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Jinja2Parser.COMMA, i);
		}
		public Set_targetsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_set_targets; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterSet_targets(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitSet_targets(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitSet_targets(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Set_targetsContext set_targets() throws RecognitionException {
		Set_targetsContext _localctx = new Set_targetsContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_set_targets);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			match(ID);
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(190);
				match(COMMA);
				setState(191);
				match(ID);
				}
				}
				setState(196);
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
		public TemplateContext template() {
			return getRuleContext(TemplateContext.class,0);
		}
		public TerminalNode ENDMACRO() { return getToken(Jinja2Parser.ENDMACRO, 0); }
		public List<TerminalNode> MINUS() { return getTokens(Jinja2Parser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(Jinja2Parser.MINUS, i);
		}
		public Call_paramsContext call_params() {
			return getRuleContext(Call_paramsContext.class,0);
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
		enterRule(_localctx, 22, RULE_macro_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			match(OPEN_TAG);
			setState(199);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(198);
				match(MINUS);
				}
			}

			setState(201);
			match(MACRO);
			setState(202);
			match(ID);
			setState(203);
			match(LPAREN);
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(204);
				call_params();
				}
			}

			setState(207);
			match(RPAREN);
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(208);
				match(MINUS);
				}
			}

			setState(211);
			match(CLOSE_TAG);
			setState(212);
			template();
			setState(213);
			match(OPEN_TAG);
			setState(215);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(214);
				match(MINUS);
				}
			}

			setState(217);
			match(ENDMACRO);
			setState(219);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(218);
				match(MINUS);
				}
			}

			setState(221);
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
	public static class Call_paramsContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Jinja2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Jinja2Parser.COMMA, i);
		}
		public Call_paramsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_call_params; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterCall_params(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitCall_params(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitCall_params(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Call_paramsContext call_params() throws RecognitionException {
		Call_paramsContext _localctx = new Call_paramsContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_call_params);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(223);
			param();
			setState(228);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(224);
				match(COMMA);
				setState(225);
				param();
				}
				}
				setState(230);
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
	public static class ParamContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(Jinja2Parser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(Jinja2Parser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			match(ID);
			setState(234);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(232);
				match(ASSIGN);
				setState(233);
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
		public TemplateContext template() {
			return getRuleContext(TemplateContext.class,0);
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
		enterRule(_localctx, 28, RULE_block_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			match(OPEN_TAG);
			setState(238);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(237);
				match(MINUS);
				}
			}

			setState(240);
			match(BLOCK);
			setState(241);
			match(ID);
			setState(243);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(242);
				match(MINUS);
				}
			}

			setState(245);
			match(CLOSE_TAG);
			setState(246);
			template();
			setState(247);
			match(OPEN_TAG);
			setState(249);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(248);
				match(MINUS);
				}
			}

			setState(251);
			match(ENDBLOCK);
			setState(253);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(252);
				match(MINUS);
				}
			}

			setState(255);
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
		enterRule(_localctx, 30, RULE_extends_stmt);
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
			match(EXTENDS);
			setState(262);
			match(STRING);
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
		enterRule(_localctx, 32, RULE_include_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
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
			match(INCLUDE);
			setState(273);
			expr(0);
			setState(275);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(274);
				match(MINUS);
				}
			}

			setState(277);
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
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(Jinja2Parser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(Jinja2Parser.MINUS, 0); }
		public TerminalNode NOT() { return getToken(Jinja2Parser.NOT, 0); }
		public TerminalNode STAR() { return getToken(Jinja2Parser.STAR, 0); }
		public TerminalNode SLASH() { return getToken(Jinja2Parser.SLASH, 0); }
		public TerminalNode PERCENT() { return getToken(Jinja2Parser.PERCENT, 0); }
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
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitExpr(this);
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
		int _startState = 34;
		enterRecursionRule(_localctx, 34, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case NONE:
			case LPAREN:
			case STRING:
			case NUMBER:
			case ID:
				{
				setState(280);
				primary(0);
				}
				break;
			case NOT:
			case PLUS:
			case MINUS:
				{
				setState(281);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 3288334336L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(282);
				expr(6);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(302);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(300);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(285);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(286);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 30064771072L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(287);
						expr(6);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(288);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(289);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(290);
						expr(5);
						}
						break;
					case 3:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(291);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(292);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2164671906048L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(293);
						expr(4);
						}
						break;
					case 4:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(294);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(295);
						match(AND);
						setState(296);
						expr(3);
						}
						break;
					case 5:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(297);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(298);
						match(OR);
						setState(299);
						expr(2);
						}
						break;
					}
					} 
				}
				setState(304);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
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
	public static class PrimaryContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(Jinja2Parser.NUMBER, 0); }
		public TerminalNode STRING() { return getToken(Jinja2Parser.STRING, 0); }
		public TerminalNode TRUE() { return getToken(Jinja2Parser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(Jinja2Parser.FALSE, 0); }
		public TerminalNode NONE() { return getToken(Jinja2Parser.NONE, 0); }
		public TerminalNode ID() { return getToken(Jinja2Parser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(Jinja2Parser.LPAREN, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(Jinja2Parser.RPAREN, 0); }
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public TerminalNode DOT() { return getToken(Jinja2Parser.DOT, 0); }
		public TerminalNode LBRACK() { return getToken(Jinja2Parser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(Jinja2Parser.RBRACK, 0); }
		public List<TerminalNode> COMMA() { return getTokens(Jinja2Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Jinja2Parser.COMMA, i);
		}
		public TerminalNode PIPE() { return getToken(Jinja2Parser.PIPE, 0); }
		public TerminalNode IS() { return getToken(Jinja2Parser.IS, 0); }
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).enterPrimary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Jinja2ParserListener ) ((Jinja2ParserListener)listener).exitPrimary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Jinja2ParserVisitor ) return ((Jinja2ParserVisitor<? extends T>)visitor).visitPrimary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryContext primary() throws RecognitionException {
		return primary(0);
	}

	private PrimaryContext primary(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PrimaryContext _localctx = new PrimaryContext(_ctx, _parentState);
		PrimaryContext _prevctx = _localctx;
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_primary, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER:
				{
				setState(306);
				match(NUMBER);
				}
				break;
			case STRING:
				{
				setState(307);
				match(STRING);
				}
				break;
			case TRUE:
				{
				setState(308);
				match(TRUE);
				}
				break;
			case FALSE:
				{
				setState(309);
				match(FALSE);
				}
				break;
			case NONE:
				{
				setState(310);
				match(NONE);
				}
				break;
			case ID:
				{
				setState(311);
				match(ID);
				}
				break;
			case LPAREN:
				{
				setState(312);
				match(LPAREN);
				setState(313);
				expr(0);
				setState(314);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(375);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(373);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
					case 1:
						{
						_localctx = new PrimaryContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_primary);
						setState(318);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(319);
						match(DOT);
						setState(320);
						match(ID);
						}
						break;
					case 2:
						{
						_localctx = new PrimaryContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_primary);
						setState(321);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(322);
						match(LBRACK);
						setState(323);
						expr(0);
						setState(324);
						match(RBRACK);
						}
						break;
					case 3:
						{
						_localctx = new PrimaryContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_primary);
						setState(326);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(327);
						match(LPAREN);
						setState(336);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 7951672319934464L) != 0)) {
							{
							setState(328);
							expr(0);
							setState(333);
							_errHandler.sync(this);
							_la = _input.LA(1);
							while (_la==COMMA) {
								{
								{
								setState(329);
								match(COMMA);
								setState(330);
								expr(0);
								}
								}
								setState(335);
								_errHandler.sync(this);
								_la = _input.LA(1);
							}
							}
						}

						setState(338);
						match(RPAREN);
						}
						break;
					case 4:
						{
						_localctx = new PrimaryContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_primary);
						setState(339);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(340);
						match(PIPE);
						setState(341);
						match(ID);
						setState(354);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
						case 1:
							{
							setState(342);
							match(LPAREN);
							setState(351);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 7951672319934464L) != 0)) {
								{
								setState(343);
								expr(0);
								setState(348);
								_errHandler.sync(this);
								_la = _input.LA(1);
								while (_la==COMMA) {
									{
									{
									setState(344);
									match(COMMA);
									setState(345);
									expr(0);
									}
									}
									setState(350);
									_errHandler.sync(this);
									_la = _input.LA(1);
								}
								}
							}

							setState(353);
							match(RPAREN);
							}
							break;
						}
						}
						break;
					case 5:
						{
						_localctx = new PrimaryContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_primary);
						setState(356);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(357);
						match(IS);
						setState(358);
						match(ID);
						setState(371);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
						case 1:
							{
							setState(359);
							match(LPAREN);
							setState(368);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 7951672319934464L) != 0)) {
								{
								setState(360);
								expr(0);
								setState(365);
								_errHandler.sync(this);
								_la = _input.LA(1);
								while (_la==COMMA) {
									{
									{
									setState(361);
									match(COMMA);
									setState(362);
									expr(0);
									}
									}
									setState(367);
									_errHandler.sync(this);
									_la = _input.LA(1);
								}
								}
							}

							setState(370);
							match(RPAREN);
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(377);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 17:
			return expr_sempred((ExprContext)_localctx, predIndex);
		case 18:
			return primary_sempred((PrimaryContext)_localctx, predIndex);
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
	private boolean primary_sempred(PrimaryContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return precpred(_ctx, 5);
		case 6:
			return precpred(_ctx, 4);
		case 7:
			return precpred(_ctx, 3);
		case 8:
			return precpred(_ctx, 2);
		case 9:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u00016\u017b\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000"+
		",\b\u0000\n\u0000\f\u0000/\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0003\u00015\b\u0001\u0001\u0001\u0001\u0001\u0003\u0001"+
		"9\b\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0003\u0002B\b\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u0003G\b\u0003\u0001\u0004\u0001\u0004\u0003\u0004"+
		"K\b\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0003\u0004R\b\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0003\u0005Z\b\u0005\u0001\u0005\u0001\u0005"+
		"\u0003\u0005^\b\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0005\u0006e\b\u0006\n\u0006\f\u0006h\t\u0006\u0001\u0007"+
		"\u0001\u0007\u0003\u0007l\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0003\u0007q\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0003\u0007w\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007"+
		"|\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u0081\b\u0007"+
		"\n\u0007\f\u0007\u0084\t\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u0088"+
		"\b\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u008c\b\u0007\u0001\u0007"+
		"\u0001\u0007\u0003\u0007\u0090\b\u0007\u0001\u0007\u0001\u0007\u0003\u0007"+
		"\u0094\b\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u0098\b\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0003\b\u009e\b\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0003\b\u00a5\b\b\u0001\b\u0001\b\u0001\t\u0001\t\u0003"+
		"\t\u00ab\b\t\u0001\t\u0001\t\u0001\t\u0003\t\u00b0\b\t\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0003\t\u00b6\b\t\u0001\t\u0001\t\u0003\t\u00ba\b\t\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\n\u0005\n\u00c1\b\n\n\n\f\n\u00c4\t\n"+
		"\u0001\u000b\u0001\u000b\u0003\u000b\u00c8\b\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0003\u000b\u00ce\b\u000b\u0001\u000b\u0001\u000b"+
		"\u0003\u000b\u00d2\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0003\u000b\u00d8\b\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u00dc\b"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0005\f\u00e3\b"+
		"\f\n\f\f\f\u00e6\t\f\u0001\r\u0001\r\u0001\r\u0003\r\u00eb\b\r\u0001\u000e"+
		"\u0001\u000e\u0003\u000e\u00ef\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0003\u000e\u00f4\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0003\u000e\u00fa\b\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00fe\b"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0003\u000f\u0104"+
		"\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0109\b\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0003\u0010\u010f\b\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u0114\b\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011"+
		"\u011c\b\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u012d\b\u0011"+
		"\n\u0011\f\u0011\u0130\t\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0003\u0012\u013d\b\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u014c"+
		"\b\u0012\n\u0012\f\u0012\u014f\t\u0012\u0003\u0012\u0151\b\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0005\u0012\u015b\b\u0012\n\u0012\f\u0012\u015e\t\u0012"+
		"\u0003\u0012\u0160\b\u0012\u0001\u0012\u0003\u0012\u0163\b\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0005\u0012\u016c\b\u0012\n\u0012\f\u0012\u016f\t\u0012\u0003\u0012"+
		"\u0171\b\u0012\u0001\u0012\u0003\u0012\u0174\b\u0012\u0005\u0012\u0176"+
		"\b\u0012\n\u0012\f\u0012\u0179\t\u0012\u0001\u0012\u0000\u0002\"$\u0013"+
		"\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$\u0000\u0004\u0002\u0000\u001a\u001a\u001e\u001f\u0001"+
		"\u0000 \"\u0001\u0000\u001e\u001f\u0003\u0000\b\b\u0017\u0017#(\u01b2"+
		"\u0000-\u0001\u0000\u0000\u0000\u00022\u0001\u0000\u0000\u0000\u0004A"+
		"\u0001\u0000\u0000\u0000\u0006F\u0001\u0000\u0000\u0000\bH\u0001\u0000"+
		"\u0000\u0000\nW\u0001\u0000\u0000\u0000\fa\u0001\u0000\u0000\u0000\u000e"+
		"i\u0001\u0000\u0000\u0000\u0010\u009b\u0001\u0000\u0000\u0000\u0012\u00a8"+
		"\u0001\u0000\u0000\u0000\u0014\u00bd\u0001\u0000\u0000\u0000\u0016\u00c5"+
		"\u0001\u0000\u0000\u0000\u0018\u00df\u0001\u0000\u0000\u0000\u001a\u00e7"+
		"\u0001\u0000\u0000\u0000\u001c\u00ec\u0001\u0000\u0000\u0000\u001e\u0101"+
		"\u0001\u0000\u0000\u0000 \u010c\u0001\u0000\u0000\u0000\"\u011b\u0001"+
		"\u0000\u0000\u0000$\u013c\u0001\u0000\u0000\u0000&,\u00056\u0000\u0000"+
		"\',\u0003\u0002\u0001\u0000(,\u0003\u0004\u0002\u0000),\u0003\u0006\u0003"+
		"\u0000*,\u0005\u0005\u0000\u0000+&\u0001\u0000\u0000\u0000+\'\u0001\u0000"+
		"\u0000\u0000+(\u0001\u0000\u0000\u0000+)\u0001\u0000\u0000\u0000+*\u0001"+
		"\u0000\u0000\u0000,/\u0001\u0000\u0000\u0000-+\u0001\u0000\u0000\u0000"+
		"-.\u0001\u0000\u0000\u0000.0\u0001\u0000\u0000\u0000/-\u0001\u0000\u0000"+
		"\u000001\u0005\u0000\u0000\u00011\u0001\u0001\u0000\u0000\u000024\u0005"+
		"\u0001\u0000\u000035\u0005\u001f\u0000\u000043\u0001\u0000\u0000\u0000"+
		"45\u0001\u0000\u0000\u000056\u0001\u0000\u0000\u000068\u0003\"\u0011\u0000"+
		"79\u0005\u001f\u0000\u000087\u0001\u0000\u0000\u000089\u0001\u0000\u0000"+
		"\u00009:\u0001\u0000\u0000\u0000:;\u0005\u0002\u0000\u0000;\u0003\u0001"+
		"\u0000\u0000\u0000<B\u0003\b\u0004\u0000=B\u0003\u000e\u0007\u0000>B\u0003"+
		"\u0012\t\u0000?B\u0003\u0016\u000b\u0000@B\u0003\u001c\u000e\u0000A<\u0001"+
		"\u0000\u0000\u0000A=\u0001\u0000\u0000\u0000A>\u0001\u0000\u0000\u0000"+
		"A?\u0001\u0000\u0000\u0000A@\u0001\u0000\u0000\u0000B\u0005\u0001\u0000"+
		"\u0000\u0000CG\u0003\u001e\u000f\u0000DG\u0003 \u0010\u0000EG\u0003\u0010"+
		"\b\u0000FC\u0001\u0000\u0000\u0000FD\u0001\u0000\u0000\u0000FE\u0001\u0000"+
		"\u0000\u0000G\u0007\u0001\u0000\u0000\u0000HJ\u0005\u0003\u0000\u0000"+
		"IK\u0005\u001f\u0000\u0000JI\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000"+
		"\u0000KL\u0001\u0000\u0000\u0000LM\u0005\u0007\u0000\u0000MN\u0003\f\u0006"+
		"\u0000NO\u0005\b\u0000\u0000OQ\u0003\"\u0011\u0000PR\u0005\u001f\u0000"+
		"\u0000QP\u0001\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000RS\u0001\u0000"+
		"\u0000\u0000ST\u0005\u0004\u0000\u0000TU\u0003\u0000\u0000\u0000UV\u0003"+
		"\n\u0005\u0000V\t\u0001\u0000\u0000\u0000WY\u0005\u0003\u0000\u0000XZ"+
		"\u0005\u001f\u0000\u0000YX\u0001\u0000\u0000\u0000YZ\u0001\u0000\u0000"+
		"\u0000Z[\u0001\u0000\u0000\u0000[]\u0005\t\u0000\u0000\\^\u0005\u001f"+
		"\u0000\u0000]\\\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000^_\u0001"+
		"\u0000\u0000\u0000_`\u0005\u0004\u0000\u0000`\u000b\u0001\u0000\u0000"+
		"\u0000af\u00054\u0000\u0000bc\u0005,\u0000\u0000ce\u00054\u0000\u0000"+
		"db\u0001\u0000\u0000\u0000eh\u0001\u0000\u0000\u0000fd\u0001\u0000\u0000"+
		"\u0000fg\u0001\u0000\u0000\u0000g\r\u0001\u0000\u0000\u0000hf\u0001\u0000"+
		"\u0000\u0000ik\u0005\u0003\u0000\u0000jl\u0005\u001f\u0000\u0000kj\u0001"+
		"\u0000\u0000\u0000kl\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000"+
		"mn\u0005\n\u0000\u0000np\u0003\"\u0011\u0000oq\u0005\u001f\u0000\u0000"+
		"po\u0001\u0000\u0000\u0000pq\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000"+
		"\u0000rs\u0005\u0004\u0000\u0000s\u0082\u0003\u0000\u0000\u0000tv\u0005"+
		"\u0003\u0000\u0000uw\u0005\u001f\u0000\u0000vu\u0001\u0000\u0000\u0000"+
		"vw\u0001\u0000\u0000\u0000wx\u0001\u0000\u0000\u0000xy\u0005\u000b\u0000"+
		"\u0000y{\u0003\"\u0011\u0000z|\u0005\u001f\u0000\u0000{z\u0001\u0000\u0000"+
		"\u0000{|\u0001\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}~\u0005\u0004"+
		"\u0000\u0000~\u007f\u0003\u0000\u0000\u0000\u007f\u0081\u0001\u0000\u0000"+
		"\u0000\u0080t\u0001\u0000\u0000\u0000\u0081\u0084\u0001\u0000\u0000\u0000"+
		"\u0082\u0080\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000\u0000"+
		"\u0083\u008f\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000\u0000\u0000"+
		"\u0085\u0087\u0005\u0003\u0000\u0000\u0086\u0088\u0005\u001f\u0000\u0000"+
		"\u0087\u0086\u0001\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000\u0000"+
		"\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u008b\u0005\f\u0000\u0000\u008a"+
		"\u008c\u0005\u001f\u0000\u0000\u008b\u008a\u0001\u0000\u0000\u0000\u008b"+
		"\u008c\u0001\u0000\u0000\u0000\u008c\u008d\u0001\u0000\u0000\u0000\u008d"+
		"\u008e\u0005\u0004\u0000\u0000\u008e\u0090\u0003\u0000\u0000\u0000\u008f"+
		"\u0085\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000\u0000\u0000\u0090"+
		"\u0091\u0001\u0000\u0000\u0000\u0091\u0093\u0005\u0003\u0000\u0000\u0092"+
		"\u0094\u0005\u001f\u0000\u0000\u0093\u0092\u0001\u0000\u0000\u0000\u0093"+
		"\u0094\u0001\u0000\u0000\u0000\u0094\u0095\u0001\u0000\u0000\u0000\u0095"+
		"\u0097\u0005\r\u0000\u0000\u0096\u0098\u0005\u001f\u0000\u0000\u0097\u0096"+
		"\u0001\u0000\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0099"+
		"\u0001\u0000\u0000\u0000\u0099\u009a\u0005\u0004\u0000\u0000\u009a\u000f"+
		"\u0001\u0000\u0000\u0000\u009b\u009d\u0005\u0003\u0000\u0000\u009c\u009e"+
		"\u0005\u001f\u0000\u0000\u009d\u009c\u0001\u0000\u0000\u0000\u009d\u009e"+
		"\u0001\u0000\u0000\u0000\u009e\u009f\u0001\u0000\u0000\u0000\u009f\u00a0"+
		"\u0005\u0012\u0000\u0000\u00a0\u00a1\u0003\u0014\n\u0000\u00a1\u00a2\u0005"+
		")\u0000\u0000\u00a2\u00a4\u0003\"\u0011\u0000\u00a3\u00a5\u0005\u001f"+
		"\u0000\u0000\u00a4\u00a3\u0001\u0000\u0000\u0000\u00a4\u00a5\u0001\u0000"+
		"\u0000\u0000\u00a5\u00a6\u0001\u0000\u0000\u0000\u00a6\u00a7\u0005\u0004"+
		"\u0000\u0000\u00a7\u0011\u0001\u0000\u0000\u0000\u00a8\u00aa\u0005\u0003"+
		"\u0000\u0000\u00a9\u00ab\u0005\u001f\u0000\u0000\u00aa\u00a9\u0001\u0000"+
		"\u0000\u0000\u00aa\u00ab\u0001\u0000\u0000\u0000\u00ab\u00ac\u0001\u0000"+
		"\u0000\u0000\u00ac\u00ad\u0005\u0012\u0000\u0000\u00ad\u00af\u0003\u0014"+
		"\n\u0000\u00ae\u00b0\u0005\u001f\u0000\u0000\u00af\u00ae\u0001\u0000\u0000"+
		"\u0000\u00af\u00b0\u0001\u0000\u0000\u0000\u00b0\u00b1\u0001\u0000\u0000"+
		"\u0000\u00b1\u00b2\u0005\u0004\u0000\u0000\u00b2\u00b3\u0003\u0000\u0000"+
		"\u0000\u00b3\u00b5\u0005\u0003\u0000\u0000\u00b4\u00b6\u0005\u001f\u0000"+
		"\u0000\u00b5\u00b4\u0001\u0000\u0000\u0000\u00b5\u00b6\u0001\u0000\u0000"+
		"\u0000\u00b6\u00b7\u0001\u0000\u0000\u0000\u00b7\u00b9\u0005\u0013\u0000"+
		"\u0000\u00b8\u00ba\u0005\u001f\u0000\u0000\u00b9\u00b8\u0001\u0000\u0000"+
		"\u0000\u00b9\u00ba\u0001\u0000\u0000\u0000\u00ba\u00bb\u0001\u0000\u0000"+
		"\u0000\u00bb\u00bc\u0005\u0004\u0000\u0000\u00bc\u0013\u0001\u0000\u0000"+
		"\u0000\u00bd\u00c2\u00054\u0000\u0000\u00be\u00bf\u0005,\u0000\u0000\u00bf"+
		"\u00c1\u00054\u0000\u0000\u00c0\u00be\u0001\u0000\u0000\u0000\u00c1\u00c4"+
		"\u0001\u0000\u0000\u0000\u00c2\u00c0\u0001\u0000\u0000\u0000\u00c2\u00c3"+
		"\u0001\u0000\u0000\u0000\u00c3\u0015\u0001\u0000\u0000\u0000\u00c4\u00c2"+
		"\u0001\u0000\u0000\u0000\u00c5\u00c7\u0005\u0003\u0000\u0000\u00c6\u00c8"+
		"\u0005\u001f\u0000\u0000\u00c7\u00c6\u0001\u0000\u0000\u0000\u00c7\u00c8"+
		"\u0001\u0000\u0000\u0000\u00c8\u00c9\u0001\u0000\u0000\u0000\u00c9\u00ca"+
		"\u0005\u0010\u0000\u0000\u00ca\u00cb\u00054\u0000\u0000\u00cb\u00cd\u0005"+
		".\u0000\u0000\u00cc\u00ce\u0003\u0018\f\u0000\u00cd\u00cc\u0001\u0000"+
		"\u0000\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u00cf\u0001\u0000"+
		"\u0000\u0000\u00cf\u00d1\u0005/\u0000\u0000\u00d0\u00d2\u0005\u001f\u0000"+
		"\u0000\u00d1\u00d0\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000"+
		"\u0000\u00d2\u00d3\u0001\u0000\u0000\u0000\u00d3\u00d4\u0005\u0004\u0000"+
		"\u0000\u00d4\u00d5\u0003\u0000\u0000\u0000\u00d5\u00d7\u0005\u0003\u0000"+
		"\u0000\u00d6\u00d8\u0005\u001f\u0000\u0000\u00d7\u00d6\u0001\u0000\u0000"+
		"\u0000\u00d7\u00d8\u0001\u0000\u0000\u0000\u00d8\u00d9\u0001\u0000\u0000"+
		"\u0000\u00d9\u00db\u0005\u0011\u0000\u0000\u00da\u00dc\u0005\u001f\u0000"+
		"\u0000\u00db\u00da\u0001\u0000\u0000\u0000\u00db\u00dc\u0001\u0000\u0000"+
		"\u0000\u00dc\u00dd\u0001\u0000\u0000\u0000\u00dd\u00de\u0005\u0004\u0000"+
		"\u0000\u00de\u0017\u0001\u0000\u0000\u0000\u00df\u00e4\u0003\u001a\r\u0000"+
		"\u00e0\u00e1\u0005,\u0000\u0000\u00e1\u00e3\u0003\u001a\r\u0000\u00e2"+
		"\u00e0\u0001\u0000\u0000\u0000\u00e3\u00e6\u0001\u0000\u0000\u0000\u00e4"+
		"\u00e2\u0001\u0000\u0000\u0000\u00e4\u00e5\u0001\u0000\u0000\u0000\u00e5"+
		"\u0019\u0001\u0000\u0000\u0000\u00e6\u00e4\u0001\u0000\u0000\u0000\u00e7"+
		"\u00ea\u00054\u0000\u0000\u00e8\u00e9\u0005)\u0000\u0000\u00e9\u00eb\u0003"+
		"\"\u0011\u0000\u00ea\u00e8\u0001\u0000\u0000\u0000\u00ea\u00eb\u0001\u0000"+
		"\u0000\u0000\u00eb\u001b\u0001\u0000\u0000\u0000\u00ec\u00ee\u0005\u0003"+
		"\u0000\u0000\u00ed\u00ef\u0005\u001f\u0000\u0000\u00ee\u00ed\u0001\u0000"+
		"\u0000\u0000\u00ee\u00ef\u0001\u0000\u0000\u0000\u00ef\u00f0\u0001\u0000"+
		"\u0000\u0000\u00f0\u00f1\u0005\u000e\u0000\u0000\u00f1\u00f3\u00054\u0000"+
		"\u0000\u00f2\u00f4\u0005\u001f\u0000\u0000\u00f3\u00f2\u0001\u0000\u0000"+
		"\u0000\u00f3\u00f4\u0001\u0000\u0000\u0000\u00f4\u00f5\u0001\u0000\u0000"+
		"\u0000\u00f5\u00f6\u0005\u0004\u0000\u0000\u00f6\u00f7\u0003\u0000\u0000"+
		"\u0000\u00f7\u00f9\u0005\u0003\u0000\u0000\u00f8\u00fa\u0005\u001f\u0000"+
		"\u0000\u00f9\u00f8\u0001\u0000\u0000\u0000\u00f9\u00fa\u0001\u0000\u0000"+
		"\u0000\u00fa\u00fb\u0001\u0000\u0000\u0000\u00fb\u00fd\u0005\u000f\u0000"+
		"\u0000\u00fc\u00fe\u0005\u001f\u0000\u0000\u00fd\u00fc\u0001\u0000\u0000"+
		"\u0000\u00fd\u00fe\u0001\u0000\u0000\u0000\u00fe\u00ff\u0001\u0000\u0000"+
		"\u0000\u00ff\u0100\u0005\u0004\u0000\u0000\u0100\u001d\u0001\u0000\u0000"+
		"\u0000\u0101\u0103\u0005\u0003\u0000\u0000\u0102\u0104\u0005\u001f\u0000"+
		"\u0000\u0103\u0102\u0001\u0000\u0000\u0000\u0103\u0104\u0001\u0000\u0000"+
		"\u0000\u0104\u0105\u0001\u0000\u0000\u0000\u0105\u0106\u0005\u0014\u0000"+
		"\u0000\u0106\u0108\u00052\u0000\u0000\u0107\u0109\u0005\u001f\u0000\u0000"+
		"\u0108\u0107\u0001\u0000\u0000\u0000\u0108\u0109\u0001\u0000\u0000\u0000"+
		"\u0109\u010a\u0001\u0000\u0000\u0000\u010a\u010b\u0005\u0004\u0000\u0000"+
		"\u010b\u001f\u0001\u0000\u0000\u0000\u010c\u010e\u0005\u0003\u0000\u0000"+
		"\u010d\u010f\u0005\u001f\u0000\u0000\u010e\u010d\u0001\u0000\u0000\u0000"+
		"\u010e\u010f\u0001\u0000\u0000\u0000\u010f\u0110\u0001\u0000\u0000\u0000"+
		"\u0110\u0111\u0005\u0006\u0000\u0000\u0111\u0113\u0003\"\u0011\u0000\u0112"+
		"\u0114\u0005\u001f\u0000\u0000\u0113\u0112\u0001\u0000\u0000\u0000\u0113"+
		"\u0114\u0001\u0000\u0000\u0000\u0114\u0115\u0001\u0000\u0000\u0000\u0115"+
		"\u0116\u0005\u0004\u0000\u0000\u0116!\u0001\u0000\u0000\u0000\u0117\u0118"+
		"\u0006\u0011\uffff\uffff\u0000\u0118\u011c\u0003$\u0012\u0000\u0119\u011a"+
		"\u0007\u0000\u0000\u0000\u011a\u011c\u0003\"\u0011\u0006\u011b\u0117\u0001"+
		"\u0000\u0000\u0000\u011b\u0119\u0001\u0000\u0000\u0000\u011c\u012e\u0001"+
		"\u0000\u0000\u0000\u011d\u011e\n\u0005\u0000\u0000\u011e\u011f\u0007\u0001"+
		"\u0000\u0000\u011f\u012d\u0003\"\u0011\u0006\u0120\u0121\n\u0004\u0000"+
		"\u0000\u0121\u0122\u0007\u0002\u0000\u0000\u0122\u012d\u0003\"\u0011\u0005"+
		"\u0123\u0124\n\u0003\u0000\u0000\u0124\u0125\u0007\u0003\u0000\u0000\u0125"+
		"\u012d\u0003\"\u0011\u0004\u0126\u0127\n\u0002\u0000\u0000\u0127\u0128"+
		"\u0005\u0019\u0000\u0000\u0128\u012d\u0003\"\u0011\u0003\u0129\u012a\n"+
		"\u0001\u0000\u0000\u012a\u012b\u0005\u0018\u0000\u0000\u012b\u012d\u0003"+
		"\"\u0011\u0002\u012c\u011d\u0001\u0000\u0000\u0000\u012c\u0120\u0001\u0000"+
		"\u0000\u0000\u012c\u0123\u0001\u0000\u0000\u0000\u012c\u0126\u0001\u0000"+
		"\u0000\u0000\u012c\u0129\u0001\u0000\u0000\u0000\u012d\u0130\u0001\u0000"+
		"\u0000\u0000\u012e\u012c\u0001\u0000\u0000\u0000\u012e\u012f\u0001\u0000"+
		"\u0000\u0000\u012f#\u0001\u0000\u0000\u0000\u0130\u012e\u0001\u0000\u0000"+
		"\u0000\u0131\u0132\u0006\u0012\uffff\uffff\u0000\u0132\u013d\u00053\u0000"+
		"\u0000\u0133\u013d\u00052\u0000\u0000\u0134\u013d\u0005\u001b\u0000\u0000"+
		"\u0135\u013d\u0005\u001c\u0000\u0000\u0136\u013d\u0005\u001d\u0000\u0000"+
		"\u0137\u013d\u00054\u0000\u0000\u0138\u0139\u0005.\u0000\u0000\u0139\u013a"+
		"\u0003\"\u0011\u0000\u013a\u013b\u0005/\u0000\u0000\u013b\u013d\u0001"+
		"\u0000\u0000\u0000\u013c\u0131\u0001\u0000\u0000\u0000\u013c\u0133\u0001"+
		"\u0000\u0000\u0000\u013c\u0134\u0001\u0000\u0000\u0000\u013c\u0135\u0001"+
		"\u0000\u0000\u0000\u013c\u0136\u0001\u0000\u0000\u0000\u013c\u0137\u0001"+
		"\u0000\u0000\u0000\u013c\u0138\u0001\u0000\u0000\u0000\u013d\u0177\u0001"+
		"\u0000\u0000\u0000\u013e\u013f\n\u0005\u0000\u0000\u013f\u0140\u0005+"+
		"\u0000\u0000\u0140\u0176\u00054\u0000\u0000\u0141\u0142\n\u0004\u0000"+
		"\u0000\u0142\u0143\u00050\u0000\u0000\u0143\u0144\u0003\"\u0011\u0000"+
		"\u0144\u0145\u00051\u0000\u0000\u0145\u0176\u0001\u0000\u0000\u0000\u0146"+
		"\u0147\n\u0003\u0000\u0000\u0147\u0150\u0005.\u0000\u0000\u0148\u014d"+
		"\u0003\"\u0011\u0000\u0149\u014a\u0005,\u0000\u0000\u014a\u014c\u0003"+
		"\"\u0011\u0000\u014b\u0149\u0001\u0000\u0000\u0000\u014c\u014f\u0001\u0000"+
		"\u0000\u0000\u014d\u014b\u0001\u0000\u0000\u0000\u014d\u014e\u0001\u0000"+
		"\u0000\u0000\u014e\u0151\u0001\u0000\u0000\u0000\u014f\u014d\u0001\u0000"+
		"\u0000\u0000\u0150\u0148\u0001\u0000\u0000\u0000\u0150\u0151\u0001\u0000"+
		"\u0000\u0000\u0151\u0152\u0001\u0000\u0000\u0000\u0152\u0176\u0005/\u0000"+
		"\u0000\u0153\u0154\n\u0002\u0000\u0000\u0154\u0155\u0005*\u0000\u0000"+
		"\u0155\u0162\u00054\u0000\u0000\u0156\u015f\u0005.\u0000\u0000\u0157\u015c"+
		"\u0003\"\u0011\u0000\u0158\u0159\u0005,\u0000\u0000\u0159\u015b\u0003"+
		"\"\u0011\u0000\u015a\u0158\u0001\u0000\u0000\u0000\u015b\u015e\u0001\u0000"+
		"\u0000\u0000\u015c\u015a\u0001\u0000\u0000\u0000\u015c\u015d\u0001\u0000"+
		"\u0000\u0000\u015d\u0160\u0001\u0000\u0000\u0000\u015e\u015c\u0001\u0000"+
		"\u0000\u0000\u015f\u0157\u0001\u0000\u0000\u0000\u015f\u0160\u0001\u0000"+
		"\u0000\u0000\u0160\u0161\u0001\u0000\u0000\u0000\u0161\u0163\u0005/\u0000"+
		"\u0000\u0162\u0156\u0001\u0000\u0000\u0000\u0162\u0163\u0001\u0000\u0000"+
		"\u0000\u0163\u0176\u0001\u0000\u0000\u0000\u0164\u0165\n\u0001\u0000\u0000"+
		"\u0165\u0166\u0005\u0017\u0000\u0000\u0166\u0173\u00054\u0000\u0000\u0167"+
		"\u0170\u0005.\u0000\u0000\u0168\u016d\u0003\"\u0011\u0000\u0169\u016a"+
		"\u0005,\u0000\u0000\u016a\u016c\u0003\"\u0011\u0000\u016b\u0169\u0001"+
		"\u0000\u0000\u0000\u016c\u016f\u0001\u0000\u0000\u0000\u016d\u016b\u0001"+
		"\u0000\u0000\u0000\u016d\u016e\u0001\u0000\u0000\u0000\u016e\u0171\u0001"+
		"\u0000\u0000\u0000\u016f\u016d\u0001\u0000\u0000\u0000\u0170\u0168\u0001"+
		"\u0000\u0000\u0000\u0170\u0171\u0001\u0000\u0000\u0000\u0171\u0172\u0001"+
		"\u0000\u0000\u0000\u0172\u0174\u0005/\u0000\u0000\u0173\u0167\u0001\u0000"+
		"\u0000\u0000\u0173\u0174\u0001\u0000\u0000\u0000\u0174\u0176\u0001\u0000"+
		"\u0000\u0000\u0175\u013e\u0001\u0000\u0000\u0000\u0175\u0141\u0001\u0000"+
		"\u0000\u0000\u0175\u0146\u0001\u0000\u0000\u0000\u0175\u0153\u0001\u0000"+
		"\u0000\u0000\u0175\u0164\u0001\u0000\u0000\u0000\u0176\u0179\u0001\u0000"+
		"\u0000\u0000\u0177\u0175\u0001\u0000\u0000\u0000\u0177\u0178\u0001\u0000"+
		"\u0000\u0000\u0178%\u0001\u0000\u0000\u0000\u0179\u0177\u0001\u0000\u0000"+
		"\u00009+-48AFJQY]fkpv{\u0082\u0087\u008b\u008f\u0093\u0097\u009d\u00a4"+
		"\u00aa\u00af\u00b5\u00b9\u00c2\u00c7\u00cd\u00d1\u00d7\u00db\u00e4\u00ea"+
		"\u00ee\u00f3\u00f9\u00fd\u0103\u0108\u010e\u0113\u011b\u012c\u012e\u013c"+
		"\u014d\u0150\u015c\u015f\u0162\u016d\u0170\u0173\u0175\u0177";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}