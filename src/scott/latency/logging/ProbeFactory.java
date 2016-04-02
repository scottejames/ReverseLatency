package scott.latency.logging;

import java.util.HashMap;

import org.apache.log4j.Logger;

import scott.latency.Time;
import scott.latency.exceptions.DependencyMissing;
import scott.latency.interfaces.ITimeProvider;

public class ProbeFactory
{
	private Logger l = Logger.getLogger(ProbeFactory.class.getName());

	private static ProbeFactory _instance = null;
	
	private HashMap<String, Probe> _map = new HashMap<String, Probe>();
	private ITimeProvider _time = null;
	private ProbeFactory()
	{
		l.debug("Starting probe factory, running time");
		_time = Time.getInstance();
		try
		{
			_time.start();
		} catch (DependencyMissing e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ProbeFactory getInstance()
	{
		if (_instance == null)
		{
			_instance = new ProbeFactory();			
		}
		return _instance;
	}

	public  Probe createProbe(String name)
	{
		Probe result = _map.get(name);
		
		if (result == null)
		{
			result = new Probe()
				.setName(name)
				.setTime(_time);

			_map.put(name, result);
		} 
		return result;
	
	}
}
