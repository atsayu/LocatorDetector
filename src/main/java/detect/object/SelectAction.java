package detect.object;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SelectAction extends Action{
    private String dom_locator;
    private String choice;

    private String question;
    public SelectAction(String question, String choice) {
        this.choice = choice;
        this.question = question;
        this.dom_locator = "";
    }

    @Override
    public void setDom_locator(String dom_locator) {
        this.dom_locator = dom_locator;
    }

    @Override
    public String getDom_locator() {
        return dom_locator;
    }

    public String getChoice() {
        return choice;
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public void run(WebDriver driver) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(dom_locator)));
        Select element = new Select(driver.findElement(By.xpath(dom_locator)));
        element.selectByVisibleText(choice);
    }
}
