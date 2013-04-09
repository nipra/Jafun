/*
 * Copyright (c) 2004 David Flanagan.  All rights reserved.
 * This code is from the book Java Examples in a Nutshell, 3nd Edition.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose,
 * including teaching and use in open-source projects.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, or to purchase the book, 
 * please visit http://www.davidflanagan.com/javaexamples3.
 */
package je3.classes;
import java.util.*;
import java.io.IOException;

/**
 * This class implements all the methods of the Tokenizer interface, and
 * defines two new abstract methods, {@link #createBuffer} and
 * {@link #fillBuffer} which all concrete subclasses must implement.
 * By default, instances of this class can handle tokens of up to 16*1024
 * characters in length.
 * @author David Flanagan
 */
public abstract class AbstractTokenizer implements Tokenizer {
    boolean skipSpaces;
    boolean tokenizeSpaces;
    boolean tokenizeNumbers;
    boolean tokenizeWords;
    boolean testquotes;
    Tokenizer.WordRecognizer wordRecognizer;
    Map keywordMap;
    String openquotes, closequotes;
    boolean trackPosition;

    int maximumTokenLength = 16 * 1024;

    int tokenType = BOF;
    int tokenLine = 0; 
    int tokenColumn = 0; 
    int tokenKeyword = -1;

    int line=0, column=0;  // The line and column numbers of text[p]

    // The name of this field is is a little misleading. If eof is true, it
    // means that no more characters are available. But tokenType and tokenText
    // may still be valid until the next call to next(), nextChar(), or scan().
    boolean eof;           // Set to the return value of fillBuffer()

    // The following fields keep track of the tokenizer's state
    // Invariant:  tokenStart <= tokenEnd <= p <= numChars <= text.length

    /**
     * The start of the current token in {@link #text}.
     * Subclasses may need to update this field in {@link #fillBuffer}.
     */
    protected int tokenStart = 0;

    /**
     * The index in {@link #text} of the first character after the current
     * token. Subclasses may need to update this field in {@link #fillBuffer}.
     */
    protected int tokenEnd = 0;

    /**
     * The position of the first untokenized character in {@link #text}.
     * Subclasses may need to update this field in {@link #fillBuffer}.
     */
    protected int p = 0;

    /**
     * The number of valid characters of input text stored in {@link #text}.
     * Subclasses must implement {@link #createBuffer} and {@link #fillBuffer}
     * to set this value appropriately.
     */
    protected int numChars = 0;

    /**
     * A buffer holding the text we're parsing.  Subclasses must implement
     * {@link #createBuffer} to set this field to a character array, and
     * {@link #fillBuffer} to refill the array.
     */
    protected char[] text = null;

    /**
     * Create the {@link #text} buffer to use for parsing.  This method may
     * put text in the buffer, but it is not required to.  In either case, it
     * should set {@link #numChars} appropriately.  This method will be called
     * once, before tokenizing begins.
     * 
     * @param bufferSize the minimum size of the created array, unless the 
     * subclass knows in advance that the input text is smaller than this, in 
     * which case, the input text size may be used instead.
     * @see #fillBuffer
     */
    protected abstract void createBuffer(int bufferSize);

    /**
     * Fill or refill the {@link #text} buffer and adjust related fields.
     * This method will be called when the tokenizer needs more characters to
     * tokenize. Concrete subclasses must implement this method to put
     * characters into the @{link #text} buffer, blocking if necessary to wait
     * for characters to become available.  This method may make room in the
     * buffer by shifting the contents down to remove any characters before
     * tokenStart.  It must preserve any characters after {@link #tokenStart}
     * and before {@link #numChars}, however.  After such a shift, it must
     * adjust {@link #tokenStart}, {@link #tokenEnd} and {@link #p}
     * appropriately.  After the optional shift, the method should add as many
     * new characters as possible to {@link #text} (and always at least 1) and
     * adjust {@link #numChars} appropriately.
     * 
     * @return false if no more characters are available; true otherwise.
     * @see #createBuffer
     */
    protected abstract boolean fillBuffer() throws IOException;

    public Tokenizer skipSpaces(boolean skip) {
	skipSpaces = skip;
	return this;
    }

    public Tokenizer tokenizeSpaces(boolean tokenize) {
	tokenizeSpaces = tokenize;
	return this;
    }

    public Tokenizer tokenizeNumbers(boolean tokenize) {
	tokenizeNumbers = tokenize;
	return this;
    }
    
    public Tokenizer tokenizeWords(boolean tokenize) {
	tokenizeWords = tokenize;
	return this;
    }

