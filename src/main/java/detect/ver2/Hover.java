package detect.ver2;

import detect.HandleClick;
import detect.Pair;
import detect.Process;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hover {
    public static Map<String, List<Element>> detectHoverElement(List<String> input, Elements hoverElements, boolean isAfterHoverAction) {
        Map<String, List<Element>> result = new HashMap<>();
        Map<Weight, List<Weight>> map = new HashMap<>();
        if (!isAfterHoverAction) {
            for (String s : input) {
                Weight max = new Weight();
                for (int i = 0; i < hoverElements.size(); i++) {
                    Element e = hoverElements.get(i);
                    Pair<String, Boolean> pair = HandleClick.getTextForClickableElement(e);
                    String text = pair.getFirst();
                    Boolean textIsAttribute = pair.getSecond();
                    Weight w = new Weight(s, e, text, textIsAttribute);
                    if (i == 0) {
                        max = w;
                        List<Weight> list = new ArrayList<>();
                        list.add(max);
                        map.put(max, list);
                        continue;
                    }
                    if (w.compareTo(max) > 0) {
                        max = w;
                        List<Weight> list = new ArrayList<>();
                        list.add(max);
                        map.put(max, list);
                    } else {
                        if (w.compareTo(max) == 0) {
                            map.get(max).add(w);
                        }
                    }

                }
                if (max.e != null && max.getFull() > 0 && max.getWeight() > 0) {
                    System.out.println(s);
                    List<Element> elementList = new ArrayList<>();
                    for (Weight w : map.get(max)) {
                        if (w.e != null) {
                            elementList.add(w.e);
                            System.out.println( Process.getXpath(w.e) + " " + w.text);
                        }
                    }
                    result.put(s, elementList);
                    System.out.println(max.getFull() + " " + max.getWeight());
                } else {
                    System.out.println("Cant detect element with input is " + s);
                }
            }
        } else {
            for (String s : input) {
                Weight max = new Weight();
                for (int i = 0; i < hoverElements.size(); i++) {
                    Element e = hoverElements.get(i);
                    Pair<String, Boolean> pair = HandleClick.getTextForClickableElement(e);
                    String text = pair.getFirst();
                    Boolean textIsAttribute = pair.getSecond();
                    Weight w = new Weight(s, e, text, textIsAttribute);
                    if (i == 0) {
                        max = w;
                        List<Weight> list = new ArrayList<>();
                        list.add(max);
                        map.put(max, list);
                        continue;
                    }
                    if (w.compareAfterActionHover(max) > 0) {
                        max = w;
                        List<Weight> list = new ArrayList<>();
                        list.add(max);
                        map.put(max, list);
                    } else {
                        if (w.compareAfterActionHover(max) == 0) {
                            map.get(max).add(w);
                        }
                    }

                }
                if (max.e != null && max.getWeight() > 0 && max.getFull() > 0) {
                    System.out.println(s);
                    List<Element> elementList = new ArrayList<>();
                    for (Weight w : map.get(max)) {
                        if (w.e != null) {
                            elementList.add(w.e);
                            System.out.println( Process.getXpath(w.e) + " " + w.text);
                        }
                    }
                    result.put(s, elementList);
                    System.out.println(max.getFull() + " " + max.getWeight());
                } else {
                    System.out.println("Cant detect element with input is " + s);
                }
            }
        }

        return result;
    }

}
