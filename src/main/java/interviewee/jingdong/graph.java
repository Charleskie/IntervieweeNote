package interviewee.jingdong;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    /**
     * 判断输入边集合是否满足当前图约束；当前方法仍保留部分待补全逻辑。
     *
     * @param dot     边的起点数组
     * @param nextdot 边的终点数组
     * @return 满足约束时返回 true
     */
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

    /**
     * 计算两个点之间是否存在直接边的长度草稿。
     *
     * @param x       起点
     * @param y       终点
     * @param dot     边的起点数组
     * @param nextdot 边的终点数组
     * @return 存在直接边时返回 1，否则返回 0
     */
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
