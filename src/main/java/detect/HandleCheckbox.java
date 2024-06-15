package detect;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.stream.events.EndElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleCheckbox {
    public static List<Element> searchCheckboxInSubtree(Element e) {
        Elements elems = HandleElement.selectInteractableElementsInSubtree(e);
        if (elems.size() == 0) {
            return searchCheckboxInSubtree(e.parent());
        }
        List<Element> res = new ArrayList<>();
        for (Element ele : elems) {
            if (TypeElement.isCheckboxElement(ele)) {
                res.add(ele);
            }
        }
        if (res.isEmpty()) {
            return searchCheckboxInSubtree(e.parent());
        } else {
            return res;
        }
    }

    public static Map<String, Element> searchCheckboxInSubtree(Element e, List<String> choices) {
        Elements elems = HandleElement.selectInteractableElementsInSubtree(e);
        if (elems.size() == 0) {
            return searchCheckboxInSubtree(e.parent(), choices);
        }

        Map<String, Element> res = new HashMap<>();
        int cnt = 0;
        for (Element elem :elems) {
            if (TypeElement.isCheckboxElement(elem)) {
                String t = getTextForCheckbox(elem);
                if (choices.contains(t)) {
                    if (!res.containsKey(t)) {
                        res.put(t, elem);
                        cnt++;
                    }
                }
            }
        }
        if (cnt == choices.size()) {
            return res;
        }
        return null;
    }

    public static String getTextForCheckboxElementInSubtree(Element e) {
        Elements elems = e.select("*");
        int cnt_checkbox = 0;
        int cnt_text = 0;
        String tmp = "";
        for (Element elem : elems) {
            if (TypeElement.isInteractableElement(elem) && !TypeElement.isCheckboxElement(elem)) {
                return "";
            }
            if (TypeElement.isCheckboxElement(elem)) {
                cnt_checkbox++;
            }
            String t = elem.ownText();
            if (!t.isEmpty()) {
                cnt_text++;
                tmp = t;
                System.out.println("1" + t + " " + cnt_text + " " + cnt_checkbox);
            }
        }

        if (cnt_checkbox == 1 && cnt_text == 0) {
            return getTextForCheckboxElementInSubtree(e.parent());
        }
        if (cnt_checkbox == 1 && cnt_text == 1) {
            System.out.println("2" + tmp);
            return tmp;
        }
        return "";
    }

    public static String getTextForCheckbox(Element e) {
        if (e.hasAttr("id") && !e.attr("id").isEmpty()) {
            String text = HandleElement.getAssociatedLabel(e.attr("id"), e);
            if (!text.isEmpty()) {
                return text;
            }
        }
        String res = getTextForCheckboxElementInSubtree(e);
        return res;
    }

    public static Elements getCheckboxElements(Document document) {
        return document.select("input[type='checkbox']");
    }

}
