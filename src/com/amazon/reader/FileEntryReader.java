package com.amazon.reader;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amazon.ConnectionInformation;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.io.Files;

/**
 * The Class FileEntryReader.
 */
@Component("fileEntryReader")
public class FileEntryReader extends EntryReaderBase<File> {

	/* (non-Javadoc)
	 * @see com.amazon.IEntryReader#processRead(java.lang.Object)
	 */
	public java.util.List<ConnectionInformation> processRead(File file) {
		ImmutableList.Builder<ConnectionInformation> builder = new ImmutableList.Builder<ConnectionInformation>();
		try {
			List<String> lines = Files.readLines(file, Charsets.UTF_8);
			for (String line : lines) {
				ConnectionInformation info = processConnectionInformation(line);
				builder.add(info);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.build();
	}
}
