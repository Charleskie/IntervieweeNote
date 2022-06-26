package interviewee.Leecode;

import java.util.Arrays;

public class qe {
    // N = String.valueOf(n)的长度
//O(N)time O(N)space

        public static int nextGreaterElement(int n) {
            //2^32 < 10^10
            int[] t = new int[10];
            int l = t.length;
            int temp = n;
            //将n 变成数组
            while (l > 0 && temp > 0) {
                l--;
                t[l] = temp % 10;
                temp /= 10;
            }
            for (int i = t.length - 1; i > l; i--) {
                if (t[i] > t[i - 1]) {
                    //找到前一个数小于后一个数的位置
                    int tempIndex = i;
                    //找到 i -1 后大于 t[i-1] 的最小数
                    for (int j = i; j < t.length; j++) {
                        if (t[j] > t[i - 1] && t[j] < t[tempIndex]) {
                            tempIndex = j;
                        }
                    }
                    //将 i -1 后大于 t[i-1] 的最小数 和 t[i-1] 交换位置
                    temp = t[i - 1];
                    t[i - 1] = t[tempIndex];
                    t[tempIndex] = temp;
                    //获取从i开始的后面的元素
                    int[] arr = new int[t.length - i];
                    System.arraycopy(t, i, arr, 0, t.length - i);
                    //进行从小到大排序
                    Arrays.sort(arr);
                    //然后依次赋值到 t 的 i - len 索引
                    System.arraycopy(arr, 0, t, i, arr.length);
                    break;
                }
            }
            //计算新的结果
            int res = 0;
            for (int i : t) {
                res = res * 10 + i;
            }

            String s="jk";
            return res <= n ? -1 : res;
        }

    public static void main(String[] args) {
        int n =12;
        System.out.println(nextGreaterElement(n));
    }
}
