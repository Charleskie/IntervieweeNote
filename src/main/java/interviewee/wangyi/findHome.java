package interviewee.wangyi;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;


public class findHome {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = Integer.valueOf(in.nextLine());//组数
        for (int i = 0; i < t; i++) {
            String nk = in.nextLine();
            int n = Integer.valueOf(nk.split(" ")[0]);
            int k = Integer.valueOf(nk.split(" ")[1]);
            OUT.clear();
            getHome(n,k);
        }
    }

    private static void getHome(int n, int k) {
        if(n<=k){
            System.out.println("0 0");
            return;
        }
        Set<Integer> choosed = new HashSet<>();
        ArrayList<Integer> a = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            a.add(i);
        }
        choseNum(a,0,n-1,k,choosed);//选择所有可能


        int max=0,min=0;

        for(Set<Integer> temp:OUT){
            int[] b = new int[n];
            for(int index:temp){
                b[index]=1;
            }
            int re = numbers(b);
            max=Math.max(max, re);
            min=Math.min(min, re);
        }
        System.out.println(min+" "+max);

    }

    private static int numbers(int[] a) {
        int len = a.length;
        if(len==0) return -1;
        if(len<3) return 0;
        int count =0;
        for(int i=1;i<len-1;i++){
            if(a[i]==0&&a[i-1]==1&&a[i+1]==1) count++;
        }
        return count;
    }


    static ArrayList<Set<Integer>> OUT = new ArrayList<>();

    static void choseNum(ArrayList<Integer> a,int start,int end,int choose,Set<Integer> choosed){
        if(choose==0){
            Set<Integer> temp = new HashSet<Integer>(choosed);
            OUT.add(temp);
            return;
        }
        for(int i=start;i<=end-choose+1;i++){
            choosed.add(a.get(i));
            choseNum(a,i+1,end,choose-1,choosed);
            choosed.remove(a.get(i));
        }
    }
}