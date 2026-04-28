package interviewee.alibaba;

import java.util.Scanner;

public class Goods{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        String newline = line.replaceAll("\\(","").replaceAll("\\)","");
        int[] arr = new int[newline.split(",").length];
        for(int i=0;i<newline.split(",").length;i++){
            arr[i] = Integer.valueOf(newline.split(",")[i]);
        }
        System.out.println(getChooseNum(arr.length,arr));
    }

    /**
     * 根据商品序列长度和取值计算可选数量的草稿逻辑。
     *
     * @param N   商品数量
     * @param arr 商品状态数组
     * @return 可选择数量
     */
    static int getChooseNum(int N, int[] arr){
        int sum = 0;
        if(N%2==0){
            for(int x:arr){
                if(x==1)continue;
                sum = sum + (x-1);
            }
        }
        if(N%2==1){
            sum = N;
        }
        return sum;
    }
}
