package interviewee.Algorithm;

import java.util.*;

public class quicksort {

    /**
     * 以左边界元素为基准做一次降序分区，左侧放大于基准的元素，右侧放小于等于基准的元素。
     *
     * @param arr   待分区数组
     * @param left  分区左边界
     * @param right 分区右边界
     * @return 基准元素分区后的下标
     */
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

    /**
     * 使用 {@link #splitArr(int[], int, int)} 对数组做递归快排，当前实现按降序分区。
     *
     * @param a     待排序数组
     * @param left  排序左边界
     * @param right 排序右边界
     * @return 排序后的原数组引用
     */
    public static int[] sort(int[] a, int left, int right){
        if(left < right){
            int p = splitArr(a, left, a.length-1);
            sort(a, left, p-1);
            sort(a, p+1, right);
        }
        return a;
    }


    /**
     * 标准升序快速排序入口，对指定闭区间进行原地排序。
     *
     * @param arr   待排序数组
     * @param left  排序左边界
     * @param right 排序右边界
     * @return 排序后的原数组引用
     */
    private static int[] quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int partitionIndex = partition(arr, left, right);
            quickSort(arr, left, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, right);
        }
        return arr;
    }

    /**
     * 快排分区：以左边界为基准，将小于基准的元素移动到基准左侧。
     *
     * @param arr   待分区数组
     * @param left  分区左边界
     * @param right 分区右边界
     * @return 基准元素归位后的下标
     */
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



    public static void main(String[] args) {
        int[] arr = new int[]{1,2,1,3,4,2};
        quickSortKim(arr, 0, arr.length-1);
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

    /**
     * 将数组向右旋转 k 位，借助复制后的列表窗口回填到原数组。
     *
     * @param nums 待旋转数组
     * @param k    右移步数
     */
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

    /**
     * 合并重叠区间：先按区间起点排序，再线性扫描合并。
     *
     * @param intervals 区间数组，每个元素形如 [start, end]
     * @return 合并后的区间数组
     */
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

    /**
     * 按二维数组每行的第一个元素做升序快速排序。
     *
     * @param arr   待排序区间数组
     * @param left  排序左边界
     * @param right 排序右边界
     */
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

    /**
     * 交换二维数组中的两行。
     *
     * @param arr   二维数组
     * @param left  第一个行下标
     * @param right 第二个行下标
     */
    public static void swap(int[][] arr, int left, int right){
        int[] temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }


    /**
     * 另一版原地升序快排实现，同样以左边界元素为基准做递归分区。
     *
     * @param arr   待排序数组
     * @param left  排序左边界
     * @param right 排序右边界
     */
    public static void quickSortKim(int[] arr, int left, int right){
        if(left< right){
            int p = left;
            int index = left + 1;
            for(int i=left+1; i <= right; i++){
                if(arr[left] > arr[i]) {
                    swap(arr, i, index);
                    index++;
                }
            }
            swap(arr, p, index -1);
            p = index-1;
            quickSortKim(arr, left, p-1);
            quickSortKim(arr, p+1, right);
        }
    }

    /**
     * 交换数组中的两个位置。
     *
     * @param arr 待交换数组
     * @param i   第一个下标
     * @param j   第二个下标
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 计算除自身以外数组的乘积，使用前缀乘积和后缀乘积避免除法。
     *
     * @param nums 原数组
     * @return 每个位置除自身外其他元素的乘积
     */
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

    /**
     * 判断两个字符串是否逐字符完全相同。
     *
     * @param a 第一个字符串
     * @param b 第二个字符串
     * @return 长度相同且所有字符一致时返回 true
     */
    public boolean isCode(String a, String b){
        if(a.length() != b.length()){
            return false;
        }
        char[] ca = a.toCharArray();
        char[] cb = b.toCharArray();

        for (int i = 0; i < ca.length; i++) {
            if(ca[i] != cb[i]){
                return false;
            }
        }
        return true;
    }

    /**
     * 将字符串按当前相等判断进行分组，目标意图是聚合同一类字符串。
     *
     * @param strs 待分组字符串数组
     * @return 分组后的字符串列表
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> group = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            if(strs[i] == null)continue;
            List<String> list = new ArrayList<>();
            list.add(strs[i]);
            for (int j = i+1; j < strs.length; j++) {
                if(strs[i] == null)continue;
                if(isCode(strs[i], strs[j])){
                    list.add(strs[j]);
                    strs[j] = null;
                }
            }
            group.add(list);
        }
        return group;

    }

//    public int longestConsecutive(int[] nums) {
//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < nums.length; i++) {
//            list.add(nums[i]);
//            for (int j = i+1; j < nums.length; j++) {
//                if()
//            }
//        }
//
//    }

//    public List<String> letterCombinations(String digits) {
//        Map<String, List>
//
//    }
}
