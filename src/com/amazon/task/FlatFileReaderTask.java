package com.amazon.task;

import java.io.File;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.Assert;

import com.amazon.ConnectionInformation;
import com.amazon.LogReader;
import com.amazon.reader.FileEntryReader;

public class FlatFileReaderTask implements Tasklet {
	private LogReader reader;
	private FileEntryReader fileEntry;
	private File file;
	@Override
	public RepeatStatus execute(StepContribution step, ChunkContext ctx)
			throws Exception {
		Assert.notNull(file);
		List<ConnectionInformation> log = fileEntry.processRead(file);
		for (ConnectionInformation connInfo : log) {
			reader.processEntry(connInfo, false);
		}
		return RepeatStatus.FINISHED;
	}

	public LogReader getReader() {
		return reader;
	}

	public void setReader(LogReader reader) {
		this.reader = reader;
	}

	public FileEntryReader getFileEntry() {
		return fileEntry;
	}

	public void setFileEntry(FileEntryReader fileEntry) {
		this.fileEntry = fileEntry;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
