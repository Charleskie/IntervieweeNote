package interviewee.Leecode.string;

public class LongestCommonSubstring {

    /**
     * 查找两个字符串的最长公共子串（连续）
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 最长公共子串
     */
    public static String getLongestCommonSubstring(String str1, String str2) {
        // 边界判断
        if (str1 == null || str2 == null || str1.isEmpty() || str2.isEmpty()) {
            return "";
        }

        int len1 = str1.length();
        int len2 = str2.length();

        // dp[i][j] 表示：str1前i个字符 和 str2前j个字符 的最长公共子串长度
        // 必须以 str1[i-1] 和 str2[j-1] 结尾
        int[][] dp = new int[len1 + 1][len2 + 1];

        int maxLength = 0; // 最长长度
        int endIndex = 0; // 最长子串在 str1 中的结束位置

        // 填充 dp 表
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                // 当前字符相等
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;

                    // 更新最长长度和结束位置
                    if (dp[i][j] > maxLength) {
                        maxLength = dp[i][j];
                        endIndex = i - 1;
                    }
                } else {
                    // 不相等 → 连续中断，长度置0
                    dp[i][j] = 0;
                }
            }
        }

        // 截取结果
        return str1.substring(endIndex - maxLength + 1, endIndex + 1);
    }

    // 测试
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "abcdefg";
        String s2 = "defghi";
        System.out.println("最长公共子串：" + getLongestCommonSubstring(s1, s2)); // defg

        // 测试用例2
        String s3 = "123456";
        String s4 = "345678";
        System.out.println("最长公共子串：" + getLongestCommonSubstring(s3, s4)); // 3456

        // 测试用例3
        String s5 = "aaaaa";
        String s6 = "aa";
        System.out.println("最长公共子串：" + getLongestCommonSubstring(s5, s6)); // aa
    }
}
