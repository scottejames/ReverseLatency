package scott.latency.logging;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import scott.latency.NTPMessage;
import scott.latency.StaticConfiguration;
import scott.latency.interfaces.IMessageReader;
import scott.latency.interfaces.IMessageWriter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UDPInterface implements IMessageWriter, IMessageReader
{

	private Logger l = Logger.getLogger(UDPInterface.class.getName());
	private int port = StaticConfiguration.getNTPPort();

	private String serverName = null;
	private DatagramSocket socket;
	private InetAddress address = null;

	public UDPInterface(int port)
	{
		serverName = "localhost";
		this.port = port;
		l.debug("Opening UDP interface to " + serverName + " : " + this.port);
	}

	public UDPInterface(String hostname, int port)
	{
		serverName = hostname;
		this.port = port;
		l.debug("Opening UDP interface to " + serverName + " : " + this.port);

	}

	public void close()
	{
		// TODO Auto-generated method stub

	}

	public void open()
	{

	}

	public void write(String[] arrayOfMessages)
	{
		throw new NotImplementedException();

	}

	public void write(String message)
	{
		if (socket == null)
		{
			l.debug("Writing message no socket created so building one NOTE this has a host name");
			try
			{
				socket = new DatagramSocket(this.port);
				address = InetAddress.getByName(serverName);

			} catch (SocketException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		l.debug("Writing message " + message);
		byte[] buf = message.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address,
				port);

		try
		{
			socket.send(packet);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void purge()
	{
		throw new NotImplementedException();

	}

	public String read()
	{
		
		if (socket == null)
		{
			l.debug("Reading message no socket created so building ");
			try
			{
				socket = new DatagramSocket(this.port);
				

			} catch (SocketException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		// Get response
		byte[] buf = new NTPMessage().toByteArray();
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		try
		{
			l.debug("Waiting for a message");
			socket.receive(packet);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String results = new String(buf);
		l.debug("Reading message " + results);

		return results;
	}

	public String[] read(int count)
	{
		throw new NotImplementedException();
	}

}
