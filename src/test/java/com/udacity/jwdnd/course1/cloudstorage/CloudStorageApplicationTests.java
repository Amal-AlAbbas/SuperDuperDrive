package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.JVM)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignUpPage(){
		driver.get("http:localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}
	@Test
	public void getUnauthorizedHomePage(){
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login",driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}
	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	//to create credential
	@Test
	public void credentialCreation(){
		getLoginPage();
		credentialCreation();
		try {
			Thread.sleep(500);
		}catch (InterruptedException e){
			e.printStackTrace();
		}

		Assertions.assertEquals("https://learn.udacity.com/nanodegrees/nd035",driver.findElement(By.id("urlDisplay")).getText());
		Assertions.assertEquals("Amal",driver.findElement(By.id("usernameDisplay")).getText());
		Assertions.assertEquals("Amal-123",driver.findElement(By.id("passwordDisplay")).getText());
	}

	//to edit credential
	@Test
	public void editCredential() throws InterruptedException{
		credentialCreation();
		WebDriverWait webDriverWait = new WebDriverWait(driver,2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editcredentialsbtn")));
		WebElement editcredentialsButton = driver.findElement(By.id("editcredentialsbtn"));
		editcredentialsButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement url = driver.findElement(By.id("credential-url"));
		url.click();
		url.clear();
		url.sendKeys("https://learn.udacity.com/nanodegrees/nd035");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		String password = driver.findElement(By.id("credential-password")).getAttribute("value");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSubmit")));
		WebElement credentialSubmit = driver.findElement(By.id("credentialSubmit"));
		credentialSubmit.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertTrue(driver.findElement(By.id("table-card-url")).getText().contains("https://learn.udacity.com/nanodegrees/nd035"));
		Assertions.assertNotEquals(driver.findElement(By.id("table-card-password")).getText(), password);

		Thread.sleep(3000);
	}

	//to delete credential
	@Test
	public void deletecredential() throws InterruptedException{
		credentialCreation();

		WebDriverWait webDriverWait = new WebDriverWait(driver,2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deletebtnCredential")));
		WebElement deleteCredentialButton = driver.findElement(By.id("deletebtnCredential"));
		deleteCredentialButton.click();

		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialList = credentialTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(0,credentialList.size());
		Thread.sleep(3000);
	}

	//to create note
	@Test
	public void createNote() throws InterruptedException{
		doMockSignUp("Amal", "AlAbbas", "Amal-23", "Amal-123");

		doLogIn("Amal-23", "Amal-123");

		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		WebDriverWait webDriverWait = new WebDriverWait(driver,2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
		Assertions.assertTrue(driver.findElement(By.id("nav-notes")).isDisplayed());

		WebElement addNoteButton = driver.findElement(By.id("addNotebtn"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement title = driver.findElement(By.id("note-title"));
		title.click();
		title.sendKeys("Test Note");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("Testing a create note ...");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSubmit")));
		WebElement noteSubmit = driver.findElement(By.id("noteSubmit"));
		noteSubmit.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(driver.findElement(By.id("table-note-title")).getText().contains("Test Note"));

		Thread.sleep(3000);
	}

	//to edit note
	@Test
	public void editNote() throws InterruptedException{
		createNote();
		WebDriverWait webDriverWait = new WebDriverWait(driver,2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editbtn")));
		WebElement editNote = driver.findElement(By.id("editbtn"));
		editNote.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement description = driver.findElement(By.id("note-description"));
		description.click();
		description.clear();
		description.sendKeys("Testing a edited note ...");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSubmit")));
		WebElement noteSubmit = driver.findElement(By.id("noteSubmit"));
		noteSubmit.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(driver.findElement(By.id("table-card-description")).getText().contains("edited description"));

	}

	//to delete note
	@Test
	public void deletenote() throws InterruptedException{
		WebDriverWait webDriverWait = new WebDriverWait(driver,2);
		createNote();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deletNotebtn")));
		WebElement deleteNote = driver.findElement(By.id("deletNotebtn"));
		deleteNote.click();

		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("Tbode"));
		Assertions.assertEquals(0,notesList.size());
	}




}
