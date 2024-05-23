package interviewee.Leecode.array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class maxSubArray {

    public int maxSubArray(int[] nums){
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int sum = nums[i];
            if(sum < 0){
                continue;
            }
            for (int j = i + 1; j < nums.length; j++) {
                int temp = sum + nums[j];
                if(temp <= 0){
                    break;
                }
                sum = temp;
            }
            max = Math.max(sum, max);
        }
        return max;
    }

    public int maxSubArray2(int[] nums) {
        int pre = 0, maxAns = nums[0];
        for (int x : nums) {
            pre = Math.max(pre + x, x);
            maxAns = Math.max(maxAns, pre);
        }
        return maxAns;
    }


    public static void main(String[] args) {
        maxSubArray a = new maxSubArray();
        int[] arr = new int[]{-2,1,-3,4,-1,2,1,-5,4};
        int z = a.maxSubArray(arr);
        z =a.maxSubArray2(arr);
    }
}
