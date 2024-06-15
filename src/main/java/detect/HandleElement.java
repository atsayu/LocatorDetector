package detect;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleElement {

    public static String getAssociatedLabel(String id, Element e) {
        String query = "label[for='" + id + "']";
        Elements label = e.ownerDocument().select(query);
        if (label.isEmpty()) {
            return "";
        } else {
            return label.get(0).text();
        }
    }

    public static void getElementsOfVariousTypes(Document document, Elements inputElements, Elements clickableElements, Elements selectElements) {
        inputElements = HandleInput.getInputElements(document);
        System.out.println(inputElements.size());
        selectElements = HandleSelect.getSelectElements(document);
        clickableElements = HandleClick.getClickableElements(document);
    }

    public static Elements selectInteractableElementsInSubtree(Element e) {
        Elements res = new Elements();
        Elements textarea_tag = e.getElementsByTag("textarea");
        Elements input_tag = e.getElementsByTag("input");
        Elements select_tag = e.getElementsByTag("select");
        Elements a_tag = e.getElementsByTag("a");
        Elements img_tag = e.getElementsByTag("img");
        Elements button = e.getElementsByTag("button");
        if (textarea_tag != null) {
            res.addAll(textarea_tag);
        }
        if (input_tag != null) {
            res.addAll(input_tag);
        }
        if (select_tag != null) {
            res.addAll(select_tag);
        }
        if (a_tag != null) {
            res.addAll(a_tag);
        }
        if (img_tag != null) {
            res.addAll(img_tag);
        }
        if (button != null) {
            res.addAll(button);
        }
        return res;
    }

    public static boolean isLabelHasForAttr(Element e) {
        if (e == null) {
            return false;
        }
        if (e.tagName().equals("label") && e.hasAttr("for") && !e.attr("for").isEmpty()) {
            return true;
        }
        return false;
    }

    public static int findNearestCommonAncestor (Element source, Element target) {
        Elements elements = source.select("*");
        if (elements.contains(target)) {
            return 0;
        } else {
            return 1 + findNearestCommonAncestor(source.parent(), target);
        }
//        if (e != null) {
//            if (e.equals(target)) {
//                return 0;
//            } else {
//                return 1 + findNearestCommonAncestor(source.parent(), target);
//            }
//        }
//        return -1;
    }

    public static Element findNearestElementWithSpecifiedElement(Element source, List<Element> list) {
        int min_distance = Integer.MAX_VALUE;
        Element res = null;
        for (Element target : list) {
            int dis = findNearestCommonAncestor(source, target);
            System.out.println(dis);
            if (dis >= 0 && dis < min_distance) {
                min_distance = dis;
                res = target;
            }
        }
        return res;
    }

}
