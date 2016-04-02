package scott.latency.interfaces;


public interface IMessageWriter {
	public abstract void open();
	public abstract void close();
	public void write(String[] arrayOfMessages);
	public void write(String message);


}
