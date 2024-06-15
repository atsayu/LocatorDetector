package detect.ver2;

import detect.*;
import detect.Process;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;

public class Click {
    public static Map<String, List<Element>> detectClickElement(List<String> input, Elements clickableElements, boolean isAfterHoverAction) {
        Map<String, List<Element>> result = new HashMap<>();
        Map<Weight, List<Weight>> map = new HashMap<>();
        if (!isAfterHoverAction) {
            for (String s : input) {
                Weight max = new Weight();
                for (int i = 0; i < clickableElements.size(); i++) {
                    Element e = clickableElements.get(i);
                    Pair<String, Boolean> pair = HandleClick.getTextForClickableElement(e);
                    String text = pair.getFirst();
                    Boolean textIsAttribute = pair.getSecond();
                    Weight w = new Weight(s, e, text, textIsAttribute);
                    if (i == 0) {
                        max = w;
                        List<Weight> list = new ArrayList<>();
                        list.add(max);
                        map.put(max, list);
                    } else {
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


                }
                if (max.e != null && max.getFull() > 0 && max.getWeight() > 0) {
                    System.out.println(s);
                    List<Element> elementList = new ArrayList<>();
                    for (Weight w : map.get(max)) {
                        if (w.e != null) {
                            elementList.add(w.e);
                            System.out.println( Process.getAbsoluteXpath(w.e, "") + " " + w.text);
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
                for (int i = 0; i < clickableElements.size(); i++) {
                    Element e = clickableElements.get(i);
                    Pair<String, Boolean> pair = HandleClick.getTextForClickableElement(e);
                    String text = pair.getFirst();
                    Boolean textIsAttribute = pair.getSecond();
                    Weight w = new Weight(s, e, text, textIsAttribute);
                    if (i == 0) {
                        max = w;
                        List<Weight> list = new ArrayList<>();
                        list.add(max);
                        map.put(max, list);
                    } else {
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
                    System.out.println(elementList.size());
                    result.put(s, elementList);
                    System.out.println(max.getFull() + " " + max.getWeight());
                } else {
                    System.out.println("Cant detect element with input is " + s);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {

        String linkHtml = "http://127.0.0.1:5500/src/main/resources/testcase/example.html";
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
        Elements inputElements = HandleInput.getInputElements(document);
        Elements clickableElements = HandleClick.getClickableElements(document);
        List<String> input = Arrays.asList("confirm password", "new-password", "password", "Hello");
        List<String> click = Arrays.asList("quick edit", "edit", "submit", "Cong");
        Map<String, List<Element>> res_click = detectClickElement(click, clickableElements, false);
        Map<String, List<Element>> res_input = InputElement.detectInputElement(input, inputElements, false);

    }
}
