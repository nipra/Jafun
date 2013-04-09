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
package je3.xml;
import java.util.*;
import java.io.*;
import java.nio.channels.*;
import java.nio.charset.*;
import je3.classes.Tokenizer;
import je3.classes.CharSequenceTokenizer;
import je3.io.ReaderTokenizer;
import je3.nio.ChannelTokenizer;

/**
 * This class, whose name is an acronym for "Trivial API for XML", is a 
 * container for a simple Parser class for parsing XML and its related Token, 
 * TokenType and ParseException classes and constants.
 * 
 * TAX.Parser is a simple, lightweight pull-parser that is useful for a variety
 * of simple XML parsing tasks. Note, however, that it is more of a tokenizer
 * than a true parser and that the grammar it parses is not actually XML, but a
 * simplified subset of XML. The parser has (at least) these limitations:
 *
 *   It does not enforce well-formedness. For example, it does not require
 *      tags to be properly nested.
 *   It is not a validating parser, and does not read external DTDs
 *   It does not parse the internal subset of the DOCTYPE tag, and cannot
 *      recognize any entities defined there.
 *   It is not namespace-aware
 *   It does not handle entity or character references in attribute values,
 *      not even pre-defined entities such as &quot;
 *   It strips all whitespace from the start and end of document text, which,
 *      while useful for many documents, is not generally correct.
 *   It makes no attempt to do error recovery.  The results of calling next()
 *      after a ParseException is thown are undefined.
 *   It does not provide enough detail to reconstruct the source document
 * 
 * TAX.Parser always replaces entity references with their values, or throws
 * a Tax.ParseException if no replacement value is known.  The parser coalesces
 * adjacent text and entities into a single TEXT token. CDATA sections are
 * also returned as TEXT tokens, but are not coalesced.
 **/
public class TAX {
    // Enumerated type return values for Token.type()
    public static final TokenType TAG = new TokenType("TAG");
    public static final TokenType ENDTAG = new TokenType("ENDTAG");
    public static final TokenType TEXT = new TokenType("TEXT");
    public static final TokenType COMMENT = new TokenType("COMMENT");
    public static final TokenType PI = new TokenType("PI");
    public static final TokenType DOCTYPE = new TokenType("DOCTYPE");
    public static final TokenType XMLDECL = new TokenType("XMLDECL");

    // A type-safe enumeration for token types.  Note the private constructor
    public static class TokenType {
	private static int nextOrdinal = 0;
	private final int ordinal = nextOrdinal++;
	private final String name;
	private TokenType(String name) { this.name = name; }
	public String toString() { return name; }
    }

    // Token objects are the return value of the Parser.next() method.
    // They provide details about what was parsed and where.
    public static class Token {
	TokenType type;    // One of the constants above
	String text;       // Tagname for TAG & XMLDECL, 
	                   // Complete text minus delimiters for other types
	int line, column;  // Position of start of token
	Map attributes;    // name/value map for TAG and XMLDECL,null otherwise
	boolean empty;     // true for XMLDECL and TAGs ending with "/>".
	
	// We use this constructor for TAG and XMLDECL tokens
	Token(TokenType t, String s, int l,int c,Map a,boolean e) {
	    this(t,s,l,c);
	    this.attributes = a;
	    this.empty = e;
	}
	// This constructor for other token types
	Token(TokenType type, String text, int line, int column) {
	    this.type = type;
	    this.text = text;
	    this.line = line;
	    this.column = column;
	}

	// Property accessor methods
	public TokenType type() { return type; }
	public String text() { return text; }
	public int line() { return line; }
	public int column() { return column; }
	public Map attributes() { return attributes; }
	public boolean empty() { return empty; }
    }

    // Exceptions of this type are thrown for syntax errors or unknown entities
    public static class ParseException extends Exception {
	public ParseException(String msg) { super(msg); }

	static ParseException expected(Token t, String expected) {
	    return new ParseException("Expected " + expected + " at line " +
				      t.line() + ", column " + t.column());
	}
    }

    // This is the parser class. It relies internally on a Tokenizer.
    // The public constructors allow you to parse XML from a CharSequence,
    // a Reader, or a Channel.   By default, it will return tokens of type TAG,
    // ENDTAG, and TEXT, and will ignore all others.  You can change this
    // behavior by passing token type constants to returnTokens() or
    // ignoreTokens().  By default the parser will replace character entities
    // and the pre-defined entities &amp;, &lt;, &gt;, &quot;, and &apos; with
    // their values.  You can define new entity name/replacement pairs by
    // calling defineEntity().  These configuration methods all return the
    // Parser objects so calls can be chained.  After configuring your Parser,
    // call the next() method repeatedly until it returns null.
    public static class Parser {
	Tokenizer tokenizer;   // Used to break up the input
	Map entityMap;         // Map entity name to replacment
	// Should we return tokens of these types?
	boolean[] returnTokenType = new boolean[TokenType.nextOrdinal];

