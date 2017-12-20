package assure.donnees_assure.modification;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import fr.sihm.drivers.DriverAcdc;
import fr.sihm.drivers.DriverMhm;
import fr.sihm.objects.DemandeAcdc;
import fr.sihm.objects.DemandeMhm;

import java.text.SimpleDateFormat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestModificationPrenom {
	private static Logger logger = LogManager.getRootLogger();
	private static DriverMhm driverMhm;
	private static DriverAcdc driverAcdc;
	private static String baseUrlMhm;
	private static String baseUrlAcdc;
	private static String usernameMhm;
	private static String passwordMhm;
	private static String usernameAcdc;
	private static String passwordAcdc;
	private static String usernameInfinite;
	private static String passwordInfinite;
	private static StringBuffer verificationErrors = new StringBuffer();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private static String newFirstname;
	private static String iniFirstname;
	private static String oldFirstname;
	private static DemandeMhm demandeMhm;
	private static String navigator;
	
	@BeforeClass
	public static void setUp() throws Exception {
		navigator = "${navigator}";
		//navigator="ie";

		//baseUrl = "${url}";
		baseUrlMhm = "http://mhm37rec3:7011";
		logger.debug("baseUrlMhm: "+baseUrlMhm);
		usernameMhm = "bpm03@test.fr";
		passwordMhm = "Acc1234$";
		newFirstname = "Michel";
		iniFirstname = "YOAN";
		
		baseUrlAcdc = "https://acdc-rec2/apps/CaseManager/components/login/login.html?locale=fr_FR&tcf-redirect=http://acdc-rec2/apps/CaseManager/index.html";
		logger.debug("baseUrlAcdc: "+baseUrlAcdc);
		usernameAcdc = "ailenei-a";
		passwordAcdc = "!Bubulles11!";
		usernameInfinite = "SAPCTOR1";
		passwordInfinite = "Pte<17*or";
	}

	@Test
	public void test1ModifierMHM() throws Exception {
		logger.debug("test1ModifierMHM "+baseUrlMhm);
		driverMhm = new DriverMhm(navigator, baseUrlMhm, usernameMhm, passwordMhm);
		
		logger.info("Modifier le prénom de l'assuré principal "+newFirstname);
		String returnMessage = driverMhm.setFirstname(newFirstname);
		oldFirstname = driverMhm.getOldValue();
		assertTrue(returnMessage.contains("Suivi de mes demandes"));
		assertEquals(oldFirstname, iniFirstname);
		
		demandeMhm = driverMhm.getDemande();
		assertEquals("En cours", demandeMhm.getStatus());
		Calendar calendar = Calendar.getInstance();
		assertEquals(sdf.format(calendar.getTime()), demandeMhm.getDate());
	}
	
	@Test
	public void test2ValiderACDC() throws Exception {
		logger.debug("test2ValiderACDC "+baseUrlAcdc);
		logger.debug("demandeMhm à valider: "+demandeMhm.getId());

		driverAcdc = new DriverAcdc(navigator, baseUrlAcdc, usernameAcdc, passwordAcdc);
		driverAcdc.setUsernameInfinite(usernameInfinite);
		driverAcdc.setPasswordInfinite(passwordInfinite);
		DemandeAcdc demandeAcdc = driverAcdc.getDemande(demandeMhm.getId());
		assertEquals(demandeAcdc.getStatus(), "En cours");
		assertEquals(StringUtils.stripAccents(demandeAcdc.getType()), StringUtils.stripAccents("Modification Données Assuré"));
		assertEquals(demandeAcdc.getOrigine(), "Selfcare");
		assertEquals(demandeAcdc.getStatusModif(), "COMPLETUDE");
		assertEquals(demandeAcdc.getCivitity(), driverMhm.getPersonnePhysique().getCivitity().toUpperCase());
		assertEquals(demandeAcdc.getLastname(), driverMhm.getPersonnePhysique().getLastname().toUpperCase());
		assertEquals(demandeAcdc.getFirstname(), driverMhm.getPersonnePhysique().getFirstname().toUpperCase());
		
		driverAcdc.completeDonnees(demandeAcdc);
		assertEquals(demandeAcdc.getStatusModif(), "VALIDATION");
		assertEquals(demandeAcdc.getCivitity(), driverMhm.getPersonnePhysique().getCivitity().toUpperCase());
		assertEquals(demandeAcdc.getLastname(), driverMhm.getPersonnePhysique().getLastname().toUpperCase());
		assertEquals(demandeAcdc.getFirstname(), driverMhm.getPersonnePhysique().getFirstname().toUpperCase());
		
		driverAcdc.modifInfinite(demandeAcdc);
		assertEquals(demandeAcdc.getStatusModif(), "TERMINEE");
		assertEquals(demandeAcdc.getCivitity(), driverMhm.getPersonnePhysique().getCivitity().toUpperCase());
		assertEquals(demandeAcdc.getLastname(), driverMhm.getPersonnePhysique().getLastname().toUpperCase());
		assertEquals(demandeAcdc.getFirstname(), driverMhm.getPersonnePhysique().getFirstname().toUpperCase());
	}
	
	@Test
	public void test3VerifierMHM() throws Exception {
		logger.debug("test3VerifierMHM");
		driverMhm.refreshDemande(demandeMhm);
		assertEquals(StringUtils.stripAccents(demandeMhm.getStatus()), StringUtils.stripAccents("Traitée"));
		Calendar calendar = Calendar.getInstance();
		assertEquals(sdf.format(calendar.getTime()), demandeMhm.getDate());
	}

	@AfterClass
	public static void tearDown() throws Exception {
		if(driverMhm!=null) {
			driverMhm.quit();
		}
		if(driverAcdc!=null) {
			driverAcdc.quit();
		}
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
