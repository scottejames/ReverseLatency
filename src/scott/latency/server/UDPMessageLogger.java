package scott.latency.server;

import java.util.LinkedList;

import org.apache.log4j.Logger;


import scott.latency.interfaces.IMessageReader;
import scott.latency.logging.LogMessage;
import scott.latency.logging.UDPInterface;
import scott.latency.misc.SimpleObjectSerializer;

public class UDPMessageLogger
{

	   @SuppressWarnings("unchecked")
		public static void main(String [] args) throws Exception
		    {
				Logger l = Logger.getLogger(UDPMessageLogger.class.getName());

				IMessageReader reader = new UDPInterface(1999);

				reader.open();
				
				while (true)
				{
					l.info("waiting for message");
					String message = reader.read();
					l.info("Message incomming");
					// Convert message to LinkedList
					
					LinkedList<LogMessage> listOfMessages = (LinkedList<LogMessage>) SimpleObjectSerializer.deSerialize(message.getBytes());
					for (LogMessage logMessage: listOfMessages)
					{
						l.info("MESSAGE: " + logMessage);
					}
					
				}
		    	
		           
//				reader.close();
		    }
}
