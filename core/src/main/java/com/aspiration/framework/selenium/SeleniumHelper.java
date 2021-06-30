package  com.aspiration.framework.selenium;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Vinod Kumar *
 */
public abstract class SeleniumHelper {

	protected static Logger log = LogManager.getLogger(SeleniumHelper.class.getName());
	protected static final long MAX_WAIT_MILLI_SEC = 60000;

	// ************************ find methods
	// ************************************************
	/**
	 * searches for webElement
	 * 
	 * @param driver
	 * @param by
	 */
	public static WebElement findElement(WebDriver driver, By by) {
		waitForPageContainsWebElement(driver, by);

		log.info("finding element " + by.toString());
		return driver.findElement(by);
	}

	/**
	 * searches for webElements
	 * 
	 * @param driver
	 * @param by
	 * @return list of webElements
	 */
	public static List<WebElement> findElements(WebDriver driver, By by) {
		waitForPageContainsWebElements(driver, by);

		// log.info("finding elements " + by.toString());
		return driver.findElements(by);
	}

	// ************************ action methods
	// ************************************************

	/**
	 * clicks on the element, like button, link, radio button, checkbox
	 * 
	 * @param driver
	 * @param by
	 */
	public static void click(WebDriver driver, By by) { 

		waitForElementHasStoppedMoving(driver, by);

		scrollTo(driver, by);

		waitForWebElementVisible(driver, by);

		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(by));

