package scott.latency.interfaces;

import scott.latency.exceptions.DependencyMissing;

public interface IOffsetCalculation
{
	
	public long calculate() throws DependencyMissing;
	public long getNetworkLatency();
	public long getRoundTripDelay();
	public long getComputedOffset();
}
