import detect.Process;
import detect.object.Action;
import detect.object.ClickAction;
import detect.object.InputAction;
import detect.object.SelectAction;
import jakarta.servlet.ServletOutputStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class APITest {
    public static Element findElement(JSONObject action, String pageSource, Element prevElement, WebDriver driver) {
        String type = action.get("type").toString();
        String describedLocator = null;
        String value = null;
        String question = null;
        String choice = null;
        switch (type) {
            case "click":
                describedLocator = action.get("describedLocator").toString();
                Action cLickAction = new ClickAction(describedLocator);
                Element clickElement = Process.detectElementV2(pageSource, cLickAction, false, prevElement, driver);
                return clickElement;

            case "input":
                describedLocator = action.get("describedLocator").toString();
                value = action.get("value").toString();
                Action inputAction = new InputAction(value, describedLocator);
                Element inputElement = Process.detectElementV2(pageSource, inputAction, false, prevElement, driver);
                return inputElement;
            case "select":
                System.out.println("SELECT");
                question = action.get("question").toString();
                choice = action.get("describedLocator").toString();
                Action selectAction = new SelectAction(question, choice);
                Element selectElement = Process.detectElementV2(pageSource, selectAction, false, prevElement, driver);

                return selectElement;


        }
        return null;
    }

    public static void executeAction(JSONObject action, String locator , WebDriver driver, int index) throws IOException, InterruptedException {
        String type = action.get("type").toString();
        String value = null;
        String answer = null;
        WebElement element  = driver.findElement(By.xpath(locator));
        highlightElementAndTakeScreenShot(element, driver, index);
        switch (type) {
            case "input":
                value = action.get("value").toString();
                element.sendKeys(value);
                break;
            case "click":
                element.click();
                break;
            case "select":
                System.out.println(element.isDisplayed());
                answer = action.get("describedLocator").toString();
                Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
                Select select =  new Select(driver.findElement(By.xpath(locator)));
                select.selectByVisibleText(answer);
                break;

        }
    }

    public static void highlightElementAndTakeScreenShot(WebElement element, WebDriver driver, int index) throws IOException, InterruptedException {
        // Use JavaScript to apply a border style to the element
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String originalStyle = element.getAttribute("style");

//        js.executeScript("arguments[0].setAttribute('style', arguments[0].getAttribute('style') + 'border: 2px solid red !important;');", element);
        String highlightStyle = "border: 2px solid red !important; background: yellow;";


        js.executeScript(
                "var rect = document.createElement('div');" +
                        "rect.id = 'highlightRect';" +
                        "rect.style.border = '3px solid red';" +
                        "rect.style.position = 'absolute';" +
                        "rect.style.pointerEvents = 'none';" +
                        "var rectInfo = arguments[0].getBoundingClientRect();" +
                        "rect.style.left = rectInfo.left + 'px';" +
                        "rect.style.top = rectInfo.top + 'px';" +
                        "rect.style.width = rectInfo.width + 'px';" +
                        "rect.style.height = rectInfo.height + 'px';" +
                        "document.body.appendChild(rect);",
                element);

//        // Apply final highlight
//        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, highlightStyle);
//        // Screenshot

        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destinationFile = new File("screenshot" + index + ".png");
        Files.copy(screenshot.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Revert to the original style
//        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, originalStyle);
        js.executeScript("var rect = document.getElementById('highlightRect'); if (rect) { rect.parentNode.removeChild(rect); }");
        String setMutatedFalseScript = "window.mutated = false;";
        js.executeScript(setMutatedFalseScript);
    }
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        WebDriver driver = new ChromeDriver();
        Map<String, String> locatorsMap = new HashMap<>();
        boolean mutated = false;
        Element prevElement = null;
        List<Action> actionList = new ArrayList<>();
        driver.get("https://www.saucedemo.com/");

        String mutationObserverScript =
                "window.mutated = false;" +
                        "const observerCallback = function(mutationsList) {" +
                        "    window.mutated = true;" +
                        "    console.log('Mutation detected:', mutationsList);" +
                        "};" +
                        "const observer = new MutationObserver(observerCallback);" +
                        "observer.observe(document.body, {" +
                        "    attributes: true," +
                        "    childList: true," +
                        "    subtree: true" +
                        "});" +
                        "console.log('MutationObserver is set up and running...');";
        ((JavascriptExecutor) driver).executeScript(mutationObserverScript);
//        driver.findElement((By.xpath("//*[@id=\"user-name\"]"))).sendKeys("standard_user");
//        mutated = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.mutated;");
//        System.out.println(mutated);
        String pageSource = driver.getPageSource();

        JSONObject outlineJSON = (JSONObject) new JSONParser().parse(new FileReader("outline.json"));
        JSONArray actions = (JSONArray) outlineJSON.get("actions");

        for (int i = 0; i < actions.size(); i++) {
            JSONObject actionJSON = (JSONObject) actions.get(i);
            prevElement = null;
            prevElement = findElement(actionJSON, pageSource, prevElement, driver);
            String describedLocator = actionJSON.get("describedLocator").toString();

            if (prevElement != null) {
                String xpath = Process.getXpath(prevElement);
                locatorsMap.put(describedLocator, xpath);
                executeAction(actionJSON, xpath, driver, i);
            } else {
                System.out.println(i + " action is null");
            }

            //Execute action


            mutated = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.mutated;");
            if (mutated) {
                System.out.println("Mutated for " + i + "action");
                pageSource = driver.getPageSource();
                ((JavascriptExecutor) driver).executeScript("window.mutated = false;");
                Thread.sleep(200);
            }
        }
//        driver.quit();

//        prevElement = Process.detectElement(pageSource, "username", "Input", false, prevElement, actionList,"https://www.saucedemo.com/");
//        locatorsMap.put("username", Process.getXpath(prevElement));
//        actionList.add(new InputAction("standard_user","username", locatorsMap.get("username")));
//        System.out.println(locatorsMap.get("username"));
//        driver.findElement(By.xpath(locatorsMap.get("username"))).sendKeys("standard_user");
//
//
//
//        mutated = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.mutated;");
//        if (mutated) pageSource = driver.getPageSource();
//
//        prevElement = Process.detectElement(pageSource, "password", "Input", false, prevElement, actionList,"https://www.saucedemo.com/");
//        locatorsMap.put("password", Process.getXpath(prevElement));
//        System.out.println(locatorsMap.get("password"));
//        actionList.add(new InputAction("secret_sauce", "password", locatorsMap.get("password")));
//        driver.findElement(By.xpath(locatorsMap.get("password"))).sendKeys("secret_sauce");
//        mutated = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.mutated;");
//        if (mutated) pageSource = driver.getPageSource();
//
//
//        prevElement = Process.detectElement(pageSource, "login", "Click", false, prevElement, actionList,"https://www.saucedemo.com/");
//        locatorsMap.put("login", Process.getXpath(prevElement));
//        System.out.println(locatorsMap.get("login"));
//        actionList.add(new ClickAction("login", locatorsMap.get("login")));
//        driver.findElement(By.xpath(locatorsMap.get("login"))).click();
//
//        mutated = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.mutated;");
//        if (mutated) pageSource = driver.getPageSource();
//
//
//        System.out.println(mutated);
//
//        prevElement = Process.detectElement(pageSource, "sauce labs backpack", "Click", false, prevElement, actionList,"https://www.saucedemo.com/");
//        locatorsMap.put("sauce labs backpack", Process.getXpath(prevElement));
//        System.out.println(locatorsMap.get("sauce labs backpack"));
//        actionList.add(new ClickAction("sauce labs backpack", locatorsMap.get("sauce labs backpack")));
//        driver.findElement(By.xpath(locatorsMap.get("sauce labs backpack"))).click();
//
//        mutated = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.mutated;");
//        if (mutated) pageSource = driver.getPageSource();




    }
}
