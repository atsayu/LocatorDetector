package detect;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HandleRadioButton {
    public static Element searchRadioButtonInSubtree(Element e, String choice) {
        Elements elems = HandleElement.selectInteractableElementsInSubtree(e);
        if (elems.size() == 0) {
            return searchRadioButtonInSubtree(e.parent()
                    , choice);
        }
        for (Element elem : elems) {
            if (!TypeElement.isRadioElement(elem)) {
                return null;
            }
        }
        for (Element elem :elems) {
            String t = getTextForRadioButton(elem);
            if (t.equals(choice)) {
                return elem;
            }
        }

        return null;
    }



    public static String getTextForRadioButtonElementInSubtree(Element e) {
        Elements elems = e.select("*");
        int cnt_radio = 0;
        int cnt_text = 0;
        String tmp = "";
        for (Element elem : elems) {
            if (TypeElement.isInteractableElement(elem) && !TypeElement.isRadioElement(e)) {
                return "";
            }
            if (TypeElement.isRadioElement(e)) {
                cnt_radio++;
            }
            String t = elem.ownText();
            if (!t.isEmpty()) {
                cnt_text++;
                tmp = t;
            }
        }

        if (cnt_radio == 1 && cnt_text == 0) {
            return getTextForRadioButtonElementInSubtree(e.parent());
        }
        if (cnt_radio == 1 && cnt_text == 1) {
            return tmp;
        }
        return "";
    }

    public static String getTextForRadioButton(Element e) {
        if (e.hasAttr("id") && !e.attr("id").isEmpty()) {
            String text = HandleElement.getAssociatedLabel(e.attr("id"), e);
            if (!text.isEmpty()) {
                return text;
            }
        }
        String res = getTextForRadioButtonElementInSubtree(e);
        return res;
    }
}
