package com.amazon.fieldset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * The Class Parser.
 */
@Component("Parser")
public class Parser {
	/** The default separator to use if none is supplied to the constructor. */
	public static final char DEFAULT_SEPARATOR = '|';
	/** The Constant INITIAL_READ_SIZE. */
	public static final int INITIAL_READ_SIZE = 128;
	/**
	 * The default quote character to use if none is supplied to the
	 * constructor.
	 */
	public static final char DEFAULT_QUOTE_CHARACTER = '"';
	/**
	 * The default escape character to use if none is supplied to the
	 * constructor.
	 */
	public static final char DEFAULT_ESCAPE_CHARACTER = '\\';
	/**
	 * The default strict quote behavior to use if none is supplied to the
	 * constructor.
	 */
	public static final boolean DEFAULT_STRICT_QUOTES = false;
	/** The separator char. */
	private char separatorChar;
	/** The escape char. */
	private char escapeChar;
	/** The use strict quotes. */
	private boolean useStrictQuotes;
	/** The pending. */
	private String pending;

	/**
	 * Constructs Parser using a comma for the separator.
	 */
	public Parser() {
		this(DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER,
				DEFAULT_ESCAPE_CHARACTER);
	}

	/**
	 * Constructs Parser with supplied separator.
	 * 
	 * @param separator
	 *            the delimiter to use for separating entries.
	 */
	public Parser(char separator) {
		this(separator, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER);
	}

	/**
	 * Constructs Parser with supplied separator and quote char.
	 * 
	 * @param separator
	 *            the delimiter to use for separating entries
	 * @param quotechar
	 *            the character to use for quoted elements
	 */
	public Parser(char separator, char quotechar) {
		this(separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
	}

	/**
	 * Constructs CSVReader with supplied separator and quote char.
	 * 
	 * @param separator
	 *            the delimiter to use for separating entries
	 * @param quotechar
	 *            the character to use for quoted elements
	 * @param escape
	 *            the character to use for escaping a separator or quote
	 */
	public Parser(char separator, char quotechar, char escape) {
		this(separator, quotechar, escape, DEFAULT_STRICT_QUOTES);
	}

	/**
	 * Constructs CSVReader with supplied separator and quote char. Allows
	 * setting the "strict quotes" flag
	 * 
	 * @param separator
	 *            the delimiter to use for separating entries
	 * @param quotechar
	 *            the character to use for quoted elements
	 * @param escape
	 *            the character to use for escaping a separator or quote
	 * @param strictQuotes
	 *            if true, characters outside the quotes are ignored
	 */
	public Parser(char separator, char quotechar, char escape,
			boolean strictQuotes) {
		this.separatorChar = separator;
		this.escapeChar = escape;
		this.useStrictQuotes = strictQuotes;
	}

	/**
	 * Checks if is pending.
	 * 
	 * @return true if something was left over from last call(s)
	 */
	public boolean isPending() {
		return pending != null;
	}

	/**
	 * Parses the line multi.
	 * 
	 * @param nextLine
	 *            the next line
	 * @return the string[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String[] parseLineMulti(String nextLine) throws IOException {
		return parseLine(nextLine, true);
	}

	/**
	 * Parses the line.
	 * 
	 * @param nextLine
	 *            the next line
	 * @return the string[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String[] parseLine(String nextLine) throws IOException {
		return parseLine(nextLine, false);
	}

	/**
	 * Parses an incoming String and returns an array of elements.
	 * 
	 * @param nextLine
	 *            the string to parse
	 * @param multi
	 *            the multi
	 * @return the comma-tokenized list of elements, or null if nextLine is null
	 * @throws IOException
	 *             if bad things happen during the read
	 */
	private String[] parseLine(String nextLine, boolean multi)
			throws IOException {
		if (!multi && pending != null) {
			pending = null;
		}
		if (nextLine == null) {
			if (pending != null) {
				String s = pending;
				pending = null;
				return new String[] { s };
			} else {
				return null;
			}
		}

		List<String> tokensOnThisLine = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(INITIAL_READ_SIZE);
		boolean inQuotes = false;
		if (pending != null) {
			sb.append(pending);
			pending = null;
			inQuotes = true;
		}
		for (int i = 0; i < nextLine.length(); i++) {
			char c = nextLine.charAt(i);
			if (c == this.escapeChar) {
				if (isNextCharacterEscapable(nextLine, inQuotes, i)) {
					sb.append(nextLine.charAt(i + 1));
					i++;
				}
			} else if (c == separatorChar && !inQuotes) {
				tokensOnThisLine.add((sb.toString()));
				sb = new StringBuilder(INITIAL_READ_SIZE); // start work on next
															// token
			} else {
				if (!useStrictQuotes || inQuotes) {
					sb.append(c);
				}
			}
		}
		// line is done - check status
		if (inQuotes) {
			if (multi) {
				// continuing a quoted section, re-append newline
				sb.append("\n");
				pending = sb.toString();
				sb = null; // this partial content is not to be added to field
							// list yet
			} else {
				throw new IOException(
						"Un-terminated quoted field at end of CSV line");
			}
		}
		if (sb != null) {
			tokensOnThisLine.add(sb.toString());
		}
		return tokensOnThisLine.toArray(new String[tokensOnThisLine.size()]);

	}

	/**
	 * precondition: the current character is an escape.
	 * 
	 * @param nextLine
	 *            the current line
	 * @param inQuotes
	 *            true if the current context is quoted
	 * @param i
	 *            current index in line
	 * @return true if the following character is a quote
	 */
	protected boolean isNextCharacterEscapable(String nextLine,
			boolean inQuotes, int i) {
		return inQuotes // we are in quotes, therefore there can be escaped
						// quotes in here.
				&& nextLine.length() > (i + 1) // there is indeed another
												// character to check.
				&& (nextLine.charAt(i + 1) == this.escapeChar);
	}
}
