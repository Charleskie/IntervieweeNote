package interviewee.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 面试中常见的数组算法集合。
 *
 * 重点覆盖双指针、滑动窗口、二分查找、前缀和、区间合并和矩阵原地操作。
 */
public final class ArrayInterviewAlgorithms {

    /**
     * 工具类不需要实例化。
     */
    private ArrayInterviewAlgorithms() {
    }

    /**
     * 两数之和：哈希表记录已遍历数字的下标。
     *
     * @param nums   原数组
     * @param target 目标和
     * @return 两个数的下标；不存在时返回 {-1, -1}
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (indexMap.containsKey(need)) {
                return new int[]{indexMap.get(need), i};
            }
            indexMap.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    /**
     * 三数之和：排序后固定一个数，剩余部分用双指针查找。
     *
     * @param nums 原数组
     * @return 所有不重复且和为 0 的三元组
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return result;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1;
            int right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }

    /**
     * 盛最多水的容器：双指针每次移动较短的一侧。
     *
     * @param height 每条竖线高度
     * @return 能盛水的最大面积
     */
    public static int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int best = 0;
        while (left < right) {
            best = Math.max(best, Math.min(height[left], height[right]) * (right - left));
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return best;
    }

    /**
     * 接雨水：左右双指针维护两侧最高挡板。
     *
     * @param height 柱子高度数组
     * @return 可以接住的雨水总量
     */
    public static int trapRainWater(int[] height) {
        if (height == null || height.length < 3) {
            return 0;
        }
        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int water = 0;
        while (left < right) {
            if (height[left] < height[right]) {
                leftMax = Math.max(leftMax, height[left]);
                water += leftMax - height[left];
                left++;
            } else {
                rightMax = Math.max(rightMax, height[right]);
                water += rightMax - height[right];
                right--;
            }
        }
        return water;
    }

    /**
     * 和为 k 的连续子数组数量：前缀和 + 哈希表统计出现次数。
     *
     * @param nums 原数组
     * @param k    目标和
     * @return 连续子数组数量
     */
    public static int subarraySumEqualsK(int[] nums, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        count.put(0, 1);
        int prefix = 0;
        int result = 0;
        for (int num : nums) {
            prefix += num;
            result += count.getOrDefault(prefix - k, 0);
            count.put(prefix, count.getOrDefault(prefix, 0) + 1);
        }
        return result;
    }

