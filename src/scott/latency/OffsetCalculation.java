package scott.latency;

import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import scott.latency.exceptions.DependencyMissing;
import scott.latency.interfaces.INTPServerInterface;
import scott.latency.interfaces.IOffsetCalculation;

/***
 * A function class, this object will calculate the clock offset between the
 * localhost and a specified reference host, the host name of the reference host
 * is specified by the StaticConfiguration object.
 * 
 * After the calculate() method has been called the offset is returned, the
 * roundtripDelay and time offset can also be extracted from accessor methods.
 * 
 * @author sjames1
 * 
 */
public class OffsetCalculation implements IOffsetCalculation
{
	public Logger l = Logger.getLogger(OffsetCalculation.class.getName());

	public static INTPServerInterface _ntpInterface = null;

	public long roundTripDelay = 0;
	public long computedOffset = 0;
	public long networkLatency = 0;


	public boolean ignoreNetworkLatency = false;

	public static int id = 0;

	/**
	 * This method calculates the difference in clock times between the local
	 * machine and a known reference clock. It does this using the following
	 * algorithm
	 * 
	 * A message is constructed locally and the current time inserted (known as
	 * originateTimestamp) the message is sent to the reference server and the
	 * current time (on the reference server is added) (known as
	 * transmitTimestamp) the message is returned to the local process and the
	 * time once again added (known as receiveTimestamp)
	 * 
	 * The network time is computed: originateTimestamp - receiveTimestamp
	 * 
	 * Therefore the network latency is half the network time (assuming a
	 * symmetric network connection)
	 * 
	 * The clock skew can therefore be computed by subtracting the remote time
	 * from the local time and adding the transmitTimestamp - networkLatency) -
	 * rriginateTimestamp
	 * 
	 * The code has been optimised to reduce the operations between the time
	 * stamps but of course the network latency includes some time spent
	 * performing java operations, this should be near constant and can be
	 * disregarded.
	 * 
	 * The message is defined in @see(NTPMessage)
	 * 
	 * NTPServerInterface is injected into the class to provide the transport to
	 * the NTPServer, currently a UDP implementation is provided.
	 * 
	 * @return the clock skew
	 * @throws DependencyMissing
	 *             thrown if no NTPServerInterface is supplied
	 */
	public long calculate() throws DependencyMissing
	{

		// The NTP interface is injected in prior to execution to send messages
		// to the time server
		if (_ntpInterface == null)
			throw new DependencyMissing(
					"NTP Interface dependency was not injected into CalculateOffset");
		_ntpInterface.open();

		NTPMessage message = new NTPMessage();
		message.setId(id++);

		long originateTime = Time.getInstance().getLocalTime();
		message.setOriginateTimestamp(originateTime);

		try
		{
			// Send the message then wait for a response.
			_ntpInterface.send(message);
			message = _ntpInterface.recieve();

			long receiveTimestamp = Time.getInstance().getLocalTime();
			message.setReceiveTimestamp(receiveTimestamp);

		} catch (TimeoutException e)
		{
			l
					.error("TIMEOUT waiting for response NTP server returning previous offset");
			return computedOffset;

		}
		if (ignoreNetworkLatency == false)
		{
			roundTripDelay = message.getReceiveTimestamp()
					- message.getOriginateTimestamp();
			networkLatency = roundTripDelay / 2;
		} else
		{
			l.debug("Ignoring network latency");
			networkLatency = 0;
		}

		computedOffset = (message.getTransmitTimestamp() - networkLatency)
				- message.getOriginateTimestamp();

		l.debug("Message " + message);

		l.debug("C roundTripDelay time: " + roundTripDelay);
		l.debug("C networkLatency time: " + networkLatency);
		l.debug("C computedOffset time: " + computedOffset);

		_ntpInterface.close();

		return computedOffset;
	}

	/**
	 * The time taken to send a packet from local host to the reference host.
	 * Only populated after calculate is called.
	 * 
	 * @return time
	 */
	public long getNetworkLatency()
	{
		return networkLatency;
	}

	/**
	 * The time taken to send a packet from the local host to the reference host
	 * and back. Only populated after calculate is called.
	 * 
	 * @return time
	 */
	public long getRoundTripDelay()
	{
		return roundTripDelay;
	}

	/**
	 * The difference between the local and remote clocks,  in effect the amount of milli seconds
	 * that need to be added to the local clock to match the time on the reference host.
	 * @return time
	 */
	public long getComputedOffset()
	{
		return computedOffset;
	}
	/**
	 * Injects a dependency,  the INTPServerInterface is an interface that provides a mechanism
	 * to send messages to the NTP server,   currently only UDP is supported
	 * @param interface
	 */
	public static void setNPTServerInterface(INTPServerInterface i)
	{
		_ntpInterface = i;
	}

	public static INTPServerInterface getNPTServerInterface()
	{
		return _ntpInterface;
	}

	/** 
	 * Used mostly for debugging,   the network latency is assumed to be zero.
	 * @return
	 */
	public boolean isIgnoreNetworkLatency()
	{
		return ignoreNetworkLatency;
	}

	public void setIgnoreNetworkLatency(boolean ignoreNetworkLatency)
	{
		this.ignoreNetworkLatency = ignoreNetworkLatency;
	}

}
