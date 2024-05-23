package interviewee.Leecode.tianyiyun;

public class StringAllMatch {

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
