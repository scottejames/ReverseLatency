package scott.latency.test;

import static org.junit.Assert.*;

import org.junit.Test;

import scott.latency.logging.EmsInterface;

public class TestEMSConnectivity
{
	@Test
	public void canPostMessageToQueue()
	{
		EmsInterface e = new EmsInterface("Foo");
		e.open();
		e.write("BOBAGE");
		e.close();
		
	}
	
	@Test
	public void canReadMessageFromQueue()
	{
		EmsInterface e = new EmsInterface("MyQueueTwo");
		e.open();
		e.write("BOB");
		String message = e.read();

		e.close();
		assertTrue(message.equals("BOB"));
		
	}
}
