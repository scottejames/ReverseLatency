package scott.latency;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import scott.latency.interfaces.INTPServerInterface;

public class UDPNTPServerInterface implements INTPServerInterface
{
	private Logger l = Logger.getLogger(UDPNTPServerInterface.class.getName());
	private int port = StaticConfiguration.getNTPPort();


	private String serverName = StaticConfiguration.getNTPHostName();
	public DatagramSocket socket;

	public void send(NTPMessage message)
	{
		try
		{
			byte[] buf = message.toByteArray();

			l.debug("Trying to connect to NTP server on port " + port
					+ " hostname " + serverName);
			socket = new DatagramSocket();
			socket.setSoTimeout(StaticConfiguration.setNTPServerTimeout());
			InetAddress address = InetAddress.getByName(serverName);

			DatagramPacket packet = new DatagramPacket(buf, buf.length,
					address, port);

			socket.send(packet);
			l.debug("Connected");


		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public NTPMessage recieve() throws TimeoutException
	{
		// Get response
		byte[] buf = new NTPMessage().toByteArray();
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try
		{
			socket.receive(packet);
		} catch (SocketTimeoutException e)
		{
			l
					.error("TIMEOUT waiting for response NTP server returning previous offset");

			throw new TimeoutException();

		} catch (IOException e)
		{
			l
					.error(
							"IOException while waiting for response from NTP server",
							e);
		}

		// Process response
		NTPMessage msg = new NTPMessage(packet.getData());
		

		
		return msg;
	}



	public void close()
	{
		socket.close();

	}

	public void open()
	{
		try
		{
			socket = new DatagramSocket();
			socket.setSoTimeout(StaticConfiguration.setNTPServerTimeout());
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			l.error("Failed to create socket ", e);
		}
	}
	
	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}
	

}
