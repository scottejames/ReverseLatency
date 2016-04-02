package scott.latency.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import scott.latency.logging.LogMessage;

public class SimpleObjectSerializer
{
	public static byte[] serialize(Serializable temp) throws IOException 
	{
		byte[] s = null;

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(temp);
		oos.flush();
		oos.close();
		os.close();
		s = os.toByteArray();

		return s;
	}

	public static Serializable deSerialize(byte[] s) throws IOException, ClassNotFoundException 
	{
		Serializable temp = null;

		ByteArrayInputStream is = new ByteArrayInputStream(s);
		ObjectInputStream ois = new ObjectInputStream(is);
		temp =  (Serializable) ois.readObject();

		return temp;
	}
}