    public Tokenizer wordRecognizer(Tokenizer.WordRecognizer wordRecognizer) {
	this.wordRecognizer = wordRecognizer;
	return this;
    }

    public Tokenizer quotes(String openquotes, String closequotes) {
	if (openquotes == null || closequotes == null) 
	    throw new NullPointerException("arguments must be non-null");
	if (openquotes.length() != closequotes.length()) 
	    throw new IllegalArgumentException("argument lengths differ");
	this.openquotes = openquotes;
	this.closequotes = closequotes;
	this.testquotes = openquotes.length() > 0;
	return this;
    }

    public Tokenizer trackPosition(boolean track) {
	if (text != null) throw new IllegalStateException();
	trackPosition = track;
	return this;
    }

    public Tokenizer keywords(String[] keywords) {
	if (keywords != null) {
	    keywordMap = new HashMap(keywords.length);
	    for(int i = 0; i < keywords.length; i++) 
		keywordMap.put(keywords[i], new Integer(i));
	}
	else keywordMap = null;
	return this;
    }

    public Tokenizer maximumTokenLength(int size) {
	if (size < 1) throw new IllegalArgumentException();
	if (text != null) throw new IllegalStateException();
	maximumTokenLength = size;
	return this;
    }

    public int tokenType() { return tokenType; }

    public String tokenText() {
	if (text == null || tokenStart >= numChars) return null;
	return new String(text, tokenStart, tokenEnd-tokenStart);
    }

    public int tokenLine() {
	if (trackPosition && tokenStart < numChars) return tokenLine;
	else return 0;
    }

    public int tokenColumn() {
	if (trackPosition && tokenStart < numChars) return tokenColumn;
	else return 0;
    }

    public int tokenKeyword() {
	if (tokenType == KEYWORD) return tokenKeyword;
	else return -1;
    }
                                 
    public int next() throws IOException {
	int quoteindex;
	beginNewToken();
	if (eof) return tokenType = EOF;

	char c = text[p];

	if ((skipSpaces||tokenizeSpaces) && Character.isWhitespace(c)) {
	    tokenType = SPACE;
	    do {
		if (trackPosition) updatePosition(text[p]);
		p++;
		if (p >= numChars) eof = !fillBuffer();
	    } while(!eof && Character.isWhitespace(text[p]));

	    // If we don't return space tokens then recursively call 
	    // this method to find another token. Note that the next character
	    // is not space, so we will not get into infinite recursion
	    if (skipSpaces) return next();
	    tokenEnd = p;
	}
	else if (tokenizeNumbers && Character.isDigit(c)) {
	    tokenType = NUMBER;
	    do {
		if (trackPosition) column++;
		p++;
		if (p >= numChars) eof = !fillBuffer();
	    } while(!eof && Character.isDigit(text[p]));
	    tokenEnd = p;
	}
	else if (tokenizeWords && 
		 (wordRecognizer!=null
		      ?wordRecognizer.isWordStart(c)
		      :Character.isJavaIdentifierStart(c))) {
	    tokenType = WORD;
	    do {
		if (trackPosition) column++;
		p++;
		if (p >= numChars) eof = !fillBuffer();
	    } while(!eof &&
		    (wordRecognizer!=null
		         ?wordRecognizer.isWordPart(text[p], c)
		         :Character.isJavaIdentifierPart(text[p])));

	    if (keywordMap != null) {
		String ident = new String(text,tokenStart,p-tokenStart);
		Integer index = (Integer) keywordMap.get(ident);
		if (index != null) {
		    tokenType = KEYWORD;
		    tokenKeyword = index.intValue();
		}
	    }
	    tokenEnd = p;
	}
	else if (testquotes && (quoteindex = openquotes.indexOf(c)) != -1) {
	    // Notes: we do not recognize any escape characters.
	    // We do not include the opening or closing quote.
	    // We do not report an error on EOF or OVERFLOW.
	    if (trackPosition) column++;
	    p++;
	    // Scan until we find a matching quote, but do not include
	    // the opening or closing quote.  Set the token type to the 
	    // opening delimiter
	    char closequote = closequotes.charAt(quoteindex);
	    scan(closequote, false, false, true);
	    tokenType = c;
	    // the call to scan set tokenEnd, so we don't have to
	}
	else {
	    // Otherwise, the character itself is the token
	    if (trackPosition) updatePosition(text[p]);
	    tokenType = text[p];
	    p++;
	    tokenEnd = p;
	}
	    
	// Check the invariants before returning
	assert text != null && 0 <= tokenStart && tokenStart <= tokenEnd && 
	    tokenEnd <= p && p <= numChars && numChars <= text.length;
	return tokenType;
    }

