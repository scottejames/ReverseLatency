package scott.latency.logging;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import scott.latency.StaticConfiguration;
import scott.latency.interfaces.IMessageReader;
import scott.latency.interfaces.IMessageWriter;
import scott.latency.misc.ApplicationProperties;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class EmsInterface implements IMessageWriter, IMessageReader
{

	private Logger l = Logger.getLogger(EmsInterface.class.getName());
	String serverUrl = StaticConfiguration.getMessageQueueUrl();;
	String userName = StaticConfiguration.getMessageQueueUserName();;
	String password = StaticConfiguration.getMessageQueuePassword();
	String queueName = null;
	
	// create the JNDI initial context.
	InitialContext context;
	QueueConnectionFactory factory = null;
	QueueConnection connection = null;
	QueueSession session = null;
	javax.jms.Queue queue = null;
	QueueSender sender = null;
	QueueReceiver receiver = null;

	public EmsInterface(String queueName)
	{
		this.queueName = queueName;

	}

	public void open()
	{
		try
		{
			try
			{
				context = new InitialContext();

				factory = (QueueConnectionFactory) context
						.lookup(ApplicationProperties.getInstance().getConnectionFactoryName());
			} catch (NamingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			l.info("About to create queue connection");
			if (factory == null)
			{
				l.error("Unable to create factory, fatal error death soon");
				System.exit(-1);
			}
			connection = factory.createQueueConnection();
			l.info("About to create queue session");

			session = connection.createQueueSession(false,
					javax.jms.Session.AUTO_ACKNOWLEDGE);
			
			l.info("About to create queue " + queueName);

			queue = session.createQueue(queueName);
			l.info("About to create sender");

			sender = session.createSender(queue);
			receiver = session.createReceiver(queue);
			l.info("EMS setup starting connection");
		
            connection.start();
			l.info("EMSrunning");


		} catch (JMSException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void close()
	{
		// close the context
		if (context != null)
		{
			try
			{
				context.close();
			} catch (NamingException exception)
			{
				exception.printStackTrace();
			}
		}

		// close the connection
		if (connection != null)
		{
			try
			{
				connection.close();
			} catch (JMSException exception)
			{
				exception.printStackTrace();
			}
		}

	}

	public void write(String[] arrayOfMessages)
	{
		throw new NotImplementedException();

	}

	public void write(String message)
	{
		try
		{
			l.debug("About to create JMS message");
			// TODO more efficient to use bytes message
			javax.jms.TextMessage jmsMessage = session.createTextMessage();
			jmsMessage.setText(message);
			l.debug("About to send JMS message");

			sender.send(jmsMessage);
			l.debug("Sending message list: " + message);

		} catch (JMSException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String read()
	{
		String result = null;
		try
		{
			l.debug("Waiting for message");
			javax.jms.TextMessage jmsMessage = (javax.jms.TextMessage) receiver
					.receive();

			if (jmsMessage != null)
				result = jmsMessage.getText();

		} catch (JMSException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String[] read(int count)
	{
		throw new NotImplementedException();
	}
	
	public void purge()
	{
		throw new NotImplementedException();
	}
}
