package assure.info_communication.modification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import fr.sihm.drivers.DriverMhm;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestModificationMail {
	private static Logger logger = LogManager.getRootLogger();
	private static DriverMhm driverMhm;
	private static String baseUrlMhm;
	private static StringBuffer verificationErrors = new StringBuffer();
	private String newMail = "bpmABCD@test.fr";
	private static String oldMail = "bpm03@test.fr";
	private String password = "Acc1234$";
	private static String navigator;
	
	@BeforeClass
	public static void setUp() throws Exception {
		navigator = "${navigator}";
		//navigator="ie";

		//baseUrl = "${url}";
		baseUrlMhm = "http://mhm37rec3:7011";
		logger.debug("baseUrlMhm: "+baseUrlMhm);
	}

	@Test
	public void test1ModifierMHM() throws Exception {
		logger.debug("test1ModifierMHM "+baseUrlMhm);
		driverMhm = new DriverMhm(navigator, baseUrlMhm, oldMail, password);
		driverMhm.setMail(newMail);
	}
	
	@Test
	public void test2VerifierMHM() throws Exception {
		driverMhm.disconnect();
		driverMhm.connect(newMail, password);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		// set oldMail
		driverMhm.setMail(oldMail);
		
		if(driverMhm!=null) {
			driverMhm.quit();
		}
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
