package detect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Calculator {
    public static int weightBetweenTwoString(String source, String target) {
        List<String> wordsInSource = HandleString.separateWordsInString(source);
        HandleString.lowercaseWordsInList(wordsInSource);
        List<String> distinctWordsInSource = HandleString.distinctWordsInString(wordsInSource);
        List<String> wordsInTarget = HandleString.separateWordsInString(target);
        HandleString.lowercaseWordsInList(wordsInTarget);
        int res = 0;
        for (int i = 0; i < wordsInTarget.size(); i++) {
            boolean isWeightChange = false;
            for (String s : distinctWordsInSource) {
                if (wordsInTarget.get(i).contains(s)) {
                    isWeightChange = true;
                    res++;
                }
            }
            if (!isWeightChange) {
                String tmp = "";
                int idx= HandleString.calculateWeightOfAttributeAndTextWords(i, distinctWordsInSource, tmp, wordsInTarget);
                if (idx != -1) {
                    i = idx;
                    res += 1;
                }
            }
        }
        return res;
    }

    public static void calculatePercentBetweenTwoStrings(String source, String target, Set<String> visitedWords) {
        List<String> wordsInSource = HandleString.separateWordsInString(source);
        HandleString.lowercaseWordsInList(wordsInSource);
        List<String> distinctWordsInSource = HandleString.distinctWordsInString(wordsInSource);
        List<String> wordsInTarget = HandleString.separateWordsInString(target);
        HandleString.lowercaseWordsInList(wordsInTarget);
        for (int i = 0; i < wordsInTarget.size(); i++) {
            boolean isChange = false;
            for (String w : distinctWordsInSource) {
                if (wordsInTarget.get(i).contains(w)) {
                    isChange = true;
                    visitedWords.add(w);
                }
            }
            if (!isChange) {
                String tmp = "";
                int idx= HandleString.calculateWeightOfAttributeAndTextWords(i, distinctWordsInSource, tmp, wordsInTarget, visitedWords);
                if (idx != -1) {
                    i = idx;
                }
            }
        }
    }

    public static int compareWeight(int current_weight, double current_full, int w, double f) {
        if (f > current_full) {
            return 1;
        } else {
            if (f == current_full) {
                return w - current_weight;
            }
        }
        return -1;
    }

    public static int compareWeight(int max_weight, double max_full, int current_cmp3, int w, double f, int cmp3) {
//        if (f > max_full) {
//            return 1;
//        } else {
//            if (f == max_full) {
//                if (cmp3 == 1) {
//                    if (current_cmp3 != 1) {
//                        return 1;
//                    } else {
//                        return w - max_weight;
//                    }
//                } else if (cmp3 == 0) {
//                    if (current_cmp3 == 1) {
//                        return -1;
//                    } else {
//                        return w - max_weight;
//                    }
//                } else {
//                    return w - max_weight;
//                }
//            } else {
//                return -1;
//            }
//        }
        if (f > max_full) {
            return 1;
        } else if (f == max_full) {
            if (cmp3 - current_cmp3 == 0) {
                return w - max_weight;
            } else {
                return cmp3 - current_cmp3;
            }
        } else {
            return -1;
        }
    }

    public static int compareBetweenTwoString(String source, String target) {
        List<String> wordsInSource = HandleString.separateWordsInString(source);
        HandleString.lowercaseWordsInList(wordsInSource);
        List<String> distinctWordsInSource = HandleString.distinctWordsInString(wordsInSource);
        List<String> wordsInTarget = HandleString.separateWordsInString(target);
        HandleString.lowercaseWordsInList(wordsInTarget);
        Set<String> visitedWords = new HashSet<>();
        calculatePercentBetweenTwoStrings(source, target, visitedWords);
        double full = visitedWords.size() * 1.0 / distinctWordsInSource.size();

        if (full < 1) {
            if (full == 0) {
                return 0;
            }
            return 1;
        } else {
            String s = String.join("",wordsInSource);
            String t = String.join("", wordsInTarget);
//            System.out.println(s + " " + t);
            if (s.equals(t)) {
                return 3;
            } else {
                return 2;
            }

        }
    }

    public static void main(String[] args) {
        String s = "Street Address";
        String t = "Street Address";
        System.out.println(compareBetweenTwoString(s, t));
    }
}
