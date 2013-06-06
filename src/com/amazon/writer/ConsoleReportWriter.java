package com.amazon.writer;

import java.util.Set;

import com.amazon.ConnectionInformation;

public class ConsoleReportWriter extends ReportWriterBase {
	@Override
	protected void printConnectedHostReport(String host,
			Set<ConnectionInformation> connectedHost) {
		StringBuilder sb = new StringBuilder("Connected Host For HostName: " + host);
		for (ConnectionInformation ci : connectedHost)
		{
			sb.append(ci.getFromHost() + ",");
		}
		System.out.println(sb);
	}

	@Override
	protected void printRecievedConnectedHostReport(String host,
			Set<ConnectionInformation> connectedHost) {
		
		StringBuilder sb = new StringBuilder("Recieved From HostName: " + host);
		for (ConnectionInformation ci : connectedHost)
		{
			sb.append(ci.getToHost() + ",");
		}
		System.out.println(sb);
	}

	@Override
	protected void printMostConnection(String mostConnectionHost,
			String mostConnectedHost) {
		System.out.println("Most Connection to the host is: " + mostConnectionHost);
		System.out.println("Most Connected to the host is: " + mostConnectedHost);
	}

}
