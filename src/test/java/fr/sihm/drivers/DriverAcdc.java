package fr.sihm.drivers;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.sihm.objects.DemandeAcdc;

public class DriverAcdc {
	private Logger logger = LogManager.getRootLogger();
	private WebDriver driver;
	private String numContrat;
	private String currentUrl;
	private String usernameInfinite;
	private String passwordInfinite;
	
	public DriverAcdc(String navigator, String baseUrl, String username, String password) {
		super();
		
		if(navigator.equals("ie")) {
			driver = SeleniumDriver.getInternetExplorerDriver();
		} else {
			driver = SeleniumDriver.getFirefoxDriver();
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		
		driver.get(baseUrl);
		driver.findElement(By.id("userName")).clear();
		driver.findElement(By.id("userName")).sendKeys(username);
		driver.findElement(By.id("inputPassword")).clear();
		driver.findElement(By.id("inputPassword")).sendKeys(password);
		driver.findElement(By.name("button")).click();
		(new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'titleUserName')]")));
		logger.info("Connexion Admin BPM.");
		currentUrl = driver.getCurrentUrl();
		logger.debug("currentUrl Acdc "+currentUrl);
		logger.info("Affichage Page d'accueil.");
	}

	public void setUsernameInfinite(String usernameInfinite) {
		this.usernameInfinite = usernameInfinite;
	}

	public void setPasswordInfinite(String passwordInfinite) {
		this.passwordInfinite = passwordInfinite;
	}
	
	public String validNumSecu(DemandeAcdc demandeAcdc) throws Exception {
	    driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/md-content/md-card/div/div/div/div/div/div/div[2]/div[2]/div/div[2]/div[1]/button")).click();
	    (new WebDriverWait(driver, 20)).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span"), "VALIDATION")); 
	    String statut = driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span")).getText();
	    logger.debug("statut: "+statut);
	    
	    numContrat = driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo2']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[5]/div[5]/div/div[2]/div[7]/div/div[2]/span")).getText();
	    logger.debug("numContrat: "+numContrat);
	    
	    
	  //case-class-display-directive[@id='defaultcaseinfo2']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[5]/div[5]/div/div[2]/div[7]/div/div[2]/span
	    
	    
	    String numContratCollectif = driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo2']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[6]/div[6]/div/div[2]/div[3]/div/div[2]/span")).getText();
	    logger.debug("numContratCollectif: "+numContratCollectif);
	    
	    // check la présence de bouton: Changement de caisse
	    WebElement ele = driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/md-content/md-card/div/div/div/div/div/div/div[1]/div[2]/div[3]/div[2]/div[1]/a"));
	    if(ele==null) {
	    	throw new Exception("Bouton Changement de caisse non présente");
	    } 
	    
	    ele.click();
	    ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
	    DriverInfinite driverInfinite = new DriverInfinite( driver.switchTo().window(tabs.get(1)), usernameInfinite, passwordInfinite); 
	    driverInfinite.checkNumContrat(numContrat, numContratCollectif);
		driver.close();
	    driver.switchTo().window(tabs.get(0));
	    
	    // check la présence de bouton: Changement numéro RO enfant
	    ele = driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/md-content/md-card/div/div/div/div/div/div/div[1]/div[2]/div[4]/div[2]/div[1]/a"));
	    if(ele==null) {
	    	throw new Exception("Bouton Changement numéro RO enfant non présente");
	    } 
	    
	    ele.click();
	    tabs = new ArrayList<String> (driver.getWindowHandles());
	    driverInfinite.setDriver(driver.switchTo().window(tabs.get(1)));  
	    driverInfinite.checkNumContrat(numContrat, numContratCollectif);
	    driver.close();
	    driver.switchTo().window(tabs.get(0));
	    
	    // check la présence de bouton: Autres demandes
	    ele = driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/md-content/md-card/div/div/div/div/div/div/div[1]/div[2]/div[5]/div[2]/div[1]/a"));
	    if(ele==null) {
	    	throw new Exception("Bouton Autres demandes non présente");
	    }
	    
	    ele.click();
	    tabs = new ArrayList<String> (driver.getWindowHandles());
	    driverInfinite.setDriver(driver.switchTo().window(tabs.get(1)));  
	    driverInfinite.checkNumContrat(numContrat, numContratCollectif);
	    driver.close();
	    driver.switchTo().window(tabs.get(0));
	    
	    driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/md-content/md-card/div/div/div/div/div/div/div[2]/div[2]/div/div[2]/div[1]/button")).click();
	    (new WebDriverWait(driver, 20)).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span"), "TERMINEE"));
	    statut = driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span")).getText();
	    logger.debug("statut: "+statut);
	    
		String numeroRO = driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[3]/div/div[2]/custom-case-data-directive/div/div/div/div[2]/span")).getText();
	    String cleRO = driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[3]/div/div[2]/custom-case-data-directive/div/div[2]/div/div[2]/span")).getText(); 
	    logger.debug("numSecu valid: "+numeroRO+cleRO);
	    return numeroRO+cleRO;
	}
	
	public String validTel(DemandeAcdc demandeAcdc) {
		driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/md-content/md-card/div/div/div/div/div/div/div[2]/div[2]/div/div[2]/div[1]/button")).click();
	    (new WebDriverWait(driver, 20))
	    .until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span"), "TERMINEE"));
	    String statut = driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span")).getText();
	    logger.debug("statut: "+statut);
	    
	    numContrat = driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo2']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[5]/div[5]/div/div[2]/div[7]/div/div[2]/span")).getText();
	    logger.debug("numContrat: "+numContrat);
	
	    String telephone = driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[3]/div/div[2]/span")).getText();
	    logger.debug("telephone vaid: "+telephone);
	    return telephone;
	}

//	public String getNumContrat() {
//		return numContrat;
//	}
	
