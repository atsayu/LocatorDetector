package detect.ver2;

import detect.HandleCheckbox;
import detect.Process;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickCheckbox {
    public static Map<String, Element> detectCheckboxElement(List<String> listChoices, Document document) {
        Elements checkboxElements = HandleCheckbox.getCheckboxElements(document);
        Map<String, Element> res = new HashMap<>();
        for (Element checkbox : checkboxElements) {
            String choice = HandleCheckbox.getTextForCheckbox(checkbox);
            if (listChoices.contains(choice)) {
                res.put(choice, checkbox);
                System.out.println(choice + " " + Process.getXpath(checkbox));
            }
        }
        return res;
    }
}
