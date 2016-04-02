package scott.latency.test;

import org.apache.log4j.Logger;

import scott.latency.StaticConfiguration;
import scott.latency.logging.Probe;
import scott.latency.logging.ProbeFactory;
import scott.latency.server.EMSMessageLogger;

public class TestClient {
	
	public static void main(String [] args) throws InterruptedException { 
		Logger l = Logger.getLogger(TestClient.class.getName());

		l.info("Running Test Client");

		StaticConfiguration.applyOverrides(args);
		StaticConfiguration.setUpStaticConfiguration();		
		ProbeFactory pf = ProbeFactory.getInstance();
		
		Probe p1 = pf.createProbe("probe");
		
		
		for (int i = 0; i < 100000; i++)
		{
			p1.mark("Start",i);
			Thread.sleep(10000);
			p1.mark("End",i);
		
		}
	}
}
