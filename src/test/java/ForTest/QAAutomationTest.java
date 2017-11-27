package ForTest;
// first commit to new branch
// second

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QAAutomationTest {
    private static FirefoxDriver driver;
    WebElement element;

    @BeforeClass
    public static void openBrowser(){
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void passTheTest(){

        System.out.println("Starting test " + new Object(){}.getClass().getEnclosingMethod().getName());
        driver.get("https://docs.google.com/forms/d/e/1FAIpQLSeY_W-zSf2_JxR4drhbgIxdEwdbUbE4wXhxHZLgxZGiMcNl7g/viewform?c=0&w=1");

        // Filling first step questions
        List<WebElement> itemList = driver.findElements(By.xpath("/.//*[text()='Check this']/../../../div[@role='checkbox']"));
        for (int i=0; i<itemList.size();i++)
            itemList.get(i).click();

        LocalDate dt = LocalDate.now().plusDays(6);
        String day = new String(dt.format(DateTimeFormatter.ofPattern("dd")));
        String month = new String(dt.format(DateTimeFormatter.ofPattern("MM")));
        String year = new String(dt.format(DateTimeFormatter.ofPattern("YYYY")));
        WebElement date = driver.findElement(By.xpath("//div[@class='freebirdFormviewerViewItemsDateDateInputs']"));
        List<WebElement> dateList = date.findElements(By.xpath("//input[@class='quantumWizTextinputPaperinputInput exportInput']"));
        dateList.get(0).sendKeys(day);
        dateList.get(1).sendKeys(month);
        dateList.get(2).sendKeys(year);

        String nextButtonFirstStep = "//div[@class='freebirdFormviewerViewNavigationButtons']/div";
        driver.findElement(By.xpath(nextButtonFirstStep)).click();
        try {
            driver.findElement(By.xpath("//div[contains(@class, 'HasError')]"));
        } catch (Exception e){
            System.out.println("Error message wasn't found.");
            Assert.assertNull(e.getMessage());
        }

        String elCurrMonth = "//div[@class='freebirdFormviewerViewItemsTextItemWrapper']/.//input";
        driver.findElement(By.xpath(elCurrMonth)).sendKeys(dt.getMonth().toString());

        new WebDriverWait(driver, 1)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'HasError')]")));
        driver.findElement(By.xpath(nextButtonFirstStep)).click();

        // Filling second step questions
        String movies = "House M.D., Friends, Now you see me";
        String color = "Red";
        driver.findElement(By.xpath("//textarea[@class='quantumWizTextinputPapertextareaInput exportTextarea']")).sendKeys(movies);
        driver.findElement(By.xpath("//div[@class='quantumWizMenuPaperselectOptionList']")).click();

        Actions actions = new Actions(driver);// to finish
        String pathToColor = String.format("//div[@class='exportSelectPopup quantumWizMenuPaperselectPopup']/div[@data-value='%s']", color);
        actions.moveToElement(driver.findElement(By.xpath(pathToColor))).perform();
        driver.findElement(By.xpath(pathToColor)).click();
        driver.findElement(By.xpath("//div[@class='freebirdFormviewerViewNavigationButtons']/div[1]")).click();

        // Returned to first step section
        WebElement currMonth = driver.findElement(By.xpath(elCurrMonth));
        String oldValue = currMonth.getAttribute("value");
        currMonth.clear();
        driver.findElement(By.xpath(elCurrMonth)).sendKeys(new StringBuilder(oldValue).reverse().toString());
        driver.findElement(By.xpath(nextButtonFirstStep)).click();

        //Assertion of second step section
        String actualMovies = driver.findElement(By.xpath("//textarea[@class='quantumWizTextinputPapertextareaInput exportTextarea']"))
                .getAttribute("value");
        Assert.assertTrue("Actual and expected strings are not equal.", actualMovies.equals(movies));

        String actualColor = driver.findElement(By.xpath("//div[@class='quantumWizMenuPaperselectOptionList']/./div[@aria-selected='true']"))
                .getAttribute("data-value");
        Assert.assertTrue("Actual and expected strings are not equal.", actualColor.equals(color));
        driver.findElement(By.xpath("//div[@class='freebirdFormviewerViewNavigationButtons']/div[2]")).click();

        //Third Step section
        driver.findElement(By.xpath("//div[@aria-label='Yes']")).click();
        driver.findElement(By.xpath("//div[@class='freebirdFormviewerViewNavigationButtons']/div[2]")).click();
        System.out.println("Ending test " + new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @AfterClass
    public static void closeBrowser(){
        driver.quit();
    }
}