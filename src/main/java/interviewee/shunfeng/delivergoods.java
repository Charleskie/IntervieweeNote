package interviewee.shunfeng;

import java.util.Scanner;

public class delivergoods{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        sc.close();
        String[] str = line.split(" ");
        int[] arr = new int[str.length];
        for(int i=0;i<arr.length;i++){
            arr[i] = Integer.valueOf(str[i]);
        }
        int sum = arr[arr.length-1];
        sum = sum +arr[5] + arr[4] + arr[3]/4;
        arr = change(5,arr);
        arr = change(4,arr);
        int s3 = arr[3]%4;
        if(s3 != 0){

        }

    }

    /**
     * 根据较大规格箱子的占用情况，扣减可被填充的小规格货物数量。
     *
     * @param index 当前处理的规格下标
     * @param arr   各规格货物数量
     * @return 扣减后的货物数量数组
     */
    static int[] change(int index, int[] arr){
        if(index == 5){
            if(arr[0]>=(index*11)){
                arr[0] = arr[0] - index*11;
            }else arr[0] = 0;
        }else if(index == 4){
            if(arr[1]>=index*5){
                arr[1] = arr[1] - index*5;
            }else if(arr[1]>=(index-1)*5){
                arr[1] = (index-1)*5;
                if(arr[0] >= arr[0] - arr[1] *2){
                    arr[0] = arr[0] - arr[1] *2;
                }else arr[0] = 0;
                arr[1] = 0;
            }
        }
        return arr;
    }

}
