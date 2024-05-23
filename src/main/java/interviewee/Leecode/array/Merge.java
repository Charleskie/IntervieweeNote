package interviewee.Leecode.array;

import java.util.ArrayList;
import java.util.List;

public class Merge {

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

    public void swap(int[][] arr, int left, int right){
        int[] temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }
}
