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
import java.io.IOException;

/**
 * This interface defines basic character sequence tokenizing capabilities.
 * It can serve as the underpinnings of simple parsers.
 * <p>
 * The methods of this class fall into three categories:
 * <ul>
 * <li>methods to configure the tokenizer, such as {@link #skipSpaces} and
 *     {@link #tokenizeWords}.
 * <li>methods to read a token: {@link #next}, {@link #nextChar}, and
 *     {@link #scan(char,boolean,boolean,boolean)}.
 * <li>methods to query the current token, such as {@link #tokenType},
 *     {@link #tokenText} and {@link #tokenKeyword}.
 * </ul>
 * <p>
 * In its default state, a Tokenizer performs no tokenization at all: 
 * {@link #next} returns each input character as an individual token.
 * You must call one or more configuration methods to specify the type of
 * tokenization to be performed.  Note that the configuration methods all
 * return the Tokenizer object so that repeated method calls can be chained.
 * For example:
 * <pre>
 * Tokenizer t;
 * t.skipSpaces().tokenizeNumbers().tokenizeWords().quotes("'#","'\n");
 * </pre>
 * <p>
 * One particularly important configuration method is
 * {@link #maximumTokenLength}
 * which is used to specify the maximum token length in the input.  A
 * Tokenizer implementation must ensure that it can handle tokens at least
 * this long, typically by allocating a buffer at least that long.
 * <p>
 * The constant fields of this interface are token type constants.
 * Note that their values are all negative. Non-negative token types
 * always represent Unicode characters.
 * <p>
 * A tokenizer may be in one of three states: <ol>
 * <li>Before any tokens have been read.  In this state, {@link #tokenType}
 * always returns (@link #BOF}, and {@link #tokenLine} always returns 0.
 * {@link #maximumTokenLength} and {@link #trackPosition} may only be called
 * in this state.
 * <li>During tokenization.  In this state, {@link #next}, {@link #nextChar},
 * and {@link #scan(char,boolean,boolean,boolean)} are being called to tokenize
 * input characters, but none of these methods has yet returned {@link #EOF}.
 * Configuration methods other than those listed above may be called from this
 * state to dynamically change tokenizing behavior.
 * <li>End-of-file.  Once one of the tokenizing methods have returned EOF,
 * the tokenizer has reached the end of its input.  Any subsequent calls to
 * the tokenizing methods or to {@link #tokenType} will return EOF. Most 
 * methods may still be called from this state, although it is not useful 
 * to do so.
 * </ol>
 * @author David Flanagan
 */
public interface Tokenizer {
    // The following are token type constants.
    /** End-of-file.  Returned when there are no more characters to tokenize */
    public static final int EOF = -1;
    /** The token is a run of whitespace. @see #tokenizeSpaces() */
    public static final int SPACE = -2;
    /** The token is a run of digits. @see #tokenizeNumbers() */
    public static final int NUMBER = -3;
    /** The token is a run of word characters. @see #tokenizeWords() */
    public static final int WORD = -4;
    /** The token is a keyword. @see #keywords() */
    public static final int KEYWORD = -5;
    /** 
     * The token is arbitrary text returned by
     * {@link #scan(char,boolean,boolean,boolean)}.
     */
    public static final int TEXT = -6;
    /**
     * Beginning-of-file. This is the value returned by {@link #tokenType}
     * when it is called before tokenization begins.
     */
    public static final int BOF = -7;
    /** Special return value for {@link #scan(char,boolean,boolean,boolean)}.*/
    public static final int OVERFLOW = -8; // internal buffer overflow

    /**
     * Specify whether to skip spaces or return them.
     * @param skip If false (the default) then return whitespace characters
     *             or tokens.  If true, then next() never returns whitespace.
     * @return this Tokenizer object for method chaining.
     * @see #tokenizeSpaces
     */
    public Tokenizer skipSpaces(boolean skip);

    /**
     * Specify whether adjacent whitespace characters should be coalesced
     * into a single SPACE token.  This has no effect if spaces are being
     * skipped.  The default is false.
     * @param tokenize whether {@link #next} should colaesce adjacent 
     *    whitespace into a single {@link #SPACE} token.
     * @return this Tokenizer object for method chaining.
     * @see #skipSpaces
     */
    public Tokenizer tokenizeSpaces(boolean tokenize);

