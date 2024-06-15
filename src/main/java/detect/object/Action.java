package detect.object;

import org.openqa.selenium.WebDriver;

import java.util.List;

public abstract class Action {
    public String getText_locator() {return null;}
    public void setDom_locator(String dom_locator) {
        return;
    }

    public String getDom_locator() {
        return null;
    }
    public void run(WebDriver driver) {
        return;
    }
    public static void runActions(List<Action> list, WebDriver driver) {
        for (Action action : list) {
            action.run(driver);
        }
    }
}
