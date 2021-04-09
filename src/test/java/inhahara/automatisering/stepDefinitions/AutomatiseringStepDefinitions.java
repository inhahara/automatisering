package inhahara.automatisering.stepDefinitions;

import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AutomatiseringStepDefinitions {
    // För korrekt arbete med ChromeDriver måste du installera Selenium ChromeDriver
	// Vi kan inte använda en specifik sökväg till chromedriver och vi kan inte lägga till chromedriver till projektet eftersom det beror på OS, Chrome-version ...
    // Se https://github.com/SeleniumHQ/selenium/wiki/ChromeDriver
    private WebDriver driver;
    // Unikt nummer: Används för att skapa unika email och username 
    private long emailPostfix;

    @Before
    public void initiate() {
        // Öppna browser
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Skapa unik email-postfix som aktuell tid i millisekunder
        emailPostfix = System.currentTimeMillis();
    }

    @After
    public void tearDown() {
        // stäng driver efter testet
        driver.close();
    }
    
    @Given("I go to the Mailchimp Sign Up site")
    public void i_go_to_signup_site() {
    	// Öppna registreringssidan och vänta med att öppna
        driver.get("https://login.mailchimp.com/signup/");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }
    

    @When("I have entered {string} into the sign up page")
    public void i_have_entered_email_into_the_signup_page(String emailLocalPart) {
        // ange e-post om det finns 
        if (emailLocalPart.length() > 0) { 
            // Hitta e-mail-ange element by id
            WebElement emailElement = driver.findElement(By.id("email"));
            // Ange genererade unik e-mail: <local-part><unique-postfix>@<domain>
            emailElement.sendKeys(emailLocalPart + emailPostfix + "@" + "somedomain.com");
        }
    }

    @When("I have also entered {string} into the sign up page")
    public void i_have_entered_username_into_the_signup_page(String username) {
        // Hitta username ange element by id
        WebElement usernameElement = driver.findElement(By.id("new_username"));
        if (username.indexOf("@") > 0) { 
            // Ange email som username från test data
            usernameElement.sendKeys(username);
        } else {
            // Ange genererade unik username: <username><unique-postfix>
            usernameElement.sendKeys(username + emailPostfix);
        }
    }

    @When("I have entered {string} into the sign up page too")
    public void i_have_entered_password_into_the_signup_page(String password) {
        // Hitta ange - element för lösenord by id
        WebElement passwordElement = driver.findElement(By.id("new_password"));
        // Ange lösenord
        passwordElement.sendKeys(password);
    }
    
    @When("I press Accept All Cookies")
    public void i_press_accept_all_cookies() {
        // Vi måste stänga Cookies Popup eftersom det döljer registreringsknappen på små skärmar
        // Hitta Accept all cookies knappen by id
        WebElement cookiesButton = driver.findElement(By.id("onetrust-accept-btn-handler"));
        // Tryck på cookies knappen
        cookiesButton.click();
    }

    @When("I press Sign Up")
    public void i_press_signup() {
    	// Vänta 
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        // Hitta signup knappen by id
        WebElement signupButton = driver.findElement(By.id("create-account"));
        // Tryck på knappen
        signupButton.click();
    }

    @Then("The result of Sign Up should be {string}")
    public void the_result_should_be_on_the_screen(String result) {
        // Vänta 
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        // Hämta url av browser
        String url = driver.getCurrentUrl(); 
        
        if (result.equals("Success")) { // Kontrollera resultatet "Success"
            // Om signup är success browser omdirigerar till https://login.mailchimp.com/signup/success...
            Assert.assertTrue(url.indexOf("https://login.mailchimp.com/signup/success") >= 0);
        } else if (result.equals("Failure")) { // Kontrollera resultatet "Failure"
            // Om signup är failure browser omdirigerar till https://login.mailchimp.com/signup/post
            Assert.assertTrue(url.indexOf("https://login.mailchimp.com/signup/post") >= 0);
        } else { 
            Assert.assertTrue(false); // Påstå fel om felaktig test input
        }
    }
}
