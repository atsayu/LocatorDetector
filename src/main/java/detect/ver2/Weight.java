package detect.ver2;

import detect.Calculator;
import detect.HandleCheckbox;
import detect.HandleString;
import detect.Setting;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Weight implements Comparable<Weight> {
    public String source;
    public Element e;
    public String text;
    public Boolean textIsAttribute;
    public double full;
    public int weight;

    public Weight() {

    }

    public Weight(String source, Element e, String text) {
        this.source = source;
        this.e = e;
        this.text = text;
        this.full = 0;
        this.weight = 0;
        textIsAttribute = false;
    }

    public Weight(String source, Element e, String text, Boolean textIsAttribute) {
        this.source = source;
        this.e = e;
        this.text = text;
        this.full = 0;
        this.weight = 0;
        this.textIsAttribute = textIsAttribute;
    }

    public int getWeight() {
        int res = 0;
        if (!text.isEmpty() && textIsAttribute.equals(false)) {
            res += Calculator.weightBetweenTwoString(source, text);
        }
        Attributes attributes = e.attributes();
        if (e.attributesSize() > 0) {
            for (Attribute attr : attributes) {
                String typeAttr = attr.getKey();
                if (!Setting.EXCEPT_ATTRS.contains(typeAttr)) {
                    String valueOfAttr = attr.getValue();
                    if (!valueOfAttr.isEmpty()) {
                        res += Calculator.weightBetweenTwoString(source, valueOfAttr);
                    }
                }
            }
        }
        weight = res;
        return res;
    }

    public double getFull() {
        double res = 0;
        Set<String> visitedWord = new HashSet<>();
        List<String> wordsInSource = HandleString.separateWordsInString(source);
        HandleString.lowercaseWordsInList(wordsInSource);
        List<String> distinctWordsInSource = HandleString.distinctWordsInString(wordsInSource);
        if (!text.isEmpty() && textIsAttribute.equals(false)) {
            Calculator.calculatePercentBetweenTwoStrings(source, text, visitedWord);
        }
        if (e.attributesSize() > 0) {
            Attributes attributes = e.attributes();
            for (Attribute attr : attributes) {
                String typeAttr = attr.getKey();
                if (!Setting.EXCEPT_ATTRS.contains(typeAttr)) {
                    String valueOfAttr = attr.getValue();
                    if (!valueOfAttr.isEmpty()) {
                        Calculator.calculatePercentBetweenTwoStrings(source, valueOfAttr, visitedWord);
                    }
                }
            }
        }
        int size = visitedWord.size();
        res = 1.0 * size / distinctWordsInSource.size();
        full = res;
        return res;
    }

    public int compareBetweenSourceAndText() {
        return Calculator.compareBetweenTwoString(source, text);
    }
    public int compareTo(Weight o) {
        int w1 = getWeight();
        int w2 = o.getWeight();
        double f1 = getFull();
        double f2 = o.getFull();
        int c1 = compareBetweenSourceAndText();
        int c2 = o.compareBetweenSourceAndText();
//        if (f1 > f2) {
//            return 1;
//        } else {
//            if (f1 == f2) {
//                if (c1 < 1) {
//                    if (c2 < 1) {
//                        return w1 - w2;
//                    }
//                    if (c2 == 1) {
//                        return -1;
//                    }
//                    if (c2 > 1) {
//                        return 1;
//                    }
//                } else {
//                    if (c1 == 1) {
//                        if (c2 == 1) {
//                            return w1 - w2;
//                        } else {
//                            return 1;
//                        }
//                    } else {
//                        if (c2 > 1) {
//                            return w1 - w2;
//                        } else {
//                            return -1;
//                        }
//                    }
//                }
//            }
//            return -1;

        if (f1 > f2) {
            return 1;
        } else if (f1 == f2) {
            if (c1 == c2) {
                return w1 - w2;
            } else {
                return c1 - c2;
            }
        } else {
            return -1;
        }
    }

    public int compareAfterActionHover(Weight o) {
        double f1 = getFull();
        double f2 = o.getFull();
        int c1 = compareBetweenSourceAndText();
        int c2 = o.compareBetweenSourceAndText();
        if (f1 > f2) {
            return 1;
        } else if (f1 == f2) {
            if (c1 == c2) {
                return 0;
            } else {
                return c1 - c2;
            }
        } else {
            return -1;
        }
    }


}
