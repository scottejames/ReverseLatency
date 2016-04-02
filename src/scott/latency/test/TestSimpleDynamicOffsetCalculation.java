package scott.latency.test;

import static org.junit.Assert.*;

import org.junit.Test;

import scott.latency.SimpleDynamicOffsetCalculation;

public class TestSimpleDynamicOffsetCalculation
{
	@Test
	public void canConstruct()
	{
		SimpleDynamicOffsetCalculation sdoc = new SimpleDynamicOffsetCalculation();
		assertNotNull(sdoc);
		
	}
	@Test
	public void canOffSetCalcWorks()
	{
		SimpleDynamicOffsetCalculation sdoc = new SimpleDynamicOffsetCalculation();
		sdoc.reComputeOffset(100);
		assertEquals(sdoc.getCurrentOffset(),100);
	}
	@Test
	public void canAvgCalcWorks()
	{
		SimpleDynamicOffsetCalculation sdoc = new SimpleDynamicOffsetCalculation();
		sdoc.reComputeOffset(100);
		assertEquals(sdoc.getAvgOffset(),100);
		sdoc.reComputeOffset(100);
		assertEquals(sdoc.getAvgOffset(),100);
		sdoc.reComputeOffset(200);
		assertEquals(sdoc.getAvgOffset(),(100 + 100 + 200) / 3);
	}
}