		log.info("clicking element " + by.toString());
		while (true) {
			try {
				driver.findElement(by).click();
				break;
			} catch (StaleElementReferenceException ex) {
				continue;
			}
		}
	}

	public static void doubleClick(WebDriver driver, By by) {
		Actions actions = new Actions(driver);
		WebElement onElement = findElement(driver, by);
		log.info("performing double click on the element: " + by.toString());
		actions.doubleClick(onElement).build().perform();
	}

	/**
	 * scrolls to the given element's location and performs click
	 * 
	 * @param driver
	 * @param webElement
	 */
	public static void scrollAndClick(WebDriver driver, WebElement webElement) {
		// Scroll the browser to the element's Y position
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, " + webElement.getLocation().y + ")");
		// Click the element
		int iAttempts = 0;
		while (iAttempts < 5) {
			try {
				webElement.click();
				break;
			} catch (Exception e) {
			}
			iAttempts++;
		}
	}

	public static void scrollTo(WebDriver driver, By by) {
		// Scroll the browser to the element's Y position
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", findElement(driver, by));
	}

	public static void scrollTo(WebDriver driver, WebElement ele) {
		// Scroll the browser to the element's Y position
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
	}

	public static void scrollDown(WebDriver driver, int noOfScrolls) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		for (int i = 1; i <= noOfScrolls; i++) {
			executor.executeScript("window.scrollBy(" + i + ",600)", "");
			sleep(1000);
		}
	}

	/**
	 * enters the text into the textbox
	 * 
	 * @param driver
	 * @param by
	 * @param data
	 *            to be entered into the textbox
	 */
	public static void enterInput(WebDriver driver, By by, String data) {
		waitForWebElementVisible(driver, by);

		WebElement textBox = findElement(driver, by);
		if (textBox.getAttribute("type") != null) {
			if (!textBox.getAttribute("type").equals("file")) {
				textBox.clear();
			}
		}
		log.info("typing " + data + " in " + by.toString());
		textBox.sendKeys(data);
	}

	/**
	 * selects item from the dropDown
	 * 
	 * @param driver
	 * @param by
	 * @param item
	 *            to be selected from the dropDown
	 */
	public static void selectItemFromListBox(WebDriver driver, By by, String item) {
		waitForWebElementVisible(driver, by);

		log.info("selecting " + item + " from dropdown " + by.toString());
		Select select = new Select(driver.findElement(by));
		select.selectByVisibleText(item);
	}

	/*
	 * selects item from the dropDown
	 * 
	 * @param driver
	 * 
	 * @param WebElement
	 * 
	 * @param item to be selected from the dropDown
	 */
	public static void selectItemFromListBox(WebDriver driver, WebElement ele, String item) {  
		log.info("selecting " + item + " from dropdown " + ele.toString());
		Select select = new Select(ele);
		select.selectByVisibleText(item);
	}

	public static void switchToIFrame(WebDriver driver, String iframe) {
		waitForPageContains(driver, iframe);

		log.info("switcing to iframe " + iframe);
		driver.switchTo().frame(iframe);
	}

	public static void switchToIFrame(WebDriver driver, By by) {
		waitForPageContainsWebElement(driver, by);

		log.info("switcing to iframe " + by.toString());
		driver.switchTo().frame(findElement(driver, by));
	}

	public static void switchToDefaultContent(WebDriver driver) {
		log.info("switching to default content");
		driver.switchTo().defaultContent();
	}

	/**
	 * switches to window alert dialog box
	 */
	public static Alert switchToWindowAlert(WebDriver driver) {
		long end = System.currentTimeMillis() + MAX_WAIT_MILLI_SEC;
		Alert alert = null;
		while (System.currentTimeMillis() < end) {
			try {
				alert = driver.switchTo().alert();
				if (alert != null) {
					return alert;
				}
			} catch (NoAlertPresentException e) {
				// fine
			}
		}
		return alert;
	}

	/**
	 * closes the window alert dialog box by clicking on OK button
	 */
	public static void acceptWindowAlert(Alert alert) {
		alert.accept();
	}

	/**
	 * closes the window alert dialog box by clicking on Cancel button
	 */
	public static void cancelWindowAlert(Alert alert) {
		alert.dismiss();
	}

	/**
	 * loads a new web page in the current browser window
	 * 
	 * @param url
	 *            The URL to load. It is best to use a fully qualified URL
	 */
	public static void loadPage(WebDriver driver, String url) {
		log.info("navigating to the URL: " + url);
		driver.get(url);
	}

	/**
	 * opens a new blank tab
	 */
	public static void openNewTab(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.open('', '_blank')");
	}

	/**
	 * return an opaque handle to this window that uniquely identifies it within
	 * this driver instance. This can be used to switch to this window at a
	 * later date
	 * 
	 */
	public static String getCurrentWindowHandle(WebDriver driver) {
		return driver.getWindowHandle();
	}

	/**
	 * Return a set of window handles which can be used to iterate over all open
	 * windows of this webdriver instance by passing them to
	 * #switchTo().window(String)
	 * 
	 */
	public static Set<String> getAllWindowHandles(WebDriver driver) {
		return driver.getWindowHandles();
	}

	/**
	 * switches the focus of future commands for this driver to the window with
	 * the given name/handle.
	 * 
	 * @param driver
	 * @param nameOrHandle
	 *            (which got from driver.getWindowHandle())
	 */
	public static void switchToWindow(WebDriver driver, String nameOrHandle) {
		driver.switchTo().window(nameOrHandle);
	}

	/**
	 * closes the current window
	 */
	public static void closeWindow(WebDriver driver) {
		driver.close();
	}

	/**
	 * copies the specified string to the clipboard
	 * 
	 * @param str
	 *            The string to be copied
	 */
	public static void copyToClipboard(String str) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection string = new StringSelection(str);
		clipboard.setContents(string, null);
	}

	/**
	 * pastes data from clip board into the element provided by locator
	 * 
	 * @param driver
	 * @param by
	 *            identifier of the field to which data has to be pasted
	 */
	public static void pasteFromClipboard(WebDriver driver, By by) {
		WebElement searchField = findElement(driver, by);
		searchField.clear();
		searchField.sendKeys(Keys.LEFT_CONTROL, "v");
	}

	/**
	 * moves the mouse to the middle of the element
	 * 
	 * @param driver
	 * @param by
	 */
	public static void mouseHover(WebDriver driver, By by) {

		Robot robot;
		try {
			//setting the mouse location
			robot = new Robot();
			robot.mouseMove(0, 0);
			sleep(500);
		} catch (AWTException e) {
			log.info(e.getMessage());
		}

		log.info("performing mouse hover action on " + by.toString());
		Actions action = new Actions(driver);
		action.moveToElement(findElement(driver, by)).perform();
	}

	/**
	 * moves the mouse to the middle of the element
	 * 
	 * @param driver
	 * @param by
	 */
	public static void mouseHover(WebDriver driver, WebElement ele) {
		Robot robot;
		try {
			robot = new Robot();
			robot.mouseMove(0, 0);
			sleep(100);
		} catch (AWTException e) {
			log.info(e.getMessage());
		}
		log.info("performing mouse hover action on " + ele.toString());
		Actions action = new Actions(driver);
		action.moveToElement(ele).build().perform();
	}

	public static String getCurrentURL(WebDriver driver) {
		return driver.getCurrentUrl();
	}

	/**
	 * takes screenshot of the particular element present in the web page
	 * 
	 * @param driver
	 * @param element
	 * @return image file
	 * @throws IOException
	 */
	public static File captureElementScreenShot(WebDriver driver, WebElement element) throws IOException {
		File screenShot = captureScreenshot(driver);

		// read the image
		BufferedImage img = ImageIO.read(screenShot);

		Point point = element.getLocation();
		int width = element.getSize().getWidth();
		int height = element.getSize().getHeight();

		// crop the image
		BufferedImage dest = img.getSubimage(point.getX(), point.getY(), width, height);
		ImageIO.write(dest, "png", screenShot);
		return screenShot;
	}

	/**
	 * takes screenshot of the web page
	 * 
	 * @param driver
	 * @return
	 */
	public static File captureScreenshot(WebDriver driver) {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	}

	// ***************************** wait methods
	// **********************************************************

	/**
	 * sleeps for specified time
	 * 
	 * @param milliSecs
	 *            num of milli secs
	 */
	public static void sleep(int milliSecs) {
		try {
			log.info("sleeping for " + milliSecs + " milli secs");
			Thread.sleep(milliSecs);
		} catch (InterruptedException e) {
			log.info(e.getMessage());
		}
	}

	public static boolean waitForPageContains(WebDriver driver, String str) {
		boolean result = false;
		long end = System.currentTimeMillis() + MAX_WAIT_MILLI_SEC;
		while (System.currentTimeMillis() < end) {
			result = driver.getPageSource().contains(str);
			if (result == true) {
				break;
			}
		}
		return result;
	}

	public static boolean waitForPageNotContains(WebDriver driver, String str) {
		boolean result = true;
		long end = System.currentTimeMillis() + MAX_WAIT_MILLI_SEC;
		while (System.currentTimeMillis() < end) {
			result = driver.getPageSource().contains(str);
			if (result == false) {
				break;
			}
		}
		return result;
	}

	public static WebElement waitForPageContainsWebElement(WebDriver driver, By by) {
		long end = System.currentTimeMillis() + MAX_WAIT_MILLI_SEC;
		WebElement webElement = null;
		while (System.currentTimeMillis() < end) {
			try {
				webElement = driver.findElement(by);
				if (webElement != null) {
					break;
				}
			} catch (NoSuchElementException e) {
				// fine
			}
		}
		return webElement;
	}

	public static boolean waitForPageNotContainsWebElement(WebDriver driver, By by, long... waitTime) {
		long end;
		if (waitTime == null || waitTime.length == 0) {
			end = System.currentTimeMillis() + MAX_WAIT_MILLI_SEC;
		} else {
			end = System.currentTimeMillis() + waitTime[0];
		}
		while (System.currentTimeMillis() < end) {
			try {
				driver.findElement(by);
			} catch (NoSuchElementException e) {
				return true;
			}
		}
		return false;
	}

	public static List<WebElement> waitForPageContainsWebElements(WebDriver driver, By by) {
		long end = System.currentTimeMillis() + MAX_WAIT_MILLI_SEC;
		List<WebElement> webElementList = null;
		while (System.currentTimeMillis() < end) {
			try {
				webElementList = driver.findElements(by);
				if (webElementList != null) {
					break;
				}
			} catch (NoSuchElementException e) {
				// fine
			}
		}
		return webElementList;
	}

	public static boolean waitForWebElementVisible(WebDriver driver, By by, long... waitTime) {
		boolean result = false;
		long end;
		if (waitTime == null || waitTime.length == 0) {
			end = System.currentTimeMillis() + MAX_WAIT_MILLI_SEC;
		} else {
			end = System.currentTimeMillis() + waitTime[0];
		}

		while (System.currentTimeMillis() < end) {
			try {
				result = driver.findElement(by).isDisplayed();
				if (result == true) {
					break;
				}
			} catch (NoSuchElementException e) {
				continue;
			} catch (StaleElementReferenceException e) {
				// might be element is not attached to the page document, wait
				// for it to load
				continue;
				// Base exception
			} catch (Exception e) {
				continue;
			}
		}
		return result;
	}

	public static boolean waitForWebElementNotVisible(WebDriver driver, By by) {
		boolean result = false;
		long end = System.currentTimeMillis() + MAX_WAIT_MILLI_SEC;
		while (System.currentTimeMillis() < end) {
			try {
				result = driver.findElement(by).isDisplayed();
				if (result == false) {
					break;
				}
			} catch (Exception e) {
				result = false; // might be visible in previous iteration
				break; // fine
			}
		}
		return result;
	}

	/**
	 * @param driver
	 * @param by
	 * @param waitTime
	 * @return true if element is not visible or else false in a given time
	 */
	public static boolean waitForWebElementNotVisible(WebDriver driver, By by, long... waitTime) {
		boolean result = false;
		long end;
		if (waitTime == null || waitTime.length == 0) {
			end = System.currentTimeMillis() + MAX_WAIT_MILLI_SEC;
		} else {
			end = System.currentTimeMillis() + waitTime[0];
		}
		while (System.currentTimeMillis() < end) {
			try {
				if (driver.findElement(by).isDisplayed() == false) {
					result = true;
					break;
				}
			} catch (Exception e) {
				break; // fine
			}
		}
		return result;
	}

	public static boolean waitForWebElementEnabled(WebDriver driver, By by) {
		boolean result = false;
		long end = System.currentTimeMillis() + MAX_WAIT_MILLI_SEC;
		while (System.currentTimeMillis() < end) {
			try {
				result = driver.findElement(by).isEnabled();
				if (result == true) {
					break;
				}
			} catch (NoSuchElementException e) {
				continue;
			} catch (StaleElementReferenceException e) {
				// might be element is not attached to the page document, wait
				// for it to load
				continue;
			} catch (Exception e) {
				// Base exception
				continue;
			}
		}
		return result;
	}

	public static void waitForWebElementToBeClickable(WebDriver driver, By by) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.elementToBeClickable(by));
	}

	/**
	 * waits until the element is stopped moving
	 */
	public static void waitForElementHasStoppedMoving(WebDriver driver, By by) {
		final WebElement element = findElement(driver, by);
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				Point initialLocation = ((Locatable) element).getCoordinates().inViewPort();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Point finalLocation = ((Locatable) element).getCoordinates().inViewPort();
				return initialLocation.equals(finalLocation);
			}
		};

		try {
			Wait<WebDriver> wait = new WebDriverWait(driver, 30);
			wait.until(expectation);
		} catch (Throwable error) {
			error.printStackTrace();
			log.error("failed while waiting for element to stop moving", error);
		}
	}

	/**
	 * performs click-and-hold at the location of the source element, moves by a
	 * given offset, then releases the mouse.
	 * 
	 * @param driver
	 * @param by
	 * @param xOffset
	 * @param yOffset
	 */
	public static void dragAndDropBy(WebDriver driver, By by, int xOffset, int yOffset) {
		log.info("dragging element " + by.toString() + " to the offset [" + xOffset + ", " + yOffset + "]");

		Actions actions = new Actions(driver);
		actions.dragAndDropBy(findElement(driver, by), xOffset, yOffset).build().perform();
	}

	public static void dragAndDrop(WebDriver driver, By fromBy, By toBy) {
		log.info("dragging element from: " + fromBy.toString() + " to: " + toBy.toString());

		WebElement from = findElement(driver, fromBy);
		WebElement to = findElement(driver, toBy);

		Actions actions = new Actions(driver);
		actions.dragAndDrop(from, to).build().perform();
	}

	public static boolean isWebElementClickable(WebDriver driver, By by, long timeOutInSeconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(by));

			if (webElement != null)
				return true;
			else
				return false;
		} catch (Throwable error) {
			return false;
		}
	}

	public static boolean isWebElementVisible(WebDriver driver, By by, long timeOutInSeconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

			if (webElement != null)
				return true;
			else
				return false;
		} catch (Throwable error) {
			return false;
		}
	}

	public static boolean waitUntilListBoxHasOptions(WebDriver driver, By by) {
		log.info("waiting to check whether listBox " + by.toString() + " has options");

		Select select = new Select(driver.findElement(by));

		long end = System.currentTimeMillis() + MAX_WAIT_MILLI_SEC;
		while (System.currentTimeMillis() < end) {
			try {
				if (select.getOptions().size() > 0)
					return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public static void selectFileInWindow(String absolutePath) {
		log.info("selecting a file '" + absolutePath + "' in the window for uploading");

		copyToClipboard(absolutePath);
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * executes the javascript on the given web element
	 * 
	 * @param by
	 *            id, class, name or xpath of the web element on which the
	 *            javascript should get execute
	 * @param javascript
	 *            ex: <b>arguments[0].innerHTML="hello world"</b>
	 * @return WebElement Or null (if the javascript has a return value. i.e.,
	 *         if the script deosn't contains a return statement)
	 * 
	 */
	public static Object executeJavascript(WebDriver driver, By by, String javascript) {
		log.info("executing javascript '" + javascript + "' on the element: " + by.toString());
		WebElement webElement = findElement(driver, by);
		return ((JavascriptExecutor) driver).executeScript(javascript, webElement);
	}

	/**
	 * executes the javascript
	 * 
	 * @param javascript
	 *            ex: <b>return document.readyState</b>
	 * @return WebElement Or null (if the javascript has a return value. i.e.,
	 *         if the script deosn't contains a return statement)
	 * 
	 */
	public static Object executeJavascript(WebDriver driver, String javascript) {
		return ((JavascriptExecutor) driver).executeScript(javascript);
	}

	public static void waitForJavascriptToGetExecuted(WebDriver driver) {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};

		try {
			Wait<WebDriver> wait = new WebDriverWait(driver, 45);
			wait.until(expectation);
		} catch (Throwable error) {
			log.error("Timeout waiting for Page Load Request to complete.");
		}
	}

	public static void WaitForElementToBeVisible(WebElement element) {
		TryFindElement(element);
	}

	public static boolean TryFindElement(WebElement element) {
		boolean result = true;
		try {
			int counter = 0;
			while (!element.isDisplayed()) {
				if (counter == 10)
					return false;
				counter += 1;
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			result = false;
		}
		return result;
	}

	public static String getText(WebDriver driver, By by) {
		waitForPageContainsWebElements(driver, by);
		WebElement ele = driver.findElement(by);

		if (ele.getText() != null)
			return ele.getText();
		return ele.getAttribute("value");

	}

}
