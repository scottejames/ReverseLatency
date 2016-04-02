package scott.latency.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import scott.latency.OffsetCalculation;
import scott.latency.exceptions.DependencyMissing;
import scott.latency.interfaces.IOffsetCalculation;
import scott.latency.test.fakes.FakeNTPServerInterface;



public class TestCalculateOffset 
{


	@Test
	public void testCreateObject()
	{
		IOffsetCalculation co = new OffsetCalculation();
		assertNotNull(co);
	}
	
	@Test
	public void testInjectDependency()
	{

		// Check that we dont have a dependency set
		assertNull(OffsetCalculation.getNPTServerInterface());
		// check that creating an object does not set the dependency
		@SuppressWarnings("unused")
		IOffsetCalculation co = new OffsetCalculation();
		assertNull(OffsetCalculation.getNPTServerInterface());
		
		FakeNTPServerInterface fake = new FakeNTPServerInterface();
		OffsetCalculation.setNPTServerInterface(fake);
		assertEquals(fake, OffsetCalculation.getNPTServerInterface());
	}
	
	
	@Test
	public void testGetZeroResultsIfNotCalculated()
	{

		IOffsetCalculation co = new OffsetCalculation();
		assertEquals(co.getComputedOffset(),0);
		assertEquals(co.getRoundTripDelay(),0);
	}
	

	
	@Test
	public void testFailWithNoDependencies()
	{

		// this should be put in @BEfore but for some reason its not work grr
		OffsetCalculation.setNPTServerInterface(null);
		OffsetCalculation co = new OffsetCalculation();		
		try
		{
			@SuppressWarnings("unused")
			double result = co.calculate();
		} catch (DependencyMissing e)
		{
			// This is the correct path .. the error hear is no exception thrown
			return;
		}
		assertNotNull(null);
		
	}
	
	@Test(timeout=200)
	public void testWithFakeTimeServerWithNoNetworkLatency() throws DependencyMissing
	{
			
		// With this test the network latency (which will in fact be code latency) is ignored
		// The only offset taken into consideration is the clock skew.  This clock skew is
		// injected into the calculation via teh FakeNTP interface so the result should be known.
		OffsetCalculation co = new OffsetCalculation();
		FakeNTPServerInterface server = new FakeNTPServerInterface();
		server.offSet = 100;
		OffsetCalculation.setNPTServerInterface(server);
		co.setIgnoreNetworkLatency(true);
		
		try
		{
			long result = co.calculate();
			assertEquals(result,server.offSet);
			assertTrue(server.closeCalled);
			assertTrue(server.closeCalled);
		} catch (DependencyMissing e)
		{
			return;
		}
	
	}
	
	@Test
	public void testWithFakeTimeServerWithNetworkLatency() throws DependencyMissing
	{
		// This test duplicates that of above but turns the network latency check on
		// To manage this the calcualtions are performed twice once flat then once with a
		// Thread sleepw ithin teh FakeNTP server to simulate a network hop.  The first
		// calculation provides a baseline once this is subtracted from teh second
		// the only skew shoudl be cause by the thread sleep.
		
		OffsetCalculation co = new OffsetCalculation();
		FakeNTPServerInterface server = new FakeNTPServerInterface();
		server.offSet = 100;
		
		OffsetCalculation.setNPTServerInterface(server);
		co.setIgnoreNetworkLatency(false);
		long baselineResult = co.calculate();
		long baselineNetworkLatency = co.getNetworkLatency();
		
		// as we have no network hop latency should be under 100 millis frankly it should 
		// be a LONG way less than this
		assertTrue(baselineNetworkLatency < 100);
		
		// we know the clock skew is offSet so removing the observed latency should
		// return the result
		assertEquals(baselineResult, (server.offSet - baselineNetworkLatency));
		
		// Lets do it again but introduce some latency
		server.networkLatency = 1000;
		
		long result = co.calculate();
		long networkLatency = co.getNetworkLatency();
		long roundTrip = co.getRoundTripDelay();
		long offset = co.getComputedOffset();
		
		// check semantics
		assertEquals(result,offset);
		assertEquals((double)(roundTrip / 2), (double)networkLatency,0.01);
		
		long impliedLatency = networkLatency - baselineNetworkLatency;			
		assertEquals( impliedLatency, server.networkLatency/2,10);

	}
	
	
	

//	@Test(timeout=200)
//	public void testWithRealTimeServer() throws DependencyMissing
//	{
//
//		
//		UDPNTPServerInterface udpServerInterface = new UDPNTPServerInterface();
//		udpServerInterface.setPort(9998);
//		udpServerInterface.setServerName("159.156.162.154");
//		CalculateOffset co = new CalculateOffset();
//		CalculateOffset.setNPTServerInterface(udpServerInterface);
//		
//		try
//		{
//			double result = co.calculate();
//			assertEquals(0.0,result,0.1);
//		} catch (DependencyMissing e)
//		{
//			return;
//		}
//	
//	}
	
}