    /**
     * Specify whether adjacent digit characters should be coalesced into
     * a single token.  The default is false.
     * @param tokenize whether {@link #next} should colaesce adjacent digits
     *    into a single {@link #NUMBER} token.
     * @return this Tokenizer object for method chaining.
     */
    public Tokenizer tokenizeNumbers(boolean tokenize);
    
    /**
     * Specify whether adjacent word characters should be coalesced into
     * a single token.  The default is false. Word characters are defined by
     * a {@link WordRecognizer}.
     * @param tokenize whether {@link #next} should colaesce adjacent word
     *    characters into a single {@link #WORD} token.
     * @return this Tokenizer object for method chaining.
     * @see #wordRecognizer
     */
    public Tokenizer tokenizeWords(boolean tokenize);

    /**
     * Specify a {@link Tokenizer.WordRecognizer} to define what constitues a
     * word. If set to null (the default) then words are defined  by
     * {@link Character#isJavaIdentifierStart} and
     * {@link Character#isJavaIdentifierPart}.
     * This has no effect if word tokenizing has not been enabled.
     * @param wordRecognizer the {@link Tokenizer.WordRecognizer} to use.
     * @return this Tokenizer object for method chaining.
     * @see #tokenizeWords
     */
    public Tokenizer wordRecognizer(WordRecognizer wordRecognizer);

    /**
     * Specify keywords to receive special recognition.
     * If a {@link #WORD} token matches one of these keywords, then the token 
     * type will be set to {@link #KEYWORD}, and {@link #tokenKeyword} will
     * return the index of the keyword in the specified array.
     * @param keywords an array of words to be treated as keywords, or null
     *                 (the default) for no keywords.
     * @return this Tokenizer object for method chaining.
     * @see #tokenizeWords
     */
    public Tokenizer keywords(String[] keywords);

    /**
     * Specify whether the tokenizer should keep track of the line number
     * and column number for each returned token.  The default is false.
     * If set to true, then tokenLine() and tokenColumn() return the
     * line and column numbers of the current token.  
     * @param track whether to track the line and column numbers for each
     *         token.  
     * @return this Tokenizer object for method chaining.
     * @throws java.lang.IllegalStateException
     *         if invoked after tokenizing begins
     * @see #tokenizeWords
     */
    public Tokenizer trackPosition(boolean track);

    /**
     * Specify pairs of token delimiters.  If the tokenizer encounters
     * any character in <tt>openquotes</tt>, then it will scan until it
     * encounters the corresponding character in <tt>closequotes</tt>.
     * When such a token is tokenized, {@link #tokenType} returns the character
     * from <tt>openquotes</tt> that was recognized and {@link #tokenText}
     * returns the characters between, but not including the delimiters.
     * Note that no escape characters are recognized. Quote tokenization occurs
     * after other types of tokenization so <tt>openquotes</tt> should not
     * include whitespace, number or word characters, if spaces, numbers, or
     * words are being tokenized.
     * <p>
     * Quote tokenization is useful for tokens other than quoted strings.
     * For example to recognize single-quoted strings and single-line
     * comments, you might call this method like this:
     * <code>quotes("'#", "'\n");</code>
     *
     * @param openquotes The string of characters that can begin a quote, 
     * @param closequotes The string of characters that end a quote
     * @return this Tokenizer object for method chaining.
     * @throws java.lang.NullPointerException if either argument is null
     * @throws java.lang.IllegalArgumentException if <tt>openquotes</tt> and 
     *         <tt>closequotes</tt> have different lengths.
     * @see #scan(char,boolean,boolean,boolean)
     */
    public Tokenizer quotes(String openquotes, String closequotes);

