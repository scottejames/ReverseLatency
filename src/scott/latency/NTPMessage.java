package scott.latency;

/**
 * Simplified NTP message, there is no reason that this message could not
 * implement the full RFC2030 NTP message.  If this where to be done then 
 * a standard NTP server could be used as the reference host.
 * 
 * @author sjames1
 * 
 */
public class NTPMessage
{

	/**
	 * Unique ID that can be used to track the message, typically an increasing
	 * integer.
	 */
	private int id = 0;

	/**
	 * This is the time at which the request departed the client for the server
	 */
	private long originateTimestamp;
	/**
	 * This is the time at which the reply departed the server for the client,
	 */
	private long transmitTimestamp;
	/**
	 * This is the time at which the request arrived at the server,
	 */
	private long receiveTimestamp;

	// indexes into the byte array, used to allow us to populate the dates after
	// the conversion.
	public final static int originIndex = 0;
	public final static int recieveIndex = 8;
	public final static int transmitIndex = 16;
	public final static int idIndex = 24;

	/**
	 * Construct the message from the byte array,  its assumed 
	 * the byte array is constructed from the toByteArray method
	 * below.  Although the packing algorithm is well known, the array
	 * Should only be manipulated by this class.
	 * @param array
	 */
	public NTPMessage(byte[] array)
	{
		// See the packet format diagram in RFC 2030 for details

		originateTimestamp = decodeTimestamp(array, originIndex);
		receiveTimestamp = decodeTimestamp(array, recieveIndex);
		transmitTimestamp = decodeTimestamp(array, transmitIndex);
		id = decodeInt(array, idIndex);

	}
	/** 
	 * Default constructor, sets all times to zero
	 */
	public NTPMessage()
	{
	}

	/**
	 * Packs the object into a byte array, can re-create the object via
	 * the byte array constructor.
	 * @return 
	 */
	public byte[] toByteArray()
	{
		// All bytes are automatically set to 0
		byte[] p = new byte[60];

		encodeTimestamp(p, originIndex, originateTimestamp);
		encodeTimestamp(p, recieveIndex, receiveTimestamp);
		encodeTimestamp(p, transmitIndex, transmitTimestamp);
		encodeInt(p, idIndex, id);
		return p;
	}

	/***
	 * Convert the byte array into a long, returned. the pointer is an index
	 * into the byte array. 8 bits are used.
	 * 
	 * @param array
	 * @param pointer
	 * @return
	 */
	public static long decodeTimestamp(byte[] array, int pointer)
	{

		long result = 0;
		for (int i = pointer; i < pointer + 8; i++)
		{
			result <<= 8;
			result ^= (long) array[i] & 0xFF;

		}
		return result;

	}

	/***
	 * Convert the long into a byte array , returned. the pointer is an index
	 * into the byte array. 8 bits are used.
	 * 
	 * @param array
	 * @param pointer
	 * @return
	 */
	public static void encodeTimestamp(byte[] array, int pointer, long timestamp)
	{

		for (int i = 0; i < 8; i++)
		{
			array[pointer + 7 - i] = (byte) (timestamp >>> (i * 8));

		}
	}

	/**
	 * packs the int into a byte array
	 * @param array
	 * @param pointer
	 * @param value
	 */
	public static void encodeInt(byte[] array, int pointer, int value)
	{

		for (int i = 0; i < 4; i++)
		{
			array[pointer + 3 - i] = (byte) (value >>> (i * 8));
		}

	}
	/**
	 * creates the int from teh byte array
	 * @param array
	 * @param pointer
	 * @return
	 */
	public static int decodeInt(byte[] array, int pointer)
	{
		int result = 0;
		for (int i = pointer; i < pointer + 4; i++)
		{
			result <<= 8;
			result ^= (long) array[i] & 0xFF;
		}
		return result;

	}

	/***
	 * Convert the byte to a short
	 * 
	 * @param b
	 * @return
	 */

	public static short unsignedByteToShort(byte b)
	{

		if ((b & 0x80) == 0x80)
			return (short) (128 + (b & 0x7f));
		else
			return (short) b;
	}

	public String toString()
	{

		return "Originate timestamp: "
				+ Time.timestampToString(originateTimestamp) + " "
				+ "Receive timestamp:   "
				+ Time.timestampToString(receiveTimestamp) + " "
				+ "Transmit timestamp:  "
				+ Time.timestampToString(transmitTimestamp);
	}

	public long getOriginateTimestamp()
	{
		return originateTimestamp;
	}

	public void setOriginateTimestamp(long originateTimestamp)
	{
		this.originateTimestamp = originateTimestamp;
	}

	public double Receive()
	{
		return transmitTimestamp;
	}

	public void setTransmitTimestamp(long transmitTimestamp)
	{
		this.transmitTimestamp = transmitTimestamp;
	}

	public long getTransmitTimestamp()
	{
		return this.transmitTimestamp;
	}

	public long getReceiveTimestamp()
	{
		return receiveTimestamp;
	}

	public void setReceiveTimestamp(long receiveTimestamp)
	{
		this.receiveTimestamp = receiveTimestamp;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

}
