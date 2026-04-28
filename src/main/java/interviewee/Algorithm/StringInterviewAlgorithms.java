package interviewee.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 面试中常见的字符串算法集合。
 *
 * 重点覆盖滑动窗口、双指针、KMP、栈、回溯/分组等高频写法。
 */
public final class StringInterviewAlgorithms {

    /**
     * 工具类不需要实例化。
     */
    private StringInterviewAlgorithms() {
    }

    /**
     * 验证回文串：忽略非字母数字字符，并忽略大小写。
     *
     * @param s 原字符串
     * @return 规范化后为回文时返回 true
     */
    public static boolean isPalindrome(String s) {
        if (s == null) {
            return true;
        }
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * 反转字符串中的单词顺序，同时去掉多余空格。
     *
     * @param s 原字符串
     * @return 单词顺序反转后的字符串
     */
    public static String reverseWords(String s) {
        if (s == null || s.trim().isEmpty()) {
            return "";
        }
        String[] words = s.trim().split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(words[i]);
        }
        return builder.toString();
    }

    /**
     * 无重复字符的最长子串：滑动窗口维护每个字符最近出现位置。
     *
     * @param s 原字符串
     * @return 最长无重复子串长度
     */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        Map<Character, Integer> lastIndex = new HashMap<>();
        int left = 0;
        int best = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (lastIndex.containsKey(c)) {
                left = Math.max(left, lastIndex.get(c) + 1);
            }
            lastIndex.put(c, right);
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    /**
     * 最小覆盖子串：滑动窗口寻找 s 中覆盖 t 所有字符次数的最短子串。
     *
     * @param s 原字符串
     * @param t 目标字符集合
     * @return 最短覆盖子串；不存在时返回空串
     */
    public static String minWindowSubstring(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) {
            return "";
        }
        int[] need = new int[128];
        int required = t.length();
        for (int i = 0; i < t.length(); i++) {
            need[t.charAt(i)]++;
        }
        int left = 0;
        int start = 0;
        int minLen = Integer.MAX_VALUE;
        for (int right = 0; right < s.length(); right++) {
            if (need[s.charAt(right)]-- > 0) {
                required--;
            }
            while (required == 0) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }
                if (++need[s.charAt(left++)] > 0) {
                    required++;
                }
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    /**
     * 判断 s2 是否包含 s1 的某个排列：固定长度滑动窗口比较字符频次。
     *
     * @param s1 目标排列来源字符串
     * @param s2 待搜索字符串
     * @return s2 包含 s1 的排列时返回 true
     */
    public static boolean checkInclusion(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() > s2.length()) {
            return false;
        }
        int[] count = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            count[s1.charAt(i) - 'a']++;
            count[s2.charAt(i) - 'a']--;
        }
        if (allZero(count)) {
            return true;
        }
        for (int i = s1.length(); i < s2.length(); i++) {
            count[s2.charAt(i) - 'a']--;
            count[s2.charAt(i - s1.length()) - 'a']++;
            if (allZero(count)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 最长回文子串：从每个中心向两侧扩展。
     *
     * @param s 原字符串
     * @return 最长回文子串
     */
    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }
        int start = 0;
        int end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len = Math.max(expand(s, i, i), expand(s, i, i + 1));
            if (len > end - start + 1) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    /**
     * 最长公共前缀：不断缩短候选前缀，直到所有字符串都以它开头。
     *
     * @param strs 字符串数组
     * @return 最长公共前缀
     */
    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            while (!strs[i].startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) {
                    return "";
                }
            }
        }
        return prefix;
    }

    /**
     * KMP 字符串匹配，返回 pattern 在 text 中第一次出现的位置。
     *
     * @param text    主串
     * @param pattern 模式串
     * @return 首次匹配下标；不存在时返回 -1
     */
    public static int strStrKmp(String text, String pattern) {
        if (text == null || pattern == null) {
            return -1;
        }
        if (pattern.isEmpty()) {
            return 0;
        }
        int[] next = buildNext(pattern);
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = next[j - 1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            if (j == pattern.length()) {
                return i - pattern.length() + 1;
            }
        }
        return -1;
    }

    /**
     * 有效括号：使用栈匹配三类括号。
     *
     * @param s 括号字符串
     * @return 括号完全合法时返回 true
     */
    public static boolean isValidParentheses(String s) {
        if (s == null) {
            return true;
        }
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                if (stack.isEmpty() || !matches(stack.pop(), c)) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    /**
     * 字符串解码：解析类似 3[a2[c]] 的编码。
     *
     * @param s 编码字符串
     * @return 解码后的字符串
     */
    public static String decodeString(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        Stack<Integer> counts = new Stack<>();
        Stack<StringBuilder> builders = new Stack<>();
        StringBuilder current = new StringBuilder();
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                count = count * 10 + c - '0';
            } else if (c == '[') {
                counts.push(count);
                builders.push(current);
                current = new StringBuilder();
                count = 0;
            } else if (c == ']') {
                int repeat = counts.pop();
                StringBuilder parent = builders.pop();
                for (int j = 0; j < repeat; j++) {
                    parent.append(current);
                }
                current = parent;
            } else {
                current.append(c);
            }
        }
        return current.toString();
    }

    /**
     * 分组异位词：排序后的字符串作为同组 key。
     *
     * @param strs 字符串数组
     * @return 异位词分组结果
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> result = new ArrayList<>();
        if (strs == null) {
            return result;
        }
        Map<String, List<String>> groups = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            if (!groups.containsKey(key)) {
                groups.put(key, new ArrayList<String>());
            }
            groups.get(key).add(str);
        }
        result.addAll(groups.values());
        return result;
    }

    /**
     * 字符串相乘：模拟竖式乘法，避免大整数溢出。
     *
     * @param num1 第一个非负整数字符串
     * @param num2 第二个非负整数字符串
     * @return 乘积字符串
     */
    public static String multiplyStrings(String num1, String num2) {
        if ("0".equals(num1) || "0".equals(num2)) {
            return "0";
        }
        int[] result = new int[num1.length() + num2.length()];
        for (int i = num1.length() - 1; i >= 0; i--) {
            for (int j = num2.length() - 1; j >= 0; j--) {
                int product = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int sum = product + result[i + j + 1];
                result[i + j + 1] = sum % 10;
                result[i + j] += sum / 10;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int digit : result) {
            if (builder.length() == 0 && digit == 0) {
                continue;
            }
            builder.append(digit);
        }
        return builder.toString();
    }

    /**
     * 生成 KMP 的前缀表。
     *
     * @param pattern 模式串
     * @return 前缀表
     */
    private static int[] buildNext(String pattern) {
        int[] next = new int[pattern.length()];
        int j = 0;
        for (int i = 1; i < pattern.length(); i++) {
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = next[j - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            next[i] = j;
        }
        return next;
    }

    /**
     * 判断频次数组是否全为 0。
     *
     * @param count 字符频次数组
     * @return 全为 0 时返回 true
     */
    private static boolean allZero(int[] count) {
        for (int value : count) {
            if (value != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从中心向两侧扩展回文。
     *
     * @param s     原字符串
     * @param left  左中心
     * @param right 右中心
     * @return 当前中心的最长回文长度
     */
    private static int expand(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    /**
     * 判断左右括号是否匹配。
     *
     * @param left  左括号
     * @param right 右括号
     * @return 匹配时返回 true
     */
    private static boolean matches(char left, char right) {
        return (left == '(' && right == ')')
                || (left == '[' && right == ']')
                || (left == '{' && right == '}');
    }

    /**
     * 断言 int 结果相等。
     *
     * @param name     用例名称
     * @param actual   实际结果
     * @param expected 期望结果
     */
    private static void assertEquals(String name, int actual, int expected) {
        if (actual != expected) {
            throw new IllegalStateException(name + " 结果错误: " + actual + ", expected: " + expected);
        }
    }

    /**
     * 断言 boolean 结果相等。
     *
     * @param name     用例名称
     * @param actual   实际结果
     * @param expected 期望结果
     */
    private static void assertEquals(String name, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new IllegalStateException(name + " 结果错误: " + actual + ", expected: " + expected);
        }
    }

    /**
     * 断言字符串结果相等。
     *
     * @param name     用例名称
     * @param actual   实际结果
     * @param expected 期望结果
     */
    private static void assertEquals(String name, String actual, String expected) {
        if (!expected.equals(actual)) {
            throw new IllegalStateException(name + " 结果错误: " + actual + ", expected: " + expected);
        }
    }

    public static void main(String[] args) {
        assertEquals("isPalindrome", isPalindrome("A man, a plan, a canal: Panama"), true);
        assertEquals("reverseWords", reverseWords("  hello   world  "), "world hello");
        assertEquals("lengthOfLongestSubstring", lengthOfLongestSubstring("abcabcbb"), 3);
        assertEquals("minWindowSubstring", minWindowSubstring("ADOBECODEBANC", "ABC"), "BANC");
        assertEquals("checkInclusion", checkInclusion("ab", "eidbaooo"), true);
        assertEquals("longestPalindrome", longestPalindrome("cbbd"), "bb");
        assertEquals("longestCommonPrefix", longestCommonPrefix(new String[]{"flower", "flow", "flight"}), "fl");
        assertEquals("strStrKmp", strStrKmp("hello", "ll"), 2);
        assertEquals("isValidParentheses", isValidParentheses("()[]{}"), true);
        assertEquals("decodeString", decodeString("3[a2[c]]"), "accaccacc");
        assertEquals("groupAnagrams", groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}).size(), 3);
        assertEquals("multiplyStrings", multiplyStrings("123", "456"), "56088");

        System.out.println("All string algorithms passed.");
    }
}
