import detect.Process;
import detect.object.Action;
import detect.object.ClickAction;
import detect.object.InputAction;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APITest {
    public static void main(String[] args) {
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


        String pageSource = driver.getPageSource();
        prevElement = Process.detectElement(pageSource, "username", "Input", false, prevElement, actionList,"https://www.saucedemo.com/");
        locatorsMap.put("username", Process.getXpath(prevElement));
        actionList.add(new InputAction("standard_user","username", locatorsMap.get("username")));
        System.out.println(locatorsMap.get("username"));
        driver.findElement(By.xpath(locatorsMap.get("username"))).sendKeys("standard_user");



        mutated = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.mutated;");
        if (mutated) pageSource = driver.getPageSource();

        prevElement = Process.detectElement(pageSource, "password", "Input", false, prevElement, actionList,"https://www.saucedemo.com/");
        locatorsMap.put("password", Process.getXpath(prevElement));
        System.out.println(locatorsMap.get("password"));
        actionList.add(new InputAction("secret_sauce", "password", locatorsMap.get("password")));
        driver.findElement(By.xpath(locatorsMap.get("password"))).sendKeys("secret_sauce");
        mutated = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.mutated;");
        if (mutated) pageSource = driver.getPageSource();


        prevElement = Process.detectElement(pageSource, "login", "Click", false, prevElement, actionList,"https://www.saucedemo.com/");
        locatorsMap.put("login", Process.getXpath(prevElement));
        System.out.println(locatorsMap.get("login"));
        actionList.add(new ClickAction("login", locatorsMap.get("login")));
        driver.findElement(By.xpath(locatorsMap.get("login"))).click();

        mutated = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.mutated;");
        if (mutated) pageSource = driver.getPageSource();


        System.out.println(mutated);

        prevElement = Process.detectElement(pageSource, "sauce labs backpack", "Click", false, prevElement, actionList,"https://www.saucedemo.com/");
        locatorsMap.put("sauce labs backpack", Process.getXpath(prevElement));
        System.out.println(locatorsMap.get("sauce labs backpack"));
        actionList.add(new ClickAction("sauce labs backpack", locatorsMap.get("sauce labs backpack")));
        driver.findElement(By.xpath(locatorsMap.get("sauce labs backpack"))).click();

        mutated = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.mutated;");
        if (mutated) pageSource = driver.getPageSource();




    }
}
