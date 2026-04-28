package interviewee.Leecode.tianyiyun;

public class IsPalindrome {

    /**
     * 忽略非字母数字字符和大小写，判断字符串是否为回文。
     *
     * @param s 原字符串
     * @return 规范化后正反相同返回 true
     */
    public boolean isPalindrome(String s) {
        StringBuffer sgood = new StringBuffer();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char ch = s.charAt(i);
            if (Character.isLetterOrDigit(ch)) {
                sgood.append(Character.toLowerCase(ch));
            }
        }
        StringBuffer sgood_rev = new StringBuffer(sgood).reverse();
        return sgood.toString().equals(sgood_rev.toString());
    }
}
