package com.amazon.fieldset;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableList;

/**
 * The Class OptimizedFieldSet.
 */
public class OptimizedFieldSet implements FieldSet {
	/** The Constant VAL_31. */
	private static final int VAL_31 = 31;
	/** The Constant DEFAULT_DATE_PATTERN. */
	protected final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	/** The number format. */
	private static NumberFormat numberFormat;
	/** The tokens. */
	private String[] tokens;
	/** The names. */
	private ImmutableList<String> names;

	static {
		if (numberFormat == null) {
			numberFormat = NumberFormat.getInstance(Locale.US);
		}
	}

	/**
	 * Sets the number format.
	 * 
	 * @param nf
	 *            the new number format
	 */
	public static void setNumberFormat(NumberFormat nf) {
		OptimizedFieldSet.numberFormat = nf;
	}

	/**
	 * Instantiates a new optimized field set.
	 * 
	 * @param tokens
	 *            the tokens
	 */
	public OptimizedFieldSet(String[] tokens) {
		this.tokens = tokens == null ? null : (String[]) tokens.clone();
	}

	/**
	 * Instantiates a new optimized field set.
	 * 
	 * @param tokens
	 *            the tokens
	 * @param names
	 *            the names
	 */
	public OptimizedFieldSet(String[] tokens, String[] names) {
		Assert.notNull(tokens);
		Assert.notNull(names);
		if (tokens.length != names.length) {
			throw new IllegalArgumentException(
					"Field names must be same length as values: names = "
							+ Arrays.asList(names) + ", values = "
							+ Arrays.asList(tokens));
		}
		this.tokens = (String[]) tokens.clone();
		this.names = ImmutableList.copyOf(names);
	}

	/**
	 * Gets the names.
	 * 
	 * @return the names
	 */
	@Override
	public String[] getNames() {
		if (names == null) {
			throw new IllegalStateException("Field names are not known");
		}
		return names.toArray(new String[names.size()]);
	}

	/**
	 * Checks for names.
	 * 
	 * @return true, if successful
	 */
	@Override
	public boolean hasNames() {
		return names != null;
	}

	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	@Override
	public String[] getValues() {
		return tokens.clone();
	}

	/**
	 * Read string.
	 * 
	 * @param index
	 *            the index
	 * @return the string
	 */
	@Override
	public String readString(int index) {
		return readAndTrim(index);
	}

	/**
	 * Read string.
	 * 
	 * @param name
	 *            the name
	 * @return the string
	 */
	@Override
	public String readString(String name) {
		return readString(indexOf(name));
	}

	/**
	 * Read raw string.
	 * 
	 * @param index
	 *            the index
	 * @return the string
	 */
	@Override
	public String readRawString(int index) {
		return tokens[index];
	}

	/**
	 * Read raw string.
	 * 
	 * @param name
	 *            the name
	 * @return the string
	 */
	@Override
	public String readRawString(String name) {
		return readRawString(indexOf(name));
	}

	/**
	 * Read boolean.
	 * 
	 * @param index
	 *            the index
	 * @return true, if successful
	 */
	@Override
	public boolean readBoolean(int index) {
		return readBoolean(index, "true");
	}

	/**
	 * Read boolean.
	 * 
	 * @param name
	 *            the name
	 * @return true, if successful
	 */
	@Override
	public boolean readBoolean(String name) {
		return readBoolean(indexOf(name));
	}

	/**
	 * Read boolean.
	 * 
	 * @param index
	 *            the index
	 * @param trueValue
	 *            the true value
	 * @return true, if successful
	 */
	@Override
	public boolean readBoolean(int index, String trueValue) {
		Assert.notNull(trueValue, "'trueValue' cannot be null.");
		String value = readAndTrim(index);
		return trueValue.equals(value) ? true : false;
	}

	/**
	 * Read boolean.
	 * 
	 * @param name
	 *            the name
	 * @param trueValue
	 *            the true value
	 * @return true, if successful
	 */
	@Override
	public boolean readBoolean(String name, String trueValue) {
		return readBoolean(indexOf(name), trueValue);
	}

	/**
	 * Read char.
	 * 
	 * @param index
	 *            the index
	 * @return the char
	 */
	@Override
	public char readChar(int index) {
		String value = readAndTrim(index);
		Assert.isTrue(value.length() == 1, "Cannot convert field value '"
				+ value + "' to char.");
		return value.charAt(0);
	}

	/**
	 * Read char.
	 * 
	 * @param name
	 *            the name
	 * @return the char
	 */
	@Override
	public char readChar(String name) {
		return readChar(indexOf(name));
	}

