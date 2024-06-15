package detect.object;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class ClickCheckboxAction extends Action{
    private String choice;
    private String dom_locator;
    public ClickCheckboxAction(String choice) {
        this.choice = choice;
        this.dom_locator = "";
    }

    public String getChoice() {
        return choice;
    }

    public String getDom_locator() {
        return dom_locator;
    }

    public void setDom_locator(String dom_locator) {
        this.dom_locator = dom_locator;
    }

    @Override
    public void run(WebDriver driver) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(dom_locator)));
        WebElement element = driver.findElement(By.xpath(dom_locator));
        element.click();
    }
}
