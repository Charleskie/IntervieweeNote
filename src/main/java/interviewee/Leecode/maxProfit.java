package interviewee.Leecode;

public class maxProfit {

    /***
     * 买股票的最佳时机
     * @param prices
     * @return
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
     * @param nums
     * @return
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
     * @param nums
     * @return
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
