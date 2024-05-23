package interviewee.Leecode.array;

public class subarrysum {

    /***
     * 和为 K 的子数组
     * @param nums
     * @param k
     * @return
     */
    public int subarraySum(int[] nums, int k) {
        int count = 0;
        for (int start = 0; start < nums.length; ++start) {
            int sum = 0;
            for (int end = start; end >= 0; --end) {
                sum += nums[end];
                if (sum == k) {
                    count++;
                }
            }
        }
        return count;
    }

    /****
     * 除自身以外数组的乘积
     * @param nums
     * @return
     */
    public int[] productExceptSelf(int[] nums) {
        int[] num = new int[nums.length];
        num[0] = 1;
        for(int i=1; i<nums.length; i++){
            num[i] = nums[i-1] * num[i-1];
        }
        int R = 1;
        for(int j=num.length-1; j>=0; j--){
            num[j] = num[j] * R;
            R *= nums[j];
        }
        return num;
    }

}
