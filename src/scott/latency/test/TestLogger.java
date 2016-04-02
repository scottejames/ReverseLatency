package scott.latency.test;


import org.apache.log4j.Logger;
import org.junit.Test;

import scott.latency.server.UDPMessageLogger;

public class TestLogger
{
	@Test
	public void testLogMessagee()
	{
		Logger l = Logger.getLogger(TestLogger.class.getName());
		l.debug("This is a log message");
	}

}
