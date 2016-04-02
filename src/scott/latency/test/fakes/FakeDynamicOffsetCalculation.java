package scott.latency.test.fakes;

import scott.latency.interfaces.IDynamicOffsetCalculation;

public class FakeDynamicOffsetCalculation implements IDynamicOffsetCalculation
{

	public long offset = 0;
	public boolean ignoreUpdates = true;
	
	public long getCurrentOffset()
	{
		return offset;
	}

	public long reComputeOffset(long offset)
	{
		if (ignoreUpdates == true)
			return offset;
		else
			return this.offset=offset;
	}

	public double getAvgOffset()
	{
		return offset;
	}

}
