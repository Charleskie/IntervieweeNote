package interviewee.Leecode.string;

public class Permutation {
    /***
     * 无重复字符串的排列组合。编写一种方法，计算某字符串的所有排列组合，字符串每个字符均不相同。
     * 当前方法体仍是待补全草稿。
     *
     * @param str 待排列字符串
     */
    public void solution(String str){
        char[] ch = str.toCharArray();

    }

    /**
     * 全排列递归入口草稿，计划从 index 开始枚举后续字符的交换位置。
     *
     * @param arr   待排列字符数组
     * @param index 当前递归位置
     */
    public void permutation(char[] arr, int index){

    }

    /**
     * 使用交换和回溯输出字符数组的所有排列。
     *
     * @param arr   待排列字符数组
     * @param first 当前固定到的位置
     */
    public void permutate(char[] arr, int first) {
        if (first == arr.length - 1) {
            System.out.println(new String(arr));
            return;
        }
        for (int i = first; i < arr.length; i++) {
            swap(arr, first, i);
            permutate(arr, first + 1);
            swap(arr, first, i);
        }
    }

    /**
     * 交换字符数组中的两个位置。
     *
     * @param arr 字符数组
     * @param i   第一个下标
     * @param j   第二个下标
     */
    public void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        String s = "abc";
        Permutation p = new Permutation();
        p.permutate(s.toCharArray(), 0);
    }

}
