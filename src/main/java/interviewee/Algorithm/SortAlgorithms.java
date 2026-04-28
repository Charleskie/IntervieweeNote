package interviewee.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 常见排序算法集合。
 *
 * 约定：所有 public 排序方法都会在原数组上原地排序，并返回同一个数组引用；传入 null 时直接返回 null。
 */
public final class SortAlgorithms {

    private static final int DEFAULT_BUCKET_SIZE = 5;

    /**
     * 工具类不需要实例化。
     */
    private SortAlgorithms() {
    }

    /**
     * 冒泡排序：重复比较相邻元素，把当前未排序区间中的最大值逐步冒到末尾。
     *
     * @param arr 待排序数组
     * @return 升序排序后的原数组引用
     */
    public static int[] bubbleSort(int[] arr) {
        if (isTrivial(arr)) {
            return arr;
        }
        for (int end = arr.length - 1; end > 0; end--) {
            boolean swapped = false;
            for (int i = 0; i < end; i++) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return arr;
    }

    /**
     * 选择排序：每一轮从未排序区间中选择最小值，放到当前区间开头。
     *
     * @param arr 待排序数组
     * @return 升序排序后的原数组引用
     */
    public static int[] selectionSort(int[] arr) {
        if (isTrivial(arr)) {
            return arr;
        }
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                swap(arr, i, minIndex);
            }
        }
        return arr;
    }

    /**
     * 插入排序：把当前元素插入到左侧已经有序的区间中。
     *
     * @param arr 待排序数组
     * @return 升序排序后的原数组引用
     */
    public static int[] insertionSort(int[] arr) {
        if (isTrivial(arr)) {
            return arr;
        }
        for (int i = 1; i < arr.length; i++) {
            int current = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > current) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = current;
        }
        return arr;
    }

    /**
     * 希尔排序：按递减步长做分组插入排序，最后退化为一次普通插入排序。
     *
     * @param arr 待排序数组
     * @return 升序排序后的原数组引用
     */
    public static int[] shellSort(int[] arr) {
        if (isTrivial(arr)) {
            return arr;
        }
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < arr.length; i++) {
                int current = arr[i];
                int j = i - gap;
                while (j >= 0 && arr[j] > current) {
                    arr[j + gap] = arr[j];
                    j -= gap;
                }
                arr[j + gap] = current;
            }
        }
        return arr;
    }

    /**
     * 归并排序：递归拆分数组，再把两个有序区间稳定合并。
     *
     * @param arr 待排序数组
     * @return 升序排序后的原数组引用
     */
    public static int[] mergeSort(int[] arr) {
        if (isTrivial(arr)) {
            return arr;
        }
        int[] temp = new int[arr.length];
        mergeSort(arr, temp, 0, arr.length - 1);
        return arr;
    }

    /**
     * 快速排序：选择中间元素作为基准，递归处理左右两个分区。
     *
     * @param arr 待排序数组
     * @return 升序排序后的原数组引用
     */
    public static int[] quickSort(int[] arr) {
        if (isTrivial(arr)) {
            return arr;
        }
        quickSort(arr, 0, arr.length - 1);
        return arr;
    }

    /**
     * 堆排序：先建立大根堆，再反复把堆顶最大值交换到数组末尾。
     *
     * @param arr 待排序数组
     * @return 升序排序后的原数组引用
     */
    public static int[] heapSort(int[] arr) {
        if (isTrivial(arr)) {
            return arr;
        }
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            heapify(arr, arr.length, i);
        }
        for (int end = arr.length - 1; end > 0; end--) {
            swap(arr, 0, end);
            heapify(arr, end, 0);
        }
        return arr;
    }

    /**
     * 计数排序：统计每个值出现次数后回填，适合整数范围不大的场景。
     *
     * @param arr 待排序数组
     * @return 升序排序后的原数组引用
     */
    public static int[] countingSort(int[] arr) {
        if (isTrivial(arr)) {
            return arr;
        }
        int min = arr[0];
        int max = arr[0];
        for (int num : arr) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        long rangeLong = (long) max - min + 1;
        if (rangeLong > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("数据范围过大，不适合使用计数排序");
        }
        int[] count = new int[(int) rangeLong];
        for (int num : arr) {
            count[num - min]++;
        }
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            while (count[i] > 0) {
                arr[index++] = i + min;
                count[i]--;
            }
        }
        return arr;
    }

    /**
     * 桶排序：按数值范围把元素分桶，桶内用插入排序，再依次回填。
     *
     * @param arr 待排序数组
     * @return 升序排序后的原数组引用
     */
    public static int[] bucketSort(int[] arr) {
        return bucketSort(arr, DEFAULT_BUCKET_SIZE);
    }

    /**
     * 桶排序：按指定桶宽把元素分桶，桶内用插入排序，再依次回填。
     *
     * @param arr        待排序数组
     * @param bucketSize 每个桶覆盖的数值范围，必须大于 0
     * @return 升序排序后的原数组引用
     */
    public static int[] bucketSort(int[] arr, int bucketSize) {
        if (isTrivial(arr)) {
            return arr;
        }
        if (bucketSize <= 0) {
            throw new IllegalArgumentException("bucketSize 必须大于 0");
        }
        int min = arr[0];
        int max = arr[0];
        for (int num : arr) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        long bucketCountLong = ((long) max - min) / bucketSize + 1;
        if (bucketCountLong > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("数据范围过大，不适合使用桶排序");
        }
        int bucketCount = (int) bucketCountLong;
        List<List<Integer>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<Integer>());
        }
        for (int num : arr) {
            buckets.get((int) (((long) num - min) / bucketSize)).add(num);
        }
        int index = 0;
        for (List<Integer> bucket : buckets) {
            insertionSort(bucket);
            for (int num : bucket) {
                arr[index++] = num;
            }
        }
        return arr;
    }

    /**
     * 基数排序：按二进制字节从低位到高位做稳定计数排序，支持 int 的负数和正数。
     *
     * @param arr 待排序数组
     * @return 升序排序后的原数组引用
     */
    public static int[] radixSort(int[] arr) {
        if (isTrivial(arr)) {
            return arr;
        }
        int[] from = arr;
        int[] to = new int[arr.length];
        for (int shift = 0; shift < Integer.SIZE; shift += Byte.SIZE) {
            int[] count = new int[256];
            for (int value : from) {
                count[digit(value, shift)]++;
            }
            for (int i = 1; i < count.length; i++) {
                count[i] += count[i - 1];
            }
            for (int i = from.length - 1; i >= 0; i--) {
                int value = from[i];
                to[--count[digit(value, shift)]] = value;
            }
            int[] temp = from;
            from = to;
            to = temp;
        }
        if (from != arr) {
            System.arraycopy(from, 0, arr, 0, arr.length);
        }
        return arr;
    }

    /**
     * 判断数组是否不需要排序。
     *
     * @param arr 待判断数组
     * @return 数组为 null 或长度小于 2 时返回 true
     */
    private static boolean isTrivial(int[] arr) {
        return arr == null || arr.length < 2;
    }

    /**
     * 递归执行归并排序。
     *
     * @param arr   待排序数组
     * @param temp  辅助数组
     * @param left  左边界
     * @param right 右边界
     */
    private static void mergeSort(int[] arr, int[] temp, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = left + (right - left) / 2;
        mergeSort(arr, temp, left, mid);
        mergeSort(arr, temp, mid + 1, right);
        merge(arr, temp, left, mid, right);
    }

    /**
     * 合并两个相邻有序区间。
     *
     * @param arr   原数组
     * @param temp  辅助数组
     * @param left  左区间起点
     * @param mid   左区间终点
     * @param right 右区间终点
     */
    private static void merge(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        int index = left;
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[index++] = arr[i++];
            } else {
                temp[index++] = arr[j++];
            }
        }
        while (i <= mid) {
            temp[index++] = arr[i++];
        }
        while (j <= right) {
            temp[index++] = arr[j++];
        }
        for (int k = left; k <= right; k++) {
            arr[k] = temp[k];
        }
    }

    /**
     * 递归执行快速排序。
     *
     * @param arr   待排序数组
     * @param left  左边界
     * @param right 右边界
     */
    private static void quickSort(int[] arr, int left, int right) {
        int i = left;
        int j = right;
        int pivot = arr[left + (right - left) / 2];
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                swap(arr, i, j);
                i++;
                j--;
            }
        }
        if (left < j) {
            quickSort(arr, left, j);
        }
        if (i < right) {
            quickSort(arr, i, right);
        }
    }

    /**
     * 维护大根堆性质。
     *
     * @param arr      堆所在数组
     * @param heapSize 当前堆大小
     * @param root     当前下沉节点下标
     */
    private static void heapify(int[] arr, int heapSize, int root) {
        int largest = root;
        int left = root * 2 + 1;
        int right = root * 2 + 2;
        if (left < heapSize && arr[left] > arr[largest]) {
            largest = left;
        }
        if (right < heapSize && arr[right] > arr[largest]) {
            largest = right;
        }
        if (largest != root) {
            swap(arr, root, largest);
            heapify(arr, heapSize, largest);
        }
    }

    /**
     * 对桶内元素执行插入排序。
     *
     * @param bucket 当前桶
     */
    private static void insertionSort(List<Integer> bucket) {
        for (int i = 1; i < bucket.size(); i++) {
            int current = bucket.get(i);
            int j = i - 1;
            while (j >= 0 && bucket.get(j) > current) {
                bucket.set(j + 1, bucket.get(j));
                j--;
            }
            bucket.set(j + 1, current);
        }
    }

    /**
     * 计算基数排序当前字节位的桶编号，通过翻转符号位支持有符号整数排序。
     *
     * @param value 原始整数
     * @param shift 当前字节偏移量
     * @return 0 到 255 之间的桶编号
     */
    private static int digit(int value, int shift) {
        return ((value ^ Integer.MIN_VALUE) >>> shift) & 0xff;
    }

    /**
     * 交换数组中两个下标的元素。
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
     * 对排序结果做简单自检。
     *
     * @param name     算法名称
     * @param actual   实际排序结果
     * @param expected 期望排序结果
     */
    private static void assertSorted(String name, int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new IllegalStateException(name + " 排序结果错误: " + Arrays.toString(actual));
        }
    }

    public static void main(String[] args) {
        int[] source = new int[]{5, -3, 7, 1, 1, 0, 9, -8, 4, Integer.MIN_VALUE, Integer.MAX_VALUE};
        int[] expected = Arrays.copyOf(source, source.length);
        Arrays.sort(expected);

        assertSorted("bubbleSort", bubbleSort(Arrays.copyOf(source, source.length)), expected);
        assertSorted("selectionSort", selectionSort(Arrays.copyOf(source, source.length)), expected);
        assertSorted("insertionSort", insertionSort(Arrays.copyOf(source, source.length)), expected);
        assertSorted("shellSort", shellSort(Arrays.copyOf(source, source.length)), expected);
        assertSorted("mergeSort", mergeSort(Arrays.copyOf(source, source.length)), expected);
        assertSorted("quickSort", quickSort(Arrays.copyOf(source, source.length)), expected);
        assertSorted("heapSort", heapSort(Arrays.copyOf(source, source.length)), expected);
        assertSorted("countingSort", countingSort(new int[]{5, -3, 7, 1, 1, 0, 9, -8, 4}), new int[]{-8, -3, 0, 1, 1, 4, 5, 7, 9});
        assertSorted("bucketSort", bucketSort(new int[]{5, -3, 7, 1, 1, 0, 9, -8, 4}), new int[]{-8, -3, 0, 1, 1, 4, 5, 7, 9});
        assertSorted("radixSort", radixSort(Arrays.copyOf(source, source.length)), expected);

        System.out.println("All sort algorithms passed.");
    }
}
