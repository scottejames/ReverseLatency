package scott.latency.logging;

import java.io.Serializable;

import scott.latency.Time;

/**
 * Wrap class that encapsulates a log message
 * 
 * @author sjames1
 * 
 */

public class LogMessage implements Serializable
{	

	private static final long serialVersionUID = 1L;
	private String _probeName;
	private String _location;
	private int _indentifier;
	private double _time;

	public LogMessage(String probeName, String location, int identifier,
			double t)
	{
		setProbeName(probeName);
		setLocation(location);
		setIndentifier(identifier);
		setTime(t);
	}

	public String toString()
	{
		return "[ " + getIndentifier() + " " + getProbeName() + "@" + getLocation() + "["
				+ Time.timestampToString(getTime()) + "]";
	}

	public void setProbeName(String _probeName)
	{
		this._probeName = _probeName;
	}

	public String getProbeName()
	{
		return _probeName;
	}

	public void setLocation(String _location)
	{
		this._location = _location;
	}

	public String getLocation()
	{
		return _location;
	}

	public void setIndentifier(int _indentifier)
	{
		this._indentifier = _indentifier;
	}

	public int getIndentifier()
	{
		return _indentifier;
	}

	public void setTime(double _time)
	{
		this._time = _time;
	}

	public double getTime()
	{
		return _time;
	}

	

}
