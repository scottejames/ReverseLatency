package scott.latency.exceptions;

public class DependencyMissing extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DependencyMissing(String error)
	{
		super(error);
	}

}