	/**
	 * Read byte.
	 * 
	 * @param index
	 *            the index
	 * @return the byte
	 */
	public byte readByte(int index) {
		return Byte.parseByte(readAndTrim(index));
	}

	/**
	 * Read byte.
	 * 
	 * @param name
	 *            the name
	 * @return the byte
	 */
	@Override
	public byte readByte(String name) {
		return readByte(indexOf(name));
	}

	/**
	 * Read short.
	 * 
	 * @param index
	 *            the index
	 * @return the short
	 */
	@Override
	public short readShort(int index) {
		return Short.parseShort(readAndTrim(index));
	}

	/**
	 * Read short.
	 * 
	 * @param name
	 *            the name
	 * @return the short
	 */
	@Override
	public short readShort(String name) {
		return readShort(indexOf(name));
	}

	/**
	 * Read int.
	 * 
	 * @param index
	 *            the index
	 * @return the int
	 */
	@Override
	public int readInt(int index) {
		return parseNumber(readAndTrim(index)).intValue();
	}

	/**
	 * Read int.
	 * 
	 * @param name
	 *            the name
	 * @return the int
	 */
	@Override
	public int readInt(String name) {
		return readInt(indexOf(name));
	}

	/**
	 * Read int.
	 * 
	 * @param index
	 *            the index
	 * @param defaultValue
	 *            the default value
	 * @return the int
	 */
	@Override
	public int readInt(int index, int defaultValue) {
		String value = readAndTrim(index);
		return StringUtils.hasLength(value) ? Integer.parseInt(value)
				: defaultValue;
	}

	/**
	 * Read int.
	 * 
	 * @param name
	 *            the name
	 * @param defaultValue
	 *            the default value
	 * @return the int
	 */
	@Override
	public int readInt(String name, int defaultValue) {
		return readInt(indexOf(name), defaultValue);
	}

	/**
	 * Read long.
	 * 
	 * @param index
	 *            the index
	 * @return the long
	 */
	@Override
	public long readLong(int index) {
		return (Long) parseNumber(readAndTrim(index));
	}

	/**
	 * Read long.
	 * 
	 * @param name
	 *            the name
	 * @return the long
	 */
	@Override
	public long readLong(String name) {
		return readLong(indexOf(name));
	}

	/**
	 * Read long.
	 * 
	 * @param index
	 *            the index
	 * @param defaultValue
	 *            the default value
	 * @return the long
	 */
	@Override
	public long readLong(int index, long defaultValue) {
		String value = readAndTrim(index);
		return StringUtils.hasLength(value) ? Long.parseLong(value)
				: defaultValue;
	}

	/**
	 * Read long.
	 * 
	 * @param name
	 *            the name
	 * @param defaultValue
	 *            the default value
	 * @return the long
	 */
	@Override
	public long readLong(String name, long defaultValue) {
		return readLong(indexOf(name), defaultValue);
	}

	/**
	 * Read float.
	 * 
	 * @param index
	 *            the index
	 * @return the float
	 */
	@Override
	public float readFloat(int index) {
		return parseNumber(readAndTrim(index)).floatValue();
	}

	/**
	 * Read float.
	 * 
	 * @param name
	 *            the name
	 * @return the float
	 */
	@Override
	public float readFloat(String name) {
		return readFloat(indexOf(name));
	}

	/**
	 * Read double.
	 * 
	 * @param index
	 *            the index
	 * @return the double
	 */
	@Override
	public double readDouble(int index) {
		return (Double) parseNumber(readAndTrim(index));
	}

	/**
	 * Read double.
	 * 
	 * @param name
	 *            the name
	 * @return the double
	 */
	@Override
	public double readDouble(String name) {
		return readDouble(indexOf(name));
	}

	/**
	 * Read big decimal.
	 * 
	 * @param index
	 *            the index
	 * @return the big decimal
	 */
	@Override
	public BigDecimal readBigDecimal(int index) {
		return readBigDecimal(index, null);
	}

	/**
	 * Read big decimal.
	 * 
	 * @param name
	 *            the name
	 * @return the big decimal
	 */
	@Override
	public BigDecimal readBigDecimal(String name) {
		return readBigDecimal(name, null);
	}

	/**
	 * Read big decimal.
	 * 
	 * @param index
	 *            the index
	 * @param defaultValue
	 *            the default value
	 * @return the big decimal
	 */
	@Override
	public BigDecimal readBigDecimal(int index, BigDecimal defaultValue) {
		String candidate = readAndTrim(index);
		try {
			return (StringUtils.hasText(candidate)) ? new BigDecimal(candidate)
					: defaultValue;
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Unparseable number: " + candidate);
		}
	}

