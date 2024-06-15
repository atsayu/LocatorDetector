package detect;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleString {
    public static String lowercaseString(String s){
        return s.toLowerCase();
    }

    public static List<String> separateWordsInString(String s) {
        Pattern pattern = Pattern.compile("([A-Z]?[a-z]+|[A-Z]+|[0-9]+)");
        Matcher matcher = pattern.matcher(s);

        List<String> res = new ArrayList<>();
        while (matcher.find()) {
            res.add(matcher.group());
        }
        return res;
    }

    public static List<String> distinctWordsInString(List<String> list) {
        List<String> res = new ArrayList<>();
        for (String s : list) {
            if (!res.contains(s) && !Setting.STOP_WORDS.contains(s) && !Setting.HEURISTIC_STOP_WORDS.contains(s)) {
                res.add(s);
            }
        }

        return res;
    }

    public static void lowercaseWordsInList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            list.set(i, lowercaseString(list.get(i)));
        }
    }

    public static int isInStringOfList(String w, List<String> list) {
        for (String s : list) {
            if (s.contains(w)) {
                if (s.equals(w)) {
                    return 2;
                } else {
                    return 1;
                }
            }
        }
        return 0;
    }

    public static int calculateWeightOfAttributeAndTextWords(int i, List<String> s, String tmp, List<String> attr, Set<String> visitedWords) {
        if (i == attr.size()) return -1;
        if (isInStringOfList(tmp + attr.get(i), s) == 0) return -1;
        else {
            if (isInStringOfList(tmp + attr.get(i), s) == 2) {
                visitedWords.add(tmp +attr.get(i));
                return i;
            } else {
                return calculateWeightOfAttributeAndTextWords(i + 1, s, tmp + attr.get(i), attr, visitedWords);
            }
        }
    }

    public static int calculateWeightOfAttributeAndTextWords(int i, List<String> s, String tmp, List<String> attr) {
        if (i == attr.size()) return -1;
        if (isInStringOfList(tmp + attr.get(i), s) == 0) return -1;
        else {
            if (isInStringOfList(tmp + attr.get(i), s) == 2) {
                return i;
            } else {
                return calculateWeightOfAttributeAndTextWords(i + 1, s, tmp + attr.get(i), attr);
            }
        }
    }

}
