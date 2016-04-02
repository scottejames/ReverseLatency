package scott.latency.test.fakes;

import scott.latency.interfaces.IMessageWriter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class FakeMessageWriter implements IMessageWriter
{
	public String message;
	public boolean closeCalled=false;
	public boolean openCalled=false;
	
	public void close()
	{
		closeCalled = true;		
	}

	public void open()
	{
		openCalled = true;		
	}

	public void write(String[] arrayOfMessages)
	{
		throw new NotImplementedException();
		
	}

	public void write(String message)
	{
		this.message = message;
	}

}
