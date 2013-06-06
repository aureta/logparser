package com.amazon.test;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Random;

public class main {
	// 1 hour - 3600
	public static void main(String[] args) throws Exception {
		StringBuilder sb = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		System.out.println(cal.getTime());
		for (int i = 0; i < 100000; i++)
		{
			Random randomGenerator = new Random();
			sb.append((cal.getTimeInMillis() / 1000) + " host1 host2\n");
			cal.add(Calendar.SECOND, 5);
		}
		System.out.println(cal.getTime());
		FileWriter fw = new FileWriter(new File("/test/data.log"));
		fw.write(sb.toString());
		fw.close();
	}
}
