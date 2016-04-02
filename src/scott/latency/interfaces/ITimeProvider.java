package scott.latency.interfaces;

import scott.latency.exceptions.DependencyMissing;

public interface ITimeProvider extends Runnable{

	public abstract long getSkew();
	public  abstract long getLocalTime();
	public abstract long getNormalisedTime() throws DependencyMissing;
	
	public abstract void start() throws DependencyMissing;
	public abstract void stop();


}