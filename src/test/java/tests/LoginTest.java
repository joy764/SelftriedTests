package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest extends BaseTest {

    // Locators
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By roleField = By.id("role");
    private final By rememberMeCheckbox = By.id("rememberMe");
    private final By loginButton = By.xpath("//*[@id='loginForm']/div[5]/button");


    @BeforeMethod(alwaysRun = true)
    public void openPage() {
        String localHtmlPath = "file:///home/jack/Desktop/project/webpage.html";
        getDriver().get(localHtmlPath);
        log.info("Opened login page: " + localHtmlPath);
    }

    @DataProvider(name = "loginData")
    public Object[][] getData() {
        return new Object[][]{
                {"admin", "password123", "Admin", "Login successful!"},
                {"user", "wrongpass", "User", "Login failed!"},
                // This test will intentionally fail
                {"admin", "password123", "User", "Login failed!"}
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, String role, String expectedAlert) {
        log.info("Testing login with username='{}', password='{}', role='{}'", username, password, role);

        getDriver().findElement(usernameField).clear();
        getDriver().findElement(usernameField).sendKeys(username);

        getDriver().findElement(passwordField).clear();
        getDriver().findElement(passwordField).sendKeys(password);

        // Use Select for dropdown
        Select roleSelect = new Select(getDriver().findElement(roleField));
        roleSelect.selectByVisibleText(role);

        getDriver().findElement(rememberMeCheckbox).click();
        getDriver().findElement(loginButton).click();

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        String alertText = wait.until(ExpectedConditions.alertIsPresent()).getText();

        log.info("Alert displayed: {}", alertText);
        Assert.assertEquals(alertText, expectedAlert);

        getDriver().switchTo().alert().accept();
        log.info("Alert accepted, test completed.");
    }
}
