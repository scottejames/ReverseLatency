package scott.latency.test.fakes;

import scott.latency.exceptions.DependencyMissing;
import scott.latency.interfaces.IOffsetCalculation;

public class FakeOffsetCalculation implements IOffsetCalculation
{
	boolean calculateCalled = false;
	
	public long offset = 0;
	public long netLatency = 0;
	public long netRoundTrip = 0;
	
	public long calculate() throws DependencyMissing
	{
		calculateCalled = true;
		return offset;
	}

	public long getComputedOffset()
	{
		if (calculateCalled == false)
			return 0;
		else return offset;
		
	}

	public long getNetworkLatency()
	{
		if (calculateCalled == false)
			return 0;
		else return netLatency;
	}

	public long getRoundTripDelay()
	{
		if (calculateCalled == false)
			return 0;
		else return netRoundTrip;
	}

}
