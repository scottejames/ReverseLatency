package scott.latency.misc;

import java.io.IOException;
import java.util.Properties;

public class ApplicationProperties
{
    private Properties properties = null;

	private static ApplicationProperties _instance = null;

	public static synchronized ApplicationProperties getInstance()
	{
	
		if (_instance == null)
		{
			_instance = new ApplicationProperties();
		}
		return _instance;
	}
	
	private ApplicationProperties()
	{
	    properties = new Properties();
	    
	    try
	    {
	    	java.net.URL url = ClassLoader.getSystemResource("ReverseLatency.properties");
	    	properties.load(url.openStream());
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    	System.err.println("Failed to load properties file " );
	    	System.exit(-1);
	    }
	}
	
	public String getConnectionFactoryName()
	{
		return properties.getProperty("scott.jms.ConnectionFactoryName");
	}
	
	public enum JMS_PROVIDER { TIBCOEMS, OPENJMS }
	public JMS_PROVIDER getJMSProvider()
	{
		String provider = properties.getProperty("scott.jms.Provider");
		if (provider.equals("OpenJMS"))
			return JMS_PROVIDER.OPENJMS;
		if (provider.equals("TibcoEMS"))
			return JMS_PROVIDER.TIBCOEMS;
		System.err.println("scott.jms.Provider not set correctly valid values are OpenJMS or TibcoEMS supplied - " + provider);
		System.exit(-1);
		return null;	
	}
	

}
