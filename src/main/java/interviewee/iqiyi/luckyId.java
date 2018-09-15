package interviewee.iqiyi;

import java.util.Scanner;

public class luckyId{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        sc.close();
        int[] arr1 = new int[3];
        int[] arr2 = new int[3];
        char[] chars = line.toCharArray();
        for(int i=0;i<chars.length;i++){
            if(i<3){
                arr1[i]=Integer.valueOf(chars[i]-'0');
            }else arr2[i-3] = Integer.valueOf(chars[i]-'0');
        }
        int sum1 = 0,sum2 = 0;
        for(int i = 0;i<arr1.length;i++){
            sum1 = sum1 + arr1[i];
            sum2 = sum2 + arr2[i];
        }
        int index = 0;
        if (sum1 == sum2){
            System.out.println(index);
        }
        if(sum1>sum2){
            int diff = sum1 - sum2;
            System.out.println(compute(arr2,diff,index));
        }else if(sum2>sum1){
            int diff = sum2 -sum1;
            System.out.println(compute(arr1,diff,index));
        }

    }


    static int getMin(int[] arr){
        for(int i=0;i<arr.length;i++){
            for(int j=i;j<arr.length;j++){
                if(arr[i]>arr[j]){
                    int temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
        }
        return arr[0];
    }

    static int compute(int[] arr,int diff,int in){
        getMin(arr);
        if(diff<(9-arr[0])){
            arr[0] = arr[0] + diff;
            in = 1;
        }else if(diff<(9-arr[0]+9-arr[1])){
            int temp = arr[0];
            arr[0] = 9;
            arr[1] = arr[1] + diff - (arr[0] - temp);
            in = 2;
        }else {
            int temp = arr[0];
            int temp2 = arr[1];
            arr[0] = 9;arr[1] = 9;
            arr[2] = arr[2] + diff -(arr[0]+arr[1] - temp -temp2) ;
            in = 3;
        }
        for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]);
        }
        System.out.print("\n");

        return in;
    }
}