package interviewee.meituan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class tu {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int K = in.nextInt();
        int[] data = new int[N];
        int zero = 0;
        for (int i = 0; i < N; i++) {
            int temp = in.nextInt();
            data[i]=temp;
            if(temp==0) zero++;
        }
        int res = findLongest(data,K,zero);
        System.out.println(res);
    }

    private static int findLongest(int[] data, int k, int zero) {
        if(data.length==0||k<0) return 0;
        if(k>=zero){
            return data.length;
        }
        if(k==0){
            return longestOne(data);
        }
        int max = 0;
        int[] zeroIndex = new int[zero];
        int index = 0;
        for (int i = 0; i < data.length && index<zero; i++) {
            if(data[i]==0){
                zeroIndex[index++]=i;
            }
        }

        int[] a = new int[zero];
        for (int i = 0; i < a.length; i++) {
            a[i]=i;
        }
        Set<Integer> temp = new HashSet<Integer>();
        randomChoose(a,0,a.length-1,k,temp);
        for (Set<Integer> t:CH) {
            int[] newSource = data.clone();
            for(int i:t){
                newSource[zeroIndex[i]]=1;
            }
            for (int i :newSource) {
                System.out.print(i);
            }
            System.out.println();
            System.out.println(longestOne(newSource));
            max = Math.max(max, longestOne(newSource));
        }
        return max;
    }

    static ArrayList<Set<Integer>> CH = new ArrayList<Set<Integer>>();
    /**
     * 随机选择
     * @param a 数据源
     * @param start 开始的位置
     * @param end  结束的位置
     * @param choose 选几个
     * @param already_choose 已选的集合
     */
    static void randomChoose(int a[],int start,int end,int choose,Set<Integer> already_choose){
        //如果choose==0 表示选择完毕，打印输出
        if(choose==0){
            Set<Integer> temp = new HashSet<Integer>(already_choose);
            CH.add(temp);
            return;
        }
        //选择开始的基数，例如10个选1个就循环10次，里面数组依次成为基数；10个选5个就循环6次；m个选n个就循环m-n+1次
        for(int i=start;i<=end-choose+1;i++){
            already_choose.add(a[i]);
            randomChoose(a,i+1,end,choose-1,already_choose);
            already_choose.remove(a[i]);
        }
    }

    /**
     * 找最长连续1
     * @param data
     * @return
     */
    private static int longestOne(int[] data) {
        int[] use = new int[data.length+1];
        for (int i = 0; i < data.length; i++) {
            use[i]=data[i];
        }
        use[use.length-1]=0;
        int count = 0;
        int max = 0;
        for (int i = 0; i < use.length; i++) {
            if(use[i]==1){
                count++;
            }else{
                max=Math.max(max, count);
                count=0;
            }
        }
        return max;
    }

}
