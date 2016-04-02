package scott.latency.interfaces;

public interface IDynamicOffsetCalculation {

	public long getCurrentOffset();
	public long reComputeOffset(long offset);
	public double getAvgOffset();

	
}
