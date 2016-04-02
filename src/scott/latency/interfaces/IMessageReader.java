package scott.latency.interfaces;

public interface IMessageReader
{
	
	public abstract void open();
	public abstract void close();
	public abstract String read();
	public abstract String[] read(int count);
	public void purge();


}