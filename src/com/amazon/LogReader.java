package com.amazon;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazon.writer.ConsoleReportWriter;
import com.google.common.collect.ImmutableSet;

/**
 * The Class LogReader.
 */
public class LogReader implements InitializingBean {

	/** The max min. */
	private int maxMinutes;
	/** The last log print. */
	private Date lastLogPrint;
	/** The set builder. */
	private ImmutableSet.Builder<ConnectionInformation> setBuilder;
	/** The report. */
	@Autowired
	private ConsoleReportWriter report;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		setBuilder = new ImmutableSet.Builder<ConnectionInformation>();
		maxMinutes = 60;
	}

	/**
	 * Process entry.
	 * 
	 * @param connInfo
	 *            Connection Pojo
	 * @param useSystemTime
	 *            true if we shouldn't use the unix timestamp from the log file.
	 */
	public void processEntry(ConnectionInformation connInfo,
			boolean useSystemTime) {
		if (lastLogPrint == null){
			if (useSystemTime)
				lastLogPrint = Calendar.getInstance().getTime();
			else
				lastLogPrint = connInfo.getConnectionDate();
		}
		long duration = 0;
		if (useSystemTime)
		{
			duration = System.currentTimeMillis() - lastLogPrint.getTime();
		}
		else
		{
			duration = connInfo.getConnectionDate().getTime()
					- lastLogPrint.getTime();
		}
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		if (minutes >= maxMinutes) {
			report.processReport(setBuilder.build());	
			if (useSystemTime)
				lastLogPrint = Calendar.getInstance().getTime();
			else
				lastLogPrint = connInfo.getConnectionDate();
		} else {
			setBuilder.add(connInfo);
		}
	}

	/**
	 * Gets the max min.
	 * 
	 * @return the max min
	 */
	public int getMaxMinutes() {
		return maxMinutes;
	}

	/**
	 * Sets the max min.
	 * 
	 * @param maxMin
	 *            the new max min
	 */
	public void setMaxMinutes(int maxMin) {
		this.maxMinutes = maxMin;
	}

	/**
	 * Gets the report.
	 * 
	 * @return the report
	 */
	public ConsoleReportWriter getReport() {
		return report;
	}

	/**
	 * Sets the report.
	 * 
	 * @param report
	 *            the new report
	 */
	public void setReport(ConsoleReportWriter report) {
		this.report = report;
	}

}
