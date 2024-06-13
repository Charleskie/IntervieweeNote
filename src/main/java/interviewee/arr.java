package interviewee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class arr {
    /***
     * 给定一个一维整数数组，找出差值最小的一组或者多组数，例如输入[2,1,4,7,5,9]，输出[2,1],[4,5]
     */
    public static List<Integer[]> getdiffmin(int[] arr){
        Arrays.sort(arr);
        int min = Integer.MAX_VALUE;
        List<Integer[]> res = new ArrayList<>();
        for (int i = 0; i < arr.length - 1; i++) {
            int diff = arr[i+1] - arr[i];
            if(diff <= min) {
                if(diff < min){
                    res.clear();
                    min = diff;
                }
                res.add(new Integer[]{arr[i],arr[i+1]});
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] arr = {2,1,4,7,5,9};
        getdiffmin(arr).forEach(s -> {
            for (Integer i : s) {
                System.out.print(i + ",");
            }
            System.out.println("");
        });
    }
}
