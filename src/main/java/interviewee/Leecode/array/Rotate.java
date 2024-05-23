package interviewee.Leecode.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rotate {

    public void rotate(int[] nums, int k) {
        List<Integer> list = new ArrayList<>();
        for(int i: nums){
            list.add(i);
        }
        for(int i: nums){
            list.add(i);
        }

        int step = k>nums.length? k%nums.length: k;
        int j=0;
        for(int i=0; i<list.size()-1; i++){
            if(j>=nums.length) break;
            if(i<(nums.length-step)){
                continue;
            }
            nums[j++] = list.get(i);
        }
    }

    public void rotateV2(int[] nums, int k) {
        int n = nums.length;
        int[] newArr = new int[n];
        for (int i = 0; i < n; ++i) {
            newArr[(i + k) % n] = nums[i];
        }
        System.arraycopy(newArr, 0, nums, 0, n);
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1,2,5,8,9,7,45};
        Rotate rotate = new Rotate();
        rotate.rotate(arr, 3);
        Arrays.stream(arr).forEach(System.out::println);
    }
}
