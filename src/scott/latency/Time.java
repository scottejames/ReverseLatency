package scott.latency;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import scott.latency.exceptions.DependencyMissing;
import scott.latency.interfaces.IOffsetCalculation;
import scott.latency.interfaces.IDynamicOffsetCalculation;
import scott.latency.interfaces.ITimeProvider;

/**
 * Time provides a the time (in millis), this time can either be local or a normalised time that 
 * corresponds to the time on a reference server.
 * 
 * This object is a singleton
 * 
 * 
 * @author sjames1
 *
 */
public class Time implements ITimeProvider
{

	private static ITimeProvider _instance;
	private Logger l = Logger.getLogger(Time.class.getName());
	private boolean _alive = false;
	
	private static IDynamicOffsetCalculation _dynamicOffsetCalculation = null;
	private static IOffsetCalculation _offsetCalculation = null;
	
	private Thread t = null;



	private Time()
	{

	}
	/**
	 * Classic singleton accessor
	 * 
	 * @return
	 */
	public synchronized static ITimeProvider getInstance()
	{
		if (_instance == null)
		{
			_instance = new Time();
		}
		return _instance;
	}
	
	/** 
	 * reset the singleton, used mostly for debugging
	 */
	public static void clearInstance()
	{
		_offsetCalculation = null;
		_dynamicOffsetCalculation = null;
		_instance = null;
	}

	/** 
	 * returns the current system time as a long
	 */
	public  long getLocalTime()
	{
		long t = System.currentTimeMillis();			
		return t;
	}

	/**
	 * returns a time normalised against a reference source
	 */
	public long getNormalisedTime() throws DependencyMissing
	{
		if (_offsetCalculation == null)
			throw new DependencyMissing("Missing _offsetCalculation calculation dependency");
		
		// skew is in seconds
		long t = System.currentTimeMillis() ;
		long offset = 0;
		
		if (_alive == false)
		{
			l.debug("Using regular offset calculation");
			offset = _offsetCalculation.calculate();
		}
		else
		{
			l.debug("Using dynamic offset calculation");
			if (_dynamicOffsetCalculation == null)
				throw new DependencyMissing("Missing _dynamicOffsetCalculation calculation dependency");		
			offset = _dynamicOffsetCalculation.getCurrentOffset();
		}
		
		long nt = t + offset;

		return nt;
	}

	/**
	 * Returns a timestamp as a formatted date/time string.  This is expensive.  
	 *   DO NOT PUT THIS CALL IN LATENCY SENSITIVE CODE
	 */
	public static String timestampToString(double timestamp)
	{
		if (timestamp == 0)
			return "0";
		double utc = timestamp / 1000 ;
		// milliseconds
		long ms = (long) (utc * 1000.0 );

		// date/time
		String date = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSSS")
				.format(new Date(ms));


		return date;
	}

	public long getSkew()
	{
		return _dynamicOffsetCalculation.getCurrentOffset();
	}

	public void start() throws DependencyMissing
	{
		l.debug("Starting time computing offset");
		if (_offsetCalculation == null)
			throw new DependencyMissing("OffsetCalculation not set");
		_alive = true;
		t = new Thread(this);
		t.start();
	}

	public void stop()
	{
		l.debug("Stopping time from computing offset, final offset set to " +
				_dynamicOffsetCalculation.getCurrentOffset());
		_alive = false;
		if (t != null)
		{
			t.interrupt();
		}
	}

	public void run()
	{
		Thread.currentThread().setName("TimeThread");
		l.info("Running Time Service");
		while (_alive)
		{
			try
			{
				long offset = _offsetCalculation.calculate();
				l.debug("Entering offset to dynamic calculation, set to " + offset);
				_dynamicOffsetCalculation.reComputeOffset(offset);
				l.info("Calculated time offset " + _dynamicOffsetCalculation.getCurrentOffset());

				Thread.sleep(StaticConfiguration.getRecalulationDelay());
			} catch (InterruptedException e)
			{
				l.error("Sleep interrupted in run method");
			} catch (DependencyMissing e)
			{
				l.error("Dependency not injected into OffsetCalculation method");
				System.exit(-1);
			}
		}
	}

	public static void setOffsetCalculation(IOffsetCalculation offsetCalculation)
	{
		Time._offsetCalculation = offsetCalculation;
	}

	public static void setDynamicOffsetCalculation(IDynamicOffsetCalculation dynOffsetCalc)
	{
		_dynamicOffsetCalculation = dynOffsetCalc;
	}

}
