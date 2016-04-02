package scott.latency.test.fakes;

import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import scott.latency.NTPMessage;
import scott.latency.interfaces.INTPServerInterface;

public class FakeNTPServerInterface implements INTPServerInterface
{
	@SuppressWarnings("unused")
	private Logger l = Logger.getLogger(FakeNTPServerInterface.class.getName());

	public boolean openCalled = false;
	public boolean closeCalled = false;
	public NTPMessage message = null;
	public int offSet = 0;
	public int networkLatency = 0;
	private boolean useTimeServerToAddOriginateTime = false;
	
	public void close()
	{
		closeCalled = true;		
	}

	public void open()
	{
		openCalled = true;
		
	}

	public NTPMessage recieve() throws TimeoutException
	{
		if (networkLatency > 0)
		{
			try
			{
				Thread.sleep(networkLatency);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		NTPMessage result;

			this.message.setTransmitTimestamp(this.message.getOriginateTimestamp() + offSet);
			result = this.message;

		return result;
	
	}

	public void send(NTPMessage message)
	{
		this.message = message; 
	}

	public void setUseTimeServerToAddOriginateTime(
			boolean useTimeServerToAddOriginateTime)
	{
		this.useTimeServerToAddOriginateTime = useTimeServerToAddOriginateTime;
	}

	public boolean isUseTimeServerToAddOriginateTime()
	{
		return useTimeServerToAddOriginateTime;
	}

}
