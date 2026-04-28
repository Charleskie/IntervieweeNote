package interviewee.Leecode.array;


import java.util.Arrays;

/***
 * 给你一个整数数组nums，以及两个数target, start,
 * 找出i满足nums[i]==target且abs(i-start)最小化,
 * 返回abs(i-start)
 */
public class MinDistanceNums {
    /**
     * 查找等于 target 的元素到 start 下标的最小距离。
     *
     * @param nums   原数组
     * @param target 目标值
     * @param start  起始下标
     * @return 满足 nums[i] == target 的最小 |i - start|
     */
    public int get_min_distance(int[] nums, int target, int start){
        int tmp = 0;
        for(int i = start; i < nums.length; i++){
            if(nums[i] == target) {
                tmp = Math.abs(i - start);
            }
        }
        return tmp;
    }

    public static void main(String[] args) {
        MinDistanceNums minDistanceNums = new MinDistanceNums();
        int[] nums = new int[]{1,2,3,4,5};
        System.out.println(minDistanceNums.get_min_distance(nums, 3, 3));
    }
}
