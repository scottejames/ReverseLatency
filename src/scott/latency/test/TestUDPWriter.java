package scott.latency.test;

import scott.latency.interfaces.IMessageReader;
import scott.latency.interfaces.IMessageWriter;
import scott.latency.logging.UDPInterface;

public class TestUDPWriter
{
	public static void main(String [] args)
	
	{
		IMessageWriter writer = new UDPInterface(9999);
		writer.open();
		while(true)
		{
			String message = "FooBar";
			writer.write(message);
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
