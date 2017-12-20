package fr.sihm.drivers;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DriverInfinite {
	private Logger logger = LogManager.getRootLogger();
	private WebDriver driver;
	
	public DriverInfinite(String navigator, String baseUrl, String username, String password) {
		super();
		
		if(navigator.equals("ie")) {
			driver = SeleniumDriver.getInternetExplorerDriver();
		} else {
			driver = SeleniumDriver.getFirefoxDriver();
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		
		driver.get(baseUrl);
		connect(username, password);
	}
	
	public DriverInfinite(WebDriver driver, String username, String password) {
		this.driver = driver;
		connect(username, password);
	}
	
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

//	public String getLastName(String num) {
//		getContrat(num);
//		driver.findElement(By.xpath("//div[@id='workflow']/ul/li[3]/div[1]")).click();
//		String lastname = driver.findElement(By.id("nom-static")).getText();
//		logger.debug("lastname "+lastname);
//		return lastname;
//	}
//	
//	public String getFirstName(String num) {
//		getContrat(num);
//		driver.findElement(By.xpath("//div[@id='workflow']/ul/li[3]/div[1]")).click();
//		String firstname = driver.findElement(By.id("prenom-static")).getText();
//		logger.debug("firstname "+firstname);
//		return firstname;
//	}
//	
//	public String getNumSecu(String num) {
//		getContrat(num);
//		driver.findElement(By.xpath("//div[@id='workflow']/ul/li[4]/div[1]")).click();
//		String numRO = driver.findElement(By.id("numRO-static")).getText();
//		String cleRO = driver.findElement(By.id("cleRO-static")).getText();
//		logger.debug("numSecu "+numRO+cleRO);
//		return numRO+cleRO;
//	}
//	
//	public String getTel(String num) {
//		getContrat(num);
//		driver.findElement(By.xpath("//div[@id='workflow']/ul/li[3]/div[1]")).click();
//		String firstname = driver.findElement(By.id("telephonePersonnel-static")).getText();
//		logger.debug("firstname "+firstname);
//		return firstname;
//	}
	
	public void checkNumContrat(String numContrat, String numContratCollectif) throws Exception {
		String numeroContratInfinite = driver.findElement(By.id("numeroContrat2")).getAttribute("value");
		if(!numContrat.equals(numeroContratInfinite)) {
			throw new Exception("Erreur de numeroContrat: ACDC "+numContrat+" - Infinite "+numeroContratInfinite);
		}
		
		String numeroContratColInfinite = driver.findElement(By.id("numeroContratCol")).getAttribute("value");
		if(!numContrat.equals(numeroContratInfinite)) {
			throw new Exception("Erreur de numContratCollectif: ACDC "+numContratCollectif+" - Infinite "+numeroContratColInfinite);
		}
	}
	
	public void quit() {
		disconnect();
		driver.quit();
	}
	
	public void disconnect() {
		WebElement webElement = driver.findElement(By.xpath("//div[@id='navbarHeader']/div[4]/ul/li[2]/a"));
		if(webElement!=null) {
			webElement.click();
			driver.findElement(By.xpath("//button[@type='submit']")).click();
		}
	}
	
//	private void getContrat(String num) {
//		logger.debug("num "+num);
//		driver.findElement(By.xpath("//nav[@id='mainMenu']/div/a[4]")).click();
//		driver.findElement(By.linkText("Consultation")).click();
//		driver.findElement(By.id("numeroContrat")).clear();
//		driver.findElement(By.id("numeroContrat")).sendKeys(num);
//		driver.findElement(By.id("btRecherche")).click();
//	}
	
	private void connect(String username, String password) {
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(username);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.id("loginBtn")).click();
	}
}
