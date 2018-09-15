package interviewee.jingdong;

import clojure.lang.IFn;

import java.util.*;

public class graph{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for(int i=0;i<T;i++){
            String Line = sc.nextLine();
            int N = Integer.valueOf(Line.split(" ")[0]);
            int M = Integer.valueOf(Line.split(" ")[1]);
            int[] dot = new int[M];
            int[] nextdot = new int[M];

        }
    }
    static boolean calgraph(int[] dot, int[] nextdot){
        Map<Integer, Integer> map = new HashMap<>();
        for (int x:nextdot){
            map.put(x,null);
        }
        for(int x:dot){
            if(map.get(x)!=null){
                map.put(x,map.get(x)+1);
            }else map.put(x,1);
            if(map.get(x)>3) return false;
        }
        for(int i=0;i<dot.length;i++){
            for(int j=0;j<dot.length;j++){

            }
        }
        return true;
    }
    static int calLen(int x,int y,int[] dot, int[] nextdot){
        int L=0;
        for(int i=0;i<dot.length;i++){
            if(dot[i]==x&&y==nextdot[i]){
                L=1;
            }else {

            }
        }
        return L;
    }
}