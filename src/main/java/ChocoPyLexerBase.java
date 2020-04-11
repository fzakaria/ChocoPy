/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 by Bart Kiers
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * Project      : python3-parser; an ANTLR4 grammar for Python 3
 *                https://github.com/bkiers/python3-parser
 * Developed by : Bart Kiers, bart@big-o.nl
 *
 *
 * Altered to allow the consumer to instruct the parse to throw
 *		more instructively on Python2-isms, or to not throw but note
 *		that they were encountered for later reference.
 * The Java code is fragile in that it depends, in places, on specific
 *		current implementation classes referenced directly (e.g CommonToken
 *		instead of Token.)
 *				- loki der quaeler June 2017
 */

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import java.util.Stack;

public abstract class ChocoPyLexerBase extends Lexer {
    public static int TabSize = 8;

    // The amount of opened braces, brackets and parenthesis.
    private int _opened;

    // The stack that keeps track of the indentation level.
    private Stack<Integer> _indents = new Stack<>();

    // A circular buffer where extra tokens are pushed on (see the NEWLINE and WS lexer rules).
    private int _firstTokensInd;
    private int _lastTokenInd;
    private Token[] _buffer = new Token[32];
    private Token _lastToken;

    protected ChocoPyLexerBase(CharStream input) {
        super(input);
    }

    @Override
    public void emit(Token token) {
        super.setToken(token);

        if (_buffer[_firstTokensInd] != null)
        {
            _lastTokenInd = IncTokenInd(_lastTokenInd);

            if (_lastTokenInd == _firstTokensInd)
            {
                // Enlarge buffer
                Token[] newArray = new Token[_buffer.length * 2];
                int destInd = newArray.length - (_buffer.length - _firstTokensInd);

                System.arraycopy(_buffer, 0, newArray, 0, _firstTokensInd);
                System.arraycopy(_buffer, _firstTokensInd, newArray, destInd, _buffer.length - _firstTokensInd);

                _firstTokensInd = destInd;
                _buffer = newArray;
            }
        }

        _buffer[_lastTokenInd] = token;
        _lastToken = token;
    }

    @Override
    public Token nextToken() {
        // Check if the end-of-file is ahead and there are still some DEDENTS expected.
        if (_input.LA(1) == EOF)
        {
            if (_buffer[_lastTokenInd] == null || _buffer[_lastTokenInd].getType() != ChocoPyLexer.LINE_BREAK)
            {
                // First emit an extra line break that serves as the end of the statement.
                emit(ChocoPyLexer.LINE_BREAK);
            }

            // Now emit as much DEDENT tokens as needed.
            while (_indents.size() != 0)
            {
                emit(ChocoPyLexer.DEDENT);
                _indents.pop();
            }
        }

        Token next = super.nextToken();

        if (_buffer[_firstTokensInd] == null)
        {
            return next;
        }

        Token result = _buffer[_firstTokensInd];
        _buffer[_firstTokensInd] = null;

        if (_firstTokensInd != _lastTokenInd)
        {
            _firstTokensInd = IncTokenInd(_firstTokensInd);
        }

        return result;
    }

    protected void HandleNewLine() {
        emit(ChocoPyLexer.NEWLINE, HIDDEN, getText());

        char next = (char) _input.LA(1);

        // Process whitespaces in HandleSpaces
        if (next != ' ' && next != '\t' && IsNotNewLineOrComment(next))
        {
            ProcessNewLine(0);
        }
    }

    protected void HandleSpaces() {
        char next = (char) _input.LA(1);

        if ((_lastToken == null || _lastToken.getType() == ChocoPyLexer.NEWLINE) && IsNotNewLineOrComment(next))
        {
            // Calculates the indentation of the provided spaces, taking the
            // following rules into account:
            //
            // "Tabs are replaced (from left to right) by one to eight spaces
            //  such that the total number of characters up to and including
            //  the replacement is a multiple of eight [...]"
            //
            //  -- https://docs.python.org/3.1/reference/lexical_analysis.html#indentation

            int indent = 0;
            String text = getText();

            for (int i = 0; i < text.length(); i++) {
                indent += text.charAt(i) == '\t' ? TabSize - indent % TabSize : 1;
            }

            ProcessNewLine(indent);
        }

        emit(ChocoPyLexer.WHITESPACE, HIDDEN, getText());
    }

    protected void IncIndentLevel() {
        _opened++;
    }

    protected void DecIndentLevel() {
        if (_opened > 0) {
            --_opened;
        }
    }

    private boolean IsNotNewLineOrComment(char next) {
        return _opened == 0 && next != '\r' && next != '\n' && next != '\f' && next != '#';
    }

    private void ProcessNewLine(int indent) {
        emit(ChocoPyLexer.LINE_BREAK);

        int previous = _indents.size() == 0 ? 0 : _indents.peek();

        if (indent > previous)
        {
            _indents.push(indent);
            emit(ChocoPyLexer.INDENT);
        }
        else
        {
            // Possibly emit more than 1 DEDENT token.
            while (_indents.size() != 0 && _indents.peek() > indent)
            {
                emit(ChocoPyLexer.DEDENT);
                _indents.pop();
            }
        }
    }

    private int IncTokenInd(int ind) {
        return (ind + 1) % _buffer.length;
    }

    private void emit(int tokenType) {
        emit(tokenType, DEFAULT_TOKEN_CHANNEL, "");
    }

    private void emit(int tokenType, int channel, String text) {
        int charIndex = getCharIndex();
        CommonToken token = new CommonToken(_tokenFactorySourcePair, tokenType, channel, charIndex - text.length(), charIndex);
        token.setLine(getLine());
        token.setCharPositionInLine(getCharPositionInLine());
        token.setText(text);

        emit(token);
    }
}
