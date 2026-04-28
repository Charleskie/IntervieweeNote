package interviewee.meituan;

/***
 * 一次走一级或者两级台阶，请问走上n级台阶有多少种走法
 */
public class CalFootstep{
    public static void main(String[] args){
        System.out.println(calstep(4));
    }

    /**
     * 动态规划计算每次走 1 或 2 级台阶时，走上 n 级台阶的方案数。
     *
     * @param n 台阶数
     * @return 走到第 n 级的方案数
     */
    static int calstep(int n){
        int[] dp = new int[n+1];
        dp[0] = 1;
        dp[1] = 1;
        for(int i=2;i<n+1;i++){
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n];
    }
}
