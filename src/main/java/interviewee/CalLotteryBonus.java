package interviewee;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/***
 * 多场比赛按关算，N场N关算一注
 * N场N-1关算N注
 * N场多种关选择，主要计算每种选中关一共有多少种排列组合，就算多少注
 * 奖金即为每种排列组合的乘积之和中每场比赛中有多组选中的赔率最大的再乘以2元
 */

/***
 * 输入示例：1.56 000100-->代表选中本场的第二排的第一个,比值为1.56，即让球胜平负的主场胜
 * 1.56,4.55 101000-->代表选中本场的主场胜和负两两个,两组对应数据的比值为1.56，4.55
 * 如果出现前三位和后三位都出现1的情况，则需要重新输入
 * 最后一行输入所选的关的组合，最大关小于或等于所输入的场的个数
 * 输入示例：2，3，4。表示选中2关，3关，4关
 */

public class CalLotteryBonus{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        ArrayList<String> input = new ArrayList<>();
        String choose = "";
        while (sc.hasNext()){
            String Line = sc.nextLine();
            if(Line.split(" ").length==1) {
                choose = Line;
                break;
            }
            String[] arr0 = Line.split(" ")[0].split(",");
            char[] arr1 = Line.split(" ")[1].toCharArray();
            int lenOfNum1 = 0;//1的个数
            int lenOfBore = 0;//前半段1的个数
            int lenOfBack = 0;//后半段1的个数
            for(int i=0; i<arr1.length;i++) {
                lenOfNum1 += Integer.valueOf(arr1[i]-'0');
                if(i>2 && Integer.valueOf(arr1[i]-'0')==1){
                    lenOfBack++;
                }else if(Integer.valueOf(arr1[i]-'0')==1)lenOfBore++;
            }
            if(arr0.length==lenOfNum1&&(lenOfBack*lenOfBore==0)){
                input.add(Line);
            }else {
                System.out.println("输入有误，请重新输入");
            }
        }
        ArrayList<Double> Max = new ArrayList<>();
        for(String i: input){
            Max.add(getMaxSP(i.split(" ")[0]));
        }//将每场选中的最大比率组成一个新的数组，用来计算理论最高奖金
        System.out.println(Max);
        for(String i:choose.split(",")){
            int ch = Integer.valueOf(i);
            doit(Max,ch);
        }

    }

    /***
     * 取每组最大的倍率，组成数组
     * @param input
     * @return
     */
    static double getMaxSP(String input){
        ArrayList<Double> arr = new ArrayList<>();
        for(String i: input.split(","))
        arr.add(Double.valueOf(i));
        double Max = 0.0;
        for(double i: arr){
            Max = i;
            for(double j:arr){
                if(Max<j){
                    Max = j;
                }
            }
        }
        return Max;
    }
    static List getList(List list,ArrayList<Double> Max, int choose){
        if(choose==0){
            return list;
        }
        for (double i: Max){
            if(!list.contains(i)){
                list.set((list.size()-choose),i);
            }else continue;
        }
        getList(list,Max,choose-1);
        list.set((list.size() - choose), 0.0);

        return list;
    }

    static void doit(ArrayList<Double> Max, int choose) {
        if (choose <= 0 || Max == null) {
            return;
        }
        List<Double> max = new ArrayList<>();
        //通过这一步初始化序列的长度
        for (int i = 0; i < choose; i++) {
            max.add(0.0);
        }
        List list = getList(max, Max, choose);
        System.out.println("doit:"+list);
    }

}