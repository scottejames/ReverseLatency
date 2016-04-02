package scott.latency.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import scott.latency.StaticConfiguration;

public class TestUtilities
{

	
	public static void sendDataToUdpSocket(byte[] buf, String host, int port)
	{
		try
		{
			DatagramSocket socket = new DatagramSocket();

			socket.setSoTimeout(StaticConfiguration.setNTPServerTimeout());

			InetAddress address = InetAddress.getByName(host);

			DatagramPacket packet = new DatagramPacket(buf, buf.length,
					address, port);

			socket.send(packet);
			socket.close();
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
	
	public static byte [] sendDataToUdpSocketWaitForReply(byte[] buf, String host, int port)
	{
		byte[] results = new byte[1024];
		DatagramPacket responsePacket = null;
		try
		{
			DatagramSocket socket = new DatagramSocket();

			socket.setSoTimeout(StaticConfiguration.setNTPServerTimeout());

			InetAddress address = InetAddress.getByName(host);

			DatagramPacket packet = new DatagramPacket(buf, buf.length,
					address, port);

			socket.send(packet);
			
			responsePacket = new DatagramPacket(results, results.length);
			socket.receive(responsePacket);
			
			socket.close();
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
		if (responsePacket == null)
		{
			return null;
		} else
		{
			return responsePacket.getData();
		}
	}
}