	public Parser(CharSequence text) {
	    this(new CharSequenceTokenizer(text));
	}
	public Parser(Reader in) {
	    this(new ReaderTokenizer(in));
	}
	public Parser(ReadableByteChannel in, Charset encoding) {
	    this(new ChannelTokenizer(in, encoding));
	}

	Parser(Tokenizer tokenizer) {
	    this.tokenizer = tokenizer;
	    tokenizer.tokenizeSpaces(true);  // always tokenize spaces
	    tokenizer.trackPosition(true);   // track line and column #
	    // We don't always want the tokenizer to tokenize words, but when
	    // we do, this is how we want the words formed.
	    tokenizer.wordRecognizer(new Tokenizer.WordRecognizer() {
		    public boolean isWordStart(char c) {
			return Character.isLetter(c) || c == '_' || c == ':';
		    }
		    public boolean isWordPart(char c, char first) {
			if (Character.isLetterOrDigit(c) ||
			    c == '_' || c=='-' || c=='.' || c==':')
			    return true;
			int type = Character.getType(c);
			return type == Character.COMBINING_SPACING_MARK ||
			    type == Character.ENCLOSING_MARK ||
			    type == Character.NON_SPACING_MARK ||
			    type == Character.MODIFIER_LETTER;
		    }
		});

	    // Set pre-defined entitities
	    entityMap = new HashMap();
	    entityMap.put("lt", "<");
	    entityMap.put("gt", ">");
	    entityMap.put("amp", "&");
	    entityMap.put("quot", "\"");
	    entityMap.put("apos", "'");

	    // Set default values for what token types to return
	    returnTokenType[TAG.ordinal] = true;
	    returnTokenType[ENDTAG.ordinal] = true;
	    returnTokenType[TEXT.ordinal] = true;
	}

	public Parser returnTokens(TokenType t) {
	    returnTokenType[t.ordinal] = true;
	    return this;
	}

	public Parser ignoreTokens(TokenType t) {
	    returnTokenType[t.ordinal] = false;
	    return this;
	}

	// Define a mapping from entity name to entity replacement.
	// Note that the entity name should not include the & or ; delimiters.
	public Parser defineEntity(String name, String replacement) {
	    entityMap.put(name, replacement);
	    return this;
	}

	// This utility method is for reporting parsing errors
	void syntax(String msg) throws ParseException {
	    throw new ParseException(msg + " at " + 
				     tokenizer.tokenLine() + ":" +
				     tokenizer.tokenColumn());
	}

	// This method returns the next XML token of input or null if there
	// is no more input to parse.
	public Token next() throws ParseException, IOException { 
	    Token token = null;

	    // Otherwise, loop until we find a token we want to return;
	    for(;;) {
		// Invariant: we keep the tokenizer on the first unparsed token
		// This means we start our methods by calling tokenType()
		// to examine what we're currently on, not by calling next().
		// But we end by calling next() to consume the stuff we've
		// already seen.
		int t = tokenizer.tokenType();

		// If we're at the tokenizer's start state, then read a token
		if (t == Tokenizer.BOF) t = tokenizer.next();

		// If there is no more input, return null
		if (t == Tokenizer.EOF) return null;

		// Skip any space. This is not technically correct: we don't
		// know if this is ignorable whitespace or not. But in
		// practice, most clients will want to ignore it.
		if (t == Tokenizer.SPACE) {
		    tokenizer.next();
		    continue;
		}
		
		// If the token is a open angle bracket, then this is markup
		// otherwise it is text.
		if (t == '<') token = parseMarkup();
		else token = parseText();

		// If the token we've parsed is one of the kind to be returned,
		// then return it.  Otherwise, continue looping for a new token
		if (returnTokenType[token.type.ordinal]) return token;
	    }
	}

	// Read the next token and return it if it is a TAG with the specified
	// tagname.  Otherwise throw a ParseException
	public Token expect(String tagname) throws ParseException,IOException {
	    Token t = next();
	    if (t == null || t.type() != TAG || !t.text().equals(tagname)) 
		throw ParseException.expected(t, "<" + tagname + ">");
	    return t;
	}

	// Read and return the next token, if it is of the specified type.
	// Otherwise throw a ParseException
	public Token expect(TokenType type) throws ParseException,IOException {
	    Token t = next();
	    if (t == null || t.type() != type)
		throw ParseException.expected(t, type.toString());
	    return t;
	}

