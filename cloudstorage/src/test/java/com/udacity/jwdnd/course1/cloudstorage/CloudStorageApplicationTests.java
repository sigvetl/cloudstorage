package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;
	private String firstName = "Sigve";
	private String lastName = "Langnes";
	private String username = "sigvetl";
	private String password = "heksegryte";

	private String baseUrl = "http://localhost:";

	private static WebDriver driver;

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
	@Order(1)
	public void getLoginPage() {
		driver.get(baseUrl + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(2)
	public void getSignupPage(){
		driver.get(baseUrl + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	@Order(3)
	public void getHomePageNotLoggedIn(){
		driver.get(baseUrl + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(4)
	public void registerAndGetHomePageLogout(){
		driver.get(baseUrl + this.port + "/login");
		UserTest userTest = new UserTest(driver);

		userTest.signupAndLogin(firstName, lastName, username, password);

		Assertions.assertEquals("Home", driver.getTitle());

		userTest.logout();

		Assertions.assertEquals("Login", driver.getTitle());
	}

	//Write a test that creates a note, and verifies it is displayed.
	@Test
	@Order(5)
	public void createVerifyNote(){
		driver.get(baseUrl + this.port + "/login");
		UserTest userTest = new UserTest(driver);
		userTest.login(username, password);

		Note note = new Note(driver);

		String testTitle = "title";
		String testDescription = "description";

		note.submitNote(testTitle, testDescription);

		note.verifyNote(testTitle, testDescription);
	}

	//Write a test that edits an existing note and verifies that the changes are displayed.
	@Test
	@Order(6)
	public void createEditVerifyNote(){
		driver.get(baseUrl + this.port + "/login");
		UserTest userTest = new UserTest(driver);
		userTest.login(username, password);

		Note note = new Note(driver);

		String editTitle = "changed";
		String editDescription = "new description";

		note.editNote(editTitle, editDescription);

		note.verifyNote(editTitle, editDescription);
	}

	//Write a test that deletes a note and verifies that the note is no longer displayed.
	@Test
	@Order(7)
	public void deleteVerifyNote(){
		driver.get(baseUrl + this.port + "/login");
		UserTest userTest = new UserTest(driver);
		userTest.login(username, password);

		Note note = new Note(driver);

		note.deleteNote();

		Assertions.assertFalse(note.isElementPresent("noteTitle"));
	}

	//Write a test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
	@Test
	@Order(8)
	public void testCredentialCreation(){
		driver.get(baseUrl + this.port + "/login");
		UserTest userTest = new UserTest(driver);
		userTest.login(username, password);

		Credential credential = new Credential(driver);

		String[] urls = {"www.vg.no", "www.dagbladet.no", "login.google.com"};
		String[] usernames = {"sigg", "user", "googol"};
		String[] passwords = {"dustefisk13", "password", "googolPlex"};

		credential.createMultipleCredentials(urls, usernames, passwords);

		credential.verifyCredential(urls, usernames, passwords);
	}

	//Write a test that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
	@Test
	@Order(9)
	public void editCredentials(){
		driver.get(baseUrl + this.port + "/login");
		UserTest userTest = new UserTest(driver);
		userTest.login(username, password);

		Credential credential = new Credential(driver);

		String[] passwords = {"dustefisk13", "password", "googolPlex"};

		String[] newUrls = {"www.hotspot.com", "http://microservices.com", "backdoor.facebook.com"};
		String[] newUsernames = {"exampleUser", "newUser", "old_username"};
		String[] newPasswords = {"myPass98", "easypasstoremember", "asdfghjkl"};

		credential.editCredentials(newUrls, newUsernames, newPasswords, passwords);

		credential.verifyCredential(newUrls, newUsernames, newPasswords);
	}

	//Write a test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
	@Test
	@Order(10)
	public void deleteCredentials(){
		driver.get(baseUrl + this.port + "/login");
		UserTest userTest = new UserTest(driver);
		userTest.login(username, password);

		Credential credential = new Credential(driver);

		credential.deleteCredential();

		Assertions.assertFalse(credential.isElementPresent(driver, "credentialUrl"));
	}
}
