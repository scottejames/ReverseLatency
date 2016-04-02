package scott.latency.test;

import static org.junit.Assert.*;

import org.junit.Test;

import scott.latency.NTPMessage;

public class TestNTPMessage
{

	@Test
	public void testNTPMessageIntToByteAndBack()
	{
		int i = Integer.MAX_VALUE;
		byte [] arr = new byte[10];
		NTPMessage.encodeInt(arr, 0, i);
		
		int j = NTPMessage.decodeInt(arr, 0);
		assertEquals(i,j);
		
		i = Integer.MIN_VALUE;
		arr = new byte[10];
		NTPMessage.encodeInt(arr, 0, i);
		
		j = NTPMessage.decodeInt(arr, 0);
		assertEquals(i,j);
		
		i = 0;
		arr = new byte[10];
		NTPMessage.encodeInt(arr, 0, i);
		
		j = NTPMessage.decodeInt(arr, 0);
		assertEquals(i,j);
		
	}
	
	@Test
	public void testNTPMessageLongToByteAndBack()
	{
		long i = Long.MAX_VALUE;
		byte [] arr = new byte[10];
		NTPMessage.encodeTimestamp(arr, 0, i);
		long j = NTPMessage.decodeTimestamp(arr, 0);
		assertEquals(i,j);
		
		 i = Long.MIN_VALUE;
		 arr = new byte[10];
		NTPMessage.encodeTimestamp(arr, 0, i);
		 j = NTPMessage.decodeTimestamp(arr, 0);
		assertEquals(i,j);
		
		i = 0;
		 arr = new byte[10];
		NTPMessage.encodeTimestamp(arr, 0, i);
		 j = NTPMessage.decodeTimestamp(arr, 0);
		assertEquals(i,j);
	}
	@Test
	public void testObjectToByteAndBackGivesBackOriginalObjectIsh()
	{
		NTPMessage message = new NTPMessage();
		message.setId(Integer.MAX_VALUE);
		message.setOriginateTimestamp(Long.MAX_VALUE-1);
		message.setReceiveTimestamp(Long.MAX_VALUE-2);
		message.setTransmitTimestamp(Long.MAX_VALUE-3);
		
		byte [] data = message.toByteArray();
		NTPMessage messageTwo = new NTPMessage(data);
		assertEquals(Integer.MAX_VALUE,messageTwo.getId());
		assertEquals(Long.MAX_VALUE-1,messageTwo.getOriginateTimestamp());
		assertEquals(Long.MAX_VALUE-2,messageTwo.getReceiveTimestamp());
		assertEquals(Long.MAX_VALUE-3,messageTwo.getTransmitTimestamp());
		
		
	}
}
