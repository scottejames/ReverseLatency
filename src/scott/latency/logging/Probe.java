package scott.latency.logging;

import org.apache.log4j.Logger;

import scott.latency.exceptions.DependencyMissing;
import scott.latency.interfaces.ITimeProvider;


public class Probe
{	private Logger l = Logger.getLogger(Probe.class.getName());


	private ITimeProvider _time;
	private String _name;

	public Probe()
	{
		_name = "empty";
	}
	public Probe(String name)
	{
		_name = name;
	}
	
	public Probe setName(String name)
	{
		this._name = name;
		return this;
	}
	public Probe setTime(ITimeProvider time)
	{
		_time = time;
		return this;
		
	}
	public void mark(String key)
	{
		mark(key, 0);
	}

	public void mark(String key, int identifier)
	{
		l.info("Marking " + key + " / " + identifier);
		double t = 0;
		try
		{
			t = _time.getNormalisedTime();
		} catch (DependencyMissing e)
		{
			
			e.printStackTrace();
		}
		LogMessage lm = new LogMessage(_name, key, identifier, t);
		MessageList.getInstance().addMessage(lm);

	}

}
