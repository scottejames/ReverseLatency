package scott.latency.test.fakes;

import org.apache.log4j.Logger;

import scott.latency.interfaces.IMessageWriter;
import scott.latency.logging.LogMessage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DebugMessageWriter implements IMessageWriter
{
	private Logger l = Logger.getLogger(DebugMessageWriter.class.getName());

	public void write(LogMessage message) {

	}

	public void write(LogMessage[] arrayOfMessages)
	{
		for(LogMessage message: arrayOfMessages)
			l.debug(message.toString());
	}

	public void close()
	{
				
	}

	public void open()
	{
		
		
	}

	public void write(String[] arrayOfMessages)
	{
		throw new NotImplementedException();

		
	}

	public void write(String message)
	{
		throw new NotImplementedException();

		
	}

}
