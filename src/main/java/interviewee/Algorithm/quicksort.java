package interviewee.Algorithm;

import org.apache.storm.guava.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

public class quicksort {

    public static int splitArr(int[] arr, int left, int right){
        int p = arr[left];
        int index = left + 1;
        for(int i=left + 1; i<=right; i++){
            if(arr[i]>p){
                swap(arr, i, index);
                index ++;
            }
        }
        swap(arr, left, index -1);
        return index-1;
    }

    public static int[] sort(int[] a, int left, int right){
        if(left < right){
            int p = splitArr(a, left, a.length-1);
            sort(a, left, p-1);
            sort(a, p+1, right);
        }
        return a;
    }


    private static int[] quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int partitionIndex = partition(arr, left, right);
            quickSort(arr, left, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, right);
        }
        return arr;
    }

    private static int partition(int[] arr, int left, int right) {
        // 设定基准值（pivot）
        int pivot = left;
        int index = pivot + 1;
        for (int i = index; i <= right; i++) {
            if (arr[i] < arr[pivot]) {
                swap(arr, i, index);
                index++;
            }
        }
        swap(arr, pivot, index - 1);
        return index - 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1,2,1,3,4,2};
        productExceptSelf(arr);
        rotate(arr, 5);
        int[] a2 = Arrays.copyOf(arr, arr.length);
        Arrays.stream(quickSort(arr, 0, arr.length - 1)).forEach(s -> {
            System.out.println(s);
        });
        Arrays.stream(sort(a2, 0, a2.length -1)).forEach(s -> {
            System.out.println("a2: "+s);
        });

        int[][] d = new int[][]{{1,3}, {2,6}, {8,10},{15,18}};
        Arrays.stream(d).forEach(s -> {
            System.out.println(String.format("d:[%d, %d]", s[0], s[1]) );
        });

        Arrays.stream(merge(d)).forEach(s ->{
            System.out.println(String.format("dnew:[%d, %d]", s[0], s[1]) );
        });

    }

    public static void rotate(int[] nums, int k) {
        List<Integer> list = new ArrayList<>();
        for(int i: nums){
            list.add(i);
        }
        for(int i: nums){
            list.add(i);
        }

        int step = k>nums.length-1? k%nums.length-1: k;
        int index = 0;
        int j=0;
        for(int i=0; i<list.size()-1; i++){
            if(j>=nums.length) break;
            if(i<=step){
                continue;
            }
            nums[j++] = list.get(i);
        }

    }

    public static int[][] merge(int[][] intervals) {
        quickSort(intervals, 0, intervals.length-1);
        List<int[]> cp = new ArrayList<>();
        for(int i=0; i<=intervals.length-1; i++){
            int L = intervals[i][0], R = intervals[i][1];
            if(cp.size() == 0 || cp.get(cp.size()-1)[1] < L){
                cp.add(new int[]{L, R});
            }else{
                cp.get(cp.size()-1)[1] =Math.max(cp.get(cp.size()-1)[1], R);
            }

        }
        return cp.toArray(new int[cp.size()][]);
    }

    public static void quickSort(int[][] arr, int left, int right){
        if(left<right){
            int p = left;
            int index = left + 1;
            for(int i=left+1; i<=right; i++){
                if(arr[i][0]<arr[p][0]){
                    swap(arr, i, index);
                    index ++;
                }
            }
            swap(arr, p, index - 1);
            p =index-1;
            quickSort(arr, left, p-1);
            quickSort(arr, p+1, right);
        }
    }

    public static void swap(int[][] arr, int left, int right){
        int[] temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }

    public static int[] productExceptSelf(int[] nums) {
        int[] num = new int[nums.length];
        num[0] = 1;
        for(int i=1; i<nums.length; i++){
            num[i] = nums[i-1] * num[i-1];
        }
        num[num.length-1] = 1;
        int R =1;
        for(int j=num.length-1; j>=0; j--){
            num[j] = R * num[j];
            R *= nums[j];
        }
        return num;
    }
}
