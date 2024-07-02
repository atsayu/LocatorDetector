package detect;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.util.*;

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

    public static int getDistance(Element source, Element target) {
        Elements elements = source.getAllElements();
        if (elements.contains(target)) {
            return getDistanceFromLeafNodeToRoot(source, target);
        }
        return 1 + getDistance(source.parent(), target);
    }

    public static int getDistanceFromLeafNodeToRoot (Element source, Element target) {
        if (target == source) {
            return 0;
        }
        return 1 + getDistanceFromLeafNodeToRoot(source, target.parent());
    }


    public static Element findNearestElementWithSpecifiedElement(Element source, List<Element> list) {
        int min_distance = Integer.MAX_VALUE;
        Element res = null;
        for (Element target : list) {
            List<Element> visited = new ArrayList<>();
            int dis = getDistance(source, target, visited);
            System.out.println(dis);
            if (dis >= 0 && dis < min_distance) {
                min_distance = dis;
                res = target;
            }
        }
        return res;
    }

    public static int isContainTargetElement(Element source, Element target, List<Element> visited) {
        if (source == target) {
            return 0;
        }
        for (Element child : source.children()) {
            if (!visited.contains(child)) {
                int dis = 1 + isContainTargetElement(child, target, visited);
                if (dis > 0) {
                    return dis;
                }
            }
        }
        return Integer.MIN_VALUE;
    }

    public static int getDistance(Element source, Element target, List<Element> visited) {
        int dis = isContainTargetElement(source, target, visited);
        if (dis > 0) {
            return dis;
        }
        visited.add(source);
        return 1 + getDistance(source.parent(), target, visited);
    }

}
