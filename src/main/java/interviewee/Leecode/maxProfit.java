package interviewee.Leecode;

public class maxProfit {

    /***
     * 买股票的最佳时机
     *
     * @param prices 每天的股票价格
     * @return 一次买卖可获得的最大利润
     */
    public int maxProfit(int prices[]) {
        int minprice = Integer.MAX_VALUE;
        int maxprofit = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < minprice) {
                minprice = prices[i];
            } else if (prices[i] - minprice > maxprofit) {
                maxprofit = prices[i] - minprice;
            }
        }
        return maxprofit;
    }

    /****
     * 跳跃游戏
     *
     * @param nums 每个位置可跳跃的最大步数
     * @return 能否从起点跳到最后一个位置
     */
    public boolean canJump(int[] nums) {
        int maxJ = 0;
        for (int i = 0; i < nums.length; i++) {
            if(i <= maxJ) {
                maxJ = Math.max(maxJ, nums[i] + i);
                if(maxJ >= nums.length - 1){
                    return true;
                }
            }
        }
        return false;
    }

    /***
     * 调到n-1的最小次数
     *
     * @param nums 每个位置可跳跃的最大步数
     * @return 到达最后一个位置需要的最少跳跃次数
     */
    public int jump(int[] nums) {
        int maxJ = 0;
        int end = 0;
        int step = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            maxJ = Math.max(maxJ, nums[i] + i);
            if(i == end){
                step++;
                end = maxJ;
            }

        }
        return step;
    }

    /**
     * 买股票的最佳时机的另一版实现，维护历史最低买入价和当前最大利润。
     *
     * @param prices 每天的股票价格
     * @return 一次买卖可获得的最大利润
     */
    public int maxProfits(int[] prices) {
        int max = 0;
        int minPrice = prices[0];
        for (int i = 1; i < prices.length; i++) {
            if(prices[i] < minPrice){
                minPrice = prices[i];
            }else {
                max = Math.max(max, prices[i] - minPrice);
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{7,6,4,3,1};
        int[] arr1 = new int[]{7,1,5,3,6,4};
        maxProfit m = new maxProfit();
        System.out.println(m.maxProfits(arr));
        System.out.println(m.maxProfits(arr1));
    }
}
