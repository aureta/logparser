package com.amazon.fieldset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component("FlatFileMapper")
@Scope("singleton")
public class CustomFieldSetMapper implements ItemReader<List<FieldSet>> {
	private String flatFile;
	private static final OptimizedFieldSetFactory FACTORY = new OptimizedFieldSetFactory();

	/**
	 * Read.
	 * @param file
	 *            the file
	 * @param delim
	 *            the delim
	 * @return List of FieldSet.
	 */
	public static List<FieldSet> readAll(File file, String delim) {
		Assert.notNull(file, "Flat File not Specified.");
		List<FieldSet> list = new ArrayList<FieldSet>();
		try {
			String[] names = null;
			String[] lines = OptimizedFlatFileReader.read(file);
			for (String line : lines) {
				String[] values = new Parser().parseLine(line);
				FieldSet fs = FACTORY.create(values, names);
				list.add(fs);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException: "
					+ e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("IOException: " + e.getMessage(), e);
		}

		return list;
	}

	/**
	 * Read.
	 * 
	 * @return List of FieldSet.
	 */
	@Override
	public List<FieldSet> read() {
		return this.readFile(new File(flatFile));
	}

	/**
	 * Read file.
	 * 
	 * @param file
	 *            file.
	 * @return list of fieldset.
	 */
	protected List<FieldSet> readFile(File file) {
		Assert.notNull(file, "Flat File not Specified.");
		List<FieldSet> list = new ArrayList<FieldSet>();
		try {
			String[] names = null;
			String[] lines = OptimizedFlatFileReader.read(file);

			for (String line : lines) {
				String[] values = new Parser().parseLine(line);
				FieldSet fs = FACTORY.create(values, names);
				list.add(fs);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException: "
					+ e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("IOException: " + e.getMessage(), e);
		}

		return list;
	}

}
