package detect.ver2;

import detect.*;
import detect.Process;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class Select {
    public static Map<Pair<String, String>, Element> detectSelectElement(List<Pair<String, String>> list, Elements selectElements) {
        Map<Pair<String, String>, Element> res = new HashMap<>();
        for (Pair<String, String> pair : list) {
            String question = pair.getFirst();
            String choice = pair.getSecond();
            int max_weight = 0;
            double max_full = 0;
            int current_cmp3 = 2;
            List<String> wordsInQuestion = HandleString.separateWordsInString(question);
            HandleString.lowercaseWordsInList(wordsInQuestion);
            List<String> distinctWordsInQuestion = HandleString.distinctWordsInString(wordsInQuestion);
            Element tmp = null;
            for (Element e : selectElements) {
                if (HandleSelect.hasOption(e, choice)) {
                    if (question.isEmpty()) {
                        tmp = e;
                        break;
                    } else {
                        String text = HandleSelect.getTextForSelect(e);
                        Set<String> visitedWords = new HashSet<>();
                        int weight = Calculator.weightBetweenTwoString(question, text);
                        Calculator.calculatePercentBetweenTwoStrings(question, text, visitedWords);
                        double full = 1.0 * visitedWords.size() / distinctWordsInQuestion.size();
                        int cmp3 = Calculator.compareBetweenTwoString(question, text);
                        if (Calculator.compareWeight(max_weight, max_full, current_cmp3, weight, full, cmp3) > 0) {
                            max_full = full;
                            max_weight = weight;
                            current_cmp3 = cmp3;
                            tmp = e;
                        }
                    }
                }
            }
            if (tmp != null) {
                res.put(pair, tmp);
            } else {
                System.out.println("Cant detect element with pair " + "question is " + question + " and choice is " + choice);
            }

        }
        return res;
    }

    public static void main(String[] args) {
        String linkHtml = "https://form.jotform.com/233591551157458?fbclid=IwAR1ggczzG7OoN6Dgb2SDWtNyznCAAJNW-G8-_3gnejJwPFunwwBuN_NCvh0";
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
        Elements selectElements = HandleSelect.getSelectElements(document);
        List<Pair<String, String >> list = new ArrayList<>();
        list.add(new Pair<>("", "Cong"));
        list.add(new Pair<>("Country", "Aruba"));
        list.add(new Pair<>("Happy", "One Way"));
        list.add(new Pair<>("Airline", "Airline 1"));
        Map<Pair<String, String>, Element> res = detectSelectElement(list, selectElements);
        for (Map.Entry<Pair<String, String>, Element> entry : res.entrySet()) {
            Pair<String, String> pair = entry.getKey();
            Element e = entry.getValue();
            System.out.println(pair.getFirst() + " " + pair.getSecond() + " " + Process.getXpath(e));
        }
    }
}
