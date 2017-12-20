package fr.sihm.drivers;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SeleniumDriver {

	public static WebDriver getFirefoxDriver() {
		FirefoxBinary binary = new FirefoxBinary(new File(System.getenv("WEBDRIVER_FIREFOX")));
		FirefoxProfile profile = new FirefoxProfile();
		WebDriver driver = new FirefoxDriver(binary,profile);
		return driver;
	}
	
	public static WebDriver getInternetExplorerDriver() {
		File file = new File(System.getenv("WEBDRIVER_IE"));
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
		capabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
		capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, true);
		WebDriver driver = new InternetExplorerDriver(capabilities);
		
		return driver;
	}
}
