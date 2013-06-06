package com.amazon;

import com.google.common.collect.ImmutableSet;

public interface IReportWriter {
	void processReport(ImmutableSet<ConnectionInformation> connections);
}
