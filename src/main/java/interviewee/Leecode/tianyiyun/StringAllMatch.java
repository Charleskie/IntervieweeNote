package interviewee.Leecode.tianyiyun;

public class StringAllMatch {

    /**
     * 递归生成并输出字符数组在指定区间内的所有排列。
     *
     * @param arr  待排列字符数组
     * @param left 当前固定位置
     * @param end  排列区间右边界
     */
    public static void allPerm(char[] arr,int left,int end){
        if(arr == null || arr.length == 0){
            // 异常情况
            return;
        }
        if(left == end){
            // 递归到底，返回时输出结果
            for(int i = 0; i <= end; i++){
                System.out.print(arr[i]);
            }
            System.out.println();
            return;
        }
        for(int i = left; i <= end;i++){
            swap(arr,left,i);
            allPerm(arr,left + 1,end);
            //回溯至交换前的样子
            swap(arr,left,i);
        }
    }

    /**
     * 交换字符数组中的两个位置。
     *
     * @param arr 字符数组
     * @param i   第一个下标
     * @param j   第二个下标
     */
    private static void swap(char[] arr,int i,int j){
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        String s = "abcd";
        allPerm(s.toCharArray(), 0, s.length()-1);
    }
}