    /**
     * Specify the maximum token length that the Tokenizer is required to
     * accomodate. If presented with an input token longer than the specified
     * size, a Tokenizer behavior is undefined. Implementations must typically
     * allocate an internal buffer at least this large, but may use a smaller
     * buffer if they know that the total length of the input is smaller.
     * Implementations should document their default value, and are encouraged
     * to define constructors that take the token length as an argument.
     *
     * @param size maximum token length the tokenizer must handle. Must be > 0.
     * @return this Tokenizer object for method chaining.
     * @throws java.lang.IllegalArgumentException if <tt>size</tt> &lt; 1.
     * @throws java.lang.IllegalStateException
     *         if invoked after tokenizing begins
     */
    public Tokenizer maximumTokenLength(int size);

    /**
     * This nested interface defines what a "word" is.
     * @see Tokenizer#tokenizeWords
     * @see Tokenizer#wordRecognizer
     */
    public static interface WordRecognizer {
	/**
	 * Determine whether <tt>c</tt> is a valid word start character.
	 * @param c the character to test
	 * @return true if a word may begin with the character <tt>c</tt>.
	 */
	public boolean isWordStart(char c);

	/**
	 * Determine whether a word that begins with <tt>firstChar</tt> may
	 * contain <tt>c</tt>.
	 * @param c the character to test.
	 * @param firstChar the charactcter that started this word
	 * @return true if a word that begins with <tt>firstChar</tt> may
	 *         contain the the character <tt>c</tt>
	 */
	public boolean isWordPart(char c, char firstChar);
    }

    
    /**
     * Get the type of the current token. Valid token types are the token
     * type constants (all negative values) defined by this interface, and all
     * Unicode characters.  Positive return values typically represent 
     * punctuation characters or other single characters that were not 
     * tokenized.  But see {@link #quotes} for an exception.
     * @return the type of the current token, or {@link #BOF} if no tokens
     *     have been read yet, or {@link #EOF} if no more tokens are available.
     */
    public int tokenType();

    /**
     * Get the text of the current token.
     * @return the text of the current token as a String, or null, when
     *   {@link #tokenType} returns {@link #BOF} or {@link #EOF}.
     *   Tokens delimited by quote characters (see {@link #quotes}) do not
     *   include the opening and closing delimiters, so this method may return
     *   the empty string when an empty quote is tokenized.  The same is
     *   possible after a call to {@link #scan(char,boolean,boolean,boolean)}.
     */
    public String tokenText();

    /**
     * Get the index of the tokenized keyword.
     * @return the index into the keywords array of the tokenized word or 
     *   -1 if the current token type is not {@link #KEYWORD}.
     * @see #keywords
     */
    public int tokenKeyword(); 

    /**
     * Get the line number of the current token.  
     * @return The line number of the start of the current token. Lines
     * are numbered from 1, not 0. This method returns 0 if the tokenizer is
     * not tracking token position or if tokenizing has not started yet, or if
     * the current token is {@link #EOF}.
     * @see #trackPosition
     */
    public int tokenLine();

    /**
     * Get the column number of the current token.  
     * @return The column of the start of the current token. Columns
     * are numbered from 1, not 0. This method returns 0 if the tokenizer is
     * not tracking token position or if tokenizing has not started yet, or if
     * the current token is {@link #EOF}.
     * @see #trackPosition
     */
    public int tokenColumn();

