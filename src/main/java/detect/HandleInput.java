package detect;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class HandleInput {
    public static Element searchInputElementInSubtree(String text, Element e) {
        Elements input = HandleElement.selectInteractableElementsInSubtree(e);
        if (input.size() > 1 || (input.size() == 1 && !TypeElement.isInputElement(input.get(0))) || !e.text().equals(text)) {
            return null;
        }
        if (input.size() == 1 && TypeElement.isInputElement(input.get(0)) && e.text().equals(text)) {
            return input.get(0);
        }
        return searchInputElementInSubtree(text, e.parent());
    }

    public static Elements getInputElements(Document document) {
        Elements textarea_tag = document.getElementsByTag("textarea");
        Elements input_tag = document.getElementsByTag("input");
        Elements res = new Elements(textarea_tag);
        for (Element e : input_tag) {
            if (TypeElement.isInputElement(e)) {
                res.add(e);
            }
        }
        return res;
    }

    public static Pair<String, Boolean> getTextForInput(Element e) {
        if (!e.ownText().isEmpty()) {
            return new Pair<>(e.ownText(), false);
        }
        String res = "";
        if (e.hasAttr("id") && !e.attr("id").isEmpty()) {
            res = HandleElement.getAssociatedLabel(e.attr("id"), e);
            if (!res.isEmpty()) {
                return new Pair<>(res, false);
            }
        }
        res = getTextForInputInSubtree(e);
        if (res.isEmpty()) {
            if (e.hasAttr("placeholder") && !e.attr("placeholder").isEmpty()) {
                return new Pair<>(e.attr("placeholder"), true);
            } else {
                return new Pair<>("", false);
            }
        }
        return new Pair<>(res, false);
    }

    public static String getTextForInputInSubtree(Element e) {
        Elements elements = e.select("*");
        int cnt_text = 0;
        String tmp = "";
        for (Element ele : elements) {
            if (TypeElement.isInteractableElement(ele) && !TypeElement.isInputElement(ele)) {
                return "";
            }
            String text = ele.ownText();
            if (!text.isEmpty()) {
                tmp = text;
                cnt_text++;
                if (cnt_text > 1) {
                    return "";
                }
            }
        }
        if (cnt_text == 1) {
            return tmp;
        }
        return getTextForInputInSubtree(e.parent());
    }

    public static List<String> describedElement(Element e) {
        List<String> listDescribeElement = new ArrayList<>();
        Pair<String, Boolean> pair = getTextForInput(e);
        String text = pair.getFirst();
        if (!text.isEmpty() && !pair.getSecond()) {
            List<String> wordsInText = HandleString.separateWordsInString(text);
            for (int i = 0; i < wordsInText.size(); i++) {
                String s = wordsInText.get(i);
                if (!Setting.STOP_WORDS.contains(s) && !Setting.HEURISTIC_STOP_WORDS.contains(s)) {
                    listDescribeElement.add(s.toLowerCase());
                }
            }
        }
        Attributes attributes = e.attributes();
        if (!attributes.isEmpty()) {
            for (Attribute attr : attributes) {
                String typeAttr = attr.getKey();
                if (!Setting.EXCEPT_ATTRS.contains(typeAttr)) {
                    String valueOfAttr = attr.getValue();
                    if (!valueOfAttr.isEmpty()) {
                        List<String> wordsInValueOfAttr = HandleString.separateWordsInString(valueOfAttr);
                        for (int i = 0; i < wordsInValueOfAttr.size(); i++) {
                            String s = wordsInValueOfAttr.get(i);
                            if (!Setting.STOP_WORDS.contains(s) && !Setting.HEURISTIC_STOP_WORDS.contains(s)) {
                                listDescribeElement.add(s.toLowerCase());
                            }
                        }
                    }
                }
            }
        }
        return listDescribeElement;
    }


    public static void main(String[] args) {
        String linkHtml = "https://form.jotform.com/233591551157458";
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
        Element input = document.getElementById("last_3");
        Pair<String, Boolean> pair = getTextForInput(input);
        System.out.println(pair.getFirst() +" " + pair.getSecond());
    }
}