    public int nextChar() throws IOException {
	beginNewToken();
	if (eof) return tokenType = EOF;
	tokenType = text[p];
	if (trackPosition) updatePosition(text[p]);
	tokenEnd = ++p;
	// Check the invariants before returning
	assert text != null && 0 <= tokenStart && tokenStart <= tokenEnd && 
	    tokenEnd <= p && p <= numChars && numChars <= text.length;
	return tokenType;
    }

    public int scan(char delimiter, boolean extendCurrentToken,
		    boolean includeDelimiter, boolean skipDelimiter)
	throws IOException 
    {
	return scan(new char[] { delimiter }, false,
		    extendCurrentToken, includeDelimiter, skipDelimiter);
    }

    public int scan(String delimiter, boolean matchall,
		    boolean extendCurrentToken,
		    boolean includeDelimiter, boolean skipDelimiter)
	throws IOException 
    {
	return scan(delimiter.toCharArray(), matchall,
		    extendCurrentToken, includeDelimiter, skipDelimiter);
    }

    protected int scan(char[] delimiter, 
		       boolean matchall, boolean extendCurrentToken,
		       boolean includeDelimiter, boolean skipDelimiter)
	throws IOException 
    {
	if (matchall && !includeDelimiter && !skipDelimiter) 
	    throw new IllegalArgumentException("must include or skip " +
					  "delimiter when matchall is true");

	if (extendCurrentToken) ensureChars();
	else beginNewToken();

	tokenType = TEXT; // Even if return value differs
	if (eof) return EOF;

	int delimiterMatchIndex = 0;
	String delimString = null;
	if (!matchall && delimiter.length > 0)
	    delimString = new String(delimiter);

	while(!eof) {
	    // See if we've found the delimiter.  There are 3 cases here:
	    // 1) single-character delimiter
	    // 2) multi-char delimiter, and all must be matched sequentially
	    // 3) multi-char delimiter, must match any one of them.
	    if (delimiter.length == 1) {
		if (text[p] == delimiter[0]) break;
	    }
	    else if (matchall) {
		if (text[p] == delimiter[delimiterMatchIndex]) {
		    delimiterMatchIndex++;
		    if (delimiterMatchIndex == delimiter.length) break;
		}
		else delimiterMatchIndex = 0;
	    }
	    else {
		if (delimString.indexOf(text[p]) != -1) break;
	    }

	    if (trackPosition) updatePosition(text[p]);
	    p++;
	    if (p >= numChars) {    // Do we need more text?
		if (tokenStart > 0)     // Do we have room for more?
		    eof = !fillBuffer(); // Yes, so go get some
		else {                  // No room for more characters
		    tokenEnd = p;       // so report an overflow
		    return OVERFLOW;
		}
	    }
	}

	if (eof) {
	    tokenEnd = p;
	    return EOF;
	}

	if (includeDelimiter) {
	    if (trackPosition) updatePosition(text[p]);
	    p++;
	    tokenEnd = p;
	}
	else if (skipDelimiter) {
	    if (trackPosition) updatePosition(text[p]);
	    p++;
	    if (matchall) tokenEnd = p - delimiter.length;
	    else tokenEnd = p - 1;
	}
	else {
	    // we know the delimiter length is 1 in this case
	    tokenEnd = p;
	}

	// Check the invariants before returning
	assert text != null && 0 <= tokenStart && tokenStart <= tokenEnd && 
	    tokenEnd <= p && p <= numChars && numChars <= text.length;
	return TEXT;
    }

    private void ensureChars() throws IOException {
	if (text == null) {
	    createBuffer(maximumTokenLength);  // create text[], set numChars
	    p = tokenStart = tokenEnd = 0;     // initialize other state
	    if (trackPosition) line = column = 1;
	}
	if (!eof && p >= numChars) // Fill the text[] buffer if needed
	    eof = !fillBuffer();  

	// Make sure our class invariants hold true before we start a token
	assert text != null && 0 <= tokenStart && tokenStart <= tokenEnd && 
	    tokenEnd <= p && (p < numChars || (p == numChars && eof)) &&
	    numChars <= text.length;
    }

    private void beginNewToken() throws IOException {
	ensureChars();
	if (!eof) {
	    tokenStart = p;
	    tokenColumn = column;
	    tokenLine = line;
	}
    }

    private void updatePosition(char c) {
	if (c == '\n') {
	    line++;
	    column = 1;
	}
	else column++;
    }
}
