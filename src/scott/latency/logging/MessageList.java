package scott.latency.logging;

import java.io.IOException;

import org.apache.log4j.Logger;

import scott.latency.StaticConfiguration;
import scott.latency.interfaces.IMessageWriter;
import scott.latency.misc.SimpleObjectSerializer;
import scott.latency.misc.ThreadSafeList;

/**
 * Collects a list of messages @see LogMessage.
 * 
 * The message list will push messages to an IMessageWriter at a specified
 * interval (the interval controlled by
 * 
 * @see StaticConfiguration) the IMessageWriter can do what it likes with the
 * log message.
 * 
 * You can read and write to this list at the same time, the concurrency is
 * handled internally - there are no locks used though so writes will never
 * block behind a read.
 * 
 * @author sjames1
 * 
 */
public class MessageList implements Runnable
{
	private Logger l = Logger.getLogger(MessageList.class.getName());
	private static IMessageWriter _messageWriter;
	private static MessageList _instance = null;
	private ThreadSafeList<LogMessage> _list = new ThreadSafeList<LogMessage>();
	private boolean _alive = false;
	private Thread thread;

	private MessageList()
	{
		
	}
	public static void clearInstance()
	{
		if (_instance!=null)
			_instance.stop();
		if (_messageWriter!=null)
			_messageWriter.close();
		_instance = null;
	}
	public static MessageList getInstance()
	{
		if (_instance == null)
		{
			_instance = new MessageList();
			_messageWriter.open();
			_instance.start();
		}
		return _instance;
	}

	public void addMessage(LogMessage lm)
	{
		l.debug("Adding message" + lm);
		_list.add(lm);
	}

	public void run()
	{
		Thread.currentThread().setName("MessageList");
		l.info("Running MessageList Service");

		while (_alive)
		{
			try
			{
				l.debug("Sleeping for " + StaticConfiguration.getLogCycleDelay());
				Thread.sleep(StaticConfiguration.getLogCycleDelay());
				rollLogList();
			} catch (InterruptedException e)
			{
				l.error("Thread interrupted in MessageList",e);				
			}
			
		}
	}

	public void rollLogList()
	{
		try
		{
			l.info("Rolling log list there are " + _list.size() + " entries in the list");
			_list.rollList();
			
			String messageToBeWritten = new String(SimpleObjectSerializer.serialize(_list.getAll()));
			_messageWriter.write(messageToBeWritten);
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void start()
	{
		_alive = true;
		thread = new Thread(this);
		thread.start();
	}

	public void stop()
	{	
		_alive = false;
		thread.interrupt();
	}

	public static void setMessageWriter(IMessageWriter mw)
	{
		_messageWriter = mw;
	}
	public boolean isRuning()
	{
		return _alive;
	}
}
