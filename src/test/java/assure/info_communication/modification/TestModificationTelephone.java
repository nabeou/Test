package assure.info_communication.modification;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import fr.sihm.drivers.DriverAcdc;
import fr.sihm.drivers.DriverInfinite;
import fr.sihm.drivers.DriverMhm;
import fr.sihm.objects.DemandeMhm;

import java.text.SimpleDateFormat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestModificationTelephone {
	private static Logger logger = LogManager.getRootLogger();
	private static DriverMhm driverMhm;
	private static DriverAcdc driverAcdc;
	private static DriverInfinite driverInfinite;
	private static String baseUrlMhm;
	private static String baseUrlAcdc;
	private static String baseUrlInfinite;
	private static StringBuffer verificationErrors = new StringBuffer();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String newTelephone = "+33951951766";
	private static String oldTelephone;
	private static DemandeMhm demandeMhm;
	private static String navigator;
	private static String numeroContrat;
	
	@BeforeClass
	public static void setUp() throws Exception {
		navigator = "${navigator}";
		//navigator="ie";

		//baseUrl = "${url}";
		baseUrlMhm = "http://mhm37rec3:7011";
		logger.debug("baseUrlMhm: "+baseUrlMhm);
		
		baseUrlAcdc = "https://acdc-rec2/apps/CaseManager/components/login/login.html?locale=fr_FR&tcf-redirect=http://acdc-rec2/apps/CaseManager/index.html";
		logger.debug("baseUrlAcdc: "+baseUrlAcdc);
		
		baseUrlInfinite = "http://infinite-haum0d/mdg";
		logger.debug("baseUrlInfinite: "+baseUrlInfinite);
	}

	@Test
	public void test1ModifierMHM() throws Exception {
		logger.debug("test1ModifierMHM "+baseUrlMhm);
		driverMhm = new DriverMhm(navigator, baseUrlMhm, "bpm03@test.fr", "Acc1234$");
		
		String returnMessage = driverMhm.setTel(newTelephone);
		oldTelephone = driverMhm.getOldValue();
		assertTrue(returnMessage.contains("Suivi de mes demandes"));
		
		demandeMhm = driverMhm.getDemande();
		assertEquals("En cours", demandeMhm.getStatus());
		
		Calendar calendar = Calendar.getInstance();
		assertEquals(sdf.format(calendar.getTime()), demandeMhm.getDate());
	}
	
	@Test
	public void test2ValiderACDC() throws Exception {
		logger.debug("test2ValiderACDC "+baseUrlAcdc);
		logger.debug("demandeMhm à valider: "+demandeMhm.getId());
		if(demandeMhm==null) {
			throw new Exception("demandeMhm "+demandeMhm);
		}
		
//		driverAcdc = new DriverAcdc(navigator, baseUrlAcdc, "ailenei-a", "!Bubulles11!");
//		String tel = driverAcdc.validTel(demandeMhm.getId());
//	    assertEquals(newTelephone, tel);
//	    
//	    numeroContrat = driverAcdc.getNumContrat();
	}
	
	@Test
	public void test3VerifierMHM() throws Exception {
		logger.debug("test3VerifierMHM");
		if(demandeMhm==null) {
			throw new Exception("demandeMhm "+demandeMhm);
		}
		
		driverMhm.refreshDemande(demandeMhm);
		assertTrue(demandeMhm.getStatus().contains("Trait"));
	}
	
	@Test
	public void test4VerifierInfinite() throws Exception {
		logger.debug("test4VerifierInfinite "+baseUrlInfinite);
		if(numeroContrat==null) {
			throw new Exception("numeroContrat "+numeroContrat);
		}
		
		driverInfinite = new DriverInfinite(navigator, baseUrlInfinite, "SAPCTOR1", "Pte<17*or");
//		driverInfinite.getTel(numeroContrat);
		//assertEquals(firstname.toUpperCase(), firstnameVaid);
	}

	@AfterClass
	public static void tearDown() throws Exception {
//		// set oldTelephone
//		driverMhm.setTel(oldTelephone);
//		demandeMhm = driverMhm.getDemande();
//		driverAcdc.validLastname(demandeMhm.getId());
		
		if(driverMhm!=null) {
			driverMhm.quit();
		}
		if(driverAcdc!=null) {
			driverAcdc.quit();
		}
		if(driverInfinite!=null) {
			driverInfinite.quit();
		}
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
