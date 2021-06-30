package com.aspiration.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.aspiration.framework.selenium.BrowserTypes;

/**
 * @author Vinod Kumar
 *
 */
public class ConfigProperties {

	public static String getTeamName() {
		return getPropertiesFile().getProperty("team");
	}

	public static String getCluster() {
		// if the run was triggered from jenkins job and "cluster" param was
		// configured in the command line params then read it here
		if (System.getProperty("cluster") != null)
			return System.getProperty("cluster");

		// if the run was triggered from eclipse
		return getEnv();
	}
		
	
	public static String getGroups() {
		// if the run was triggered from jenkins job and "groups" param was
		// configured in the command line params then read it here
		if (System.getProperty("groups") != null)
			return System.getProperty("groups");

		// if the run was triggered from eclipse
		return getPropertiesFile().getProperty("groups");
	}

	public static String getEnv() {
		// if the run was triggered from jenkins job and "cluster" param was
		// configured in the command line params then read it here
		if (System.getProperty("cluster") != null)
			return System.getProperty("cluster");

		// if the run was triggered from eclipse
		return getPropertiesFile().getProperty("env");
	}
	
	public static String getValue(String key) {
		return getPropertiesFile().getProperty(key);
	}
	

	public static String getAutomationEnv() {
		return getPropertiesFile().getProperty("automationEnv");
	}
	
	public static String getHostUrl(String storeFront) {
		 return getPropertiesFile().getProperty(getEnv() + "." + storeFront +"testurl"); 
	}

	public static String getUserName(String storeFront) {
		return getPropertiesFile().getProperty(getEnv() + "." + storeFront +"userid");
	}

	public static String getPassword(String storeFront) {
		return getPropertiesFile().getProperty(getEnv() + "." + storeFront +"password");
	}
	
	public static String getGmailPassword(String storeFront) {
		return getPropertiesFile().getProperty("Gmailpassword");
	}

	public static BrowserTypes getBrowserType() {
		String browser = getPropertiesFile().getProperty("browser");        
		return BrowserTypes.valueOf(browser);
	}

	public static boolean sendReport() {
		return Boolean.valueOf(getPropertiesFile().getProperty("send_report"));
	}

	public static String emailReportTo() {
		return getPropertiesFile().getProperty("mail_to");
	}

	public static String getMaxRetryCount() {
		return getPropertiesFile().getProperty("maxRetryCount");
	}

	public static boolean postToDashboard() {
		// if the run was triggered from jenkins job and "dashboard.post" param was
		// configured in the command line params then read it here
		if (System.getProperty("dashboard.post") != null)
			return Boolean.valueOf(System.getProperty("dashboard.post"));

		return Boolean.valueOf(getPropertiesFile().getProperty("dashboard.post"));
	}

	private static Properties getPropertiesFile() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("appConfig/config.properties"));
			return prop;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
