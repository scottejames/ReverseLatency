package scott.latency.test;

import static org.junit.Assert.*;

import org.junit.Test;

import scott.latency.logging.LogMessage;

public class TestLogMessage
{
	@Test
	public void canCreate()
	{
		LogMessage message = new LogMessage(null, null, 0, 0);
		assertNotNull(message);
	}

}
