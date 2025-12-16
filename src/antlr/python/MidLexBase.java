package antlr.python;
import antlr.python.pyparser;
import org.antlr.v4.runtime.*;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
public abstract class MidLexBase extends Lexer {
    private Queue<Token> tokens = new LinkedList<>();
    private Stack<Integer> indents = new Stack<>();
    private int opened = 0;
    private Token lastToken = null;

    public MidLexBase(CharStream input) {
        super(input);
    }

    @Override
    public void emit(Token t) {
        super.setToken(t);
        tokens.offer(t);
    }

    @Override
    public Token nextToken() {
        // إذا وصلنا لنهاية الملف وما زال هناك مستويات indentation
        if (_input.LA(1) == EOF && !indents.isEmpty()) {
            // أزل أي EOF سابق من الـ buffer
            for (int i = tokens.size() - 1; i >= 0; i--) {
                if (tokens instanceof LinkedList && ((LinkedList<Token>) tokens).get(i).getType() == EOF) {
                    ((LinkedList<Token>) tokens).remove(i);
                }
            }

            // أرسل NEWLINE إضافي
            this.emit(commonToken(pyparser.NEWLINE, "\n"));

            // أرسل كل الـ DEDENT اللازمة
            while (!indents.isEmpty()) {
                this.emit(createDedent());
                indents.pop();
            }

            // أرسل EOF مرة أخرى
            this.emit(commonToken(pyparser.EOF, "<EOF>"));
        }

        Token next = super.nextToken();

        if (next.getChannel() == Token.DEFAULT_CHANNEL) {
            lastToken = next;
        }

        return tokens.isEmpty() ? next : tokens.poll();
    }

    private Token createDedent() {
        CommonToken dedent = commonToken(pyparser.DEDENT, "");
        dedent.setLine(lastToken.getLine());
        return dedent;
    }

    private CommonToken commonToken(int type, String text) {
        int stop = this.getCharIndex() - 1;
        int start = text.isEmpty() ? stop : stop - text.length() + 1;
        return new CommonToken(this._tokenFactorySourcePair, type, DEFAULT_TOKEN_CHANNEL, start, stop);
    }

    static int getIndentationCount(String spaces) {
        int count = 0;
        for (char ch : spaces.toCharArray()) {
            switch (ch) {
                case '\t':
                    count += 8 - (count % 8);
                    break;
                default:
                    count++;
            }
        }
        return count;
    }

    public void openBrace() { opened++; }
    public void closeBrace() { opened--; }

    public void onNewLine() {
        String newLine = getText().replaceAll("[^\r\n\f]+", "");
        String spaces = getText().replaceAll("[\r\n\f]+", "");

        int next = _input.LA(1);
        int nextnext = _input.LA(2);

        if (opened > 0 || (nextnext != -1 && (next == '\r' || next == '\n' || next == '\f' || next == '#'))) {
            skip();
        } else {
            emit(commonToken(pyparser.NEWLINE, newLine));

            int indent = getIndentationCount(spaces);
            int previous = indents.isEmpty() ? 0 : indents.peek();

            if (indent == previous) {
                skip();
            } else if (indent > previous) {
                indents.push(indent);
                emit(commonToken(pyparser.INDENT, spaces));
            } else {
                while (!indents.isEmpty() && indents.peek() > indent) {
                    emit(createDedent());
                    indents.pop();
                }
            }
        }
    }

    @Override
    public void reset() {
        tokens = new LinkedList<>();
        indents = new Stack<>();
        opened = 0;
        lastToken = null;
        super.reset();
    }
}
