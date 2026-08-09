// Generated from C:/Users/Yousef.Abdulmonaem/Documents/GitHub/flask-compiler/grammars/python/PythonParser.g4 by ANTLR 4.13.2

    package antlr.python;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class PythonParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INDENT=1, DEDENT=2, GLOBAL=3, DEF=4, RETURN=5, IMPORT=6, IF=7, ELIF=8, 
		ELSE=9, WHILE=10, FOR=11, TRY=12, FINALLY=13, OR=14, NOT=15, AND=16, TRUE=17, 
		PASS=18, FALSE=19, CONTINUE=20, BREAK=21, IN=22, IS=23, FROM=24, NONE=25, 
		ARROW=26, POWER_ASSIGN=27, ADD_ASSIGN=28, SUB_ASSIGN=29, MULT_ASSIGN=30, 
		DIV_ASSIGN=31, MOD_ASSIGN=32, AND_ASSIGN=33, OR_ASSIGN=34, XOR_ASSIGN=35, 
		LSHIFT_ASSIGN=36, RSHIFT_ASSIGN=37, IDIV_ASSIGN=38, DOT=39, STAR=40, COMMA=41, 
		COLON=42, SEMI_COLON=43, ASSIGN=44, OR_OP=45, XOR=46, AND_OP=47, LSHIFT=48, 
		RSHIFT=49, POWER=50, ADD=51, MINUS=52, DIV=53, MOD=54, IDIV=55, NOT_OP=56, 
		LESS_THAN=57, GREATER_THAN=58, EQUALS=59, GT_EQ=60, LT_EQ=61, NOT_EQ=62, 
		AT=63, STRING=64, INTEGER=65, FLOAT=66, OPEN_PAREN=67, CLOSE_PAREN=68, 
		OPEN_BRACE=69, CLOSE_BRACE=70, OPEN_BRACKET=71, CLOSE_BRACKET=72, ID=73, 
		NEWLINE=74, WS=75, COMMENT=76;
	public static final int
		RULE_prog = 0, RULE_stmt = 1, RULE_compound_stmt = 2, RULE_simple_stmt = 3, 
		RULE_small_stmt = 4, RULE_augassign_stmt = 5, RULE_pass_stmt = 6, RULE_break_stmt = 7, 
		RULE_continue_stmt = 8, RULE_return_stmt = 9, RULE_import_stmt = 10, RULE_global_stmt = 11, 
		RULE_dotted_name = 12, RULE_expr_stmt = 13, RULE_condlist = 14, RULE_cond = 15, 
		RULE_comparison = 16, RULE_expr = 17, RULE_atom = 18, RULE_trailer = 19, 
		RULE_arguments = 20, RULE_arglist = 21, RULE_argument = 22, RULE_subscriptlist = 23, 
		RULE_iter = 24, RULE_funcdef = 25, RULE_def_parameters = 26, RULE_def_parameter = 27, 
		RULE_elif_clause = 28, RULE_else_clause = 29, RULE_body = 30, RULE_list = 31, 
		RULE_dicorset = 32, RULE_decorator = 33;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "stmt", "compound_stmt", "simple_stmt", "small_stmt", "augassign_stmt", 
			"pass_stmt", "break_stmt", "continue_stmt", "return_stmt", "import_stmt", 
			"global_stmt", "dotted_name", "expr_stmt", "condlist", "cond", "comparison", 
			"expr", "atom", "trailer", "arguments", "arglist", "argument", "subscriptlist", 
			"iter", "funcdef", "def_parameters", "def_parameter", "elif_clause", 
			"else_clause", "body", "list", "dicorset", "decorator"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'global'", "'def'", "'return'", "'import'", "'if'", 
			"'elif'", "'else'", "'while'", "'for'", "'try'", "'finally'", "'or'", 
			"'not'", "'and'", "'True'", "'pass'", "'False'", "'continue'", "'break'", 
			"'in'", "'is'", "'from'", "'None'", "'->'", "'**='", "'+='", "'-='", 
			"'*='", "'/='", "'%='", "'&='", "'|='", "'^='", "'<<='", "'>>='", "'//='", 
			"'.'", "'*'", "','", "':'", "';'", "'='", "'|'", "'^'", "'&'", "'<<'", 
			"'>>'", "'**'", "'+'", "'-'", "'/'", "'%'", "'//'", "'~'", "'<'", "'>'", 
			"'=='", "'>='", "'<='", "'!='", "'@'", null, null, null, "'('", "')'", 
			"'{'", "'}'", "'['", "']'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "INDENT", "DEDENT", "GLOBAL", "DEF", "RETURN", "IMPORT", "IF", 
			"ELIF", "ELSE", "WHILE", "FOR", "TRY", "FINALLY", "OR", "NOT", "AND", 
			"TRUE", "PASS", "FALSE", "CONTINUE", "BREAK", "IN", "IS", "FROM", "NONE", 
			"ARROW", "POWER_ASSIGN", "ADD_ASSIGN", "SUB_ASSIGN", "MULT_ASSIGN", "DIV_ASSIGN", 
			"MOD_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", "LSHIFT_ASSIGN", 
			"RSHIFT_ASSIGN", "IDIV_ASSIGN", "DOT", "STAR", "COMMA", "COLON", "SEMI_COLON", 
			"ASSIGN", "OR_OP", "XOR", "AND_OP", "LSHIFT", "RSHIFT", "POWER", "ADD", 
			"MINUS", "DIV", "MOD", "IDIV", "NOT_OP", "LESS_THAN", "GREATER_THAN", 
			"EQUALS", "GT_EQ", "LT_EQ", "NOT_EQ", "AT", "STRING", "INTEGER", "FLOAT", 
			"OPEN_PAREN", "CLOSE_PAREN", "OPEN_BRACE", "CLOSE_BRACE", "OPEN_BRACKET", 
			"CLOSE_BRACKET", "ID", "NEWLINE", "WS", "COMMENT"
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
	public String getGrammarFileName() { return "PythonParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PythonParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(PythonParser.EOF, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(PythonParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(PythonParser.NEWLINE, i);
		}
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitProg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -9144559043321361160L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 1711L) != 0)) {
				{
				setState(70);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NEWLINE:
					{
					setState(68);
					match(NEWLINE);
					}
					break;
				case GLOBAL:
				case DEF:
				case RETURN:
				case IMPORT:
				case IF:
				case WHILE:
				case FOR:
				case NOT:
				case TRUE:
				case PASS:
				case FALSE:
				case CONTINUE:
				case BREAK:
				case FROM:
				case NONE:
				case ADD:
				case MINUS:
				case NOT_OP:
				case AT:
				case STRING:
				case INTEGER:
				case FLOAT:
				case OPEN_PAREN:
				case OPEN_BRACE:
				case OPEN_BRACKET:
				case ID:
					{
					setState(69);
					stmt();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(75);
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
	public static class CompoundStatementContext extends StmtContext {
		public Compound_stmtContext compound_stmt() {
			return getRuleContext(Compound_stmtContext.class,0);
		}
		public CompoundStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterCompoundStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitCompoundStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitCompoundStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleStatementContext extends StmtContext {
		public Simple_stmtContext simple_stmt() {
			return getRuleContext(Simple_stmtContext.class,0);
		}
		public SimpleStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterSimpleStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitSimpleStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitSimpleStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stmt);
		try {
			setState(79);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GLOBAL:
			case RETURN:
			case IMPORT:
			case NOT:
			case TRUE:
			case PASS:
			case FALSE:
			case CONTINUE:
			case BREAK:
			case FROM:
			case NONE:
			case ADD:
			case MINUS:
			case NOT_OP:
			case STRING:
			case INTEGER:
			case FLOAT:
			case OPEN_PAREN:
			case OPEN_BRACE:
			case OPEN_BRACKET:
			case ID:
				_localctx = new SimpleStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(77);
				simple_stmt();
				}
				break;
			case DEF:
			case IF:
			case WHILE:
			case FOR:
			case AT:
				_localctx = new CompoundStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(78);
				compound_stmt();
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
	public static class Compound_stmtContext extends ParserRuleContext {
		public Compound_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compound_stmt; }
	 
		public Compound_stmtContext() { }
		public void copyFrom(Compound_stmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfStatementContext extends Compound_stmtContext {
		public TerminalNode IF() { return getToken(PythonParser.IF, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public TerminalNode COLON() { return getToken(PythonParser.COLON, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public List<Elif_clauseContext> elif_clause() {
			return getRuleContexts(Elif_clauseContext.class);
		}
		public Elif_clauseContext elif_clause(int i) {
			return getRuleContext(Elif_clauseContext.class,i);
		}
		public Else_clauseContext else_clause() {
			return getRuleContext(Else_clauseContext.class,0);
		}
		public IfStatementContext(Compound_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatementContext extends Compound_stmtContext {
		public TerminalNode WHILE() { return getToken(PythonParser.WHILE, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public TerminalNode COLON() { return getToken(PythonParser.COLON, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public Else_clauseContext else_clause() {
			return getRuleContext(Else_clauseContext.class,0);
		}
		public WhileStatementContext(Compound_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterWhileStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitWhileStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitWhileStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DecoratorStatementContext extends Compound_stmtContext {
		public FuncdefContext funcdef() {
			return getRuleContext(FuncdefContext.class,0);
		}
		public List<DecoratorContext> decorator() {
			return getRuleContexts(DecoratorContext.class);
		}
		public DecoratorContext decorator(int i) {
			return getRuleContext(DecoratorContext.class,i);
		}
		public DecoratorStatementContext(Compound_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterDecoratorStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitDecoratorStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitDecoratorStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ForStatementContext extends Compound_stmtContext {
		public TerminalNode FOR() { return getToken(PythonParser.FOR, 0); }
		public IterContext iter() {
			return getRuleContext(IterContext.class,0);
		}
		public TerminalNode IN() { return getToken(PythonParser.IN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode COLON() { return getToken(PythonParser.COLON, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public Else_clauseContext else_clause() {
			return getRuleContext(Else_clauseContext.class,0);
		}
		public ForStatementContext(Compound_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterForStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitForStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitForStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Compound_stmtContext compound_stmt() throws RecognitionException {
		Compound_stmtContext _localctx = new Compound_stmtContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_compound_stmt);
		int _la;
		try {
			setState(117);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IF:
				_localctx = new IfStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(81);
				match(IF);
				setState(82);
				cond(0);
				setState(83);
				match(COLON);
				setState(84);
				body();
				setState(88);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ELIF) {
					{
					{
					setState(85);
					elif_clause();
					}
					}
					setState(90);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(92);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(91);
					else_clause();
					}
				}

				}
				break;
			case WHILE:
				_localctx = new WhileStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(94);
				match(WHILE);
				setState(95);
				cond(0);
				setState(96);
				match(COLON);
				setState(97);
				body();
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(98);
					else_clause();
					}
				}

				}
				break;
			case FOR:
				_localctx = new ForStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(101);
				match(FOR);
				setState(102);
				iter();
				setState(103);
				match(IN);
				setState(104);
				expr(0);
				setState(105);
				match(COLON);
				setState(106);
				body();
				setState(108);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(107);
					else_clause();
					}
				}

				}
				break;
			case DEF:
			case AT:
				_localctx = new DecoratorStatementContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==AT) {
					{
					{
					setState(110);
					decorator();
					}
					}
					setState(115);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				{
				setState(116);
				funcdef();
				}
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
	public static class Simple_stmtContext extends ParserRuleContext {
		public List<Small_stmtContext> small_stmt() {
			return getRuleContexts(Small_stmtContext.class);
		}
		public Small_stmtContext small_stmt(int i) {
			return getRuleContext(Small_stmtContext.class,i);
		}
		public TerminalNode NEWLINE() { return getToken(PythonParser.NEWLINE, 0); }
		public TerminalNode EOF() { return getToken(PythonParser.EOF, 0); }
		public List<TerminalNode> SEMI_COLON() { return getTokens(PythonParser.SEMI_COLON); }
		public TerminalNode SEMI_COLON(int i) {
			return getToken(PythonParser.SEMI_COLON, i);
		}
		public Simple_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterSimple_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitSimple_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitSimple_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Simple_stmtContext simple_stmt() throws RecognitionException {
		Simple_stmtContext _localctx = new Simple_stmtContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_simple_stmt);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			small_stmt();
			setState(124);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(120);
					match(SEMI_COLON);
					setState(121);
					small_stmt();
					}
					} 
				}
				setState(126);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			setState(128);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI_COLON) {
				{
				setState(127);
				match(SEMI_COLON);
				}
			}

			setState(130);
			_la = _input.LA(1);
			if ( !(_la==EOF || _la==NEWLINE) ) {
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
	public static class Small_stmtContext extends ParserRuleContext {
		public Small_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_small_stmt; }
	 
		public Small_stmtContext() { }
		public void copyFrom(Small_stmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BreakStatementContext extends Small_stmtContext {
		public Break_stmtContext break_stmt() {
			return getRuleContext(Break_stmtContext.class,0);
		}
		public BreakStatementContext(Small_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterBreakStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitBreakStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitBreakStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AugAssignStatementContext extends Small_stmtContext {
		public Augassign_stmtContext augassign_stmt() {
			return getRuleContext(Augassign_stmtContext.class,0);
		}
		public AugAssignStatementContext(Small_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterAugAssignStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitAugAssignStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitAugAssignStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GlobalStatementContext extends Small_stmtContext {
		public Global_stmtContext global_stmt() {
			return getRuleContext(Global_stmtContext.class,0);
		}
		public GlobalStatementContext(Small_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterGlobalStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitGlobalStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitGlobalStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionStatementContext extends Small_stmtContext {
		public Expr_stmtContext expr_stmt() {
			return getRuleContext(Expr_stmtContext.class,0);
		}
		public ExpressionStatementContext(Small_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterExpressionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitExpressionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitExpressionStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReturnStatementContext extends Small_stmtContext {
		public Return_stmtContext return_stmt() {
			return getRuleContext(Return_stmtContext.class,0);
		}
		public ReturnStatementContext(Small_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterReturnStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitReturnStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitReturnStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PassStatementContext extends Small_stmtContext {
		public Pass_stmtContext pass_stmt() {
			return getRuleContext(Pass_stmtContext.class,0);
		}
		public PassStatementContext(Small_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterPassStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitPassStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitPassStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ImportStatementContext extends Small_stmtContext {
		public Import_stmtContext import_stmt() {
			return getRuleContext(Import_stmtContext.class,0);
		}
		public ImportStatementContext(Small_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterImportStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitImportStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitImportStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStatementContext extends Small_stmtContext {
		public Continue_stmtContext continue_stmt() {
			return getRuleContext(Continue_stmtContext.class,0);
		}
		public ContinueStatementContext(Small_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterContinueStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitContinueStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitContinueStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Small_stmtContext small_stmt() throws RecognitionException {
		Small_stmtContext _localctx = new Small_stmtContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_small_stmt);
		try {
			setState(140);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				_localctx = new ExpressionStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(132);
				expr_stmt();
				}
				break;
			case 2:
				_localctx = new AugAssignStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(133);
				augassign_stmt();
				}
				break;
			case 3:
				_localctx = new PassStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(134);
				pass_stmt();
				}
				break;
			case 4:
				_localctx = new BreakStatementContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(135);
				break_stmt();
				}
				break;
			case 5:
				_localctx = new ContinueStatementContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(136);
				continue_stmt();
				}
				break;
			case 6:
				_localctx = new ReturnStatementContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(137);
				return_stmt();
				}
				break;
			case 7:
				_localctx = new ImportStatementContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(138);
				import_stmt();
				}
				break;
			case 8:
				_localctx = new GlobalStatementContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(139);
				global_stmt();
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
	public static class Augassign_stmtContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PythonParser.ID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ADD_ASSIGN() { return getToken(PythonParser.ADD_ASSIGN, 0); }
		public TerminalNode SUB_ASSIGN() { return getToken(PythonParser.SUB_ASSIGN, 0); }
		public TerminalNode MULT_ASSIGN() { return getToken(PythonParser.MULT_ASSIGN, 0); }
		public TerminalNode DIV_ASSIGN() { return getToken(PythonParser.DIV_ASSIGN, 0); }
		public TerminalNode MOD_ASSIGN() { return getToken(PythonParser.MOD_ASSIGN, 0); }
		public TerminalNode IDIV_ASSIGN() { return getToken(PythonParser.IDIV_ASSIGN, 0); }
		public TerminalNode AND_ASSIGN() { return getToken(PythonParser.AND_ASSIGN, 0); }
		public TerminalNode OR_ASSIGN() { return getToken(PythonParser.OR_ASSIGN, 0); }
		public TerminalNode XOR_ASSIGN() { return getToken(PythonParser.XOR_ASSIGN, 0); }
		public TerminalNode LSHIFT_ASSIGN() { return getToken(PythonParser.LSHIFT_ASSIGN, 0); }
		public TerminalNode RSHIFT_ASSIGN() { return getToken(PythonParser.RSHIFT_ASSIGN, 0); }
		public TerminalNode POWER_ASSIGN() { return getToken(PythonParser.POWER_ASSIGN, 0); }
		public Augassign_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_augassign_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterAugassign_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitAugassign_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitAugassign_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Augassign_stmtContext augassign_stmt() throws RecognitionException {
		Augassign_stmtContext _localctx = new Augassign_stmtContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_augassign_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			match(ID);
			setState(143);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 549621596160L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(144);
			expr(0);
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
	public static class Pass_stmtContext extends ParserRuleContext {
		public TerminalNode PASS() { return getToken(PythonParser.PASS, 0); }
		public Pass_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pass_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterPass_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitPass_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitPass_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pass_stmtContext pass_stmt() throws RecognitionException {
		Pass_stmtContext _localctx = new Pass_stmtContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_pass_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			match(PASS);
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
	public static class Break_stmtContext extends ParserRuleContext {
		public TerminalNode BREAK() { return getToken(PythonParser.BREAK, 0); }
		public Break_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_break_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterBreak_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitBreak_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitBreak_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Break_stmtContext break_stmt() throws RecognitionException {
		Break_stmtContext _localctx = new Break_stmtContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_break_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(BREAK);
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
	public static class Continue_stmtContext extends ParserRuleContext {
		public TerminalNode CONTINUE() { return getToken(PythonParser.CONTINUE, 0); }
		public Continue_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continue_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterContinue_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitContinue_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitContinue_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Continue_stmtContext continue_stmt() throws RecognitionException {
		Continue_stmtContext _localctx = new Continue_stmtContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_continue_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			match(CONTINUE);
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
	public static class Return_stmtContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(PythonParser.RETURN, 0); }
		public CondlistContext condlist() {
			return getRuleContext(CondlistContext.class,0);
		}
		public Return_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_return_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterReturn_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitReturn_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitReturn_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Return_stmtContext return_stmt() throws RecognitionException {
		Return_stmtContext _localctx = new Return_stmtContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_return_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(RETURN);
			setState(154);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 15)) & ~0x3f) == 0 && ((1L << (_la - 15)) & 386749023182128149L) != 0)) {
				{
				setState(153);
				condlist();
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
	public static class Import_stmtContext extends ParserRuleContext {
		public Import_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_import_stmt; }
	 
		public Import_stmtContext() { }
		public void copyFrom(Import_stmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleImportContext extends Import_stmtContext {
		public TerminalNode IMPORT() { return getToken(PythonParser.IMPORT, 0); }
		public Dotted_nameContext dotted_name() {
			return getRuleContext(Dotted_nameContext.class,0);
		}
		public SimpleImportContext(Import_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterSimpleImport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitSimpleImport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitSimpleImport(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FromImportContext extends Import_stmtContext {
		public TerminalNode FROM() { return getToken(PythonParser.FROM, 0); }
		public Dotted_nameContext dotted_name() {
			return getRuleContext(Dotted_nameContext.class,0);
		}
		public TerminalNode IMPORT() { return getToken(PythonParser.IMPORT, 0); }
		public TerminalNode STAR() { return getToken(PythonParser.STAR, 0); }
		public List<TerminalNode> ID() { return getTokens(PythonParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(PythonParser.ID, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PythonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PythonParser.COMMA, i);
		}
		public FromImportContext(Import_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterFromImport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitFromImport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitFromImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Import_stmtContext import_stmt() throws RecognitionException {
		Import_stmtContext _localctx = new Import_stmtContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_import_stmt);
		int _la;
		try {
			setState(172);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IMPORT:
				_localctx = new SimpleImportContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(156);
				match(IMPORT);
				setState(157);
				dotted_name();
				}
				break;
			case FROM:
				_localctx = new FromImportContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(158);
				match(FROM);
				setState(159);
				dotted_name();
				setState(160);
				match(IMPORT);
				setState(170);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case STAR:
					{
					setState(161);
					match(STAR);
					}
					break;
				case ID:
					{
					{
					setState(162);
					match(ID);
					setState(167);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(163);
						match(COMMA);
						setState(164);
						match(ID);
						}
						}
						setState(169);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
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
	public static class Global_stmtContext extends ParserRuleContext {
		public TerminalNode GLOBAL() { return getToken(PythonParser.GLOBAL, 0); }
		public List<TerminalNode> ID() { return getTokens(PythonParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(PythonParser.ID, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PythonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PythonParser.COMMA, i);
		}
		public Global_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_global_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterGlobal_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitGlobal_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitGlobal_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Global_stmtContext global_stmt() throws RecognitionException {
		Global_stmtContext _localctx = new Global_stmtContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_global_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			match(GLOBAL);
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
	public static class Dotted_nameContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(PythonParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(PythonParser.ID, i);
		}
		public List<TerminalNode> DOT() { return getTokens(PythonParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(PythonParser.DOT, i);
		}
		public Dotted_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dotted_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterDotted_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitDotted_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitDotted_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Dotted_nameContext dotted_name() throws RecognitionException {
		Dotted_nameContext _localctx = new Dotted_nameContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_dotted_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(ID);
			setState(188);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(184);
				match(DOT);
				setState(185);
				match(ID);
				}
				}
				setState(190);
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
	public static class Expr_stmtContext extends ParserRuleContext {
		public List<CondlistContext> condlist() {
			return getRuleContexts(CondlistContext.class);
		}
		public CondlistContext condlist(int i) {
			return getRuleContext(CondlistContext.class,i);
		}
		public TerminalNode ASSIGN() { return getToken(PythonParser.ASSIGN, 0); }
		public Expr_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterExpr_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitExpr_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitExpr_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Expr_stmtContext expr_stmt() throws RecognitionException {
		Expr_stmtContext _localctx = new Expr_stmtContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_expr_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			condlist();
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(192);
				match(ASSIGN);
				setState(193);
				condlist();
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
	public static class CondlistContext extends ParserRuleContext {
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PythonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PythonParser.COMMA, i);
		}
		public CondlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterCondlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitCondlist(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitCondlist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CondlistContext condlist() throws RecognitionException {
		CondlistContext _localctx = new CondlistContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_condlist);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			cond(0);
			setState(201);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(197);
					match(COMMA);
					setState(198);
					cond(0);
					}
					} 
				}
				setState(203);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			}
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(204);
				match(COMMA);
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
	public static class CondContext extends ParserRuleContext {
		public CondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cond; }
	 
		public CondContext() { }
		public void copyFrom(CondContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleConditionContext extends CondContext {
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public SimpleConditionContext(CondContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterSimpleCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitSimpleCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitSimpleCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NotConditionContext extends CondContext {
		public TerminalNode NOT() { return getToken(PythonParser.NOT, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public NotConditionContext(CondContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterNotCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitNotCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitNotCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OrConditionContext extends CondContext {
		public Token op;
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public TerminalNode OR() { return getToken(PythonParser.OR, 0); }
		public OrConditionContext(CondContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterOrCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitOrCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitOrCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AndConditionContext extends CondContext {
		public Token op;
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public TerminalNode AND() { return getToken(PythonParser.AND, 0); }
		public AndConditionContext(CondContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterAndCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitAndCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitAndCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CondContext cond() throws RecognitionException {
		return cond(0);
	}

	private CondContext cond(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		CondContext _localctx = new CondContext(_ctx, _parentState);
		CondContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_cond, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case NONE:
			case ADD:
			case MINUS:
			case NOT_OP:
			case STRING:
			case INTEGER:
			case FLOAT:
			case OPEN_PAREN:
			case OPEN_BRACE:
			case OPEN_BRACKET:
			case ID:
				{
				_localctx = new SimpleConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(208);
				comparison();
				}
				break;
			case NOT:
				{
				_localctx = new NotConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(209);
				match(NOT);
				setState(210);
				cond(3);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(221);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(219);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
					case 1:
						{
						_localctx = new AndConditionContext(new CondContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_cond);
						setState(213);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(214);
						((AndConditionContext)_localctx).op = match(AND);
						setState(215);
						cond(3);
						}
						break;
					case 2:
						{
						_localctx = new OrConditionContext(new CondContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_cond);
						setState(216);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(217);
						((OrConditionContext)_localctx).op = match(OR);
						setState(218);
						cond(2);
						}
						break;
					}
					} 
				}
				setState(223);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
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
	public static class ComparisonContext extends ParserRuleContext {
		public ComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparison; }
	 
		public ComparisonContext() { }
		public void copyFrom(ComparisonContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionComparisonContext extends ComparisonContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExpressionComparisonContext(ComparisonContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterExpressionComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitExpressionComparison(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitExpressionComparison(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RelationalComparisonContext extends ComparisonContext {
		public Token optional;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LESS_THAN() { return getToken(PythonParser.LESS_THAN, 0); }
		public TerminalNode GREATER_THAN() { return getToken(PythonParser.GREATER_THAN, 0); }
		public TerminalNode EQUALS() { return getToken(PythonParser.EQUALS, 0); }
		public TerminalNode GT_EQ() { return getToken(PythonParser.GT_EQ, 0); }
		public TerminalNode LT_EQ() { return getToken(PythonParser.LT_EQ, 0); }
		public TerminalNode NOT_EQ() { return getToken(PythonParser.NOT_EQ, 0); }
		public TerminalNode IN() { return getToken(PythonParser.IN, 0); }
		public TerminalNode IS() { return getToken(PythonParser.IS, 0); }
		public TerminalNode NOT() { return getToken(PythonParser.NOT, 0); }
		public RelationalComparisonContext(ComparisonContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterRelationalComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitRelationalComparison(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitRelationalComparison(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonContext comparison() throws RecognitionException {
		ComparisonContext _localctx = new ComparisonContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_comparison);
		int _la;
		try {
			setState(244);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				_localctx = new RelationalComparisonContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(224);
				expr(0);
				setState(239);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case LESS_THAN:
					{
					setState(225);
					match(LESS_THAN);
					}
					break;
				case GREATER_THAN:
					{
					setState(226);
					match(GREATER_THAN);
					}
					break;
				case EQUALS:
					{
					setState(227);
					match(EQUALS);
					}
					break;
				case GT_EQ:
					{
					setState(228);
					match(GT_EQ);
					}
					break;
				case LT_EQ:
					{
					setState(229);
					match(LT_EQ);
					}
					break;
				case NOT_EQ:
					{
					setState(230);
					match(NOT_EQ);
					}
					break;
				case NOT:
				case IN:
					{
					setState(232);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==NOT) {
						{
						setState(231);
						((RelationalComparisonContext)_localctx).optional = match(NOT);
						}
					}

					setState(234);
					match(IN);
					}
					break;
				case IS:
					{
					setState(235);
					match(IS);
					setState(237);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==NOT) {
						{
						setState(236);
						((RelationalComparisonContext)_localctx).optional = match(NOT);
						}
					}

					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(241);
				expr(0);
				}
				break;
			case 2:
				_localctx = new ExpressionComparisonContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(243);
				expr(0);
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
	public static class AddSubExpressionContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ADD() { return getToken(PythonParser.ADD, 0); }
		public TerminalNode MINUS() { return getToken(PythonParser.MINUS, 0); }
		public AddSubExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterAddSubExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitAddSubExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitAddSubExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PowerExpressionContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode POWER() { return getToken(PythonParser.POWER, 0); }
		public PowerExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterPowerExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitPowerExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitPowerExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AndExpressionContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode AND_OP() { return getToken(PythonParser.AND_OP, 0); }
		public AndExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IDExpressionContext extends ExprContext {
		public TerminalNode ID() { return getToken(PythonParser.ID, 0); }
		public List<TrailerContext> trailer() {
			return getRuleContexts(TrailerContext.class);
		}
		public TrailerContext trailer(int i) {
			return getRuleContext(TrailerContext.class,i);
		}
		public IDExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterIDExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitIDExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitIDExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class XorExpressionContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode XOR() { return getToken(PythonParser.XOR, 0); }
		public XorExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterXorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitXorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitXorExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AtomExpressionContext extends ExprContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public AtomExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterAtomExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitAtomExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitAtomExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryExpressionContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ADD() { return getToken(PythonParser.ADD, 0); }
		public TerminalNode MINUS() { return getToken(PythonParser.MINUS, 0); }
		public TerminalNode NOT_OP() { return getToken(PythonParser.NOT_OP, 0); }
		public UnaryExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterUnaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitUnaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitUnaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ShiftExpressionContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LSHIFT() { return getToken(PythonParser.LSHIFT, 0); }
		public TerminalNode RSHIFT() { return getToken(PythonParser.RSHIFT, 0); }
		public ShiftExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterShiftExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitShiftExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitShiftExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OrExpressionContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OR_OP() { return getToken(PythonParser.OR_OP, 0); }
		public OrExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MulDivExpressionContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode STAR() { return getToken(PythonParser.STAR, 0); }
		public TerminalNode DIV() { return getToken(PythonParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(PythonParser.MOD, 0); }
		public TerminalNode IDIV() { return getToken(PythonParser.IDIV, 0); }
		public TerminalNode AT() { return getToken(PythonParser.AT, 0); }
		public MulDivExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterMulDivExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitMulDivExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitMulDivExpression(this);
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
			setState(257);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				_localctx = new IDExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(247);
				match(ID);
				setState(251);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(248);
						trailer();
						}
						} 
					}
					setState(253);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				}
				}
				break;
			case 2:
				{
				_localctx = new AtomExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(254);
				atom();
				}
				break;
			case 3:
				{
				_localctx = new UnaryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(255);
				((UnaryExpressionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 78812993478983680L) != 0)) ) {
					((UnaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(256);
				expr(7);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(282);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(280);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
					case 1:
						{
						_localctx = new PowerExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(259);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(260);
						((PowerExpressionContext)_localctx).op = match(POWER);
						setState(261);
						expr(8);
						}
						break;
					case 2:
						{
						_localctx = new MulDivExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(262);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(263);
						((MulDivExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & -9160320542559961088L) != 0)) ) {
							((MulDivExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(264);
						expr(7);
						}
						break;
					case 3:
						{
						_localctx = new AddSubExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(265);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(266);
						((AddSubExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==MINUS) ) {
							((AddSubExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(267);
						expr(6);
						}
						break;
					case 4:
						{
						_localctx = new ShiftExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(268);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(269);
						((ShiftExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==LSHIFT || _la==RSHIFT) ) {
							((ShiftExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(270);
						expr(5);
						}
						break;
					case 5:
						{
						_localctx = new AndExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(271);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(272);
						((AndExpressionContext)_localctx).op = match(AND_OP);
						setState(273);
						expr(4);
						}
						break;
					case 6:
						{
						_localctx = new XorExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(274);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(275);
						((XorExpressionContext)_localctx).op = match(XOR);
						setState(276);
						expr(3);
						}
						break;
					case 7:
						{
						_localctx = new OrExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(277);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(278);
						((OrExpressionContext)_localctx).op = match(OR_OP);
						setState(279);
						expr(2);
						}
						break;
					}
					} 
				}
				setState(284);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
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
	public static class AtomContext extends ParserRuleContext {
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
	 
		public AtomContext() { }
		public void copyFrom(AtomContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntegerAtomContext extends AtomContext {
		public TerminalNode INTEGER() { return getToken(PythonParser.INTEGER, 0); }
		public TerminalNode MINUS() { return getToken(PythonParser.MINUS, 0); }
		public IntegerAtomContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterIntegerAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitIntegerAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitIntegerAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IDAtomContext extends AtomContext {
		public TerminalNode ID() { return getToken(PythonParser.ID, 0); }
		public IDAtomContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterIDAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitIDAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitIDAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringAtomContext extends AtomContext {
		public TerminalNode STRING() { return getToken(PythonParser.STRING, 0); }
		public StringAtomContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterStringAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitStringAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitStringAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BoolAtomContext extends AtomContext {
		public TerminalNode TRUE() { return getToken(PythonParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(PythonParser.FALSE, 0); }
		public BoolAtomContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterBoolAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitBoolAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitBoolAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ListAtomContext extends AtomContext {
		public TerminalNode OPEN_BRACKET() { return getToken(PythonParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PythonParser.CLOSE_BRACKET, 0); }
		public ListContext list() {
			return getRuleContext(ListContext.class,0);
		}
		public ListAtomContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterListAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitListAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitListAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenAtomContext extends AtomContext {
		public TerminalNode OPEN_PAREN() { return getToken(PythonParser.OPEN_PAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(PythonParser.CLOSE_PAREN, 0); }
		public ParenAtomContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterParenAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitParenAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitParenAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DicOrSetAtomContext extends AtomContext {
		public TerminalNode OPEN_BRACE() { return getToken(PythonParser.OPEN_BRACE, 0); }
		public TerminalNode CLOSE_BRACE() { return getToken(PythonParser.CLOSE_BRACE, 0); }
		public DicorsetContext dicorset() {
			return getRuleContext(DicorsetContext.class,0);
		}
		public DicOrSetAtomContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterDicOrSetAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitDicOrSetAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitDicOrSetAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NoneAtomContext extends AtomContext {
		public TerminalNode NONE() { return getToken(PythonParser.NONE, 0); }
		public NoneAtomContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterNoneAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitNoneAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitNoneAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FloatAtomContext extends AtomContext {
		public TerminalNode FLOAT() { return getToken(PythonParser.FLOAT, 0); }
		public TerminalNode MINUS() { return getToken(PythonParser.MINUS, 0); }
		public FloatAtomContext(AtomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterFloatAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitFloatAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitFloatAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_atom);
		int _la;
		try {
			setState(312);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				_localctx = new ParenAtomContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(285);
				match(OPEN_PAREN);
				setState(286);
				expr(0);
				setState(287);
				match(CLOSE_PAREN);
				}
				break;
			case 2:
				_localctx = new ListAtomContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(289);
				match(OPEN_BRACKET);
				setState(291);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 17)) & ~0x3f) == 0 && ((1L << (_la - 17)) & 96687255795532037L) != 0)) {
					{
					setState(290);
					list();
					}
				}

				setState(293);
				match(CLOSE_BRACKET);
				}
				break;
			case 3:
				_localctx = new DicOrSetAtomContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(294);
				match(OPEN_BRACE);
				setState(296);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 17)) & ~0x3f) == 0 && ((1L << (_la - 17)) & 96687255795532037L) != 0)) {
					{
					setState(295);
					dicorset();
					}
				}

				setState(298);
				match(CLOSE_BRACE);
				}
				break;
			case 4:
				_localctx = new IDAtomContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(299);
				match(ID);
				}
				break;
			case 5:
				_localctx = new BoolAtomContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(300);
				match(TRUE);
				}
				break;
			case 6:
				_localctx = new BoolAtomContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(301);
				match(FALSE);
				}
				break;
			case 7:
				_localctx = new IntegerAtomContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(303);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(302);
					match(MINUS);
					}
				}

				setState(305);
				match(INTEGER);
				}
				break;
			case 8:
				_localctx = new FloatAtomContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(307);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(306);
					match(MINUS);
					}
				}

				setState(309);
				match(FLOAT);
				}
				break;
			case 9:
				_localctx = new NoneAtomContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(310);
				match(NONE);
				}
				break;
			case 10:
				_localctx = new StringAtomContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(311);
				match(STRING);
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
	public static class TrailerContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(PythonParser.DOT, 0); }
		public TerminalNode ID() { return getToken(PythonParser.ID, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public TrailerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trailer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterTrailer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitTrailer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitTrailer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TrailerContext trailer() throws RecognitionException {
		TrailerContext _localctx = new TrailerContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_trailer);
		try {
			setState(320);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(314);
				match(DOT);
				setState(315);
				match(ID);
				setState(317);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
				case 1:
					{
					setState(316);
					arguments();
					}
					break;
				}
				}
				break;
			case OPEN_PAREN:
			case OPEN_BRACKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(319);
				arguments();
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
	public static class ArgumentsContext extends ParserRuleContext {
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
	 
		public ArgumentsContext() { }
		public void copyFrom(ArgumentsContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CallArgumentsContext extends ArgumentsContext {
		public TerminalNode OPEN_PAREN() { return getToken(PythonParser.OPEN_PAREN, 0); }
		public TerminalNode CLOSE_PAREN() { return getToken(PythonParser.CLOSE_PAREN, 0); }
		public ArglistContext arglist() {
			return getRuleContext(ArglistContext.class,0);
		}
		public CallArgumentsContext(ArgumentsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterCallArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitCallArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitCallArguments(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SubscriptArgumentsContext extends ArgumentsContext {
		public TerminalNode OPEN_BRACKET() { return getToken(PythonParser.OPEN_BRACKET, 0); }
		public SubscriptlistContext subscriptlist() {
			return getRuleContext(SubscriptlistContext.class,0);
		}
		public TerminalNode CLOSE_BRACKET() { return getToken(PythonParser.CLOSE_BRACKET, 0); }
		public SubscriptArgumentsContext(ArgumentsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterSubscriptArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitSubscriptArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitSubscriptArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_arguments);
		int _la;
		try {
			setState(331);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OPEN_PAREN:
				_localctx = new CallArgumentsContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(322);
				match(OPEN_PAREN);
				setState(324);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 15)) & ~0x3f) == 0 && ((1L << (_la - 15)) & 386749023182128149L) != 0)) {
					{
					setState(323);
					arglist();
					}
				}

				setState(326);
				match(CLOSE_PAREN);
				}
				break;
			case OPEN_BRACKET:
				_localctx = new SubscriptArgumentsContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(327);
				match(OPEN_BRACKET);
				setState(328);
				subscriptlist();
				setState(329);
				match(CLOSE_BRACKET);
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
	public static class ArglistContext extends ParserRuleContext {
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PythonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PythonParser.COMMA, i);
		}
		public ArglistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arglist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterArglist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitArglist(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitArglist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArglistContext arglist() throws RecognitionException {
		ArglistContext _localctx = new ArglistContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_arglist);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(333);
			argument();
			setState(338);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(334);
					match(COMMA);
					setState(335);
					argument();
					}
					} 
				}
				setState(340);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
			}
			setState(342);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(341);
				match(COMMA);
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
	public static class ArgumentContext extends ParserRuleContext {
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public TerminalNode ASSIGN() { return getToken(PythonParser.ASSIGN, 0); }
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_argument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
			cond(0);
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(345);
				match(ASSIGN);
				setState(346);
				cond(0);
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
	public static class SubscriptlistContext extends ParserRuleContext {
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PythonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PythonParser.COMMA, i);
		}
		public SubscriptlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subscriptlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterSubscriptlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitSubscriptlist(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitSubscriptlist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubscriptlistContext subscriptlist() throws RecognitionException {
		SubscriptlistContext _localctx = new SubscriptlistContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_subscriptlist);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(349);
			cond(0);
			setState(354);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(350);
					match(COMMA);
					setState(351);
					cond(0);
					}
					} 
				}
				setState(356);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
			}
			setState(358);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(357);
				match(COMMA);
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
	public static class IterContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(PythonParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(PythonParser.ID, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PythonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PythonParser.COMMA, i);
		}
		public IterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterIter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitIter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitIter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IterContext iter() throws RecognitionException {
		IterContext _localctx = new IterContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_iter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
			match(ID);
			setState(365);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(361);
				match(COMMA);
				setState(362);
				match(ID);
				}
				}
				setState(367);
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
	public static class FuncdefContext extends ParserRuleContext {
		public TerminalNode DEF() { return getToken(PythonParser.DEF, 0); }
		public TerminalNode ID() { return getToken(PythonParser.ID, 0); }
		public TerminalNode OPEN_PAREN() { return getToken(PythonParser.OPEN_PAREN, 0); }
		public TerminalNode CLOSE_PAREN() { return getToken(PythonParser.CLOSE_PAREN, 0); }
		public TerminalNode COLON() { return getToken(PythonParser.COLON, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public Def_parametersContext def_parameters() {
			return getRuleContext(Def_parametersContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(PythonParser.COMMA, 0); }
		public TerminalNode ARROW() { return getToken(PythonParser.ARROW, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public FuncdefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcdef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterFuncdef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitFuncdef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitFuncdef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncdefContext funcdef() throws RecognitionException {
		FuncdefContext _localctx = new FuncdefContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_funcdef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(368);
			match(DEF);
			setState(369);
			match(ID);
			setState(370);
			match(OPEN_PAREN);
			setState(372);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(371);
				def_parameters();
				}
			}

			setState(375);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(374);
				match(COMMA);
				}
			}

			setState(377);
			match(CLOSE_PAREN);
			setState(380);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ARROW) {
				{
				setState(378);
				match(ARROW);
				setState(379);
				cond(0);
				}
			}

			setState(382);
			match(COLON);
			setState(383);
			body();
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
	public static class Def_parametersContext extends ParserRuleContext {
		public List<Def_parameterContext> def_parameter() {
			return getRuleContexts(Def_parameterContext.class);
		}
		public Def_parameterContext def_parameter(int i) {
			return getRuleContext(Def_parameterContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PythonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PythonParser.COMMA, i);
		}
		public Def_parametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_def_parameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterDef_parameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitDef_parameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitDef_parameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Def_parametersContext def_parameters() throws RecognitionException {
		Def_parametersContext _localctx = new Def_parametersContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_def_parameters);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(385);
			def_parameter();
			setState(390);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(386);
					match(COMMA);
					setState(387);
					def_parameter();
					}
					} 
				}
				setState(392);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
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
	public static class Def_parameterContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PythonParser.ID, 0); }
		public TerminalNode COLON() { return getToken(PythonParser.COLON, 0); }
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public TerminalNode ASSIGN() { return getToken(PythonParser.ASSIGN, 0); }
		public Def_parameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_def_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterDef_parameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitDef_parameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitDef_parameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Def_parameterContext def_parameter() throws RecognitionException {
		Def_parameterContext _localctx = new Def_parameterContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_def_parameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			match(ID);
			setState(396);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(394);
				match(COLON);
				setState(395);
				cond(0);
				}
			}

			setState(400);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(398);
				match(ASSIGN);
				setState(399);
				cond(0);
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
	public static class Elif_clauseContext extends ParserRuleContext {
		public TerminalNode ELIF() { return getToken(PythonParser.ELIF, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public TerminalNode COLON() { return getToken(PythonParser.COLON, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public Elif_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elif_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterElif_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitElif_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitElif_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Elif_clauseContext elif_clause() throws RecognitionException {
		Elif_clauseContext _localctx = new Elif_clauseContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_elif_clause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402);
			match(ELIF);
			setState(403);
			cond(0);
			setState(404);
			match(COLON);
			setState(405);
			body();
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
	public static class Else_clauseContext extends ParserRuleContext {
		public TerminalNode ELSE() { return getToken(PythonParser.ELSE, 0); }
		public TerminalNode COLON() { return getToken(PythonParser.COLON, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public Else_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_else_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterElse_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitElse_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitElse_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Else_clauseContext else_clause() throws RecognitionException {
		Else_clauseContext _localctx = new Else_clauseContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_else_clause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(407);
			match(ELSE);
			setState(408);
			match(COLON);
			setState(409);
			body();
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
		public Simple_stmtContext simple_stmt() {
			return getRuleContext(Simple_stmtContext.class,0);
		}
		public TerminalNode NEWLINE() { return getToken(PythonParser.NEWLINE, 0); }
		public TerminalNode INDENT() { return getToken(PythonParser.INDENT, 0); }
		public TerminalNode DEDENT() { return getToken(PythonParser.DEDENT, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public BodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyContext body() throws RecognitionException {
		BodyContext _localctx = new BodyContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_body);
		int _la;
		try {
			setState(421);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GLOBAL:
			case RETURN:
			case IMPORT:
			case NOT:
			case TRUE:
			case PASS:
			case FALSE:
			case CONTINUE:
			case BREAK:
			case FROM:
			case NONE:
			case ADD:
			case MINUS:
			case NOT_OP:
			case STRING:
			case INTEGER:
			case FLOAT:
			case OPEN_PAREN:
			case OPEN_BRACE:
			case OPEN_BRACKET:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(411);
				simple_stmt();
				}
				break;
			case NEWLINE:
				enterOuterAlt(_localctx, 2);
				{
				setState(412);
				match(NEWLINE);
				setState(413);
				match(INDENT);
				setState(415); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(414);
					stmt();
					}
					}
					setState(417); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & -9144559043321361160L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 687L) != 0) );
				setState(419);
				match(DEDENT);
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
	public static class ListContext extends ParserRuleContext {
		public ListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list; }
	 
		public ListContext() { }
		public void copyFrom(ListContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ListDefContext extends ListContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PythonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PythonParser.COMMA, i);
		}
		public ListDefContext(ListContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterListDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitListDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitListDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListContext list() throws RecognitionException {
		ListContext _localctx = new ListContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_list);
		int _la;
		try {
			_localctx = new ListDefContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(423);
			expr(0);
			setState(428);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(424);
				match(COMMA);
				setState(425);
				expr(0);
				}
				}
				setState(430);
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
	public static class DicorsetContext extends ParserRuleContext {
		public DicorsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dicorset; }
	 
		public DicorsetContext() { }
		public void copyFrom(DicorsetContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SetDefContext extends DicorsetContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PythonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PythonParser.COMMA, i);
		}
		public SetDefContext(DicorsetContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterSetDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitSetDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitSetDef(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DictionaryDefContext extends DicorsetContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COLON() { return getTokens(PythonParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(PythonParser.COLON, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PythonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PythonParser.COMMA, i);
		}
		public DictionaryDefContext(DicorsetContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterDictionaryDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitDictionaryDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitDictionaryDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DicorsetContext dicorset() throws RecognitionException {
		DicorsetContext _localctx = new DicorsetContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_dicorset);
		int _la;
		try {
			setState(452);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				_localctx = new SetDefContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(431);
				expr(0);
				setState(436);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(432);
					match(COMMA);
					setState(433);
					expr(0);
					}
					}
					setState(438);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 2:
				_localctx = new DictionaryDefContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(439);
				expr(0);
				setState(440);
				match(COLON);
				setState(441);
				expr(0);
				setState(449);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(442);
					match(COMMA);
					setState(443);
					expr(0);
					setState(444);
					match(COLON);
					setState(445);
					expr(0);
					}
					}
					setState(451);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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
	public static class DecoratorContext extends ParserRuleContext {
		public TerminalNode AT() { return getToken(PythonParser.AT, 0); }
		public Dotted_nameContext dotted_name() {
			return getRuleContext(Dotted_nameContext.class,0);
		}
		public TerminalNode NEWLINE() { return getToken(PythonParser.NEWLINE, 0); }
		public TerminalNode OPEN_PAREN() { return getToken(PythonParser.OPEN_PAREN, 0); }
		public TerminalNode CLOSE_PAREN() { return getToken(PythonParser.CLOSE_PAREN, 0); }
		public ArglistContext arglist() {
			return getRuleContext(ArglistContext.class,0);
		}
		public DecoratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decorator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).enterDecorator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PythonParserListener ) ((PythonParserListener)listener).exitDecorator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PythonParserVisitor ) return ((PythonParserVisitor<? extends T>)visitor).visitDecorator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecoratorContext decorator() throws RecognitionException {
		DecoratorContext _localctx = new DecoratorContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_decorator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(454);
			match(AT);
			setState(455);
			dotted_name();
			setState(461);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPEN_PAREN) {
				{
				setState(456);
				match(OPEN_PAREN);
				setState(458);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 15)) & ~0x3f) == 0 && ((1L << (_la - 15)) & 386749023182128149L) != 0)) {
					{
					setState(457);
					arglist();
					}
				}

				setState(460);
				match(CLOSE_PAREN);
				}
			}

			setState(463);
			match(NEWLINE);
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
		case 15:
			return cond_sempred((CondContext)_localctx, predIndex);
		case 17:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean cond_sempred(CondContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 8);
		case 3:
			return precpred(_ctx, 6);
		case 4:
			return precpred(_ctx, 5);
		case 5:
			return precpred(_ctx, 4);
		case 6:
			return precpred(_ctx, 3);
		case 7:
			return precpred(_ctx, 2);
		case 8:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001L\u01d2\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0001\u0000\u0001"+
		"\u0000\u0005\u0000G\b\u0000\n\u0000\f\u0000J\t\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0001\u0001\u0003\u0001P\b\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002W\b\u0002\n\u0002"+
		"\f\u0002Z\t\u0002\u0001\u0002\u0003\u0002]\b\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002d\b\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0003\u0002m\b\u0002\u0001\u0002\u0005\u0002p\b\u0002\n\u0002\f\u0002"+
		"s\t\u0002\u0001\u0002\u0003\u0002v\b\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0005\u0003{\b\u0003\n\u0003\f\u0003~\t\u0003\u0001\u0003\u0003"+
		"\u0003\u0081\b\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003"+
		"\u0004\u008d\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001"+
		"\t\u0003\t\u009b\b\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0005\n\u00a6\b\n\n\n\f\n\u00a9\t\n\u0003\n\u00ab\b"+
		"\n\u0003\n\u00ad\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005"+
		"\u000b\u00b3\b\u000b\n\u000b\f\u000b\u00b6\t\u000b\u0001\f\u0001\f\u0001"+
		"\f\u0005\f\u00bb\b\f\n\f\f\f\u00be\t\f\u0001\r\u0001\r\u0001\r\u0003\r"+
		"\u00c3\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000e\u00c8\b\u000e"+
		"\n\u000e\f\u000e\u00cb\t\u000e\u0001\u000e\u0003\u000e\u00ce\b\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u00d4\b\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0005"+
		"\u000f\u00dc\b\u000f\n\u000f\f\u000f\u00df\t\u000f\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0003\u0010\u00e9\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0003\u0010"+
		"\u00ee\b\u0010\u0003\u0010\u00f0\b\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0003\u0010\u00f5\b\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0005"+
		"\u0011\u00fa\b\u0011\n\u0011\f\u0011\u00fd\t\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0003\u0011\u0102\b\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0005\u0011\u0119\b\u0011\n\u0011\f\u0011\u011c\t\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0124"+
		"\b\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0129\b\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012"+
		"\u0130\b\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0134\b\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0139\b\u0012\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0003\u0013\u013e\b\u0013\u0001\u0013\u0003\u0013\u0141"+
		"\b\u0013\u0001\u0014\u0001\u0014\u0003\u0014\u0145\b\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u014c\b\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0005\u0015\u0151\b\u0015\n\u0015"+
		"\f\u0015\u0154\t\u0015\u0001\u0015\u0003\u0015\u0157\b\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0003\u0016\u015c\b\u0016\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0005\u0017\u0161\b\u0017\n\u0017\f\u0017\u0164\t\u0017\u0001"+
		"\u0017\u0003\u0017\u0167\b\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0005"+
		"\u0018\u016c\b\u0018\n\u0018\f\u0018\u016f\t\u0018\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0003\u0019\u0175\b\u0019\u0001\u0019\u0003\u0019"+
		"\u0178\b\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u017d\b"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0005\u001a\u0185\b\u001a\n\u001a\f\u001a\u0188\t\u001a\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0003\u001b\u018d\b\u001b\u0001\u001b\u0001\u001b"+
		"\u0003\u001b\u0191\b\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0004\u001e\u01a0\b\u001e\u000b\u001e"+
		"\f\u001e\u01a1\u0001\u001e\u0001\u001e\u0003\u001e\u01a6\b\u001e\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0005\u001f\u01ab\b\u001f\n\u001f\f\u001f"+
		"\u01ae\t\u001f\u0001 \u0001 \u0001 \u0005 \u01b3\b \n \f \u01b6\t \u0001"+
		" \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0005 \u01c0\b \n "+
		"\f \u01c3\t \u0003 \u01c5\b \u0001!\u0001!\u0001!\u0001!\u0003!\u01cb"+
		"\b!\u0001!\u0003!\u01ce\b!\u0001!\u0001!\u0001!\u0000\u0002\u001e\"\""+
		"\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$&(*,.02468:<>@B\u0000\u0006\u0001\u0001JJ\u0001\u0000"+
		"\u001b&\u0002\u00003488\u0003\u0000((57??\u0001\u000034\u0001\u000001"+
		"\u0208\u0000H\u0001\u0000\u0000\u0000\u0002O\u0001\u0000\u0000\u0000\u0004"+
		"u\u0001\u0000\u0000\u0000\u0006w\u0001\u0000\u0000\u0000\b\u008c\u0001"+
		"\u0000\u0000\u0000\n\u008e\u0001\u0000\u0000\u0000\f\u0092\u0001\u0000"+
		"\u0000\u0000\u000e\u0094\u0001\u0000\u0000\u0000\u0010\u0096\u0001\u0000"+
		"\u0000\u0000\u0012\u0098\u0001\u0000\u0000\u0000\u0014\u00ac\u0001\u0000"+
		"\u0000\u0000\u0016\u00ae\u0001\u0000\u0000\u0000\u0018\u00b7\u0001\u0000"+
		"\u0000\u0000\u001a\u00bf\u0001\u0000\u0000\u0000\u001c\u00c4\u0001\u0000"+
		"\u0000\u0000\u001e\u00d3\u0001\u0000\u0000\u0000 \u00f4\u0001\u0000\u0000"+
		"\u0000\"\u0101\u0001\u0000\u0000\u0000$\u0138\u0001\u0000\u0000\u0000"+
		"&\u0140\u0001\u0000\u0000\u0000(\u014b\u0001\u0000\u0000\u0000*\u014d"+
		"\u0001\u0000\u0000\u0000,\u0158\u0001\u0000\u0000\u0000.\u015d\u0001\u0000"+
		"\u0000\u00000\u0168\u0001\u0000\u0000\u00002\u0170\u0001\u0000\u0000\u0000"+
		"4\u0181\u0001\u0000\u0000\u00006\u0189\u0001\u0000\u0000\u00008\u0192"+
		"\u0001\u0000\u0000\u0000:\u0197\u0001\u0000\u0000\u0000<\u01a5\u0001\u0000"+
		"\u0000\u0000>\u01a7\u0001\u0000\u0000\u0000@\u01c4\u0001\u0000\u0000\u0000"+
		"B\u01c6\u0001\u0000\u0000\u0000DG\u0005J\u0000\u0000EG\u0003\u0002\u0001"+
		"\u0000FD\u0001\u0000\u0000\u0000FE\u0001\u0000\u0000\u0000GJ\u0001\u0000"+
		"\u0000\u0000HF\u0001\u0000\u0000\u0000HI\u0001\u0000\u0000\u0000IK\u0001"+
		"\u0000\u0000\u0000JH\u0001\u0000\u0000\u0000KL\u0005\u0000\u0000\u0001"+
		"L\u0001\u0001\u0000\u0000\u0000MP\u0003\u0006\u0003\u0000NP\u0003\u0004"+
		"\u0002\u0000OM\u0001\u0000\u0000\u0000ON\u0001\u0000\u0000\u0000P\u0003"+
		"\u0001\u0000\u0000\u0000QR\u0005\u0007\u0000\u0000RS\u0003\u001e\u000f"+
		"\u0000ST\u0005*\u0000\u0000TX\u0003<\u001e\u0000UW\u00038\u001c\u0000"+
		"VU\u0001\u0000\u0000\u0000WZ\u0001\u0000\u0000\u0000XV\u0001\u0000\u0000"+
		"\u0000XY\u0001\u0000\u0000\u0000Y\\\u0001\u0000\u0000\u0000ZX\u0001\u0000"+
		"\u0000\u0000[]\u0003:\u001d\u0000\\[\u0001\u0000\u0000\u0000\\]\u0001"+
		"\u0000\u0000\u0000]v\u0001\u0000\u0000\u0000^_\u0005\n\u0000\u0000_`\u0003"+
		"\u001e\u000f\u0000`a\u0005*\u0000\u0000ac\u0003<\u001e\u0000bd\u0003:"+
		"\u001d\u0000cb\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000dv\u0001"+
		"\u0000\u0000\u0000ef\u0005\u000b\u0000\u0000fg\u00030\u0018\u0000gh\u0005"+
		"\u0016\u0000\u0000hi\u0003\"\u0011\u0000ij\u0005*\u0000\u0000jl\u0003"+
		"<\u001e\u0000km\u0003:\u001d\u0000lk\u0001\u0000\u0000\u0000lm\u0001\u0000"+
		"\u0000\u0000mv\u0001\u0000\u0000\u0000np\u0003B!\u0000on\u0001\u0000\u0000"+
		"\u0000ps\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000qr\u0001\u0000"+
		"\u0000\u0000rt\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000tv\u0003"+
		"2\u0019\u0000uQ\u0001\u0000\u0000\u0000u^\u0001\u0000\u0000\u0000ue\u0001"+
		"\u0000\u0000\u0000uq\u0001\u0000\u0000\u0000v\u0005\u0001\u0000\u0000"+
		"\u0000w|\u0003\b\u0004\u0000xy\u0005+\u0000\u0000y{\u0003\b\u0004\u0000"+
		"zx\u0001\u0000\u0000\u0000{~\u0001\u0000\u0000\u0000|z\u0001\u0000\u0000"+
		"\u0000|}\u0001\u0000\u0000\u0000}\u0080\u0001\u0000\u0000\u0000~|\u0001"+
		"\u0000\u0000\u0000\u007f\u0081\u0005+\u0000\u0000\u0080\u007f\u0001\u0000"+
		"\u0000\u0000\u0080\u0081\u0001\u0000\u0000\u0000\u0081\u0082\u0001\u0000"+
		"\u0000\u0000\u0082\u0083\u0007\u0000\u0000\u0000\u0083\u0007\u0001\u0000"+
		"\u0000\u0000\u0084\u008d\u0003\u001a\r\u0000\u0085\u008d\u0003\n\u0005"+
		"\u0000\u0086\u008d\u0003\f\u0006\u0000\u0087\u008d\u0003\u000e\u0007\u0000"+
		"\u0088\u008d\u0003\u0010\b\u0000\u0089\u008d\u0003\u0012\t\u0000\u008a"+
		"\u008d\u0003\u0014\n\u0000\u008b\u008d\u0003\u0016\u000b\u0000\u008c\u0084"+
		"\u0001\u0000\u0000\u0000\u008c\u0085\u0001\u0000\u0000\u0000\u008c\u0086"+
		"\u0001\u0000\u0000\u0000\u008c\u0087\u0001\u0000\u0000\u0000\u008c\u0088"+
		"\u0001\u0000\u0000\u0000\u008c\u0089\u0001\u0000\u0000\u0000\u008c\u008a"+
		"\u0001\u0000\u0000\u0000\u008c\u008b\u0001\u0000\u0000\u0000\u008d\t\u0001"+
		"\u0000\u0000\u0000\u008e\u008f\u0005I\u0000\u0000\u008f\u0090\u0007\u0001"+
		"\u0000\u0000\u0090\u0091\u0003\"\u0011\u0000\u0091\u000b\u0001\u0000\u0000"+
		"\u0000\u0092\u0093\u0005\u0012\u0000\u0000\u0093\r\u0001\u0000\u0000\u0000"+
		"\u0094\u0095\u0005\u0015\u0000\u0000\u0095\u000f\u0001\u0000\u0000\u0000"+
		"\u0096\u0097\u0005\u0014\u0000\u0000\u0097\u0011\u0001\u0000\u0000\u0000"+
		"\u0098\u009a\u0005\u0005\u0000\u0000\u0099\u009b\u0003\u001c\u000e\u0000"+
		"\u009a\u0099\u0001\u0000\u0000\u0000\u009a\u009b\u0001\u0000\u0000\u0000"+
		"\u009b\u0013\u0001\u0000\u0000\u0000\u009c\u009d\u0005\u0006\u0000\u0000"+
		"\u009d\u00ad\u0003\u0018\f\u0000\u009e\u009f\u0005\u0018\u0000\u0000\u009f"+
		"\u00a0\u0003\u0018\f\u0000\u00a0\u00aa\u0005\u0006\u0000\u0000\u00a1\u00ab"+
		"\u0005(\u0000\u0000\u00a2\u00a7\u0005I\u0000\u0000\u00a3\u00a4\u0005)"+
		"\u0000\u0000\u00a4\u00a6\u0005I\u0000\u0000\u00a5\u00a3\u0001\u0000\u0000"+
		"\u0000\u00a6\u00a9\u0001\u0000\u0000\u0000\u00a7\u00a5\u0001\u0000\u0000"+
		"\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000\u00a8\u00ab\u0001\u0000\u0000"+
		"\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000\u00aa\u00a1\u0001\u0000\u0000"+
		"\u0000\u00aa\u00a2\u0001\u0000\u0000\u0000\u00ab\u00ad\u0001\u0000\u0000"+
		"\u0000\u00ac\u009c\u0001\u0000\u0000\u0000\u00ac\u009e\u0001\u0000\u0000"+
		"\u0000\u00ad\u0015\u0001\u0000\u0000\u0000\u00ae\u00af\u0005\u0003\u0000"+
		"\u0000\u00af\u00b4\u0005I\u0000\u0000\u00b0\u00b1\u0005)\u0000\u0000\u00b1"+
		"\u00b3\u0005I\u0000\u0000\u00b2\u00b0\u0001\u0000\u0000\u0000\u00b3\u00b6"+
		"\u0001\u0000\u0000\u0000\u00b4\u00b2\u0001\u0000\u0000\u0000\u00b4\u00b5"+
		"\u0001\u0000\u0000\u0000\u00b5\u0017\u0001\u0000\u0000\u0000\u00b6\u00b4"+
		"\u0001\u0000\u0000\u0000\u00b7\u00bc\u0005I\u0000\u0000\u00b8\u00b9\u0005"+
		"\'\u0000\u0000\u00b9\u00bb\u0005I\u0000\u0000\u00ba\u00b8\u0001\u0000"+
		"\u0000\u0000\u00bb\u00be\u0001\u0000\u0000\u0000\u00bc\u00ba\u0001\u0000"+
		"\u0000\u0000\u00bc\u00bd\u0001\u0000\u0000\u0000\u00bd\u0019\u0001\u0000"+
		"\u0000\u0000\u00be\u00bc\u0001\u0000\u0000\u0000\u00bf\u00c2\u0003\u001c"+
		"\u000e\u0000\u00c0\u00c1\u0005,\u0000\u0000\u00c1\u00c3\u0003\u001c\u000e"+
		"\u0000\u00c2\u00c0\u0001\u0000\u0000\u0000\u00c2\u00c3\u0001\u0000\u0000"+
		"\u0000\u00c3\u001b\u0001\u0000\u0000\u0000\u00c4\u00c9\u0003\u001e\u000f"+
		"\u0000\u00c5\u00c6\u0005)\u0000\u0000\u00c6\u00c8\u0003\u001e\u000f\u0000"+
		"\u00c7\u00c5\u0001\u0000\u0000\u0000\u00c8\u00cb\u0001\u0000\u0000\u0000"+
		"\u00c9\u00c7\u0001\u0000\u0000\u0000\u00c9\u00ca\u0001\u0000\u0000\u0000"+
		"\u00ca\u00cd\u0001\u0000\u0000\u0000\u00cb\u00c9\u0001\u0000\u0000\u0000"+
		"\u00cc\u00ce\u0005)\u0000\u0000\u00cd\u00cc\u0001\u0000\u0000\u0000\u00cd"+
		"\u00ce\u0001\u0000\u0000\u0000\u00ce\u001d\u0001\u0000\u0000\u0000\u00cf"+
		"\u00d0\u0006\u000f\uffff\uffff\u0000\u00d0\u00d4\u0003 \u0010\u0000\u00d1"+
		"\u00d2\u0005\u000f\u0000\u0000\u00d2\u00d4\u0003\u001e\u000f\u0003\u00d3"+
		"\u00cf\u0001\u0000\u0000\u0000\u00d3\u00d1\u0001\u0000\u0000\u0000\u00d4"+
		"\u00dd\u0001\u0000\u0000\u0000\u00d5\u00d6\n\u0002\u0000\u0000\u00d6\u00d7"+
		"\u0005\u0010\u0000\u0000\u00d7\u00dc\u0003\u001e\u000f\u0003\u00d8\u00d9"+
		"\n\u0001\u0000\u0000\u00d9\u00da\u0005\u000e\u0000\u0000\u00da\u00dc\u0003"+
		"\u001e\u000f\u0002\u00db\u00d5\u0001\u0000\u0000\u0000\u00db\u00d8\u0001"+
		"\u0000\u0000\u0000\u00dc\u00df\u0001\u0000\u0000\u0000\u00dd\u00db\u0001"+
		"\u0000\u0000\u0000\u00dd\u00de\u0001\u0000\u0000\u0000\u00de\u001f\u0001"+
		"\u0000\u0000\u0000\u00df\u00dd\u0001\u0000\u0000\u0000\u00e0\u00ef\u0003"+
		"\"\u0011\u0000\u00e1\u00f0\u00059\u0000\u0000\u00e2\u00f0\u0005:\u0000"+
		"\u0000\u00e3\u00f0\u0005;\u0000\u0000\u00e4\u00f0\u0005<\u0000\u0000\u00e5"+
		"\u00f0\u0005=\u0000\u0000\u00e6\u00f0\u0005>\u0000\u0000\u00e7\u00e9\u0005"+
		"\u000f\u0000\u0000\u00e8\u00e7\u0001\u0000\u0000\u0000\u00e8\u00e9\u0001"+
		"\u0000\u0000\u0000\u00e9\u00ea\u0001\u0000\u0000\u0000\u00ea\u00f0\u0005"+
		"\u0016\u0000\u0000\u00eb\u00ed\u0005\u0017\u0000\u0000\u00ec\u00ee\u0005"+
		"\u000f\u0000\u0000\u00ed\u00ec\u0001\u0000\u0000\u0000\u00ed\u00ee\u0001"+
		"\u0000\u0000\u0000\u00ee\u00f0\u0001\u0000\u0000\u0000\u00ef\u00e1\u0001"+
		"\u0000\u0000\u0000\u00ef\u00e2\u0001\u0000\u0000\u0000\u00ef\u00e3\u0001"+
		"\u0000\u0000\u0000\u00ef\u00e4\u0001\u0000\u0000\u0000\u00ef\u00e5\u0001"+
		"\u0000\u0000\u0000\u00ef\u00e6\u0001\u0000\u0000\u0000\u00ef\u00e8\u0001"+
		"\u0000\u0000\u0000\u00ef\u00eb\u0001\u0000\u0000\u0000\u00f0\u00f1\u0001"+
		"\u0000\u0000\u0000\u00f1\u00f2\u0003\"\u0011\u0000\u00f2\u00f5\u0001\u0000"+
		"\u0000\u0000\u00f3\u00f5\u0003\"\u0011\u0000\u00f4\u00e0\u0001\u0000\u0000"+
		"\u0000\u00f4\u00f3\u0001\u0000\u0000\u0000\u00f5!\u0001\u0000\u0000\u0000"+
		"\u00f6\u00f7\u0006\u0011\uffff\uffff\u0000\u00f7\u00fb\u0005I\u0000\u0000"+
		"\u00f8\u00fa\u0003&\u0013\u0000\u00f9\u00f8\u0001\u0000\u0000\u0000\u00fa"+
		"\u00fd\u0001\u0000\u0000\u0000\u00fb\u00f9\u0001\u0000\u0000\u0000\u00fb"+
		"\u00fc\u0001\u0000\u0000\u0000\u00fc\u0102\u0001\u0000\u0000\u0000\u00fd"+
		"\u00fb\u0001\u0000\u0000\u0000\u00fe\u0102\u0003$\u0012\u0000\u00ff\u0100"+
		"\u0007\u0002\u0000\u0000\u0100\u0102\u0003\"\u0011\u0007\u0101\u00f6\u0001"+
		"\u0000\u0000\u0000\u0101\u00fe\u0001\u0000\u0000\u0000\u0101\u00ff\u0001"+
		"\u0000\u0000\u0000\u0102\u011a\u0001\u0000\u0000\u0000\u0103\u0104\n\b"+
		"\u0000\u0000\u0104\u0105\u00052\u0000\u0000\u0105\u0119\u0003\"\u0011"+
		"\b\u0106\u0107\n\u0006\u0000\u0000\u0107\u0108\u0007\u0003\u0000\u0000"+
		"\u0108\u0119\u0003\"\u0011\u0007\u0109\u010a\n\u0005\u0000\u0000\u010a"+
		"\u010b\u0007\u0004\u0000\u0000\u010b\u0119\u0003\"\u0011\u0006\u010c\u010d"+
		"\n\u0004\u0000\u0000\u010d\u010e\u0007\u0005\u0000\u0000\u010e\u0119\u0003"+
		"\"\u0011\u0005\u010f\u0110\n\u0003\u0000\u0000\u0110\u0111\u0005/\u0000"+
		"\u0000\u0111\u0119\u0003\"\u0011\u0004\u0112\u0113\n\u0002\u0000\u0000"+
		"\u0113\u0114\u0005.\u0000\u0000\u0114\u0119\u0003\"\u0011\u0003\u0115"+
		"\u0116\n\u0001\u0000\u0000\u0116\u0117\u0005-\u0000\u0000\u0117\u0119"+
		"\u0003\"\u0011\u0002\u0118\u0103\u0001\u0000\u0000\u0000\u0118\u0106\u0001"+
		"\u0000\u0000\u0000\u0118\u0109\u0001\u0000\u0000\u0000\u0118\u010c\u0001"+
		"\u0000\u0000\u0000\u0118\u010f\u0001\u0000\u0000\u0000\u0118\u0112\u0001"+
		"\u0000\u0000\u0000\u0118\u0115\u0001\u0000\u0000\u0000\u0119\u011c\u0001"+
		"\u0000\u0000\u0000\u011a\u0118\u0001\u0000\u0000\u0000\u011a\u011b\u0001"+
		"\u0000\u0000\u0000\u011b#\u0001\u0000\u0000\u0000\u011c\u011a\u0001\u0000"+
		"\u0000\u0000\u011d\u011e\u0005C\u0000\u0000\u011e\u011f\u0003\"\u0011"+
		"\u0000\u011f\u0120\u0005D\u0000\u0000\u0120\u0139\u0001\u0000\u0000\u0000"+
		"\u0121\u0123\u0005G\u0000\u0000\u0122\u0124\u0003>\u001f\u0000\u0123\u0122"+
		"\u0001\u0000\u0000\u0000\u0123\u0124\u0001\u0000\u0000\u0000\u0124\u0125"+
		"\u0001\u0000\u0000\u0000\u0125\u0139\u0005H\u0000\u0000\u0126\u0128\u0005"+
		"E\u0000\u0000\u0127\u0129\u0003@ \u0000\u0128\u0127\u0001\u0000\u0000"+
		"\u0000\u0128\u0129\u0001\u0000\u0000\u0000\u0129\u012a\u0001\u0000\u0000"+
		"\u0000\u012a\u0139\u0005F\u0000\u0000\u012b\u0139\u0005I\u0000\u0000\u012c"+
		"\u0139\u0005\u0011\u0000\u0000\u012d\u0139\u0005\u0013\u0000\u0000\u012e"+
		"\u0130\u00054\u0000\u0000\u012f\u012e\u0001\u0000\u0000\u0000\u012f\u0130"+
		"\u0001\u0000\u0000\u0000\u0130\u0131\u0001\u0000\u0000\u0000\u0131\u0139"+
		"\u0005A\u0000\u0000\u0132\u0134\u00054\u0000\u0000\u0133\u0132\u0001\u0000"+
		"\u0000\u0000\u0133\u0134\u0001\u0000\u0000\u0000\u0134\u0135\u0001\u0000"+
		"\u0000\u0000\u0135\u0139\u0005B\u0000\u0000\u0136\u0139\u0005\u0019\u0000"+
		"\u0000\u0137\u0139\u0005@\u0000\u0000\u0138\u011d\u0001\u0000\u0000\u0000"+
		"\u0138\u0121\u0001\u0000\u0000\u0000\u0138\u0126\u0001\u0000\u0000\u0000"+
		"\u0138\u012b\u0001\u0000\u0000\u0000\u0138\u012c\u0001\u0000\u0000\u0000"+
		"\u0138\u012d\u0001\u0000\u0000\u0000\u0138\u012f\u0001\u0000\u0000\u0000"+
		"\u0138\u0133\u0001\u0000\u0000\u0000\u0138\u0136\u0001\u0000\u0000\u0000"+
		"\u0138\u0137\u0001\u0000\u0000\u0000\u0139%\u0001\u0000\u0000\u0000\u013a"+
		"\u013b\u0005\'\u0000\u0000\u013b\u013d\u0005I\u0000\u0000\u013c\u013e"+
		"\u0003(\u0014\u0000\u013d\u013c\u0001\u0000\u0000\u0000\u013d\u013e\u0001"+
		"\u0000\u0000\u0000\u013e\u0141\u0001\u0000\u0000\u0000\u013f\u0141\u0003"+
		"(\u0014\u0000\u0140\u013a\u0001\u0000\u0000\u0000\u0140\u013f\u0001\u0000"+
		"\u0000\u0000\u0141\'\u0001\u0000\u0000\u0000\u0142\u0144\u0005C\u0000"+
		"\u0000\u0143\u0145\u0003*\u0015\u0000\u0144\u0143\u0001\u0000\u0000\u0000"+
		"\u0144\u0145\u0001\u0000\u0000\u0000\u0145\u0146\u0001\u0000\u0000\u0000"+
		"\u0146\u014c\u0005D\u0000\u0000\u0147\u0148\u0005G\u0000\u0000\u0148\u0149"+
		"\u0003.\u0017\u0000\u0149\u014a\u0005H\u0000\u0000\u014a\u014c\u0001\u0000"+
		"\u0000\u0000\u014b\u0142\u0001\u0000\u0000\u0000\u014b\u0147\u0001\u0000"+
		"\u0000\u0000\u014c)\u0001\u0000\u0000\u0000\u014d\u0152\u0003,\u0016\u0000"+
		"\u014e\u014f\u0005)\u0000\u0000\u014f\u0151\u0003,\u0016\u0000\u0150\u014e"+
		"\u0001\u0000\u0000\u0000\u0151\u0154\u0001\u0000\u0000\u0000\u0152\u0150"+
		"\u0001\u0000\u0000\u0000\u0152\u0153\u0001\u0000\u0000\u0000\u0153\u0156"+
		"\u0001\u0000\u0000\u0000\u0154\u0152\u0001\u0000\u0000\u0000\u0155\u0157"+
		"\u0005)\u0000\u0000\u0156\u0155\u0001\u0000\u0000\u0000\u0156\u0157\u0001"+
		"\u0000\u0000\u0000\u0157+\u0001\u0000\u0000\u0000\u0158\u015b\u0003\u001e"+
		"\u000f\u0000\u0159\u015a\u0005,\u0000\u0000\u015a\u015c\u0003\u001e\u000f"+
		"\u0000\u015b\u0159\u0001\u0000\u0000\u0000\u015b\u015c\u0001\u0000\u0000"+
		"\u0000\u015c-\u0001\u0000\u0000\u0000\u015d\u0162\u0003\u001e\u000f\u0000"+
		"\u015e\u015f\u0005)\u0000\u0000\u015f\u0161\u0003\u001e\u000f\u0000\u0160"+
		"\u015e\u0001\u0000\u0000\u0000\u0161\u0164\u0001\u0000\u0000\u0000\u0162"+
		"\u0160\u0001\u0000\u0000\u0000\u0162\u0163\u0001\u0000\u0000\u0000\u0163"+
		"\u0166\u0001\u0000\u0000\u0000\u0164\u0162\u0001\u0000\u0000\u0000\u0165"+
		"\u0167\u0005)\u0000\u0000\u0166\u0165\u0001\u0000\u0000\u0000\u0166\u0167"+
		"\u0001\u0000\u0000\u0000\u0167/\u0001\u0000\u0000\u0000\u0168\u016d\u0005"+
		"I\u0000\u0000\u0169\u016a\u0005)\u0000\u0000\u016a\u016c\u0005I\u0000"+
		"\u0000\u016b\u0169\u0001\u0000\u0000\u0000\u016c\u016f\u0001\u0000\u0000"+
		"\u0000\u016d\u016b\u0001\u0000\u0000\u0000\u016d\u016e\u0001\u0000\u0000"+
		"\u0000\u016e1\u0001\u0000\u0000\u0000\u016f\u016d\u0001\u0000\u0000\u0000"+
		"\u0170\u0171\u0005\u0004\u0000\u0000\u0171\u0172\u0005I\u0000\u0000\u0172"+
		"\u0174\u0005C\u0000\u0000\u0173\u0175\u00034\u001a\u0000\u0174\u0173\u0001"+
		"\u0000\u0000\u0000\u0174\u0175\u0001\u0000\u0000\u0000\u0175\u0177\u0001"+
		"\u0000\u0000\u0000\u0176\u0178\u0005)\u0000\u0000\u0177\u0176\u0001\u0000"+
		"\u0000\u0000\u0177\u0178\u0001\u0000\u0000\u0000\u0178\u0179\u0001\u0000"+
		"\u0000\u0000\u0179\u017c\u0005D\u0000\u0000\u017a\u017b\u0005\u001a\u0000"+
		"\u0000\u017b\u017d\u0003\u001e\u000f\u0000\u017c\u017a\u0001\u0000\u0000"+
		"\u0000\u017c\u017d\u0001\u0000\u0000\u0000\u017d\u017e\u0001\u0000\u0000"+
		"\u0000\u017e\u017f\u0005*\u0000\u0000\u017f\u0180\u0003<\u001e\u0000\u0180"+
		"3\u0001\u0000\u0000\u0000\u0181\u0186\u00036\u001b\u0000\u0182\u0183\u0005"+
		")\u0000\u0000\u0183\u0185\u00036\u001b\u0000\u0184\u0182\u0001\u0000\u0000"+
		"\u0000\u0185\u0188\u0001\u0000\u0000\u0000\u0186\u0184\u0001\u0000\u0000"+
		"\u0000\u0186\u0187\u0001\u0000\u0000\u0000\u01875\u0001\u0000\u0000\u0000"+
		"\u0188\u0186\u0001\u0000\u0000\u0000\u0189\u018c\u0005I\u0000\u0000\u018a"+
		"\u018b\u0005*\u0000\u0000\u018b\u018d\u0003\u001e\u000f\u0000\u018c\u018a"+
		"\u0001\u0000\u0000\u0000\u018c\u018d\u0001\u0000\u0000\u0000\u018d\u0190"+
		"\u0001\u0000\u0000\u0000\u018e\u018f\u0005,\u0000\u0000\u018f\u0191\u0003"+
		"\u001e\u000f\u0000\u0190\u018e\u0001\u0000\u0000\u0000\u0190\u0191\u0001"+
		"\u0000\u0000\u0000\u01917\u0001\u0000\u0000\u0000\u0192\u0193\u0005\b"+
		"\u0000\u0000\u0193\u0194\u0003\u001e\u000f\u0000\u0194\u0195\u0005*\u0000"+
		"\u0000\u0195\u0196\u0003<\u001e\u0000\u01969\u0001\u0000\u0000\u0000\u0197"+
		"\u0198\u0005\t\u0000\u0000\u0198\u0199\u0005*\u0000\u0000\u0199\u019a"+
		"\u0003<\u001e\u0000\u019a;\u0001\u0000\u0000\u0000\u019b\u01a6\u0003\u0006"+
		"\u0003\u0000\u019c\u019d\u0005J\u0000\u0000\u019d\u019f\u0005\u0001\u0000"+
		"\u0000\u019e\u01a0\u0003\u0002\u0001\u0000\u019f\u019e\u0001\u0000\u0000"+
		"\u0000\u01a0\u01a1\u0001\u0000\u0000\u0000\u01a1\u019f\u0001\u0000\u0000"+
		"\u0000\u01a1\u01a2\u0001\u0000\u0000\u0000\u01a2\u01a3\u0001\u0000\u0000"+
		"\u0000\u01a3\u01a4\u0005\u0002\u0000\u0000\u01a4\u01a6\u0001\u0000\u0000"+
		"\u0000\u01a5\u019b\u0001\u0000\u0000\u0000\u01a5\u019c\u0001\u0000\u0000"+
		"\u0000\u01a6=\u0001\u0000\u0000\u0000\u01a7\u01ac\u0003\"\u0011\u0000"+
		"\u01a8\u01a9\u0005)\u0000\u0000\u01a9\u01ab\u0003\"\u0011\u0000\u01aa"+
		"\u01a8\u0001\u0000\u0000\u0000\u01ab\u01ae\u0001\u0000\u0000\u0000\u01ac"+
		"\u01aa\u0001\u0000\u0000\u0000\u01ac\u01ad\u0001\u0000\u0000\u0000\u01ad"+
		"?\u0001\u0000\u0000\u0000\u01ae\u01ac\u0001\u0000\u0000\u0000\u01af\u01b4"+
		"\u0003\"\u0011\u0000\u01b0\u01b1\u0005)\u0000\u0000\u01b1\u01b3\u0003"+
		"\"\u0011\u0000\u01b2\u01b0\u0001\u0000\u0000\u0000\u01b3\u01b6\u0001\u0000"+
		"\u0000\u0000\u01b4\u01b2\u0001\u0000\u0000\u0000\u01b4\u01b5\u0001\u0000"+
		"\u0000\u0000\u01b5\u01c5\u0001\u0000\u0000\u0000\u01b6\u01b4\u0001\u0000"+
		"\u0000\u0000\u01b7\u01b8\u0003\"\u0011\u0000\u01b8\u01b9\u0005*\u0000"+
		"\u0000\u01b9\u01c1\u0003\"\u0011\u0000\u01ba\u01bb\u0005)\u0000\u0000"+
		"\u01bb\u01bc\u0003\"\u0011\u0000\u01bc\u01bd\u0005*\u0000\u0000\u01bd"+
		"\u01be\u0003\"\u0011\u0000\u01be\u01c0\u0001\u0000\u0000\u0000\u01bf\u01ba"+
		"\u0001\u0000\u0000\u0000\u01c0\u01c3\u0001\u0000\u0000\u0000\u01c1\u01bf"+
		"\u0001\u0000\u0000\u0000\u01c1\u01c2\u0001\u0000\u0000\u0000\u01c2\u01c5"+
		"\u0001\u0000\u0000\u0000\u01c3\u01c1\u0001\u0000\u0000\u0000\u01c4\u01af"+
		"\u0001\u0000\u0000\u0000\u01c4\u01b7\u0001\u0000\u0000\u0000\u01c5A\u0001"+
		"\u0000\u0000\u0000\u01c6\u01c7\u0005?\u0000\u0000\u01c7\u01cd\u0003\u0018"+
		"\f\u0000\u01c8\u01ca\u0005C\u0000\u0000\u01c9\u01cb\u0003*\u0015\u0000"+
		"\u01ca\u01c9\u0001\u0000\u0000\u0000\u01ca\u01cb\u0001\u0000\u0000\u0000"+
		"\u01cb\u01cc\u0001\u0000\u0000\u0000\u01cc\u01ce\u0005D\u0000\u0000\u01cd"+
		"\u01c8\u0001\u0000\u0000\u0000\u01cd\u01ce\u0001\u0000\u0000\u0000\u01ce"+
		"\u01cf\u0001\u0000\u0000\u0000\u01cf\u01d0\u0005J\u0000\u0000\u01d0C\u0001"+
		"\u0000\u0000\u0000=FHOX\\clqu|\u0080\u008c\u009a\u00a7\u00aa\u00ac\u00b4"+
		"\u00bc\u00c2\u00c9\u00cd\u00d3\u00db\u00dd\u00e8\u00ed\u00ef\u00f4\u00fb"+
		"\u0101\u0118\u011a\u0123\u0128\u012f\u0133\u0138\u013d\u0140\u0144\u014b"+
		"\u0152\u0156\u015b\u0162\u0166\u016d\u0174\u0177\u017c\u0186\u018c\u0190"+
		"\u01a1\u01a5\u01ac\u01b4\u01c1\u01c4\u01ca\u01cd";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}