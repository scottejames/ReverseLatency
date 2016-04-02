package scott.latency.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import scott.latency.logging.LogMessage;
import scott.latency.logging.MessageList;
import scott.latency.misc.SimpleObjectSerializer;
import scott.latency.test.fakes.FakeMessageWriter;

public class TestMessageList
{

	@Before
	public void before()
	{
		MessageList.clearInstance();

	}
	@Test
	public void canCreate()
	{
		FakeMessageWriter fw = new FakeMessageWriter();
		MessageList.setMessageWriter(fw);
		
		MessageList listOne = MessageList.getInstance();
		MessageList.clearInstance();
		MessageList listTwo = MessageList.getInstance();
		assertNotSame(listOne,listTwo);
		assertFalse(listOne.isRuning());
		// while we are here check we can shut it down
		listTwo.stop();
		assertFalse(listTwo.isRuning());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void canAddAMessage() throws IOException, ClassNotFoundException
	{
		FakeMessageWriter fw = new FakeMessageWriter();
		MessageList.setMessageWriter(fw);
		MessageList listOne = MessageList.getInstance();
		// I will handle the rolls thank you
		listOne.stop();
		LogMessage lm = new LogMessage("Name","Location",10,100);
		listOne.addMessage(lm);
		assertNull(fw.message);
		listOne.rollLogList();
		assertNotNull(fw.message);
		LinkedList<LogMessage> newMessageList = 
			(LinkedList<LogMessage>) SimpleObjectSerializer.deSerialize(fw.message.getBytes());
		assertNotNull(newMessageList);
		
		assertEquals(newMessageList.get(0).getIndentifier(),lm.getIndentifier());
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void canAddAManyMessage() throws IOException, ClassNotFoundException
	{
		FakeMessageWriter fw = new FakeMessageWriter();
		MessageList.setMessageWriter(fw);
		MessageList listOne = MessageList.getInstance();
		// I will handle the rolls thank you
		listOne.stop();
		for (int i = 0;i < 10; i++)
		{
			LogMessage lm = new LogMessage("Name" + i,"Location",10,100);
			listOne.addMessage(lm);
		}	
		
		listOne.rollLogList();
		assertNotNull(fw.message);
		LinkedList<LogMessage> newMessageList = 
			(LinkedList<LogMessage>) SimpleObjectSerializer.deSerialize(fw.message.getBytes());
		assertNotNull(newMessageList);
		
		// Note not an efficient way to get info from a list :-)
		for (int i = 0;i < 10; i++)
		{
			assertEquals(newMessageList.get(i).getProbeName(),"Name" + i);

		}	
		
	}
	
}
