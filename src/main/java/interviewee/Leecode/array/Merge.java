package interviewee.Leecode.array;

import java.util.ArrayList;
import java.util.List;

public class Merge {

    /**
     * 合并重叠区间：先按区间起点排序，再依次合并相交区间。
     *
     * @param intervals 区间数组
     * @return 合并后的区间数组
     */
    public int[][] merge(int[][] intervals) {
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

    /**
     * 按每个区间的起点对二维数组做快速排序。
     *
     * @param arr   待排序区间数组
     * @param left  排序左边界
     * @param right 排序右边界
     */
    public void quickSort(int[][] arr, int left, int right){
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

    /**
     * 交换二维数组中的两行。
     *
     * @param arr   二维数组
     * @param left  第一个行下标
     * @param right 第二个行下标
     */
    public void swap(int[][] arr, int left, int right){
        int[] temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }
}
