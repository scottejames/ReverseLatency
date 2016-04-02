package scott.latency.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import scott.latency.NTPMessage;
import scott.latency.StaticConfiguration;
import scott.latency.Time;

public class TimeServer implements Runnable
{
	private int processMessageCountLimit = 0;
	private int processMessageCount = 0;
	private boolean running = false;
	private void applyOverrides(String[] args)
	{
		StaticConfiguration.applyOverrides(args);

	}

	public void run()
	{
		running = true;
		Logger l = Logger.getLogger(TimeServer.class.getName());
		int port = StaticConfiguration.getNTPPort();

		l.info("Starting server on port " + port);
		DatagramSocket socket = null;
		try
		{
			socket = new DatagramSocket(port);
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			l.error("Socket Exception " + e.toString()); 	
			System.exit(-1);
		}

		byte[] buf = new NTPMessage().toByteArray();

		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		while (processMessageCount++ < processMessageCountLimit || 
				processMessageCountLimit == 0)
		{
			try
			{
				l.info("Waiting for a new request");
				socket.receive(packet);

				NTPMessage msg = new NTPMessage(packet.getData());
		
				msg.setTransmitTimestamp(Time.getInstance().getLocalTime());

				buf = msg.toByteArray();
				packet.setData(buf);

				socket.send(packet);
				l.info("Request recieved sent response " + msg);
//				l.info("Sent response to message " + msg.getId());
//				l.info("Set timestamp to be  " + msg.getReceiveTimestamp());
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		socket.close();
		l.info("Stopping time server on port ");
		
		running = false;
	}

	public static void main(String[] args) throws IOException
	{
		TimeServer ts = new TimeServer();
		ts.applyOverrides(args);
		ts.run();

	}



	public int getProcessMessageCount()
	{
		return processMessageCount;
	}

	public void setProcessMessageCountLimit(int processMessageCountLimit)
	{
		this.processMessageCountLimit = processMessageCountLimit;
	}


	public boolean isRunning()
	{
		return running;
	}


}
