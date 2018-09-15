package interviewee.pinduoduo;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
求将手机号码换成靓号(K位相同)的最低花费，更改号码中一个数字的金额为原数字与更改数字的差值
输入两个整数，N-->号码的数字个数；K-->靓号至少需要K个数字相同
第二行输入号码
2<=K<=N<=10000
 输出字典序最小的一种
 **/
public class specialphonenumber{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int K = sc.nextInt();
        String num = sc.nextLine();
        char[] Phone = num.toCharArray();
        int[] PhoneArr = new int[N];
        if(AlreadyExist(PhoneArr,K)){
            Double[] minusArr = new Double[N];
            int i = 0;
//            for(int n:PhoneArr(num)){
//                minusArr[i++]=Math.abs(n-MeanValue(PhoneArr(num)));
//            }

        }else {
        }
    }
    public static Double MeanValue(char[] PhoneArr){
        Double MeanValue = 0.0;
        for(int n:PhoneArr){
            MeanValue += n;
        }
        return MeanValue/PhoneArr.length;
    }


    /**
     * 检验是否已经是靓号
     * @param PhoneArr
     * @return
     */
    public static boolean AlreadyExist(int[] PhoneArr, int K){
        boolean flag = false;
        if(ExistNum(PhoneArr,K)>0){
            flag = true;
        }
        return flag;
    }

    //TODO 判断是否是靓号，找出有几种靓号，输出字典序最小的一种
    //TODO 不是靓号，哪种方式转换成靓号，有几种转换方式，输出字典序最小的一种
    /**
     * 计算存在几种靓号
     * @param PhoneArr
     * @param K
     * @return
     */
    public static int ExistNum(int[] PhoneArr, int K){
        int ExistNum = 0;
        for(int n:PhoneArr){
//            if(CountNumInArr(n,PhoneArr)>=K)ExistNum++;
        }
        return ExistNum;
    }
    public static int CountNumInArr(int[] PhoneArr){
        Map<Integer,Integer> CountMap = new HashMap<>();
        int count = 0;
        for(int n:PhoneArr){
//            if(Num==n)count++;
        }
        return count;
    }
    public int calmoney(){
        return 0;
    }
}