	// This method called with a current token of '<' to parse various
	// forms of XML markup
	Token parseMarkup() throws ParseException, IOException {
	    assert tokenizer.tokenType() == '<' : tokenizer.tokenType();
	    try {
		// Turn on word tokenizing. It is turned off in finally clause.
		tokenizer.tokenizeWords(true); 
		int t = tokenizer.next();
		if (t == '?') {    // Markup is a PI or XMLDECL
		    t = tokenizer.next();
		    if (t != Tokenizer.WORD) syntax("XMLDECL or PI expected");
		    if (tokenizer.tokenText().equals("xml")) {
			Token token =
			    new Token(XMLDECL, tokenizer.tokenText(),
				      tokenizer.tokenLine(),
				      tokenizer.tokenColumn() - 2,
				      parseAttributes(),
				      true);

			if (tokenizer.tokenType()!='?') syntax("'?' expected");
			if (tokenizer.next() != '>') syntax("'>' expected");
			return token;
		    }
		    else {
			Token token = new Token(PI, null,tokenizer.tokenLine(),
						tokenizer.tokenColumn()-2);
			// Read to end of PI
			tokenizer.scan("?>", true, true, false, true);
			token.text = tokenizer.tokenText();
			return token;
		    }
		}
		
		if (t == '!') {         // Markup is DOCTYPE, CDATA, or Comment
		    t = tokenizer.next();
		    if (t == Tokenizer.WORD &&
			tokenizer.tokenText().equals("DOCTYPE")) {
			return parseDoctype();
		    }
		    else if (t == '[') {
			if (tokenizer.next() == Tokenizer.WORD &&
			    tokenizer.tokenText().equals("CDATA") &&
			    tokenizer.next() == '[') {
			    Token token = new Token(TEXT, null,
						    tokenizer.tokenLine(),
						    tokenizer.tokenColumn()-8);
			    tokenizer.scan("]]>", true, false, false, true);
			    token.text = tokenizer.tokenText();
			    return token;
			}
			else syntax("CDATA expected");
		    }
		    else if (t == '-' && tokenizer.next() == '-') {
			// a COMMENT token
			Token token = new Token(COMMENT, null, 
						tokenizer.tokenLine(),
						tokenizer.tokenColumn()-4);
			tokenizer.scan("-->", true, false, false, true);
			token.text = tokenizer.tokenText();
			return token;
		    }
		    else syntax("DOCTYPE, CDATA, or Comment expected");
		}
		if (t == '/') {    // Markup is an element end tag
		    t = tokenizer.next();
		    if (t == Tokenizer.WORD) {
			Token token = new Token(ENDTAG, tokenizer.tokenText(),
						tokenizer.tokenLine(),
						tokenizer.tokenColumn()-2);
			
			t = tokenizer.next();
			if (t == Tokenizer.SPACE) t = tokenizer.next();
			if (t != '>') syntax("Expected '>'");
			return token;
		    }
		    else syntax("ENDTAG expected.");
		}
		if (t == Tokenizer.WORD) { // Markup is an element start tag
		    Token token = new Token(TAG, tokenizer.tokenText(),
					    tokenizer.tokenLine(),
					    tokenizer.tokenColumn() - 1,
					    parseAttributes(),
					    tokenizer.tokenType() == '/');
		    
		    if (tokenizer.tokenType() == '/') tokenizer.next();
		    if (tokenizer.tokenType() != '>') syntax("'>' expected");
		    return token;
		}
		
		// If none of the above matched, this is a syntax error
		syntax("Invalid character following '<'");

		// The compiler doesn't realize that syntax() never returns,
		// so it requires a return statement here.
		return null;
	    }
	    finally {
		// restore tokenizer state
		tokenizer.tokenizeWords(false);
		// Get the next token ready
		tokenizer.next();	    
	    }
	}

	Token parseDoctype() throws IOException {
	    assert (tokenizer.tokenType() == Tokenizer.WORD &&
		    tokenizer.tokenText().equals("DOCTYPE"));

	    int line = tokenizer.tokenLine();
     	    int column = tokenizer.tokenColumn();
	    StringBuffer b = new StringBuffer();

	    int t = tokenizer.next();
	    while(t != '>' && t != '[' && t != Tokenizer.EOF) {
		b.append(tokenizer.tokenText());
		t = tokenizer.next();
	    }
	    
	    if (t == '[') { // If there is an internal subset, scan for its end
		tokenizer.scan("]>", true, true, false, true);
		b.append(tokenizer.tokenText());
		b.append(']');
	    }

	    return new Token(DOCTYPE, b.toString(), line, column);
	}

