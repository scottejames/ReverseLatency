package scott.latency.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
		{
			TestCalculateOffset.class,			
			TestNTPMessage.class,
			TestTimeServer.class,
			TestTime.class,
			TestSimpleSerializer.class,
			TestSimpleDynamicOffsetCalculation.class,
			TestThreadSafeList.class,
			TestLogMessage.class,
			TestMessageList.class,
			TestEMSConnectivity.class,
			TestProbe.class,
			TestApplicationProperties.class
		})
public class AllTests
{

}
