package interviewee.Algorithm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 面试中常见的动态规划算法集合。
 *
 * 这些方法尽量保留 DP 的模板感：先定义状态，再按状态转移填表或滚动优化。
 */
public final class DynamicProgrammingAlgorithms {

    /**
     * 工具类不需要实例化。
     */
    private DynamicProgrammingAlgorithms() {
    }

    /**
     * 斐波那契数列：dp[i] = dp[i - 1] + dp[i - 2]，使用两个变量做空间优化。
     *
     * @param n 下标，要求 n >= 0
     * @return 第 n 个斐波那契数，f(0)=0，f(1)=1
     */
    public static long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n 不能小于 0");
        }
        if (n < 2) {
            return n;
        }
        long prev = 0;
        long curr = 1;
        for (int i = 2; i <= n; i++) {
            long next = prev + curr;
            prev = curr;
            curr = next;
        }
        return curr;
    }

    /**
     * 爬楼梯：每次爬 1 或 2 阶，dp[i] = dp[i - 1] + dp[i - 2]。
     *
     * @param n 楼梯阶数
     * @return 爬到第 n 阶的方案数
     */
    public static int climbStairs(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n <= 2) {
            return n;
        }
        int prev = 1;
        int curr = 2;
        for (int i = 3; i <= n; i++) {
            int next = prev + curr;
            prev = curr;
            curr = next;
        }
        return curr;
    }

    /**
     * 最大子数组和：dp 表示以当前位置结尾的最大连续子数组和。
     *
     * @param nums 原数组
     * @return 连续子数组的最大和
     */
    public static int maxSubArray(int[] nums) {
        requireNonEmpty(nums, "nums");
        int current = nums[0];
        int best = nums[0];
        for (int i = 1; i < nums.length; i++) {
            current = Math.max(nums[i], current + nums[i]);
            best = Math.max(best, current);
        }
        return best;
    }

    /**
     * 打家劫舍：dp[i] 表示考虑到第 i 间房时能偷到的最大金额。
     *
     * @param nums 每间房的金额
     * @return 不偷相邻房子的最大收益
     */
    public static int houseRobber(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int skip = 0;
        int take = 0;
        for (int num : nums) {
            int newTake = skip + num;
            int newSkip = Math.max(skip, take);
            take = newTake;
            skip = newSkip;
        }
        return Math.max(skip, take);
    }

    /**
     * 不同路径：机器人从左上角到右下角，只能向右或向下走。
     *
     * @param m 行数
     * @param n 列数
     * @return 不同路径数量
     */
    public static int uniquePaths(int m, int n) {
        if (m <= 0 || n <= 0) {
            return 0;
        }
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[j] += dp[j - 1];
            }
        }
        return dp[n - 1];
    }

    /**
     * 最小路径和：dp[j] 表示走到当前行第 j 列的最小路径和。
     *
     * @param grid 非负整数网格
     * @return 从左上角到右下角的最小路径和
     */
    public static int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int rows = grid.length;
        int cols = grid[0].length;
        int[] dp = new int[cols];
        dp[0] = grid[0][0];
        for (int j = 1; j < cols; j++) {
            dp[j] = dp[j - 1] + grid[0][j];
        }
        for (int i = 1; i < rows; i++) {
            dp[0] += grid[i][0];
            for (int j = 1; j < cols; j++) {
                dp[j] = Math.min(dp[j], dp[j - 1]) + grid[i][j];
            }
        }
        return dp[cols - 1];
    }

    /**
     * 最长公共子序列：dp[i][j] 表示 text1 前 i 个字符和 text2 前 j 个字符的 LCS 长度。
     *
     * @param text1 第一个字符串
     * @param text2 第二个字符串
     * @return 最长公共子序列长度
     */
    public static int longestCommonSubsequence(String text1, String text2) {
        requireNonNull(text1, "text1");
        requireNonNull(text2, "text2");
        int[][] dp = new int[text1.length() + 1][text2.length() + 1];
        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[text1.length()][text2.length()];
    }

    /**
     * 最长公共子串：dp[i][j] 表示以两个字符串当前位置结尾的公共连续后缀长度。
     *
     * @param text1 第一个字符串
     * @param text2 第二个字符串
     * @return 最长公共子串长度
     */
    public static int longestCommonSubstring(String text1, String text2) {
        requireNonNull(text1, "text1");
        requireNonNull(text2, "text2");
        int[][] dp = new int[text1.length() + 1][text2.length() + 1];
        int best = 0;
        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    best = Math.max(best, dp[i][j]);
                }
            }
        }
        return best;
    }

    /**
     * 编辑距离：dp[i][j] 表示 word1 前 i 个字符变成 word2 前 j 个字符的最少操作数。
     *
     * @param word1 第一个字符串
     * @param word2 第二个字符串
     * @return 插入、删除、替换的最少次数
     */
    public static int editDistance(String word1, String word2) {
        requireNonNull(word1, "word1");
        requireNonNull(word2, "word2");
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 0; i <= word1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= word2.length(); j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= word1.length(); i++) {
            for (int j = 1; j <= word2.length(); j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        return dp[word1.length()][word2.length()];
    }

    /**
     * 最长递增子序列：tails[len] 保存长度为 len+1 的递增子序列的最小结尾值。
     *
     * @param nums 原数组
     * @return 最长严格递增子序列长度
     */
    public static int longestIncreasingSubsequence(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int[] tails = new int[nums.length];
        int size = 0;
        for (int num : nums) {
            int left = 0;
            int right = size;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            tails[left] = num;
            if (left == size) {
                size++;
            }
        }
        return size;
    }

    /**
     * 零钱兑换最少硬币数：dp[i] 表示凑出金额 i 所需的最少硬币数量。
     *
     * @param coins  硬币面额数组
     * @param amount 目标金额
     * @return 最少硬币数，无法凑出时返回 -1
     */
    public static int coinChangeMinCoins(int[] coins, int amount) {
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        requireNonEmpty(coins, "coins");
        int inf = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, inf);
        dp[0] = 0;
        for (int coin : coins) {
            if (coin <= 0) {
                continue;
            }
            for (int i = coin; i <= amount; i++) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
        return dp[amount] == inf ? -1 : dp[amount];
    }

    /**
     * 零钱兑换组合数：dp[i] 表示凑出金额 i 的组合数量，外层枚举硬币避免重复排列。
     *
     * @param coins  硬币面额数组
     * @param amount 目标金额
     * @return 可凑出 amount 的组合数量
     */
    public static int coinChangeCombinations(int[] coins, int amount) {
        if (amount < 0) {
            return 0;
        }
        requireNonEmpty(coins, "coins");
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int coin : coins) {
            if (coin <= 0) {
                continue;
            }
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        return dp[amount];
    }

    /**
     * 0/1 背包：每个物品最多选一次，容量倒序遍历保证不会重复选同一物品。
     *
     * @param weights  每个物品重量
     * @param values   每个物品价值
     * @param capacity 背包容量
     * @return 容量限制内的最大价值
     */
    public static int knapsack01(int[] weights, int[] values, int capacity) {
        validateKnapsackInput(weights, values, capacity);
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < weights.length; i++) {
            for (int c = capacity; c >= weights[i]; c--) {
                dp[c] = Math.max(dp[c], dp[c - weights[i]] + values[i]);
            }
        }
        return dp[capacity];
    }

    /**
     * 完全背包：每个物品可以选择多次，容量正序遍历允许当前物品重复进入状态。
     *
     * @param weights  每个物品重量
     * @param values   每个物品价值
     * @param capacity 背包容量
     * @return 容量限制内的最大价值
     */
    public static int completeKnapsack(int[] weights, int[] values, int capacity) {
        validateKnapsackInput(weights, values, capacity);
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < weights.length; i++) {
            for (int c = weights[i]; c <= capacity; c++) {
                dp[c] = Math.max(dp[c], dp[c - weights[i]] + values[i]);
            }
        }
        return dp[capacity];
    }

    /**
     * 分割等和子集：转换为是否能从数组中选出和为 sum/2 的子集。
     *
     * @param nums 原数组
     * @return 可以分成两个和相等的子集时返回 true
     */
    public static boolean canPartition(int[] nums) {
        if (nums == null || nums.length == 0) {
            return false;
        }
        int sum = 0;
        for (int num : nums) {
            if (num < 0) {
                throw new IllegalArgumentException("canPartition 只处理非负整数数组");
            }
            sum += num;
        }
        if ((sum & 1) == 1) {
            return false;
        }
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        for (int num : nums) {
            for (int j = target; j >= num; j--) {
                dp[j] = dp[j] || dp[j - num];
            }
        }
        return dp[target];
    }

    /**
     * 单词拆分：dp[i] 表示 s 的前 i 个字符能否被字典中的单词拼出。
     *
     * @param s        原字符串
     * @param wordDict 单词字典
     * @return 可以完全拆分时返回 true
     */
    public static boolean wordBreak(String s, List<String> wordDict) {
        if (s == null || wordDict == null) {
            return false;
        }
        Set<String> dict = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    /**
     * 最长回文子串：从每个中心向两侧扩展，属于回文类 DP 面试题的常用优化写法。
     *
     * @param s 原字符串
     * @return 最长回文子串
     */
    public static String longestPalindromicSubstring(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }
        int start = 0;
        int end = 0;
        for (int i = 0; i < s.length(); i++) {
            int odd = expandAroundCenter(s, i, i);
            int even = expandAroundCenter(s, i, i + 1);
            int len = Math.max(odd, even);
            if (len > end - start + 1) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    /**
     * 验证数组不为空。
     *
     * @param arr  数组
     * @param name 参数名
     */
    private static void requireNonEmpty(int[] arr, String name) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(name + " 不能为空");
        }
    }

    /**
     * 验证对象不为 null。
     *
     * @param value 对象
     * @param name  参数名
     */
    private static void requireNonNull(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " 不能为 null");
        }
    }

    /**
     * 验证背包问题输入合法性。
     *
     * @param weights  重量数组
     * @param values   价值数组
     * @param capacity 背包容量
     */
    private static void validateKnapsackInput(int[] weights, int[] values, int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity 不能小于 0");
        }
        requireNonEmpty(weights, "weights");
        requireNonEmpty(values, "values");
        if (weights.length != values.length) {
            throw new IllegalArgumentException("weights 和 values 长度必须一致");
        }
        for (int weight : weights) {
            if (weight <= 0) {
                throw new IllegalArgumentException("物品重量必须大于 0");
            }
        }
    }

    /**
     * 从中心向两侧扩展，计算当前中心能形成的最长回文长度。
     *
     * @param s     原字符串
     * @param left  左中心
     * @param right 右中心
     * @return 当前中心对应的最长回文长度
     */
    private static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
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
     * 断言 long 结果相等。
     *
     * @param name     用例名称
     * @param actual   实际结果
     * @param expected 期望结果
     */
    private static void assertEquals(String name, long actual, long expected) {
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
        assertEquals("fibonacci", fibonacci(10), 55L);
        assertEquals("climbStairs", climbStairs(5), 8);
        assertEquals("maxSubArray", maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}), 6);
        assertEquals("houseRobber", houseRobber(new int[]{2, 7, 9, 3, 1}), 12);
        assertEquals("uniquePaths", uniquePaths(3, 7), 28);
        assertEquals("minPathSum", minPathSum(new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}}), 7);
        assertEquals("longestCommonSubsequence", longestCommonSubsequence("abcde", "ace"), 3);
        assertEquals("longestCommonSubstring", longestCommonSubstring("abcdefg", "defghi"), 4);
        assertEquals("editDistance", editDistance("horse", "ros"), 3);
        assertEquals("longestIncreasingSubsequence", longestIncreasingSubsequence(new int[]{10, 9, 2, 5, 3, 7, 101, 18}), 4);
        assertEquals("coinChangeMinCoins", coinChangeMinCoins(new int[]{1, 2, 5}, 11), 3);
        assertEquals("coinChangeCombinations", coinChangeCombinations(new int[]{1, 2, 5}, 5), 4);
        assertEquals("knapsack01", knapsack01(new int[]{1, 3, 4}, new int[]{15, 20, 30}, 4), 35);
        assertEquals("completeKnapsack", completeKnapsack(new int[]{1, 3, 4}, new int[]{15, 20, 30}, 4), 60);
        assertEquals("canPartition", canPartition(new int[]{1, 5, 11, 5}), true);
        assertEquals("wordBreak", wordBreak("leetcode", Arrays.asList("leet", "code")), true);
        assertEquals("longestPalindromicSubstring", longestPalindromicSubstring("cbbd"), "bb");

        System.out.println("All dynamic programming algorithms passed.");
    }
}
