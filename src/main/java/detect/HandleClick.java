package detect;


import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class HandleClick {
    public static Elements getClickableElements (Document document) {
        Elements button_tag = document.getElementsByTag("button");
        Elements a_tag = document.getElementsByTag("a");
        Elements img_tag = document.getElementsByTag("img");
        Elements input_tag = document.getElementsByTag("input");
        Elements res = new Elements();
        res.addAll(button_tag);
        res.addAll(a_tag);
        res.addAll(img_tag);
        for (Element e : input_tag) {
            if (TypeElement.isClickElementTagInput(e)) {
                res.add(e);
            }
        }
        return res;
    }

    public static Pair<String, Boolean> getTextForClickableElement (Element e) {
        if (!TypeElement.isClickElementTagInput(e)) {
            return new Pair<>(e.text(), false);
        } else {
            String text = e.attr("value");
            if (!text.isEmpty()) {
                return new Pair<>(text, true);
            } else {
                if (e.attr("type").equals("submit")) {
                    return new Pair<>("Submit", true);
                }
                if (e.attr("type").equals("reset")) {
                    return new Pair<>("Reset", true);
                }
                return new Pair<>("", true);
            }
        }

    }

    public static List<String> describedElement(Element e) {
        List<String> listDescribeElement = new ArrayList<>();
        Pair<String, Boolean> pair = getTextForClickableElement(e);
        String text = pair.getFirst();
        if (!text.isEmpty()) {
            if (!pair.getSecond()) {
                List<String> wordsInText = HandleString.separateWordsInString(text);
                for (int i = 0; i < wordsInText.size(); i++) {
                    String s = wordsInText.get(i);
                    if (!Setting.STOP_WORDS.contains(s) && !Setting.HEURISTIC_STOP_WORDS.contains(s)) {
                        listDescribeElement.add(s.toLowerCase());
                    }
                }
            } else {
                if (!e.hasAttr("value") || !e.attr("value").equals(text)) {
                    List<String> wordsInText = HandleString.separateWordsInString(text);
                    for (int i = 0; i < wordsInText.size(); i++) {
                        String s = wordsInText.get(i);
                        if (!Setting.STOP_WORDS.contains(s) && !Setting.HEURISTIC_STOP_WORDS.contains(s)) {
                            listDescribeElement.add(s.toLowerCase());
                        }
                    }
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

}
