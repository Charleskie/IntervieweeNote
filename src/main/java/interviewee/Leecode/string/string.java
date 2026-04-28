package interviewee.Leecode.string;

import java.util.Collections;
import java.util.HashMap;

public class string {
    /***
     * 给你一个字符串 s ，请你找出 至多 包含 两个不同字符 的最长子串，并返回该子串的长度。
     * 当前方法体仍是待补全草稿。
     *
     * @param s 原字符串
     * @return 当前草稿固定返回 0
     */
    public int lengthOfLongestSubstringTwoDistinct(String s) {
        int left = 0;
        int right = 0;
        HashMap<Character, Integer> map = new HashMap<>();
        while (right < s.length()){
            map.put(s.charAt(right), right++);
            if(map.size() == 3){
                int index = Collections.min(map.values());
//                map.remove()
            }
        }
        return 0;
    }
}
