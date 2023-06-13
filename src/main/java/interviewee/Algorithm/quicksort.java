package interviewee.Algorithm;

import java.util.Arrays;

public class quicksort {

    public void sort(int p, int[] a, int left, int right){
        for (int i = left; i < right; i++) {
            if(a[p]<a[right]){
                swap(left, right, a);
            }
        }
    }

    public void swap(int a, int b, int[] arr){
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
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
        int[] arr = new int[]{11,45,4,47,56,8,1,35,45,45,65,87,49};
        Arrays.stream(quickSort(arr, 0, arr.length - 1)).forEach(s -> {
            System.out.println(s);
        });
    }
}
