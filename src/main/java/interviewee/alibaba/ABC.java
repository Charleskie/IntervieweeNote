package interviewee.alibaba;

import java.util.Scanner;

/**
 * 广告
 * @author Administrator
 *
 */

public class ABC{
    static final int A = 1;
    static final int B = 2;
    static final int C = 3;
    static int COUNT = 0;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] ns = new int[n];
        int index = 0;
        ideas(n,ns,index);
        System.out.println(COUNT);
    }
    private static void ideas(int n, int[] ns, int index) {
        int[] temp = ns.clone();
        if(index>n-1){
            COUNT++;
            return;
        }
        if(index+A<=n){
            if((index>0 && ns[index-1]!=A)||index==0){
                temp[index]=A;
                ideas(n,temp,index+A);
            }
        }
        if(index+B<=n){
            temp[index]=B;
            ideas(n,temp,index+B);
        }
        if(index+C<=n){
            temp[index]=C;
            ideas(n,temp,index+C);
        }
    }
}