	// Parse a sequence of name=value attributes, where value is always
	// quoted in single or double quotes, and return them as a Map.
	// When this method is called, the tokenizer is looking at the element
	// name, not at the first token to parse.
	// This used when parsing element start tags and XMLDECLs
	Map parseAttributes() throws ParseException, IOException {
	    try {
		// Adjust tokenizer to recognize quotes.
		// Defaults are restored in finally clause below
		tokenizer.quotes("'\"", "'\"");
		int t = tokenizer.next(); // Consume the element name

		// Skip optional space
		if (t == Tokenizer.SPACE) t = tokenizer.next();

		// This is a special case for elements with no attributes
		if (t != Tokenizer.WORD) return Collections.EMPTY_MAP; 

		Map m = new HashMap();  // Where we'll store attributes

		while(t == Tokenizer.WORD) {
		    String name = tokenizer.tokenText();  // get attribute name
		    // The next token must be '='
		    if (tokenizer.next() != '=') syntax("'=' expected");
		    t = tokenizer.next();
		    // The next token must be a quoted string
		    if (t != '"' && t != '\'')
			syntax("quoted attribute value expected");
		    // Map attribute name to attribute value.
		    // The tokenizer strips the quotes for us.
		    // Note that we do not handle entity references here.
		    m.put(name, tokenizer.tokenText());
		    // Consume the value and skip an optional space after it
		    t = tokenizer.next();
		    if (t == Tokenizer.SPACE) t=tokenizer.next();
		}
		return m;
	    }
	    finally { // Always turn off quote tokenizing
		tokenizer.quotes("", "");
	    }
	}

	// Coalesce any character data and entity references into a single 
	// TEXT token and return it, or throw an exception for undefined
	// entities.  Note that CDATA elements are also returned as TEXT 
	// tokens but are not coalesced like this.  When this method is called
	// we know that the tokenizer is looking at a char other than '<'.
	Token parseText() throws ParseException, IOException {
	    assert tokenizer.tokenType() != '<' : tokenizer.tokenType();
	    // Save line and column info of the start of the text
	    int line = tokenizer.tokenLine();
	    int column = tokenizer.tokenColumn();
	    StringBuffer b = new StringBuffer(); // where we accumulate text

	    int t;
	    while((t = tokenizer.tokenType()) != '<') {
		if (t == '&') b.append(parseEntityReference());
		else {
		    // Otherwise we've found some text
		    tokenizer.scan("<&",  // scan until we find one of these
		       false, // just match one, not the whole string
		       true,  // extend the token we've already started
		       false, // don't include delimiter char in the token
		       false);// don't skip delimiter; save for next token
		    b.append(tokenizer.tokenText());
		    tokenizer.next();
		}
	    }
	    // Strip trailiing space and return as a TEXT token
	    return new Token(TEXT, b.toString().trim(), line, column);
	}


	// Parse a reference to a general entity or character entity and
	// return its value as a string, or throw an exception for undefined
	// entities. Called when tokenizer is looking at an '&'.
	String parseEntityReference() throws ParseException, IOException {
	    assert tokenizer.tokenType() == '&' : tokenizer.tokenType();
	    String s = null;
	    try {
		tokenizer.tokenizeWords(true);
		int t = tokenizer.next();
		if (t == '#') {  // if its a character reference
		    tokenizer.tokenizeNumbers(true);
		    t = tokenizer.next();
		    String text = tokenizer.tokenText();
		    if (t == Tokenizer.NUMBER) {  // a decimal character ref
			int n = Integer.parseInt(text);  // parse as base-10
			s = Character.toString((char)n); // convert to string
		    }
		    else if (t == Tokenizer.WORD && text.charAt(0) != 'x') {
			// a hexadecimal character reference
			String hex = text.substring(1);    // skip the 'x'
			int n = Integer.parseInt(hex, 16); // parse as hex
			s = Character.toString((char)n);   // convert to string
		    }
		    else syntax("illegal character following '&#'");
		}
		else { // otherwise a regular entity reference
		    if (t != Tokenizer.WORD) syntax("entity expected");
		    // look up entity replacement
		    s = (String) entityMap.get(tokenizer.tokenText());
		    if (s == null) syntax("Undefined entity: '&" + 
					  tokenizer.tokenText() + ";'");
		}
	    }
	    catch (NumberFormatException e) {
		// Convert NFE errors to syntax errors
		syntax("malformed character entity");
	    }
	    finally {  // Restore tokenizer state
		tokenizer.tokenizeWords(false).tokenizeNumbers(false);
	    }
	    
	    // Require and consume the trailing semicolon
	    if (tokenizer.next() != ';') syntax("';' expected");
	    tokenizer.next();
	    return s;
	}
    }
}
