package interviewee.wangyi;

import java.util.ArrayList;
import java.util.Scanner;

public class wangyibidata1{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String ss = sc.nextLine();
        int n = Integer.valueOf(ss.split(" ")[0]);
        int k = Integer.valueOf(ss.split(" ")[1]);
        String l = sc.nextLine();
        String[] chars = l.split(" ");
        int[] arr = new int[n];
        for(int i=0;i<n;i++){
            arr[i] = Integer.valueOf(chars[i]);
        }
        int m =0;
        ArrayList<Integer> index1 = new ArrayList<>();
        ArrayList<Integer> index2 = new ArrayList<>();
        while (Integer.valueOf(CalDiff(arr).split(",")[0])-Integer.valueOf(CalDiff(arr).split(",")[1])!=0 && m<= k){
            index1.add(GetMax(arr)+1);
            index2.add(GetMin(arr)+1);
            arr[GetMax(arr)] = arr[GetMax(arr)] -1;
            arr[GetMin(arr)] = arr[GetMin(arr)] +1;
            m++;
        }
        int s = Integer.valueOf(CalDiff(arr).split(",")[0])-Integer.valueOf(CalDiff(arr).split(",")[1]);
        System.out.println(s+ " " + m);
        for(int i=0;i<index1.size();i++){
            System.out.println(index1.get(i)+ " "+ index2.get(i));
        }
    }
    public static int GetMax(int[] arr){
        int Max = 0;
        for(int i=0;i<arr.length;i++){
            if(arr[i]==Integer.valueOf(CalDiff(arr).split(",")[0]))
                Max = i;
        }
        return Max;
    }
    public static int GetMin(int[] arr){
        int Min = 0;
        for(int i=0;i<arr.length;i++){
            if(arr[i]== Integer.valueOf(CalDiff(arr).split(",")[1]))
                Min = i;
        }
        return Min;
    }
    public static String CalDiff(int[] arr){
        for(int n=0;n< arr.length;n++){
            for(int i=0;i<arr.length-1-n;i++){
                if(arr[i]>arr[n+1]){
                    int t = arr[i];
                    arr[i] = arr[i+1];
                    arr[i+1] = t;
                }
            }
        }
        return arr[arr.length-1]+","+arr[0];
    }
}