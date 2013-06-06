package com.amazon.reader;

import java.util.List;

import org.springframework.stereotype.Component;

import com.amazon.ConnectionInformation;
@Component("lineEntryReader")
public class LineEntryReader extends EntryReaderBase<String> {
	@Override
	public List<ConnectionInformation> processRead(String input) {
		return processRead(input);
	}
}
