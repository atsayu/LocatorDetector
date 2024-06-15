package detect;

import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.List;

public class TypeElement {
    public static boolean isInputElement(Element e) {
        if (e == null) {
            return false;
        }
        List<String> list = Arrays.asList("date", "datetime-local", "email", "month", "number", "password", "search", "tel", "text", "time", "url", "week", "");
        String tag = e.tagName();
        if (tag.equals("textarea")) {
            return true;
        }
        if (tag.equals("input")) {
            if (e.hasAttr("type")) {
                String type = e.attr("type");
                return list.contains(type);
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean isDefaultClickableElement(Element e) {
        if (e == null) {
            return false;
        }
        String tag = e.tagName();
        if (tag.equals("button") || tag.equals("img") || tag.equals("a")) {
            return true;
        }
        return isClickElementTagInput(e);
    }

    public static boolean isClickElementTagInput(Element e) {
        if (e == null) {
            return false;
        }
        String tag = e.tagName();
        if (tag.equals("input")) {
            if (e.hasAttr("type") && (e.attr("type").equals("button") || e.attr("type").equals("reset") || e.attr("type").equals("submit") || e.attr("type").equals("image"))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRadioElement(Element e) {
        if (e == null) {
            return false;
        }
        if (e.tagName().equals("input") && e.attr("type").equals("radio")) {
            return true;
        }
        return false;
    }

    public static boolean isCheckboxElement(Element e) {
        if (e == null) {
            return false;
        }
        if (e.tagName().equals("input") && e.attr("type").equals("checkbox")) {
            return true;
        }
        return false;
    }
    public static boolean isSelectElement(Element e) {
        if (e == null) {
            return false;
        }
        if (e.tagName().equals("select")) {
            return true;
        }
        return false;
    }

    public static boolean isInteractableElement(Element e) {
        if (e == null) {
            return false;
        }
        List<String> list = Arrays.asList("input", "button", "img", "a", "select", "textarea");
        String tag = e.tagName();
        return list.contains(tag);
    }

}
