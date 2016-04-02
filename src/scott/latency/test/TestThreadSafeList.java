package scott.latency.test;

import static org.junit.Assert.*;

import org.junit.Test;

import scott.latency.misc.ThreadSafeList;

public class TestThreadSafeList
{
	@Test
	public void canCreateMe()
	{
		ThreadSafeList<String> foo = new ThreadSafeList<String>();
		assertNotNull(foo);
	}
	
	@Test
	public void canAddAnItem()
	{
		ThreadSafeList<String> foo = new ThreadSafeList<String>();
		assertNotNull(foo);
		foo.add("Test");
		String nullString = null;
		try
		{
			nullString = foo.get(0);
		}
		catch (IndexOutOfBoundsException e)
		{}
		
		assertNull(nullString);
		foo.rollList();
		String notNullString = foo.get(0);
		assertNotNull(notNullString);

	}
	
	@Test
	public void canAddSeveralItems()
	{
		ThreadSafeList<String> foo = new ThreadSafeList<String>();
		assertNotNull(foo);
		
		for (int i = 0 ; i < 10 ; i ++)
		{
			foo.add("" + i);
		}
		
		foo.rollList();

		for (int i = 0 ; i < 10 ; i ++)
		{
			assertEquals(foo.get(i),("" + i));
		}
		

	}
	
	@Test
	public void canAddSeveralItemsAfterAroll()
	{
		ThreadSafeList<String> foo = new ThreadSafeList<String>();
		assertNotNull(foo);
		// Firstly add 10 items
		for (int i = 0 ; i < 10 ; i ++)
		{
			foo.add("" + i);
		}
		
		foo.rollList();
		// We have rolled so if I add more they will not ovewrite until I roll again
		for (int i = 0 ; i < 10 ; i ++)
		{
			foo.add("NEXT" + i);
		}
		// Check by trying to get the original 10 items
		for (int i = 0 ; i < 10 ; i ++)
		{
			assertEquals(foo.get(i),("" + i));
		}
		foo.rollList();
		// Roll and get the new onces
		for (int i = 0 ; i < 10 ; i ++)
		{
			assertEquals(foo.get(i),("NEXT" + i));
		}
		

	}

}
