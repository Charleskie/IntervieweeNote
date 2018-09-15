package interviewee.meituan;

import java.util.Scanner;

public class meituan{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int m = Integer.valueOf(line.split(" ")[1]);
        int n = Integer.valueOf(line.split(" ")[0]);
        int min = m;
        if(m>n){
            min = n;
        }
        if(min>(m+n)/3){
            min = (m+n)/3;
        }
        System.out.println(min);
    }
}