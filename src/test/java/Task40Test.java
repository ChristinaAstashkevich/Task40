import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class Task40Test {
    private WebDriver driver;

    @BeforeTest
    public void BrowserSetup() {
        System.setProperty("webdriver.chrome.driver", ".\\drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @AfterTest
    public void BrowserClose() {
        driver.quit();
    }

    @Test
    public void fillingLoginForm() {

        driver.get("https://192.168.100.26/");

        //Type of waiter is Explicity wait.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement usernameField = driver.findElement(By.xpath("//*[@id='Username']"));
        WebElement passwordField = driver.findElement(By.xpath("//*[@id='Password']"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id='SubmitButton']"));

        usernameField.sendKeys("EugenBorisik");
        passwordField.sendKeys("qwerty12345");
        loginButton.click();

        //WebDriverWait wait = new WebDriverWait(WebDriver driver, long timeOutInSeconds);
        (new WebDriverWait(driver, 10)).
                until(ExpectedConditions.elementToBeClickable(By.cssSelector(".sign-out-span>a")));
        WebElement signOutSpan = driver.findElement(By.cssSelector(".sign-out-span>a"));
        Assert.assertNotNull(signOutSpan);
    }

    @Test
    public void ExplicitCondTest() {
        driver.get("https://192.168.100.26/");

        (new WebDriverWait(driver, 10)).until(ExpectedConditions.titleIs("RMSys - Sign In"));

        WebElement usernameField = driver.findElement(By.xpath("//*[@id='Username']"));
        WebElement passwordField = driver.findElement(By.xpath("//*[@id='Password']"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id='SubmitButton']"));

        usernameField.sendKeys("EugenBorisik");
        passwordField.sendKeys("qwerty12345");

        (new WebDriverWait(driver, 10)).
                until(ExpectedConditions.textToBePresentInElementValue(usernameField, "EugenBorisik"));
        (new WebDriverWait(driver, 10)).
                until(ExpectedConditions.textToBePresentInElementValue(passwordField, "qwerty12345"));
        loginButton.click();

        (new WebDriverWait(driver, 10)).until(ExpectedConditions.titleIs("RMSys - Home"));
        WebElement officeTab = driver.findElement(By.xpath("//*[@id='officeMenu']"));

        waitForLoad(driver);
        officeTab.click();

        (new WebDriverWait(driver, 15, (long) 2.7)).
                until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='input-search']")));
        WebElement signOutSpan = driver.findElement(By.cssSelector(".sign-out-span>a"));

        Assert.assertNotNull(signOutSpan);
    }

    @Test(dataProvider = "IncorrectDataSets", dataProviderClass = DataProvider.class)
    public void InvalidCredsDDTTest(String username, String password) {
        driver.get("https://192.168.100.26/");

        WebElement usernameField = driver.findElement(By.xpath("//*[@id='Username']"));
        WebElement passwordField = driver.findElement(By.xpath("//*[@id='Password']"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id='SubmitButton']"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

        WebElement ErrorMessage = driver.findElement(By.xpath("//*[@class='validation-summary-errors']//li"));

        Assert.assertEquals(ErrorMessage.getText(), "*Invalid credentials.");
    }

    @Test(dataProvider = "IncorrectTestUsers", dataProviderClass = DataProvider.class)
    public void PasswordRequiredDDTTest(String username) {
        driver.get("https://192.168.100.26/");

        WebElement usernameField = driver.findElement(By.xpath("//*[@id='Username']"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id='SubmitButton']"));

        usernameField.sendKeys(username);
        loginButton.click();

        WebElement ErrorMessage = (new WebDriverWait(driver, 10).
                until(ExpectedConditions.
                        presenceOfElementLocated(By.xpath("//*[@id='password-box-validation']/span"))));

        Assert.assertEquals(ErrorMessage.getText(), "Password is required");
    }

    @Test(dataProvider = "CorrectTestUser", dataProviderClass = DataProvider.class)
    public void CorrectLoginDDTTest(String username, String password) {
        driver.get("https://192.168.100.26/");

        WebElement usernameField = driver.findElement(By.xpath("//*[@id='Username']"));
        WebElement passwordField = driver.findElement(By.xpath("//*[@id='Password']"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id='SubmitButton']"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

        (new WebDriverWait(driver, 10)).until(ExpectedConditions.
                elementToBeClickable(By.cssSelector(".sign-out-span>a")));

        WebElement signOutSpan = driver.findElement(By.cssSelector(".sign-out-span>a"));

        Assert.assertNotNull(signOutSpan);
    }

    @Test
    public void iFrameTest() {
        driver.get("https://the-internet.herokuapp.com/iframe");

        WebElement boldButton = (new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='mceu_3']/button"))));

        driver.switchTo().frame("mce_0_ifr");

        WebElement body = driver.findElement(By.xpath("//*[@id='tinymce']"));

        Actions action = new Actions(driver);

        action.click(body);
        action.sendKeys(Keys.CONTROL).
                sendKeys(Keys.chord("A")).
                sendKeys(Keys.BACK_SPACE).
                build().perform();
        action.click(body).sendKeys("Hello ").build().perform();

        driver.switchTo().defaultContent();
        action.click(boldButton).build().perform();

        driver.switchTo().frame("mce_0_ifr");
        action.click(body);
        action.sendKeys("world!").build().perform();

       Assert.assertEquals(body.getText() ,"Hello \uFEFFworld!");
    }

    @Test
    public void AlertAcceptTest() {
        driver.get("https://the-internet.herokuapp.com/javascript_alerts");

        WebElement firstButton = driver.findElement(By.xpath("//*[contains(text(),'Click for JS Alert')]"));

        firstButton.click();
        Alert alert = (new WebDriverWait(driver, 10).until(ExpectedConditions.alertIsPresent()));

        alert.accept();
        WebElement result = (new WebDriverWait(driver, 10).
                until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='result']"))));

        Assert.assertEquals(result.getText(), "You successfuly clicked an alert");
    }

    @Test
    public void AlertDismissTest() {
        driver.get("https://the-internet.herokuapp.com/javascript_alerts");

        WebElement firstButton = driver.findElement(By.xpath("//*[contains(text(),'Click for JS Confirm')]"));

        firstButton.click();
        Alert alert = (new WebDriverWait(driver, 10).until(ExpectedConditions.alertIsPresent()));

        alert.dismiss();
        WebElement result = (new WebDriverWait(driver, 10).
                until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='result']"))));

        Assert.assertEquals(result.getText(), "You clicked: Cancel");
    }

    @Test
    public void AlertClickPromptTest() {
        driver.get("https://the-internet.herokuapp.com/javascript_alerts");

        WebElement promptButton = driver.findElement(By.xpath("//*[contains(text(),'Click for JS Prompt')]"));

        promptButton.click();
        Alert alert = (new WebDriverWait(driver, 10).until(ExpectedConditions.alertIsPresent()));

        alert.sendKeys("test");
        alert.accept();
        WebElement result = (new WebDriverWait(driver, 10).
                until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='result']"))));

        Assert.assertEquals(result.getText(), "You entered: test");
    }

    void waitForLoad(WebDriver driver) {
        new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }
}
