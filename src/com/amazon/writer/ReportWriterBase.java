package com.amazon.writer;

import java.util.Set;

import com.amazon.ConnectionInformation;
import com.amazon.IReportWriter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

public abstract class ReportWriterBase implements IReportWriter {

	private Set<String> connectedHostNames;
	private Set<String> receivedConnectionsFrom;

	@Override
	public void processReport(ImmutableSet<ConnectionInformation> connections) {
		ImmutableSetMultimap<String, ConnectionInformation> connectedHosts = null;
		ImmutableSetMultimap<String, ConnectionInformation> recievedFromHosts = null;
		if (connectedHostNames != null && connectedHostNames.size() > 0) {
			// process the list of hosts names that has connected to this host
			ImmutableSetMultimap.Builder<String, ConnectionInformation> builder = new ImmutableSetMultimap.Builder<String, ConnectionInformation>();
			for (String host : connectedHostNames) {
				for (ConnectionInformation ci : connections) {
					if (ci.getFromHost().equals(host)) {
						builder.put(host, ci);
					}
				}
			}
			connectedHosts = builder.build();
		}
		if (receivedConnectionsFrom != null
				&& receivedConnectionsFrom.size() > 0) {
			// process the hosts that has connections
			ImmutableSetMultimap.Builder<String, ConnectionInformation> builder = new ImmutableSetMultimap.Builder<String, ConnectionInformation>();
			for (String host : receivedConnectionsFrom) {
				for (ConnectionInformation ci : connections) {
					if (ci.getToHost().equals(host)) {
						builder.put(host, ci);
					}
				}
			}
			recievedFromHosts = builder.build();
		}
		int mostConnected = 0;
		String mostConnectedHost = null;
		if (connectedHosts != null) {
			for (String host : connectedHostNames) {
				ImmutableSet<ConnectionInformation> immutableSet = connectedHosts
						.get(host);
				if (mostConnected < connectedHosts.size())
				{
					mostConnectedHost = host;
					mostConnected = connectedHosts.size();
				}
				printConnectedHostReport(host, immutableSet);
			}

		}
		int mostConnectionRecieved = 0;
		String mostConnectionHost = null;
		if (recievedFromHosts != null) {
			for (String host : receivedConnectionsFrom) {
				ImmutableSet<ConnectionInformation> immutableSet = recievedFromHosts
						.get(host);
				if (mostConnectionRecieved < recievedFromHosts.size())
				{
					mostConnectionRecieved = recievedFromHosts.size();
					mostConnectedHost = host;
				}
				printRecievedConnectedHostReport(host, immutableSet);
			}
		}
		printMostConnection(mostConnectionHost, mostConnectedHost);
	}
	
	protected abstract void printMostConnection(String mostConnectionHost, String mostConnectedHost)
	;
	
	protected abstract void printConnectedHostReport(String host,
			Set<ConnectionInformation> connectedHost);

	protected abstract void printRecievedConnectedHostReport(String host,
			Set<ConnectionInformation> connectedHost);

	public Set<String> getConnectedHostNames() {
		return connectedHostNames;
	}

	public void setConnectedHostNames(Set<String> connectedHostNames) {
		this.connectedHostNames = connectedHostNames;
	}

	public Set<String> getReceivedConnectionsFrom() {
		return receivedConnectionsFrom;
	}

	public void setReceivedConnectionsFrom(Set<String> receivedConnectionsFrom) {
		this.receivedConnectionsFrom = receivedConnectionsFrom;
	}
}
