package interviewee.nowcode;

import java.util.Scanner;

public class CalFanbei {

    /***
     * 给定数字A，B A<B 和系数 p q
     * 每次操作可以将A变成A+p 或者 p*q
     * 问至少几次操作可以将 B <= A
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        // 注意 while 处理多个 case
        int num = in.nextInt();
        while (num > 0){
            String[] s = in.nextLine().split(" ");
            int A=Integer.parseInt(s[0]);
            int B=Integer.parseInt(s[1]);
            int p=Integer.parseInt(s[2]);
            int q=Integer.parseInt(s[3]);

        }

    }

    private static int cal(int a, int b, int p, int q, int num){
        if(b - a <= p){
            num += 1;
            return num;
        }
        if((b-a) / p <= q){
            num += 2;
            return num;
        }
        return cal(a,b,p,q,num);
    }

}
