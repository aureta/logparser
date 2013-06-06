package com.amazon;

import java.util.Date;

@SuppressWarnings("serial")
public class ConnectionInformation implements java.io.Serializable, Comparable<ConnectionInformation> {
	private Long unixTimeStamp;
	private Date connectionDate;
	private String fromHost;
	private String toHost;

	public ConnectionInformation(long unixTimeStamp, String fromHost,
			String toHost) {
		setConnectionDate(new Date(unixTimeStamp * 1000));
		setFromHost(fromHost);
		setToHost(toHost);
	}

	public Long getUnixTimeStamp() {
		return unixTimeStamp;
	}

	public void setUnixTimeStamp(Long unixTimeStamp) {
		this.unixTimeStamp = unixTimeStamp;
	}

	public Date getConnectionDate() {
		return connectionDate;
	}

	public void setConnectionDate(Date connectionDate) {
		this.connectionDate = connectionDate;
	}

	public String getFromHost() {
		return fromHost;
	}

	public void setFromHost(String fromHost) {
		this.fromHost = fromHost;
	}

	public String getToHost() {
		return toHost;
	}

	public void setToHost(String toHost) {
		this.toHost = toHost;
	}

	@Override
	public String toString() {
		return "From: " + fromHost + " to " + toHost;
	}

	@Override
	public int compareTo(ConnectionInformation o) {
		return Long.signum(this.unixTimeStamp - o.unixTimeStamp);
	}
}
