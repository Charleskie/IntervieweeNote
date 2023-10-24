package interviewee.Leecode.dp;

public class ClimbStairs {
    public int climbStairs(int n) {
        if(n <= 2){
            return n;
        }
        return climbStairs(n-1) + climbStairs(n-2);
    }

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
