package com.amazon.reader;

import java.io.IOException;
import java.util.List;

import com.amazon.ConnectionInformation;
import com.amazon.IEntryReader;
import com.amazon.fieldset.Parser;

public abstract class EntryReaderBase<Input> implements
		IEntryReader<Input, List<ConnectionInformation>> {

	protected ConnectionInformation processConnectionInformation(String line)
			throws IOException {
		String[] parsedLine = new Parser(' ').parseLine(line);
		String fromHost = parsedLine[1];
		String toHost = parsedLine[2];
		ConnectionInformation info = new ConnectionInformation(
				Long.valueOf(parsedLine[0].trim()), fromHost, toHost);
		return info;
	}
}