	public void quit() {
		WebElement webElement = driver.findElement(By.xpath("//button[@ng-click='logout();']"));
		if(webElement!=null) {
			webElement.click();
		}
		driver.quit();
	}
	
	public DemandeAcdc getDemande(String idDemande) {
		driver.get(currentUrl+"/pageWorkList");
		driver.findElement(By.xpath("//div[@id='panel1']")).findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//div[@id='panel1']")).findElement(By.xpath("//input[@type='text']")).sendKeys(idDemande);
		driver.findElement(By.name("button")).click();
	    (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table/tbody/tr[1]/td[11]/span[contains(text(), '"+idDemande+"')]")));
	    logger.debug(driver.findElement(By.xpath("//table/tbody/tr[1]")).getText());
	    driver.findElement(By.xpath("//table/tbody/tr[1]")).click();
	    (new WebDriverWait(driver, 20)).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span"), "COMPLETUDE"));
	    
	    DemandeAcdc demandeAcdc = new DemandeAcdc();
	    demandeAcdc.setId(driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/div/ng-include/md-content/case-class-display-directive[1]/div/md-content/md-card/div/div/custom-case-data-directive/div/div[1]/div[1]/div/div[2]/span")).getText());
	    demandeAcdc.setStatus(driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/div/ng-include/md-content/case-class-display-directive[1]/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div[2]/div/div[2]/span")).getText());
	    demandeAcdc.setType(driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/div/ng-include/md-content/case-class-display-directive[1]/div/md-content/md-card/div/div/custom-case-data-directive/div/div[3]/div[3]/div/div[2]/span")).getText());
	    demandeAcdc.setOrigine(driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/div/ng-include/md-content/case-class-display-directive[1]/div/md-content/md-card/div/div/custom-case-data-directive/div/div[4]/div[4]/div/div[2]/span")).getText());
	    logger.info("demandeAcdc "+demandeAcdc.getId()+" - "+demandeAcdc.getStatus()+" - "+demandeAcdc.getType()+" - "+demandeAcdc.getOrigine());
	    demandeAcdc.setStatusModif(driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span")).getText());
	    demandeAcdc.setCivitity(driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[3]/div/div[2]/span")).getText());
		demandeAcdc.setLastname(driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[4]/div/div[2]/span")).getText());
		demandeAcdc.setFirstname(driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[5]/div/div[2]/span")).getText());
		logger.info("demandeAcdc modif "+demandeAcdc.getId()+" - "+demandeAcdc.getStatusModif()+" - "+demandeAcdc.getCivitity()+" - "+demandeAcdc.getLastname()+" - "+demandeAcdc.getFirstname());
	    return demandeAcdc;
	}
	
	public void completeDonnees(DemandeAcdc demandeAcdc) {
	    driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/md-content/md-card/div/div/div/div/div/div/div[2]/div[2]/div/div[2]/div[1]/button")).click();
	    (new WebDriverWait(driver, 20)).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span"), "VALIDATION")); 
	    demandeAcdc.setStatusModif(driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span")).getText());
	    logger.info("demandeAcdc modif "+demandeAcdc.getId()+" - "+demandeAcdc.getStatusModif()+" - "+demandeAcdc.getCivitity()+" - "+demandeAcdc.getLastname()+" - "+demandeAcdc.getFirstname());
	}
	
	public void modifInfinite(DemandeAcdc demandeAcdc) throws Exception {
	    numContrat = driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo2']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[5]/div[5]/div/div[2]/div[7]/div/div[2]/span")).getText();
	    logger.debug("numContrat: "+numContrat);
	    
	    String numContratCollectif = driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo2']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[6]/div[6]/div/div[2]/div[3]/div/div[2]/span")).getText();
	    logger.debug("numContratCollectif: "+numContratCollectif);
	    
	    // check la présence de bouton: Débranchement dans Infinite
	    WebElement ele = driver.findElement(By.xpath("/html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/md-content/md-card/div/div/div/div/div/div/div[1]/div[2]/div[2]/div[2]/div[1]/a"));
	    if(ele==null) {
	    	throw new Exception("Bouton Débranchement dans Infinite non présente");
	    } 
	    
	    ele.click();
	    ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
	    DriverInfinite driverInfinite = new DriverInfinite( driver.switchTo().window(tabs.get(1)), usernameInfinite, passwordInfinite); 
	   
    	try {
			driverInfinite.checkNumContrat(numContrat, numContratCollectif);
		} catch (Exception e) {
			throw e;
		} finally {
			driverInfinite.disconnect();
			driver.close();
		    driver.switchTo().window(tabs.get(0));
		}
    	
	    driver.findElement(By.xpath("//html/body/div[1]/div/div/tasks-section-directive/div/case-display-directive/div/div/div[2]/div/div/md-content/md-card/div/div/div/div/div/div/div[2]/div[2]/div/div[2]/div[1]/button")).click();
	    (new WebDriverWait(driver, 20)).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span"), "TERMINEE"));
	    demandeAcdc.setStatusModif(driver.findElement(By.xpath("//case-class-display-directive[@id='defaultcaseinfo']/div/md-content/md-card/div/div/custom-case-data-directive/div/div[2]/div/div[2]/span")).getText());
	    logger.info("demandeAcdc modif "+demandeAcdc.getId()+" - "+demandeAcdc.getStatusModif()+" - "+demandeAcdc.getCivitity()+" - "+demandeAcdc.getLastname()+" - "+demandeAcdc.getFirstname());
	}	
}
