package scott.latency.test;

import scott.latency.interfaces.IMessageReader;
import scott.latency.logging.UDPInterface;

public class TestUDPListener
{
	public static void main(String [] args) throws InterruptedException
	
	{
		IMessageReader reader = new UDPInterface(9999);
		reader.open();
		while(true)
		{
			String message = reader.read();
			System.out.println("Recieved message : " + message);
			Thread.sleep(1000);
		}
		
	}

}