    /**
     * Make the next token of input the current token, and return its type.
     * Implementations must tokenize input using the following algorithm, and
     * must perform each step in the order listed. <ol>
     *
     * <li>If there are no more input characters, set the current token to
     * {@link #EOF} and return that value.
     * 
     * <li>If configured to skip or tokenize spaces, and the current character
     * is whitespace, coalesce any subsequent whitespace characters into a 
     * token.  If spaces are being skipped, start tokenizing a new token,
     * otherwise, make the spaces the current token and return {@link #SPACE}.
     * See {@link #skipSpaces}, {@link #tokenizeSpaces}, and
     * {@link Character#isWhitespace}.
     * 
     * <li>If configured to tokenize numbers and the current character is a 
     * digit, coalesce all adjacent digits into a single token, make it the
     * current token, and return {@link #NUMBER}. See {@link #tokenizeNumbers}
     * and {@link Character#isDigit}
     *
     * <li>If configured to tokenize words, and the current character is a
     * word character, coalesce all adjacent word characters into a single
     * token, and make it the current token. If the word matches a registered
     * keyword, determine the keyword index and return {@link #KEYWORD}.
     * Otherwise return {@link #WORD}. Determine whether a character is a 
     * word character using the registered {@link WordRecognizer}, if any, 
     * or with {@link Character#isJavaIdentifierStart} and
     * {@link Character#isJavaIdentifierPart}.  See also
     * {@link #tokenizeWords} and {@link #wordRecognizer}.
     * 
     * <li>If configured to tokenize quotes or other delimited tokens, and the
     * current character appears in the string of opening delimiters, then
     * scan until the character at the same position in the string of closing
     * delimiters is encountered or until there is no more input of the
     * maximum token size is reached.  Coalesce the characters between (but
     * not including) the delimiters into a single token, set the token type
     * to the opening delimiter, and return this character.
     * See {@link #quotes}.
     * 
     * <li>If none of the steps above has returned a token, then make the
     * current character the current token, and return the current character.
     * </ol>
     *
     * @return the type of the next token, or {@link #EOF} if there are 
     *         no more tokens to be read.
     * @see #nextChar @see #scan(char,boolean,boolean,boolean) */
    public int next() throws IOException;

    /**
     * Make the next character of input the current token, and return it.
     * @return the next character or {@link #EOF} if there are no more.
     * @see #next @see #scan(char,boolean,boolean,boolean)
     */
    public int nextChar() throws IOException;

    /** 
     * Scan until the first occurrance of the specified delimiter character.
     * Because a token scanned in this way may contain arbitrary characters,
     * the current token type is set to {@link #TEXT}.
     * @param delimiter the character to scan until.
     * @param extendCurrentToken if true, the scanned characters extend the
     *   current token.  Otherwise, they are a token of their own.
     * @param includeDelimiter if true, then the delimiter character is
     *   included at the token.  If false, then see skipDelimiter.
     * @param skipDelimiter if <tt>includeDelimiter</tt> is false, then this
     *     parameter specifies whether to skip the delimiter or return it in
     *     the next token.
     * @return the token type {@link #TEXT} if the delimiter character is
     *   successfully found.  If the delimiter is not found, the return value
     *   is {@link #EOF} if all input was read, or {@link #OVERFLOW} if the
     *   maximum token length was exceeded.  Note that even when this method
     *   does not return {@link #TEXT}, {@link #tokenType} does still return
     *   that value, and {@link #tokenText} returns as much of the token
     *   as could be read.
     * @see #scan(java.lang.String,boolean,boolean,boolean,boolean)
     * @see #next @see #nextChar
     */
    public int scan(char delimiter, boolean extendCurrentToken,
		    boolean includeDelimiter, boolean skipDelimiter)
	throws IOException;

    /**
     * This method is just {@link #scan(char,boolean,boolean,boolean)} except
     * that it uses a String delimiter, possibly containing more than one
     * character.
     * @param delimiter the string of characters that will terminate the scan.
     *     This argument must not be null, and must be of length 1 or greater.
     * @param matchall true if all characters of the delimiter must be matched
     *     sequentially.  False if any one character in the string will do.
     * @param extendCurrentToken add scanned text to current token if true.
     * @param includeDelimiter include delimiter text in token if true.
     * @param skipDelimiter if <tt>includeDelimiter</tt> is false, then this
     *     parameter specifies whether to skip the delimiter or return it in
     *     the next token.
     * @return {@link #TEXT}, {@link #EOF}, or {@link #OVERFLOW}.  See
     *     {@link #scan(char,boolean,boolean,boolean)} for details.
     * @throws java.lang.NullPointerException if delimiter is null.
     * @throws java.lang.IllegalArgumentException if delimiter is empty.
     * @throws java.lang.IllegalArgumentException matchall is true and
     *    includeDelimiter and skipDelimiter are both false.
     * @see #scan(char,boolean,boolean,boolean)
     */
    public int scan(String delimiter, boolean matchAll,
		    boolean extendCurrentToken, boolean includeDelimiter,
		    boolean skipDelimiter)
	throws IOException;
}
