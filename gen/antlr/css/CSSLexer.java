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
		WS=1, LBRACE=2, RBRACE=3, COLON=4, SEMI=5, CLASS_SELECTOR=6, ID_SELECTOR=7, 
		ELEMENT_SELECTOR=8, PROPERTY=9, VALUE=10;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "LBRACE", "RBRACE", "COLON", "SEMI", "CLASS_SELECTOR", "ID_SELECTOR", 
			"ELEMENT_SELECTOR", "PROPERTY", "VALUE", "IDENT", "NUMBER", "UNIT", "COLOR", 
			"STRING"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'{'", "'}'", "':'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "LBRACE", "RBRACE", "COLON", "SEMI", "CLASS_SELECTOR", "ID_SELECTOR", 
			"ELEMENT_SELECTOR", "PROPERTY", "VALUE"
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
		case 13:
			COLOR_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void COLOR_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			3,6
			break;
		}
	}

	public static final String _serializedATN =
		"\u0004\u0000\nu\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0001"+
		"\u0000\u0004\u0000!\b\u0000\u000b\u0000\f\u0000\"\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\t\u0003\t<\b\t\u0001\t\u0001\t\u0003\t@\b\t\u0001\n\u0001"+
		"\n\u0005\nD\b\n\n\n\f\nG\t\n\u0001\u000b\u0004\u000bJ\b\u000b\u000b\u000b"+
		"\f\u000bK\u0001\u000b\u0001\u000b\u0004\u000bP\b\u000b\u000b\u000b\f\u000b"+
		"Q\u0003\u000bT\b\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0003\f^\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0005\u000ef\b\u000e\n\u000e\f\u000ei\t\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0005\u000en\b\u000e\n\u000e\f\u000eq\t\u000e"+
		"\u0001\u000e\u0003\u000et\b\u000e\u0000\u0000\u000f\u0001\u0001\u0003"+
		"\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011"+
		"\t\u0013\n\u0015\u0000\u0017\u0000\u0019\u0000\u001b\u0000\u001d\u0000"+
		"\u0001\u0000\u0007\u0003\u0000\t\n\r\r  \u0003\u0000AZ__az\u0005\u0000"+
		"--09AZ__az\u0001\u000009\u0003\u000009AFaf\u0003\u0000\n\n\r\r\"\"\u0003"+
		"\u0000\n\n\r\r\'\'~\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001"+
		"\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001"+
		"\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000"+
		"\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000"+
		"\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000"+
		"\u0000\u0001 \u0001\u0000\u0000\u0000\u0003&\u0001\u0000\u0000\u0000\u0005"+
		"(\u0001\u0000\u0000\u0000\u0007*\u0001\u0000\u0000\u0000\t,\u0001\u0000"+
		"\u0000\u0000\u000b.\u0001\u0000\u0000\u0000\r1\u0001\u0000\u0000\u0000"+
		"\u000f4\u0001\u0000\u0000\u0000\u00116\u0001\u0000\u0000\u0000\u0013?"+
		"\u0001\u0000\u0000\u0000\u0015A\u0001\u0000\u0000\u0000\u0017I\u0001\u0000"+
		"\u0000\u0000\u0019]\u0001\u0000\u0000\u0000\u001b_\u0001\u0000\u0000\u0000"+
		"\u001ds\u0001\u0000\u0000\u0000\u001f!\u0007\u0000\u0000\u0000 \u001f"+
		"\u0001\u0000\u0000\u0000!\"\u0001\u0000\u0000\u0000\" \u0001\u0000\u0000"+
		"\u0000\"#\u0001\u0000\u0000\u0000#$\u0001\u0000\u0000\u0000$%\u0006\u0000"+
		"\u0000\u0000%\u0002\u0001\u0000\u0000\u0000&\'\u0005{\u0000\u0000\'\u0004"+
		"\u0001\u0000\u0000\u0000()\u0005}\u0000\u0000)\u0006\u0001\u0000\u0000"+
		"\u0000*+\u0005:\u0000\u0000+\b\u0001\u0000\u0000\u0000,-\u0005;\u0000"+
		"\u0000-\n\u0001\u0000\u0000\u0000./\u0005.\u0000\u0000/0\u0003\u0015\n"+
		"\u00000\f\u0001\u0000\u0000\u000012\u0005#\u0000\u000023\u0003\u0015\n"+
		"\u00003\u000e\u0001\u0000\u0000\u000045\u0003\u0015\n\u00005\u0010\u0001"+
		"\u0000\u0000\u000067\u0003\u0015\n\u00007\u0012\u0001\u0000\u0000\u0000"+
		"8@\u0003\u0015\n\u00009;\u0003\u0017\u000b\u0000:<\u0003\u0019\f\u0000"+
		";:\u0001\u0000\u0000\u0000;<\u0001\u0000\u0000\u0000<@\u0001\u0000\u0000"+
		"\u0000=@\u0003\u001d\u000e\u0000>@\u0003\u001b\r\u0000?8\u0001\u0000\u0000"+
		"\u0000?9\u0001\u0000\u0000\u0000?=\u0001\u0000\u0000\u0000?>\u0001\u0000"+
		"\u0000\u0000@\u0014\u0001\u0000\u0000\u0000AE\u0007\u0001\u0000\u0000"+
		"BD\u0007\u0002\u0000\u0000CB\u0001\u0000\u0000\u0000DG\u0001\u0000\u0000"+
		"\u0000EC\u0001\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000F\u0016\u0001"+
		"\u0000\u0000\u0000GE\u0001\u0000\u0000\u0000HJ\u0007\u0003\u0000\u0000"+
		"IH\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000KI\u0001\u0000\u0000"+
		"\u0000KL\u0001\u0000\u0000\u0000LS\u0001\u0000\u0000\u0000MO\u0005.\u0000"+
		"\u0000NP\u0007\u0003\u0000\u0000ON\u0001\u0000\u0000\u0000PQ\u0001\u0000"+
		"\u0000\u0000QO\u0001\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000RT\u0001"+
		"\u0000\u0000\u0000SM\u0001\u0000\u0000\u0000ST\u0001\u0000\u0000\u0000"+
		"T\u0018\u0001\u0000\u0000\u0000UV\u0005p\u0000\u0000V^\u0005x\u0000\u0000"+
		"WX\u0005e\u0000\u0000X^\u0005m\u0000\u0000YZ\u0005r\u0000\u0000Z[\u0005"+
		"e\u0000\u0000[^\u0005m\u0000\u0000\\^\u0005%\u0000\u0000]U\u0001\u0000"+
		"\u0000\u0000]W\u0001\u0000\u0000\u0000]Y\u0001\u0000\u0000\u0000]\\\u0001"+
		"\u0000\u0000\u0000^\u001a\u0001\u0000\u0000\u0000_`\u0005#\u0000\u0000"+
		"`a\u0007\u0004\u0000\u0000ab\u0006\r\u0001\u0000b\u001c\u0001\u0000\u0000"+
		"\u0000cg\u0005\"\u0000\u0000df\b\u0005\u0000\u0000ed\u0001\u0000\u0000"+
		"\u0000fi\u0001\u0000\u0000\u0000ge\u0001\u0000\u0000\u0000gh\u0001\u0000"+
		"\u0000\u0000hj\u0001\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000jt\u0005"+
		"\"\u0000\u0000ko\u0005\'\u0000\u0000ln\b\u0006\u0000\u0000ml\u0001\u0000"+
		"\u0000\u0000nq\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000op\u0001"+
		"\u0000\u0000\u0000pr\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000"+
		"rt\u0005\'\u0000\u0000sc\u0001\u0000\u0000\u0000sk\u0001\u0000\u0000\u0000"+
		"t\u001e\u0001\u0000\u0000\u0000\f\u0000\";?EKQS]gos\u0002\u0006\u0000"+
		"\u0000\u0001\r\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}