package fr.sihm.drivers;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.sihm.objects.DemandeMhm;
import fr.sihm.objects.PersonnePhysique;

public class DriverMhm {
	private Logger logger = LogManager.getRootLogger();
	private WebDriver driver;
	private String oldValue;
	private PersonnePhysique personnePhysique;

	public DriverMhm(String navigator, String baseUrl, String username, String password) {
		super();
		
		if(navigator.equals("ie")) {
			driver = SeleniumDriver.getInternetExplorerDriver();
		} else {
			driver = SeleniumDriver.getFirefoxDriver();
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		
		driver.get(baseUrl + "/web/tout-harmonie");
		connect(username, password);
	}
	
	public String setCilivity(String civility) {
		setPersonnePhysique();
		oldValue = personnePhysique.getCivitity();
		logger.debug("oldValue: "+oldValue);
		
		// Modifier
		driver.findElement(By.xpath("//form[@id='editMonIdentite']/div/div/a")).click();
		new Select(driver.findElement(By.id("selectcivilite"))).selectByVisibleText(civility);
		driver.findElement(By.xpath("//form[@id='editMonIdentite']/div/div[2]/a")).click();
		
		String returnMessage = driver.findElement(By.cssSelector("p.info-colla")).getText(); 
		logger.info(returnMessage);
		personnePhysique.setCivitity(civility);
		return returnMessage;
	}
	
	public String setLastname(String lastname) {
		setPersonnePhysique();
		oldValue = personnePhysique.getLastname();
		logger.debug("oldValue: "+oldValue);
		
		// Modifier
		driver.findElement(By.xpath("//form[@id='editMonIdentite']/div/div/a")).click();
		driver.findElement(By.id("lastname")).clear();
		driver.findElement(By.id("lastname")).sendKeys(lastname);
		driver.findElement(By.xpath("//form[@id='editMonIdentite']/div/div[2]/a")).click();
		
		String returnMessage = driver.findElement(By.cssSelector("p.info-colla")).getText(); 
		logger.debug(returnMessage);
		personnePhysique.setLastname(lastname);
		return returnMessage;
	}
	
	public String setFirstname(String firstname) {
		setPersonnePhysique();
		oldValue = personnePhysique.getFirstname();
		logger.debug("oldValue: "+oldValue);
		
		// Modifier
		driver.findElement(By.xpath("//form[@id='editMonIdentite']/div/div/a")).click();
		driver.findElement(By.id("firstname")).clear();
		driver.findElement(By.id("firstname")).sendKeys(firstname);
		driver.findElement(By.xpath("//form[@id='editMonIdentite']/div/div[2]/a")).click();
		
		String returnMessage = driver.findElement(By.cssSelector("p.info-colla")).getText(); 
		logger.debug(returnMessage);
		personnePhysique.setFirstname(firstname);
		return returnMessage;
	}
	
	public String setNumSecu(String numSecu, File file) {
		setPersonnePhysique();
		oldValue = driver.findElement(By.id("numeroSecu")).getAttribute("value");
		logger.debug("oldValue: "+oldValue);
		
		// Modifier
		driver.findElement(By.xpath("//form[@id='editMonIdentite']/div/div/a")).click();
		driver.findElement(By.id("numeroSecu")).clear();
		driver.findElement(By.id("numeroSecu")).sendKeys(numSecu);
		logger.debug(file.getAbsolutePath());
		driver.findElement(By.id("my_file_element")).clear();
		driver.findElement(By.id("my_file_element")).sendKeys(file.getAbsolutePath());
		driver.findElement(By.xpath("//form[@id='editMonIdentite']/div/div[2]/a")).click();
		
		String returnMessage = driver.findElement(By.cssSelector("p.info-colla")).getText(); 
		logger.debug(returnMessage);
		return returnMessage;
	}
	
	public String setTel(String numTel) {
		modifyContact();
		oldValue = driver.findElement(By.id("telephone")).getAttribute("value");
		logger.debug("oldValue: "+oldValue);
		
		driver.findElement(By.id("telephone")).clear();
		driver.findElement(By.id("telephone")).sendKeys(numTel);
		driver.findElement(By.xpath("//form[@id='editMoyensContact']/div/div[2]/a")).click();
		
		String returnMessage = driver.findElement(By.cssSelector("p.info-colla")).getText(); 
		logger.debug(returnMessage);
		return returnMessage;
	}
	
	public String setMail(String mail) {
		modifyContact();
		oldValue = driver.findElement(By.id("Adresse_Mail")).getAttribute("value");
		logger.debug("oldValue: "+oldValue);
		
		driver.findElement(By.id("Adresse_Mail")).clear();
		driver.findElement(By.id("Adresse_Mail")).sendKeys(mail);
		driver.findElement(By.xpath("//form[@id='editMoyensContact']/div/div[2]/a")).click();
		
		String returnMessage = driver.findElement(By.cssSelector("p.info-colla")).getText(); 
		logger.debug(returnMessage);
		return returnMessage;
	}	

	public String getOldValue() {
		return oldValue;
	}

	public DemandeMhm getDemande() {
		driver.findElement(By.xpath("//a[@href='/web/mon-compte/mon-profil?showSuivi=yes']")).click();
		
		String string = driver.findElement(By.xpath("//div[contains(@class,'suivi-table')]/table/tbody/tr[1]/th")).getText(); 
		logger.debug(string);
		String idDemande = string.substring(string.indexOf("N")+2);
		String status = driver.findElement(By.xpath("//div[contains(@class,'suivi-table')]/table/tbody/tr[1]/td[2]/div/div[contains(@class,'demande-state__text')]")).getText(); 
		String date = driver.findElement(By.xpath("//div[contains(@class,'suivi-table')]/table/tbody/tr[1]/td[2]/div/div[contains(@class,'demande-state__date')]")).getText();
		
		DemandeMhm demandeMhm = new DemandeMhm(idDemande, status, date);
		logger.info("demandeMhm: "+idDemande+" - "+status+" - "+date);
		return demandeMhm;
	}
	
	public void refreshDemande(DemandeMhm demandeMhm) {
		driver.findElement(By.xpath("//a[@href='/web/mon-compte/mon-profil?showSuivi=yes']")).click();
		WebElement row = driver.findElement(By.xpath("//div[contains(@class,'suivi-table')]/table/tbody/tr/th[contains(text(), '"+demandeMhm.getId()+"')]")); 
		logger.debug(row);
		
		String status = row.findElement(By.xpath("//td[2]/div/div[contains(@class,'demande-state__text')]")).getText();
		String date = row.findElement(By.xpath("//td[2]/div/div[contains(@class,'demande-state__date')]")).getText();
		demandeMhm.setStatus(status);
		demandeMhm.setDate(date);
		logger.debug("new statut: "+demandeMhm.getStatus());
	}
	
	public void disconnect() {
		driver.findElement(By.xpath("//ul[contains(@class,'nav')]/li[4]/a")).click();
		WebElement webElement = driver.findElement(By.xpath("//li[@id='js-account-dropdown']/ul/li[4]/a"));
		if(webElement!=null) {
			webElement.click();
		}
	}
	
	public void connect(String username, String password) {
		// Connexion
		driver.findElement(By.xpath("//ul[contains(@class,'nav')]/li[4]/a")).click();
		// Particuliers, salariés
		driver.findElement(By.xpath("//div[@id='login-accordion']/div/div/h4/a")).click();
		driver.findElement(By.id("mail_universe-consumers")).clear();
		driver.findElement(By.id("mail_universe-consumers")).sendKeys(username);
		driver.findElement(By.id("password_universe-consumers")).clear();
		driver.findElement(By.id("password_universe-consumers")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-rounded")).click();
		logger.info("Se connecter sur le portail MHM");
	}
	
	public void quit() {
		disconnect();
		
		driver.quit();
	}
	
	private void modifyContact() {
		
		// Modifier
		driver.findElement(By.xpath("//form[@id='editMoyensContact']/div/div/a")).click();
	}
	
	private void setPersonnePhysique() {
		// Mon compte
		driver.findElement(By.xpath("//ul[contains(@class,'nav')]/li[4]/a")).click();
		// Mon profil
		driver.findElement(By.xpath("//li[@id='js-account-dropdown']/ul/li[3]/a")).click();
		logger.info("accéder à \"Mon Profil\"");
		
		this.personnePhysique = new PersonnePhysique();
		personnePhysique.setCivitity(driver.findElement(By.xpath("//html/body/section/div/div/div/div[2]/div[2]/div/div/div[1]/div/div/div/section/div[2]/section/div/div/div/form[1]/p[1]/span/input")).getAttribute("value"));
		personnePhysique.setLastname(driver.findElement(By.id("lastname")).getAttribute("value"));
		personnePhysique.setFirstname(driver.findElement(By.id("firstname")).getAttribute("value"));
	}
	
	public PersonnePhysique getPersonnePhysique() {
		return this.personnePhysique;
	}
}
