package interviewee.Leecode.tianyiyun;

import java.util.ArrayDeque;
import java.util.Arrays;

public class MostCompetitive {
    /****
     * 给你一个整数数组 nums 和一个正整数 k ，返回长度为 k 且最具 竞争力 的 nums 子序列。
     *
     * 数组的子序列是从数组中删除一些元素（可能不删除元素）得到的序列。
     *
     * 在子序列 a 和子序列 b 第一个不相同的位置上，如果 a 中的数字小于 b 中对应的数字，那么我们称子序列 a 比子序列 b（相同长度下）更具 竞争力 。 例如，[1,3,4] 比 [1,3,5] 更具竞争力，在第一个不相同的位置，也就是最后一个位置上， 4 小于 5 。
     */

    public int[] mostCompetitive(int[] nums, int k) {
        // 维护一个长度为 k 的单调递增的栈
        int n = nums.length;
        int[] ans = new int[k];
        int del = n - k;
        ArrayDeque<Integer> stacks = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stacks.isEmpty() && del > 0 && stacks.peek() > nums[i]) {
                del--;
                stacks.pop();
            }
            stacks.push(nums[i]);
        }
        while (del > 0) {
            stacks.pop();
            del--;
        }
        for (int num : stacks) {
            ans[--k] = num;
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{2,3,4,2,3,3};
        MostCompetitive a = new MostCompetitive();
        Arrays.stream(a.mostCompetitive(arr, 3)).forEach(System.out::println);
    }
}
