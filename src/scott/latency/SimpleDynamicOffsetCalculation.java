package scott.latency;

import org.apache.log4j.Logger;

import scott.latency.interfaces.IDynamicOffsetCalculation;

// TODO: this class is not thread safe - the skew and min/max/avg may be updated seperatly
public class SimpleDynamicOffsetCalculation implements IDynamicOffsetCalculation
{
	private long _currentOffset = 0;
	private long _maxOffset = 0;
	private long _minOffset = 0;
	private long _totalOffset = 0;
	private int _offsetPointCount = 0;
	
	@SuppressWarnings("unused")
	private Logger l = Logger.getLogger(SimpleDynamicOffsetCalculation.class.getName());


	public double getAvgOffset()
	{
		return _totalOffset / _offsetPointCount;
	}

	public long getCurrentOffset() {
		return _currentOffset;
	}

	public long reComputeOffset(long offset) 
	{
		_currentOffset = offset;
		_totalOffset += _currentOffset;
		_offsetPointCount++;
		
		if (_currentOffset < _minOffset)
			_minOffset = _currentOffset;
		if (_currentOffset > _maxOffset)
			_maxOffset = _currentOffset;
		return _currentOffset;
	}


	
	public String toString()
	{
		return "[ SK: " + _currentOffset + " MX: " + _maxOffset + " MX: " + _minOffset + " AX: " + getAvgOffset() + " ]";
	}
}
