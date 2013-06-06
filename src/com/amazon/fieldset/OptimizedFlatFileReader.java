package com.amazon.fieldset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The Class OptimizedFlatFileReader.
 */
@Component("OptimizedFlatFileReader")
@Scope("singleton")
public class OptimizedFlatFileReader {

	/**
	 * Read.
	 * 
	 * @param file
	 *            the file
	 * @param code
	 *            the code
	 * @return the string[]
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String[] read(File file, String code) throws FileNotFoundException,
			IOException {
		BufferedReader reader = null;
		FileInputStream fileReader = null;
		InputStreamReader inputStreamReader = null;
		try {
			List<String> result = new LinkedList<String>();
			fileReader = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileReader);
			reader = new BufferedReader(inputStreamReader);
			String line;
			boolean hasHeader = false;
			while ((line = reader.readLine()) != null) {
				if (!hasHeader) {
					result.add(line);
					hasHeader = true;
					continue;
				}
				if (line.indexOf(code) == 0) {
					result.add(line);
				}
			}
			String[] arrayResult = result.toArray(new String[result.size()]);
			result = null;
			return arrayResult;
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
			try {
				fileReader.close();
			} catch (Exception e) {
			}
			try {
				inputStreamReader.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Read.
	 * 
	 * @param file
	 *            the file
	 * @return the string[]
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String[] read(File file) throws FileNotFoundException,
			IOException {
		BufferedReader reader = null;
		FileReader fileReader = null;
		try {
			List<String> result = new LinkedList<String>();
			fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
			String line;
			while ((line = reader.readLine()) != null) {
				result.add(line);
			}
			String[] arrayResult = result.toArray(new String[result.size()]);
			result = null;
			return arrayResult;
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
			try {
				fileReader.close();
			} catch (Exception e) {
			}
		}
	}
}