	/**
	 * Read big decimal.
	 * 
	 * @param name
	 *            the name
	 * @param defaultValue
	 *            the default value
	 * @return the big decimal
	 */
	@Override
	public BigDecimal readBigDecimal(String name, BigDecimal defaultValue) {
		try {
			return readBigDecimal(indexOf(name), defaultValue);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage() + ", name: ["
					+ name + "]");
		}
	}

	/**
	 * Read date.
	 * 
	 * @param index
	 *            the index
	 * @return the date
	 */
	@Override
	public Date readDate(int index) {
		return parseDate(readAndTrim(index), new SimpleDateFormat(
				DEFAULT_DATE_PATTERN));
	}

	/**
	 * Read date.
	 * 
	 * @param name
	 *            the name
	 * @return the date
	 */
	@Override
	public Date readDate(String name) {
		try {
			return readDate(indexOf(name));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage() + ", name: ["
					+ name + "]");
		}
	}

	/**
	 * Read date.
	 * 
	 * @param index
	 *            the index
	 * @param pattern
	 *            the pattern
	 * @return the date
	 */
	@Override
	public Date readDate(int index, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);
		return parseDate(readAndTrim(index), sdf);
	}

	/**
	 * Read date.
	 * 
	 * @param name
	 *            the name
	 * @param pattern
	 *            the pattern
	 * @return the date
	 */
	@Override
	public Date readDate(String name, String pattern) {
		try {
			return readDate(indexOf(name), pattern);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage() + ", name: ["
					+ name + "]");
		}
	}

	/**
	 * Gets the field count.
	 * 
	 * @return the field count
	 */
	@Override
	public int getFieldCount() {
		return tokens.length;
	}

	/**
	 * Read and trim the {@link String} value at '<code>index</code>'.
	 * 
	 * @param index
	 *            the index
	 * @return the string
	 */
	protected String readAndTrim(int index) {
		String value = tokens[index];

		if (value != null) {
			return value.trim();
		} else {
			return null;
		}
	}

	/**
	 * Read and trim the {@link String} value from column with given '
	 * <code>name</code>.
	 * 
	 * @param name
	 *            the name
	 * @return the int
	 */
	protected int indexOf(String name) {
		if (names == null) {
			throw new IllegalArgumentException(
					"Cannot access columns by name without meta data");
		}
		int index = names.indexOf(name);
		if (index >= 0) {
			return index;
		}
		throw new IllegalArgumentException("Cannot access column [" + name
				+ "] from " + names);
	}

	/**
	 * Equals.
	 * 
	 * @param object
	 *            the object
	 * @return true, if successful
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object instanceof DefaultFieldSet) {
			OptimizedFieldSet fs = (OptimizedFieldSet) object;
			if (this.tokens == null) {
				return fs.tokens == null;
			} else {
				return Arrays.equals(this.tokens, fs.tokens);
			}
		}

		return false;
	}

	/**
	 * Hash code.
	 * 
	 * @return Integer
	 */
	@Override
	public int hashCode() {
		if (tokens == null) {
			return 0;
		}
		int result = 1;
		for (int i = 0; i < tokens.length; i++) {
			result = VAL_31 * result
					+ (tokens[i] == null ? 0 : tokens[i].hashCode());
		}
		return result;
	}

	/**
	 * Gets the properties.
	 * 
	 * @return the properties
	 */
	@Override
	public Properties getProperties() {
		if (names == null) {
			throw new IllegalStateException(
					"Cannot create properties without meta data");
		}
		Properties props = new Properties();
		for (int i = 0; i < tokens.length; i++) {
			String value = readAndTrim(i);
			if (value != null) {
				props.setProperty((String) names.get(i), value);
			}
		}
		return props;
	}

	/**
	 * Parses the number.
	 * 
	 * @param candidate
	 *            the candidate
	 * @return the number
	 */
	private Number parseNumber(String candidate) {
		try {
			return OptimizedFieldSet.numberFormat.parse(candidate);
		} catch (ParseException e) {
			throw new NumberFormatException("Unparseable number: " + candidate);
		}
	}

	/**
	 * Parses the date.
	 * 
	 * @param readAndTrim
	 *            the read and trim
	 * @param df
	 *            the date format
	 * @return the date
	 */
	private Date parseDate(String readAndTrim, DateFormat df) {
		try {
			return df.parse(readAndTrim);
		} catch (ParseException e) {
			String pattern = null;
			if (df instanceof SimpleDateFormat) {
				pattern = ((SimpleDateFormat) df).toPattern();
			} else {
				pattern = df.toString();
			}
			throw new IllegalArgumentException(e.getMessage() + ", format: ["
					+ pattern + "]");
		}
	}
}
