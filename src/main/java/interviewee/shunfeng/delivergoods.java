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