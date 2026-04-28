package interviewee.Leecode;

public class minCount {

    /**
     * 计算拿走所有硬币的最少次数，每次可从同一堆拿 1 或 2 枚。
     *
     * @param coins 每堆硬币数量
     * @return 拿完所有硬币所需的最少次数
     */
    public int minCount(int[] coins) {
        int sum = 0;
        for(int c: coins){
            sum += (c % 2 + c/2);
        }
        return sum;
    }
}
