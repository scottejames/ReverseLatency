package scott.latency.test;

import static org.junit.Assert.*;

import org.junit.Test;

import scott.latency.logging.Probe;

public class TestProbe
{
	@Test
	public void canCreate()
	{
		Probe p = new Probe();
		assertNotNull(p);
		
	}
	
	
}
