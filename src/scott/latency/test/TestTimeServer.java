package scott.latency.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import scott.latency.NTPMessage;
import scott.latency.StaticConfiguration;
import scott.latency.Time;
import scott.latency.server.TimeServer;

public class TestTimeServer
{
	@Test 
	public void canCreateATimeServer()
	{
		TimeServer ts = new TimeServer();
		assertNotNull(ts);
	}
	
	@Test
	public void sendSingleMessageToServerBeSureItStops() throws InterruptedException
	{
	
		TimeServer ts = new TimeServer();
		ts.setProcessMessageCountLimit(1);
		Thread t = new Thread(ts);
		t.start();
		
		NTPMessage message = new NTPMessage();
		byte[] data= message.toByteArray();
		Thread.sleep(100); // ensure the Time Server thread has time to react
		assertTrue(ts.isRunning());
		TestUtilities.sendDataToUdpSocket(data,"localhost",StaticConfiguration.getNTPPort());
		Thread.sleep(200);// ensure the Time Server thread has time to react
		assertFalse(ts.isRunning());
		assertFalse(t.isAlive());		
	}
	
	
	@Test
	public void sendSeveralMessagesToServer() throws InterruptedException
	{
		int messageCount = 10;
		TimeServer ts = new TimeServer();
		ts.setProcessMessageCountLimit(messageCount);
		Thread t = new Thread(ts);
		t.start();
		Thread.sleep(100);// ensure the Time Server thread has time to react
		assertTrue(ts.isRunning());
		for (int i = 0 ; i < messageCount ;i++)
		{
			NTPMessage message = new NTPMessage();
			message.setOriginateTimestamp(69);
			byte[] data= message.toByteArray();			
			
			long timeStamp = Time.getInstance().getLocalTime();
			byte [] results = 
				TestUtilities.sendDataToUdpSocketWaitForReply(data,"localhost",StaticConfiguration.getNTPPort());
			NTPMessage resultMessage = new NTPMessage(results);
		
			// The resultant message should have a timestamp which is a similar time
			// to the time before the message was sent.  The Originate time stamp should
			// be preserved.  The recieveTimeStamp should be unset.
			
			assertNotNull(resultMessage.getTransmitTimestamp());
			assertEquals(resultMessage.getOriginateTimestamp(),69);
			assertEquals(resultMessage.getTransmitTimestamp(),timeStamp,100);
			assertEquals(resultMessage.getReceiveTimestamp(),0);
			
		}
	
		Thread.sleep(200);// ensure the Time Server thread has time to react
		assertFalse(ts.isRunning());
		assertFalse(t.isAlive());		
	}
	

}
