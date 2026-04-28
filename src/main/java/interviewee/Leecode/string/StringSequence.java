package interviewee.Leecode.string;

import java.util.*;

public class StringSequence {
    /**
     * 构造从空字符串逐步按字母递增生成 target 的所有中间字符串。
     *
     * @param target 目标字符串
     * @return 生成 target 过程中出现的字符串序列
     */
    public List<String> stringSequence(String target) {
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < target.length(); i++) {
            char c = target.charAt(i);
            char tmp = 'a';
            if(tmp == c){
                sb.append(tmp);
                res.add(sb.toString());
                continue;
            }
            sb.append(tmp);
            res.add(sb.toString());
            while (tmp != c){
                StringBuilder tb;
                tb = sb.deleteCharAt(sb.length() - 1);
                if(tmp == 'z'){
                    tmp = 'a';
                }else {
                    tmp = (char)(tmp+1);
                }
                tb.append(tmp);
                res.add(tb.toString());
            }
        }
        return res;
    }

    /**
     * 更简洁的字符串生成序列实现：每个新位置从 'a' 递增到目标字符。
     *
     * @param target 目标字符串
     * @return 生成 target 过程中出现的字符串序列
     */
    List<String> stringSequenceV2(String target) {
        List<String> ans = new ArrayList<>();
        StringBuilder s = new StringBuilder();
        for (int c : target.toCharArray()) {
            s.append('a'); // 占位
            for (char j = 'a'; j <= c; j++) {
                s.setCharAt(s.length() - 1, j);
                ans.add(s.toString());
            }
        }
        return ans;
    }

    /**
     * 统计原始输入字符串可能被长按后形成 word 的数量；当前实现按连续重复字符段累加可能性。
     *
     * @param word 观测到的字符串
     * @return 可能的原始字符串数量
     */
    public int possibleStringCount(String word) {
        int a = 1;
        Map<Character, Integer> map = new HashMap<>(1);
        for (int i = 0; i < word.length(); i++) {
            Integer count = map.getOrDefault(word.charAt(i), 0);
            if(i != 0){
                if(count == 0 && map.get(word.charAt(i - 1)) > 1){
                    a += map.get(word.charAt(i - 1));
                }
            }
            map.put(word.charAt(i), count + 1);
            if(i == word.length() - 1 && count > 1){
                a += count;
            }
        }
        return a;

    }


    public static void main(String[] args) {
        StringSequence s = new StringSequence();
        System.out.println(s.stringSequence("abcd"));

        System.out.println((char)('z' + 1));
        System.out.println(s.possibleStringCount("abcd"));
        System.out.println(s.possibleStringCount("aabcd"));
        System.out.println(s.possibleStringCount("aabbcd"));
    }
}