    /**
     * 除自身以外数组的乘积：先写入前缀乘积，再乘以后缀乘积。
     *
     * @param nums 原数组
     * @return 每个位置除自身外其他元素的乘积
     */
    public static int[] productExceptSelf(int[] nums) {
        int[] result = new int[nums.length];
        result[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }
        int suffix = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            result[i] *= suffix;
            suffix *= nums[i];
        }
        return result;
    }

    /**
     * 合并重叠区间：按左端点排序后线性扫描。
     *
     * @param intervals 区间数组
     * @return 合并后的区间数组
     */
    public static int[][] mergeIntervals(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return new int[0][0];
        }
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> merged = new ArrayList<>();
        for (int[] interval : intervals) {
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(new int[]{interval[0], interval[1]});
            } else {
                merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], interval[1]);
            }
        }
        return merged.toArray(new int[merged.size()][]);
    }

    /**
     * 搜索旋转排序数组：二分时判断哪一半有序。
     *
     * @param nums   旋转排序数组
     * @param target 目标值
     * @return 目标下标；不存在时返回 -1
     */
    public static int searchRotatedSortedArray(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }

    /**
     * 查找旋转排序数组中的最小值。
     *
     * @param nums 旋转排序数组
     * @return 最小值
     */
    public static int findMinInRotatedSortedArray(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return nums[left];
    }

    /**
     * 移动零：保持非零元素相对顺序，把所有 0 移到末尾。
     *
     * @param nums 原数组
     */
    public static void moveZeroes(int[] nums) {
        int insert = 0;
        for (int num : nums) {
            if (num != 0) {
                nums[insert++] = num;
            }
        }
        while (insert < nums.length) {
            nums[insert++] = 0;
        }
    }

    /**
     * 原地旋转数组：整体反转，再分别反转两段。
     *
     * @param nums 原数组
     * @param k    向右旋转步数
     */
    public static void rotateArray(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return;
        }
        k %= nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    /**
     * 下一个排列：从右向左找下降点，交换后反转后缀。
     *
     * @param nums 原数组
     */
    public static void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }
        if (i >= 0) {
            int j = nums.length - 1;
            while (nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }
        reverse(nums, i + 1, nums.length - 1);
    }

    /**
     * 多数元素：Boyer-Moore 投票算法。
     *
     * @param nums 原数组
     * @return 出现次数超过 n/2 的元素
     */
    public static int majorityElement(int[] nums) {
        int candidate = 0;
        int votes = 0;
        for (int num : nums) {
            if (votes == 0) {
                candidate = num;
            }
            votes += num == candidate ? 1 : -1;
        }
        return candidate;
    }

    /**
     * 最长连续序列：哈希集合只从连续段起点开始扩展。
     *
     * @param nums 原数组
     * @return 最长连续整数序列长度
     */
    public static int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int best = 0;
        for (int num : set) {
            if (!set.contains(num - 1)) {
                int current = num;
                int length = 1;
                while (set.contains(current + 1)) {
                    current++;
                    length++;
                }
                best = Math.max(best, length);
            }
        }
        return best;
    }

    /**
     * 股票买卖最佳时机：维护历史最低价和最大利润。
     *
     * @param prices 每日股票价格
     * @return 一次买卖可获得的最大利润
     */
    public static int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int best = 0;
        for (int price : prices) {
            minPrice = Math.min(minPrice, price);
            best = Math.max(best, price - minPrice);
        }
        return best;
    }

    /**
     * 矩阵置零：使用第一行和第一列作为标记位，原地完成置零。
     *
     * @param matrix 矩阵
     */
    public static void setZeroes(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }
        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean firstRowZero = false;
        boolean firstColZero = false;
        for (int j = 0; j < cols; j++) {
            if (matrix[0][j] == 0) {
                firstRowZero = true;
                break;
            }
        }
        for (int i = 0; i < rows; i++) {
            if (matrix[i][0] == 0) {
                firstColZero = true;
                break;
            }
        }
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        if (firstRowZero) {
            Arrays.fill(matrix[0], 0);
        }
        if (firstColZero) {
            for (int i = 0; i < rows; i++) {
                matrix[i][0] = 0;
            }
        }
    }

    /**
     * 螺旋遍历矩阵。
     *
     * @param matrix 矩阵
     * @return 螺旋顺序的元素列表
     */
    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return result;
        }
        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int j = left; j <= right; j++) {
                result.add(matrix[top][j]);
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                result.add(matrix[i][right]);
            }
            right--;
            if (top <= bottom) {
                for (int j = right; j >= left; j--) {
                    result.add(matrix[bottom][j]);
                }
                bottom--;
            }
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    result.add(matrix[i][left]);
                }
                left++;
            }
        }
        return result;
    }

    /**
     * 反转数组指定闭区间。
     *
     * @param nums  原数组
     * @param left  左边界
     * @param right 右边界
     */
    private static void reverse(int[] nums, int left, int right) {
        while (left < right) {
            swap(nums, left++, right--);
        }
    }

    /**
     * 交换数组两个位置。
     *
     * @param nums 原数组
     * @param i    第一个下标
     * @param j    第二个下标
     */
    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * 断言 int 结果相等。
     *
     * @param name     用例名称
     * @param actual   实际结果
     * @param expected 期望结果
     */
    private static void assertEquals(String name, int actual, int expected) {
        if (actual != expected) {
            throw new IllegalStateException(name + " 结果错误: " + actual + ", expected: " + expected);
        }
    }

    /**
     * 断言数组结果相等。
     *
     * @param name     用例名称
     * @param actual   实际数组
     * @param expected 期望数组
     */
    private static void assertArrayEquals(String name, int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new IllegalStateException(name + " 结果错误: " + Arrays.toString(actual));
        }
    }

    /**
     * 断言二维数组结果相等。
     *
     * @param name     用例名称
     * @param actual   实际二维数组
     * @param expected 期望二维数组
     */
    private static void assertMatrixEquals(String name, int[][] actual, int[][] expected) {
        if (!Arrays.deepEquals(actual, expected)) {
            throw new IllegalStateException(name + " 结果错误: " + Arrays.deepToString(actual));
        }
    }

    public static void main(String[] args) {
        assertArrayEquals("twoSum", twoSum(new int[]{2, 7, 11, 15}, 9), new int[]{0, 1});
        assertEquals("threeSum", threeSum(new int[]{-1, 0, 1, 2, -1, -4}).size(), 2);
        assertEquals("maxArea", maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}), 49);
        assertEquals("trapRainWater", trapRainWater(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}), 6);
        assertEquals("subarraySumEqualsK", subarraySumEqualsK(new int[]{1, 1, 1}, 2), 2);
        assertArrayEquals("productExceptSelf", productExceptSelf(new int[]{1, 2, 3, 4}), new int[]{24, 12, 8, 6});
        assertMatrixEquals("mergeIntervals", mergeIntervals(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}}), new int[][]{{1, 6}, {8, 10}, {15, 18}});
        assertEquals("searchRotatedSortedArray", searchRotatedSortedArray(new int[]{4, 5, 6, 7, 0, 1, 2}, 0), 4);
        assertEquals("findMinInRotatedSortedArray", findMinInRotatedSortedArray(new int[]{3, 4, 5, 1, 2}), 1);

        int[] zeroes = new int[]{0, 1, 0, 3, 12};
        moveZeroes(zeroes);
        assertArrayEquals("moveZeroes", zeroes, new int[]{1, 3, 12, 0, 0});

        int[] rotated = new int[]{1, 2, 3, 4, 5, 6, 7};
        rotateArray(rotated, 3);
        assertArrayEquals("rotateArray", rotated, new int[]{5, 6, 7, 1, 2, 3, 4});

        int[] permutation = new int[]{1, 2, 3};
        nextPermutation(permutation);
        assertArrayEquals("nextPermutation", permutation, new int[]{1, 3, 2});

        assertEquals("majorityElement", majorityElement(new int[]{2, 2, 1, 1, 1, 2, 2}), 2);
        assertEquals("longestConsecutive", longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}), 4);
        assertEquals("maxProfit", maxProfit(new int[]{7, 1, 5, 3, 6, 4}), 5);

        int[][] matrix = new int[][]{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}};
        setZeroes(matrix);
        assertMatrixEquals("setZeroes", matrix, new int[][]{{1, 0, 1}, {0, 0, 0}, {1, 0, 1}});
        assertEquals("spiralOrder", spiralOrder(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}).size(), 9);

        System.out.println("All array algorithms passed.");
    }
}
