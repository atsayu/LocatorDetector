package detect;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class HandleSelect {
    public static Element searchSelectElementInSubtree(String text, Element e) {
        Elements elems = e.select("*");
        int cnt_interact = 0;
        Element select = null;
        for (Element ele : elems) {
            if (TypeElement.isInteractableElement(ele) && !TypeElement.isSelectElement(ele)) {
                return null;
            }
            if (ele.tagName().equals("option")) {
                continue;
            }
            String t = e.ownText();
            if (!t.isEmpty() && !t.equals(text)) {
                return null;
            }
            if (TypeElement.isSelectElement(ele)) {
                select = ele;
            }
        }
        if (select == null) {
            return searchSelectElementInSubtree(text, e.parent());
        }
        return select;
    }

    public static String getTextForSelect(Element e) {
        String res = "";
        if (e.hasAttr("id") && !e.attr("id").isEmpty()) {
            res = HandleElement.getAssociatedLabel(e.attr("id"), e);
            if (!res.isEmpty()) {
                return res;
            }
        }
        res = getTextForSelectElementInSubtree(e.parent());
        return res;
    }



    public static String getTextForSelectElementInSubtree(Element e) {
        Elements elements = HandleElement.selectInteractableElementsInSubtree(e);
        if (elements.size() > 1) {
            return "";
        }
        Elements elems = e.select("*");
        String text = e.selectFirst("select").text();
        int cnt = 0;
        String tmp = "";
        for (Element ele : elems) {
            String tag = ele.tagName();
            if (!tag.equals("select") && !tag.equals("option")) {
                String t = ele.ownText();
                if (!t.isEmpty()) {
                    cnt++;
                    tmp = t;
                    if (cnt > 1 || text.contains(t)) {
                        return "";
                    }
                }
            }
        }
        if (cnt == 1) {
            return tmp;
        }
        return getTextForSelectElementInSubtree(e.parent());
    }

    public static Elements getSelectElements(Document document) {
        return document.getElementsByTag("select");
    }

    public static boolean hasOption(Element e, String choice) {
        Elements optionElements = e.select("option");
        for (Element option : optionElements) {
            String text = option.ownText();
            if (text.equals(choice)) {
                return true;
            }
        }
        return false;
    }
}
