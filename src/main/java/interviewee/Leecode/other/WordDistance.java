package interviewee.Leecode.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WordDistance {
    private Map<String, List<Integer>> map;

    /**
     * 预处理词典中每个单词出现的位置列表，便于后续计算距离。
     *
     * @param wordsDict 单词词典数组
     */
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

    /**
     * 计算两个单词在词典中的最短下标距离。
     *
     * @param word1 第一个单词
     * @param word2 第二个单词
     * @return 两个单词任意出现位置之间的最短距离
     */
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
