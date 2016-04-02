package scott.latency.test;

import static org.junit.Assert.*;

import org.junit.Test;

import scott.latency.misc.ApplicationProperties;

public class TestApplicationProperties
{
	@Test
	public void testCanGetProperty()
	{
		assertNotNull(ApplicationProperties.getInstance().getConnectionFactoryName());
	}

}
