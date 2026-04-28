package interviewee.pinduoduo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/***
 * 输入N，L
 * N为单词数，L为单词长度
 * 输出由这些单词中字母组成的字典序最小的新单词，没有则输出"-"
 */
public class t4{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int N = Integer.valueOf(line.split(" ")[0]);
        int L = Integer.valueOf(line.split(" ")[1]);
        ArrayList<String> string = new ArrayList<>();
        int index = N;
        while (index>0){
            string.add(sc.nextLine());
            index--;
        }
        String[] strings = getCharArr(N,L,string.toArray(new String[0]));
        if(strings.length==0){
            System.out.println("-");
        }else {
            System.out.println(getMinString(strings));
        }
    }

    /**
     * 按列从输入单词中抽取字符组成候选新单词，并过滤掉已存在的单词。
     *
     * @param N      单词数量
     * @param L      单词长度
     * @param strArr 原始单词数组
     * @return 不在原始单词集合中的候选单词数组
     */
    static String[] getCharArr(int N, int L, String[] strArr){
        String specialstr = "";
        ArrayList<String> getStrArr = new ArrayList<>();
        for(int n=0;n<N;n++){
            for(int i=0;i<L;i++){
                specialstr = specialstr + String.valueOf(strArr[n].toCharArray()[i]);
            }
            if(Arrays.asList(strArr).contains(specialstr)){
                specialstr = "";
            }else getStrArr.add(specialstr);
        }
        return getStrArr.toArray(new String[0]);
    }

    /**
     * 从候选字符串中选出字典序最小的字符串。
     *
     * @param strings 候选字符串数组
     * @return 字典序最小的候选字符串
     */
    static String getMinString(String[] strings){
        String minstr = new String();
        for(int n=0;n<strings.length;n++){
            for(int i=n;i<strings.length;i++){
                if(minstr.compareTo(strings[i])==1){
                    String temp = strings[i];
                    strings[i]= strings[n];
                    strings[n] = temp;
                }
            }
        }
        return strings[0];
    }
}
