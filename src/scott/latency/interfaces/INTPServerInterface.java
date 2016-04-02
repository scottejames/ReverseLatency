package scott.latency.interfaces;

import java.util.concurrent.TimeoutException;

import scott.latency.NTPMessage;

public interface INTPServerInterface
{
	public void send(NTPMessage message);
	public NTPMessage recieve() throws TimeoutException;
	public void open();
	public void close();
	
}
