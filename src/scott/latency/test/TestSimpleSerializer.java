package scott.latency.test;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import scott.latency.misc.SimpleObjectSerializer;

class TestSerialise implements Serializable
{
	private static final long serialVersionUID = 2L;
	public int i = 0;
	public int j = 0;
	public TestSerialise(int i, int j){this.i = i; this.j = j;};
}
public class TestSimpleSerializer
{
	
	
	@Test
	public void seralizeString() throws Exception
	{
		String s = "Foo";
	
		
		byte[] result = SimpleObjectSerializer.serialize(s);
		String sTwo = (String) SimpleObjectSerializer.deSerialize(result);
		
		assertTrue(s.equals(sTwo));
	
	}

	
	@Test
	public void seralizeASimpleObject() throws Exception
	{
		TestSerialise s = new TestSerialise(10,20);
		
		
		byte[] result = SimpleObjectSerializer.serialize(s);
		TestSerialise sTwo = (TestSerialise) SimpleObjectSerializer.deSerialize(result);
		
		assertEquals(sTwo.i,s.i);
		assertEquals(sTwo.j,s.j);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void seralizeCollection() throws Exception
	{
		ArrayList<TestSerialise> l = new ArrayList<TestSerialise>();
		for (int i = 0;i < 10; i++)
			l.add(new TestSerialise(i,i+1));

		byte[] result = SimpleObjectSerializer.serialize(l);
		ArrayList<TestSerialise> lTwo = 
			(ArrayList<TestSerialise>) SimpleObjectSerializer.deSerialize(result);
		assertEquals(lTwo.size(),10);
		for (int i = 0;i < 10; i++)
			assertEquals(lTwo.get(i).i, i);
		
	}
	
	@Test
	public void seralizeArray() throws Exception
	{
		TestSerialise[] l = new TestSerialise[10];
		for (int i = 0;i < 10; i++)
			l[i]=(new TestSerialise(i,i+1));

		byte[] result = SimpleObjectSerializer.serialize(l);
		TestSerialise[] lTwo = 
			(TestSerialise[]) SimpleObjectSerializer.deSerialize(result);
		
		assertEquals(lTwo.length,10);
		for (int i = 0;i < 10; i++)
			assertEquals(lTwo[i].i, i);
		
	}
	
	
}
