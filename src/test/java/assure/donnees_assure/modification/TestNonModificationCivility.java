package assure.donnees_assure.modification;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import fr.sihm.drivers.DriverMhm;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNonModificationCivility {
	private static Logger logger = LogManager.getRootLogger();
	private static DriverMhm driverMhm;
	private static String baseUrlMhm;
	private static StringBuffer verificationErrors = new StringBuffer();
	private static String newCivilite;
	private static String oldCivilite;
	private static String iniCivilite;
	private static String navigator;
	
	@BeforeClass
	public static void setUp() throws Exception {
		navigator = "${navigator}";
		//navigator="ie";

		//baseUrl = "${url}";
		baseUrlMhm = "http://mhm37rec3:7011";
		logger.debug("baseUrlMhm: "+baseUrlMhm);
		newCivilite = "Monsieur";
		iniCivilite = "Monsieur";
		
	}

	@Test
	public void test1ModifierMHM() throws Exception {
		logger.debug("test1ModifierMHM "+baseUrlMhm);
		driverMhm = new DriverMhm(navigator, baseUrlMhm, "bpm03@test.fr", "Acc1234$");

		logger.info("Modifier la civilité de l'assuré principal "+newCivilite);
		String returnMessage = driverMhm.setCilivity(newCivilite);
		oldCivilite = driverMhm.getOldValue();
		assertTrue(returnMessage.contains("Aucun changement"));
		assertEquals(oldCivilite, iniCivilite);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		if(driverMhm!=null) {
			driverMhm.quit();
		}
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
