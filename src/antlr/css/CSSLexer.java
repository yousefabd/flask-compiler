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
		PREFIX=17, SUFFIX=18, SUBSTR=19, DOUBLEDASH=20, STRING=21, NUMBER=22, 
		PERCENT=23, DIMENSION=24, HEX=25, IDENT=26, WS=27, IMPORTANT=28;
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
			"PREFIX", "SUFFIX", "SUBSTR", "DOUBLEDASH", "STRING", "NUMBER", "PERCENT", 
			"DIMENSION", "HEX", "IDENT", "WS", "IMPORTANT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "'['", "']'", "'('", "')'", "':'", "';'", "','", 
			"'.'", "'#'", "'*'", "'>'", "'+'", "'~'", "'='", "'^='", "'$='", "'*='", 
			"'--'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LBRACE", "RBRACE", "LBRACK", "RBRACK", "LPAREN", "RPAREN", "COLON", 
			"SEMI", "COMMA", "DOT", "HASH", "STAR", "GT", "PLUS", "TILDE", "EQUAL", 
			"PREFIX", "SUFFIX", "SUBSTR", "DOUBLEDASH", "STRING", "NUMBER", "PERCENT", 
			"DIMENSION", "HEX", "IDENT", "WS", "IMPORTANT"
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
		case 24:
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
		"\u0004\u0000\u001c\u00af\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"+
		"\u0002\u001b\u0007\u001b\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001"+
		"\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014j\b"+
		"\u0014\n\u0014\f\u0014m\t\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0005\u0014t\b\u0014\n\u0014\f\u0014w\t\u0014\u0001"+
		"\u0014\u0003\u0014z\b\u0014\u0001\u0015\u0004\u0015}\b\u0015\u000b\u0015"+
		"\f\u0015~\u0001\u0015\u0001\u0015\u0004\u0015\u0083\b\u0015\u000b\u0015"+
		"\f\u0015\u0084\u0003\u0015\u0087\b\u0015\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0005\u0019\u0095\b\u0019\n"+
		"\u0019\f\u0019\u0098\t\u0019\u0001\u001a\u0004\u001a\u009b\b\u001a\u000b"+
		"\u001a\f\u001a\u009c\u0001\u001b\u0001\u001b\u0005\u001b\u00a1\b\u001b"+
		"\n\u001b\f\u001b\u00a4\t\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0000\u0000\u001c\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004"+
		"\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017"+
		"\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'"+
		"\u0014)\u0015+\u0016-\u0017/\u00181\u00193\u001a5\u001b7\u001c\u0001\u0000"+
		"\u0007\u0004\u0000\n\n\r\r\"\"\\\\\u0004\u0000\n\n\r\r\'\'\\\\\u0001\u0000"+
		"09\u0003\u000009AFaf\u0003\u0000AZ__az\u0005\u0000--09AZ__az\u0003\u0000"+
		"\t\n\r\r  \u00b9\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001"+
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
		"3\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000\u00007\u0001"+
		"\u0000\u0000\u0000\u00019\u0001\u0000\u0000\u0000\u0003;\u0001\u0000\u0000"+
		"\u0000\u0005=\u0001\u0000\u0000\u0000\u0007?\u0001\u0000\u0000\u0000\t"+
		"A\u0001\u0000\u0000\u0000\u000bC\u0001\u0000\u0000\u0000\rE\u0001\u0000"+
		"\u0000\u0000\u000fG\u0001\u0000\u0000\u0000\u0011I\u0001\u0000\u0000\u0000"+
		"\u0013K\u0001\u0000\u0000\u0000\u0015M\u0001\u0000\u0000\u0000\u0017O"+
		"\u0001\u0000\u0000\u0000\u0019Q\u0001\u0000\u0000\u0000\u001bS\u0001\u0000"+
		"\u0000\u0000\u001dU\u0001\u0000\u0000\u0000\u001fW\u0001\u0000\u0000\u0000"+
		"!Y\u0001\u0000\u0000\u0000#\\\u0001\u0000\u0000\u0000%_\u0001\u0000\u0000"+
		"\u0000\'b\u0001\u0000\u0000\u0000)y\u0001\u0000\u0000\u0000+|\u0001\u0000"+
		"\u0000\u0000-\u0088\u0001\u0000\u0000\u0000/\u008b\u0001\u0000\u0000\u0000"+
		"1\u008e\u0001\u0000\u0000\u00003\u0092\u0001\u0000\u0000\u00005\u009a"+
		"\u0001\u0000\u0000\u00007\u009e\u0001\u0000\u0000\u00009:\u0005{\u0000"+
		"\u0000:\u0002\u0001\u0000\u0000\u0000;<\u0005}\u0000\u0000<\u0004\u0001"+
		"\u0000\u0000\u0000=>\u0005[\u0000\u0000>\u0006\u0001\u0000\u0000\u0000"+
		"?@\u0005]\u0000\u0000@\b\u0001\u0000\u0000\u0000AB\u0005(\u0000\u0000"+
		"B\n\u0001\u0000\u0000\u0000CD\u0005)\u0000\u0000D\f\u0001\u0000\u0000"+
		"\u0000EF\u0005:\u0000\u0000F\u000e\u0001\u0000\u0000\u0000GH\u0005;\u0000"+
		"\u0000H\u0010\u0001\u0000\u0000\u0000IJ\u0005,\u0000\u0000J\u0012\u0001"+
		"\u0000\u0000\u0000KL\u0005.\u0000\u0000L\u0014\u0001\u0000\u0000\u0000"+
		"MN\u0005#\u0000\u0000N\u0016\u0001\u0000\u0000\u0000OP\u0005*\u0000\u0000"+
		"P\u0018\u0001\u0000\u0000\u0000QR\u0005>\u0000\u0000R\u001a\u0001\u0000"+
		"\u0000\u0000ST\u0005+\u0000\u0000T\u001c\u0001\u0000\u0000\u0000UV\u0005"+
		"~\u0000\u0000V\u001e\u0001\u0000\u0000\u0000WX\u0005=\u0000\u0000X \u0001"+
		"\u0000\u0000\u0000YZ\u0005^\u0000\u0000Z[\u0005=\u0000\u0000[\"\u0001"+
		"\u0000\u0000\u0000\\]\u0005$\u0000\u0000]^\u0005=\u0000\u0000^$\u0001"+
		"\u0000\u0000\u0000_`\u0005*\u0000\u0000`a\u0005=\u0000\u0000a&\u0001\u0000"+
		"\u0000\u0000bc\u0005-\u0000\u0000cd\u0005-\u0000\u0000d(\u0001\u0000\u0000"+
		"\u0000ek\u0005\"\u0000\u0000fj\b\u0000\u0000\u0000gh\u0005\\\u0000\u0000"+
		"hj\t\u0000\u0000\u0000if\u0001\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000"+
		"jm\u0001\u0000\u0000\u0000ki\u0001\u0000\u0000\u0000kl\u0001\u0000\u0000"+
		"\u0000ln\u0001\u0000\u0000\u0000mk\u0001\u0000\u0000\u0000nz\u0005\"\u0000"+
		"\u0000ou\u0005\'\u0000\u0000pt\b\u0001\u0000\u0000qr\u0005\\\u0000\u0000"+
		"rt\t\u0000\u0000\u0000sp\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000"+
		"tw\u0001\u0000\u0000\u0000us\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000"+
		"\u0000vx\u0001\u0000\u0000\u0000wu\u0001\u0000\u0000\u0000xz\u0005\'\u0000"+
		"\u0000ye\u0001\u0000\u0000\u0000yo\u0001\u0000\u0000\u0000z*\u0001\u0000"+
		"\u0000\u0000{}\u0007\u0002\u0000\u0000|{\u0001\u0000\u0000\u0000}~\u0001"+
		"\u0000\u0000\u0000~|\u0001\u0000\u0000\u0000~\u007f\u0001\u0000\u0000"+
		"\u0000\u007f\u0086\u0001\u0000\u0000\u0000\u0080\u0082\u0005.\u0000\u0000"+
		"\u0081\u0083\u0007\u0002\u0000\u0000\u0082\u0081\u0001\u0000\u0000\u0000"+
		"\u0083\u0084\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000\u0000\u0000"+
		"\u0084\u0085\u0001\u0000\u0000\u0000\u0085\u0087\u0001\u0000\u0000\u0000"+
		"\u0086\u0080\u0001\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000"+
		"\u0087,\u0001\u0000\u0000\u0000\u0088\u0089\u0003+\u0015\u0000\u0089\u008a"+
		"\u0005%\u0000\u0000\u008a.\u0001\u0000\u0000\u0000\u008b\u008c\u0003+"+
		"\u0015\u0000\u008c\u008d\u00033\u0019\u0000\u008d0\u0001\u0000\u0000\u0000"+
		"\u008e\u008f\u0005#\u0000\u0000\u008f\u0090\u0007\u0003\u0000\u0000\u0090"+
		"\u0091\u0006\u0018\u0000\u0000\u00912\u0001\u0000\u0000\u0000\u0092\u0096"+
		"\u0007\u0004\u0000\u0000\u0093\u0095\u0007\u0005\u0000\u0000\u0094\u0093"+
		"\u0001\u0000\u0000\u0000\u0095\u0098\u0001\u0000\u0000\u0000\u0096\u0094"+
		"\u0001\u0000\u0000\u0000\u0096\u0097\u0001\u0000\u0000\u0000\u00974\u0001"+
		"\u0000\u0000\u0000\u0098\u0096\u0001\u0000\u0000\u0000\u0099\u009b\u0007"+
		"\u0006\u0000\u0000\u009a\u0099\u0001\u0000\u0000\u0000\u009b\u009c\u0001"+
		"\u0000\u0000\u0000\u009c\u009a\u0001\u0000\u0000\u0000\u009c\u009d\u0001"+
		"\u0000\u0000\u0000\u009d6\u0001\u0000\u0000\u0000\u009e\u00a2\u0005!\u0000"+
		"\u0000\u009f\u00a1\u00035\u001a\u0000\u00a0\u009f\u0001\u0000\u0000\u0000"+
		"\u00a1\u00a4\u0001\u0000\u0000\u0000\u00a2\u00a0\u0001\u0000\u0000\u0000"+
		"\u00a2\u00a3\u0001\u0000\u0000\u0000\u00a3\u00a5\u0001\u0000\u0000\u0000"+
		"\u00a4\u00a2\u0001\u0000\u0000\u0000\u00a5\u00a6\u0005i\u0000\u0000\u00a6"+
		"\u00a7\u0005m\u0000\u0000\u00a7\u00a8\u0005p\u0000\u0000\u00a8\u00a9\u0005"+
		"o\u0000\u0000\u00a9\u00aa\u0005r\u0000\u0000\u00aa\u00ab\u0005t\u0000"+
		"\u0000\u00ab\u00ac\u0005a\u0000\u0000\u00ac\u00ad\u0005n\u0000\u0000\u00ad"+
		"\u00ae\u0005t\u0000\u0000\u00ae8\u0001\u0000\u0000\u0000\f\u0000iksuy"+
		"~\u0084\u0086\u0096\u009c\u00a2\u0001\u0001\u0018\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}