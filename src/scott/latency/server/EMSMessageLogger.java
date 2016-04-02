package scott.latency.server;


import java.util.LinkedList;

import org.apache.log4j.Logger;

import scott.latency.StaticConfiguration;
import scott.latency.interfaces.IMessageReader;
import scott.latency.logging.EmsInterface;
import scott.latency.logging.LogMessage;
import scott.latency.misc.SimpleObjectSerializer;

public class EMSMessageLogger
{
	   @SuppressWarnings("unchecked")
	public static void main(String [] args) throws Exception
	    {
			Logger l = Logger.getLogger(EMSMessageLogger.class.getName());

			IMessageReader reader = new EmsInterface(StaticConfiguration.getMessageQueueName());
    		l.debug("Setting up EMS queue to listen too");

			reader.open();
			
			while (true)
			{
				String message = reader.read();
				
				// Convert message to LinkedList
				
				LinkedList<LogMessage> listOfMessages = (LinkedList<LogMessage>) SimpleObjectSerializer.deSerialize(message.getBytes());
				for (LogMessage logMessage: listOfMessages)
				{
					l.debug("MESSAGE: " + logMessage);
				}
				
			}
	    	
	           
//			reader.close();
	    }
}
