package com.amazon.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.amazon.ConnectionInformation;
import com.amazon.LogReader;
import com.amazon.reader.LineEntryReader;

public class TailFileReaderTask implements Tasklet {

	LogReader reader;
	LineEntryReader fileEntry;
	File file;

	@SuppressWarnings("finally")
	@Override
	public RepeatStatus execute(StepContribution step, ChunkContext ctx)
			throws Exception {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while (true) {
				line = br.readLine();
				if (line == null) {
					Thread.sleep(1000);
				} else {
					// do something interesting with the line
					List<ConnectionInformation> log = fileEntry
							.processRead(line);
					for (ConnectionInformation connInfo : log) {
						reader.processEntry(connInfo, true);
					}
				}
			}
		} finally {
			try {
				br.close();
			} catch (Exception e) {
			}
			return RepeatStatus.FINISHED;
		}
	}
}
