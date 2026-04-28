package interviewee.Leecode.dp;

public class ClimbStairs {
    /**
     * 递归计算爬 n 阶楼梯的方案数，每次可以爬 1 或 2 阶。
     *
     * @param n 台阶数
     * @return 爬到第 n 阶的方案数
     */
    public int climbStairs(int n) {
        if(n <= 2){
            return n;
        }
        return climbStairs(n-1) + climbStairs(n-2);
    }

    /**
     * 迭代动态规划计算爬楼梯方案数，避免递归重复计算。
     *
     * @param n 台阶数
     * @return 爬到第 n 阶的方案数
     */
    public int climbStairs2(int n) {
        int pre = 0;
        int next = 0;
        int step = 1;
        for (int i = 0; i < n; i++) {
            pre = next;
            next = step;
            step = pre + next;
        }
        return step;
    }

    public static void main(String[] args) {
        ClimbStairs c = new ClimbStairs();
        System.out.println(c.climbStairs2(45));
    }
}
