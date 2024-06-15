package detect.object;

import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HoverAction extends Action{
    String text_locator;
    String dom_locator;
    public HoverAction(String text_locator) {
        this.text_locator = text_locator;
        this.dom_locator = "";
    }

    public String getText_locator() {
        return text_locator;
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
        Actions action = new Actions(driver);
        action.moveToElement(element).perform();
    }
}
