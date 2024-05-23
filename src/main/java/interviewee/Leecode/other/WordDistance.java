package interviewee.Leecode.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WordDistance {
    private Map<String, List<Integer>> map;

    public WordDistance(String[] wordsDict) {
        if(map == null){
            map = new HashMap<>();
        }
        for (int i = 0; i < wordsDict.length; i++) {
            List<Integer> index = map.get(wordsDict[i]);
            if(index == null) index = new ArrayList<>();
            index.add(i+1);
            map.put(wordsDict[i], index);
        }
    }

    public int shortest(String word1, String word2) {
        List<Integer> a = map.get(word1);
        List<Integer> b = map.get(word2);
        int distance = Integer.MAX_VALUE;
        for (int index: a){
            for(int in: b){
                distance = Math.min(Math.abs(in - index), distance);
            }
        }
        return distance;
    }

    public static void main(String[] args) {
        WordDistance distance = new WordDistance(new String[]{"practice","makes","perfect","coding","makes"});
        System.out.println(distance.shortest("coding","practice"));
        System.out.println(distance.shortest("makes","coding"));
    }
}
