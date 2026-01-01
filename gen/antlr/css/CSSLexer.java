// Generated from C:/Users/youus/IdeaProjects/flask-compiler/src/antlr/css/CSSLexer.g4 by ANTLR 4.13.2
package antlr.css;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class CSSLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LBRACE=1, RBRACE=2, LBRACK=3, RBRACK=4, LPAREN=5, RPAREN=6, COLON=7, SEMI=8, 
		COMMA=9, DOT=10, HASH=11, STAR=12, GT=13, PLUS=14, TILDE=15, EQUAL=16, 
		PREFIX=17, SUFFIX=18, SUBSTR=19, STRING=20, NUMBER=21, PERCENT=22, DIMENSION=23, 
		HEX=24, IDENT=25, WS=26, IMPORTANT=27;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LBRACE", "RBRACE", "LBRACK", "RBRACK", "LPAREN", "RPAREN", "COLON", 
			"SEMI", "COMMA", "DOT", "HASH", "STAR", "GT", "PLUS", "TILDE", "EQUAL", 
			"PREFIX", "SUFFIX", "SUBSTR", "STRING", "NUMBER", "PERCENT", "DIMENSION", 
			"HEX", "IDENT", "WS", "IMPORTANT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "'['", "']'", "'('", "')'", "':'", "';'", "','", 
			"'.'", "'#'", "'*'", "'>'", "'+'", "'~'", "'='", "'^='", "'$='", "'*='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LBRACE", "RBRACE", "LBRACK", "RBRACK", "LPAREN", "RPAREN", "COLON", 
			"SEMI", "COMMA", "DOT", "HASH", "STAR", "GT", "PLUS", "TILDE", "EQUAL", 
			"PREFIX", "SUFFIX", "SUBSTR", "STRING", "NUMBER", "PERCENT", "DIMENSION", 
			"HEX", "IDENT", "WS", "IMPORTANT"
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


	public CSSLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CSSLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 23:
			HEX_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void HEX_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			3,6
			break;
		}
	}

	public static final String _serializedATN =
		"\u0004\u0000\u001b\u00aa\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0005\u0013e\b\u0013\n\u0013\f\u0013h\t\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0005\u0013o\b\u0013\n\u0013\f\u0013"+
		"r\t\u0013\u0001\u0013\u0003\u0013u\b\u0013\u0001\u0014\u0004\u0014x\b"+
		"\u0014\u000b\u0014\f\u0014y\u0001\u0014\u0001\u0014\u0004\u0014~\b\u0014"+
		"\u000b\u0014\f\u0014\u007f\u0003\u0014\u0082\b\u0014\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0005\u0018\u0090"+
		"\b\u0018\n\u0018\f\u0018\u0093\t\u0018\u0001\u0019\u0004\u0019\u0096\b"+
		"\u0019\u000b\u0019\f\u0019\u0097\u0001\u001a\u0001\u001a\u0005\u001a\u009c"+
		"\b\u001a\n\u001a\f\u001a\u009f\t\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0000\u0000\u001b\u0001\u0001\u0003\u0002\u0005\u0003\u0007"+
		"\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b"+
		"\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013"+
		"\'\u0014)\u0015+\u0016-\u0017/\u00181\u00193\u001a5\u001b\u0001\u0000"+
		"\u0007\u0004\u0000\n\n\r\r\"\"\\\\\u0004\u0000\n\n\r\r\'\'\\\\\u0001\u0000"+
		"09\u0003\u000009AFaf\u0003\u0000AZ__az\u0005\u0000--09AZ__az\u0003\u0000"+
		"\t\n\r\r  \u00b4\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001"+
		"\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001"+
		"\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000"+
		"\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000"+
		"\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000"+
		"\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000"+
		"\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000"+
		"\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000"+
		"\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000"+
		"%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001"+
		"\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-\u0001\u0000\u0000"+
		"\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000\u0000\u0000\u0000"+
		"3\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000\u00017\u0001"+
		"\u0000\u0000\u0000\u00039\u0001\u0000\u0000\u0000\u0005;\u0001\u0000\u0000"+
		"\u0000\u0007=\u0001\u0000\u0000\u0000\t?\u0001\u0000\u0000\u0000\u000b"+
		"A\u0001\u0000\u0000\u0000\rC\u0001\u0000\u0000\u0000\u000fE\u0001\u0000"+
		"\u0000\u0000\u0011G\u0001\u0000\u0000\u0000\u0013I\u0001\u0000\u0000\u0000"+
		"\u0015K\u0001\u0000\u0000\u0000\u0017M\u0001\u0000\u0000\u0000\u0019O"+
		"\u0001\u0000\u0000\u0000\u001bQ\u0001\u0000\u0000\u0000\u001dS\u0001\u0000"+
		"\u0000\u0000\u001fU\u0001\u0000\u0000\u0000!W\u0001\u0000\u0000\u0000"+
		"#Z\u0001\u0000\u0000\u0000%]\u0001\u0000\u0000\u0000\'t\u0001\u0000\u0000"+
		"\u0000)w\u0001\u0000\u0000\u0000+\u0083\u0001\u0000\u0000\u0000-\u0086"+
		"\u0001\u0000\u0000\u0000/\u0089\u0001\u0000\u0000\u00001\u008d\u0001\u0000"+
		"\u0000\u00003\u0095\u0001\u0000\u0000\u00005\u0099\u0001\u0000\u0000\u0000"+
		"78\u0005{\u0000\u00008\u0002\u0001\u0000\u0000\u00009:\u0005}\u0000\u0000"+
		":\u0004\u0001\u0000\u0000\u0000;<\u0005[\u0000\u0000<\u0006\u0001\u0000"+
		"\u0000\u0000=>\u0005]\u0000\u0000>\b\u0001\u0000\u0000\u0000?@\u0005("+
		"\u0000\u0000@\n\u0001\u0000\u0000\u0000AB\u0005)\u0000\u0000B\f\u0001"+
		"\u0000\u0000\u0000CD\u0005:\u0000\u0000D\u000e\u0001\u0000\u0000\u0000"+
		"EF\u0005;\u0000\u0000F\u0010\u0001\u0000\u0000\u0000GH\u0005,\u0000\u0000"+
		"H\u0012\u0001\u0000\u0000\u0000IJ\u0005.\u0000\u0000J\u0014\u0001\u0000"+
		"\u0000\u0000KL\u0005#\u0000\u0000L\u0016\u0001\u0000\u0000\u0000MN\u0005"+
		"*\u0000\u0000N\u0018\u0001\u0000\u0000\u0000OP\u0005>\u0000\u0000P\u001a"+
		"\u0001\u0000\u0000\u0000QR\u0005+\u0000\u0000R\u001c\u0001\u0000\u0000"+
		"\u0000ST\u0005~\u0000\u0000T\u001e\u0001\u0000\u0000\u0000UV\u0005=\u0000"+
		"\u0000V \u0001\u0000\u0000\u0000WX\u0005^\u0000\u0000XY\u0005=\u0000\u0000"+
		"Y\"\u0001\u0000\u0000\u0000Z[\u0005$\u0000\u0000[\\\u0005=\u0000\u0000"+
		"\\$\u0001\u0000\u0000\u0000]^\u0005*\u0000\u0000^_\u0005=\u0000\u0000"+
		"_&\u0001\u0000\u0000\u0000`f\u0005\"\u0000\u0000ae\b\u0000\u0000\u0000"+
		"bc\u0005\\\u0000\u0000ce\t\u0000\u0000\u0000da\u0001\u0000\u0000\u0000"+
		"db\u0001\u0000\u0000\u0000eh\u0001\u0000\u0000\u0000fd\u0001\u0000\u0000"+
		"\u0000fg\u0001\u0000\u0000\u0000gi\u0001\u0000\u0000\u0000hf\u0001\u0000"+
		"\u0000\u0000iu\u0005\"\u0000\u0000jp\u0005\'\u0000\u0000ko\b\u0001\u0000"+
		"\u0000lm\u0005\\\u0000\u0000mo\t\u0000\u0000\u0000nk\u0001\u0000\u0000"+
		"\u0000nl\u0001\u0000\u0000\u0000or\u0001\u0000\u0000\u0000pn\u0001\u0000"+
		"\u0000\u0000pq\u0001\u0000\u0000\u0000qs\u0001\u0000\u0000\u0000rp\u0001"+
		"\u0000\u0000\u0000su\u0005\'\u0000\u0000t`\u0001\u0000\u0000\u0000tj\u0001"+
		"\u0000\u0000\u0000u(\u0001\u0000\u0000\u0000vx\u0007\u0002\u0000\u0000"+
		"wv\u0001\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000yw\u0001\u0000\u0000"+
		"\u0000yz\u0001\u0000\u0000\u0000z\u0081\u0001\u0000\u0000\u0000{}\u0005"+
		".\u0000\u0000|~\u0007\u0002\u0000\u0000}|\u0001\u0000\u0000\u0000~\u007f"+
		"\u0001\u0000\u0000\u0000\u007f}\u0001\u0000\u0000\u0000\u007f\u0080\u0001"+
		"\u0000\u0000\u0000\u0080\u0082\u0001\u0000\u0000\u0000\u0081{\u0001\u0000"+
		"\u0000\u0000\u0081\u0082\u0001\u0000\u0000\u0000\u0082*\u0001\u0000\u0000"+
		"\u0000\u0083\u0084\u0003)\u0014\u0000\u0084\u0085\u0005%\u0000\u0000\u0085"+
		",\u0001\u0000\u0000\u0000\u0086\u0087\u0003)\u0014\u0000\u0087\u0088\u0003"+
		"1\u0018\u0000\u0088.\u0001\u0000\u0000\u0000\u0089\u008a\u0005#\u0000"+
		"\u0000\u008a\u008b\u0007\u0003\u0000\u0000\u008b\u008c\u0006\u0017\u0000"+
		"\u0000\u008c0\u0001\u0000\u0000\u0000\u008d\u0091\u0007\u0004\u0000\u0000"+
		"\u008e\u0090\u0007\u0005\u0000\u0000\u008f\u008e\u0001\u0000\u0000\u0000"+
		"\u0090\u0093\u0001\u0000\u0000\u0000\u0091\u008f\u0001\u0000\u0000\u0000"+
		"\u0091\u0092\u0001\u0000\u0000\u0000\u00922\u0001\u0000\u0000\u0000\u0093"+
		"\u0091\u0001\u0000\u0000\u0000\u0094\u0096\u0007\u0006\u0000\u0000\u0095"+
		"\u0094\u0001\u0000\u0000\u0000\u0096\u0097\u0001\u0000\u0000\u0000\u0097"+
		"\u0095\u0001\u0000\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098"+
		"4\u0001\u0000\u0000\u0000\u0099\u009d\u0005!\u0000\u0000\u009a\u009c\u0003"+
		"3\u0019\u0000\u009b\u009a\u0001\u0000\u0000\u0000\u009c\u009f\u0001\u0000"+
		"\u0000\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000"+
		"\u0000\u0000\u009e\u00a0\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000"+
		"\u0000\u0000\u00a0\u00a1\u0005i\u0000\u0000\u00a1\u00a2\u0005m\u0000\u0000"+
		"\u00a2\u00a3\u0005p\u0000\u0000\u00a3\u00a4\u0005o\u0000\u0000\u00a4\u00a5"+
		"\u0005r\u0000\u0000\u00a5\u00a6\u0005t\u0000\u0000\u00a6\u00a7\u0005a"+
		"\u0000\u0000\u00a7\u00a8\u0005n\u0000\u0000\u00a8\u00a9\u0005t\u0000\u0000"+
		"\u00a96\u0001\u0000\u0000\u0000\f\u0000dfnpty\u007f\u0081\u0091\u0097"+
		"\u009d\u0001\u0001\u0017\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}