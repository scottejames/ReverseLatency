package scott.latency;



import org.apache.log4j.Logger;

import scott.latency.interfaces.IMessageWriter;
import scott.latency.logging.MessageList;
import scott.latency.logging.UDPInterface;

public class StaticConfiguration
{
	private static Logger l = Logger.getLogger(StaticConfiguration.class.getName());
	
	private static int ntpPort = 9998;
	// "vsol45a-0113.eu.hedani.net";
	private static String ntpHost = "localhost";
	
	/**
	 * This allows the user to override certain configuration parameters through 
	 * arguments,  its assumed that args come from main(String [] args)
	 * currently the following args are supported
	 *  -ntp-port xxx - sets a new ntp port (used for recieve and send)
	 *  -ntp-host xxx - sets the ntp host name, only used to send the reciever is always local
	 * 	 * @param args
	 */
	public static void applyOverrides(String [] args)
	{
		
		int i = 0;
		String arg;
		
		while (i < args.length && args[i].startsWith("-"))
		{
			arg = args[i++];

			if (arg.equals("-ntp-port"))
			{
				l.info("Over-riding the port number");
				if (i < args.length)
					ntpPort = Integer.parseInt(args[i++]);
				else
					System.err.println("-port requires a number");
				l.info("overriding ntp port number = " + ntpPort);
			}
			
			if (arg.equals("-ntp-host"))
			{
				l.info("Over-riding the NTP host name");
				if (i < args.length)
					ntpHost = args[i++];
				else
					System.err.println("-port requires a number");
				l.info("overriding ntp hostname  = " + ntpHost);
			}
		}

	}
	public static String getNTPHostName()
	{	
		return ntpHost;
	}

	public static int getNTPPort()
	{
		return ntpPort;
	}

	public static long getRecalulationDelay()
	{

		return 10000;
	}

	public static long getLogCycleDelay()
	{

		return 9000;
	}

	/** 
	 * this method MUST be called before anything else it injects the dependencies into the classes
	 * that need it.
	 */
	public static void setUpStaticConfiguration()
	{
		//MessageList.setMessageWriter(new DebugMessageWriter());
		IMessageWriter writer = new UDPInterface(1999);
		MessageList.setMessageWriter(writer);
		Time.setDynamicOffsetCalculation(new SimpleDynamicOffsetCalculation());
		Time.setOffsetCalculation(new OffsetCalculation());
		OffsetCalculation.setNPTServerInterface(new UDPNTPServerInterface());
	}

	public static int setNTPServerTimeout()
	{
		return 9000;
	}

	public static String getMessageQueueName()
	{
	
		return "MyQueueNameShouldBeSet";
	}

	public static String getMessageQueueUrl()
	{
		return null;
	}

	public static String getMessageQueueUserName()
	{
		return null;
	}

	public static String getMessageQueuePassword()
	{
		return null;
	}

	public static String getEmsServerName()
	{
		return "localhost";
	}

}
