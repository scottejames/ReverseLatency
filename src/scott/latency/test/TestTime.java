package scott.latency.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import scott.latency.StaticConfiguration;
import scott.latency.Time;
import scott.latency.exceptions.DependencyMissing;
import scott.latency.interfaces.ITimeProvider;
import scott.latency.test.fakes.FakeOffsetCalculation;
import scott.latency.test.fakes.FakeDynamicOffsetCalculation;

public class TestTime
{

	@Before
	public void before()
	{
		Time.clearInstance();
	}
	@Test
	public void testCanConstructAndClear()
	{
		ITimeProvider t1 = Time.getInstance();
		assertNotNull(t1);
		Time.clearInstance();
		ITimeProvider t2 = Time.getInstance();
		assertNotNull(t2);
		assertNotSame(t1,t2);
	}
	
	@Test
	public void canGetLocalTime()
	{
		long time = Time.getInstance().getLocalTime();
		assertEquals(time,System.currentTimeMillis(),10);
	}
	
	@Test
	public void canGetLocalTimeWithOffsetCalcSet()
	{
		FakeOffsetCalculation offsetCalc = new FakeOffsetCalculation();
		offsetCalc.offset = 1000;
		Time.setOffsetCalculation(offsetCalc);
		
		long time = Time.getInstance().getLocalTime();
		assertEquals(time,System.currentTimeMillis(),10);
	}
	
	@Test
	public void canGetRemoteTimeWithOffsetCalcSet() throws DependencyMissing
	{
		FakeOffsetCalculation offsetCalc = new FakeOffsetCalculation();
		offsetCalc.offset = 1000;
		Time.setOffsetCalculation(offsetCalc);
	
		long time = Time.getInstance().getNormalisedTime();
		assertEquals(time,System.currentTimeMillis()+offsetCalc.offset,10);
	}
	
	@Test
	public void ensureDynamicOffsetCalcIsIgnoredIfNotRunning() throws DependencyMissing, InterruptedException
	{
		FakeOffsetCalculation offsetCalc = new FakeOffsetCalculation();
		offsetCalc.offset = 1000;
		Time.setOffsetCalculation(offsetCalc);
		FakeDynamicOffsetCalculation dynOffsetCalc = new FakeDynamicOffsetCalculation();
		dynOffsetCalc.offset = 2000;
		Time.setDynamicOffsetCalculation(dynOffsetCalc);
		
		long time = Time.getInstance().getNormalisedTime();
		assertEquals(time,System.currentTimeMillis()+offsetCalc.offset,10);
		Time.getInstance().start();
		Thread.sleep(100);
		time = Time.getInstance().getNormalisedTime();
		assertEquals(time,System.currentTimeMillis()+dynOffsetCalc.offset,10);
		Time.getInstance().stop();
		
	}
	
	@Test
	public void remoteTimeFailsWithoutSkewCalc()  
	{
		
		try
		{
			Time.getInstance().getNormalisedTime();	
		}
		catch (DependencyMissing e)
		{
			return;
		}
		assertNotNull(null);
		
	}
	
	@Test
	public void testRuningTimeReactsToSkewChanges() throws DependencyMissing, InterruptedException
	{
		FakeDynamicOffsetCalculation offsetCalculation = new FakeDynamicOffsetCalculation();
		FakeOffsetCalculation offset = new FakeOffsetCalculation();
		
		offsetCalculation.offset = 1000;
		Time.setDynamicOffsetCalculation(offsetCalculation);
		Time.setOffsetCalculation(offset);
		
		Time.getInstance().start();
		
		long time = Time.getInstance().getNormalisedTime();
		assertEquals(time,System.currentTimeMillis()+offsetCalculation.offset,10);
		offsetCalculation.offset = 2000;
		Thread.sleep(StaticConfiguration.getRecalulationDelay());
		time = Time.getInstance().getNormalisedTime();
		assertEquals(time,System.currentTimeMillis()+offsetCalculation.offset,10);
		
		Time.getInstance().stop();

		
	}
	
	

	
}
