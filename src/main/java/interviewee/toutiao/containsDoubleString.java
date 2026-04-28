package interviewee.toutiao;

import java.util.Arrays;
import java.util.Scanner;

public class containsDoubleString {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int times = Integer.valueOf(in.nextLine());
        for (int i = 0; i < times; i++) {
            int num = Integer.valueOf(in.nextLine());
            String[] data = new String[num];
            for (int j = 0; j < data.length; j++) {
                data[j]=in.nextLine();
            }
            boolean result = hasDouble(data);
            if(result){
                System.out.println("Yeah");
            }else{
                System.out.println("Sad");
            }
        }
    }

    /**
     * 判断字符串数组中是否存在两个字符串互为旋转或旋转后反转关系。
     *
     * @param data 待检查字符串数组
     * @return 存在满足条件的字符串对时返回 true
     */
    private static boolean hasDouble(String[] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = i+1; j < data.length; j++) {
                if(doubleString(data[i],data[j])) return true;
            }
        }
        return false;
    }

    /**
     * 判断两个字符串是否相同，或是否可以通过循环位移、循环位移后反转得到彼此。
     *
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 满足双生字符串关系时返回 true
     */
    public static boolean doubleString(String str1, String str2){
        if(str1.equals(str2)) return true;
        for(int i=0;i<str1.toCharArray().length;i++){
            char[] newCh = getCircle(str1.toCharArray(),i);
            if(Arrays.equals(newCh,str2.toCharArray())) return true;
            if(Arrays.equals(getReverseCh(newCh),str2.toCharArray())) return true;
        }

        return false;
    }

    /**
     * 从指定下标开始对字符数组做一次循环旋转。
     *
     * @param ch    原字符数组
     * @param start 旋转起点
     * @return 旋转后的新字符数组
     */
    public static char[] getCircle(char[] ch,int start){
        char[] newCh = new char[ch.length];
        for(int i=0;i<ch.length;i++){
            int index = start+i;
            if(index>ch.length-1){
                index = index-ch.length;
            }
            newCh[i] = ch[index];
        }
        return newCh;
    }

    /**
     * 反转字符数组并返回新数组。
     *
     * @param ch 原字符数组
     * @return 反转后的字符数组
     */
    public static char[] getReverseCh(char[] ch){
        char[] newCh = new char[ch.length];
        int j=0;
        for(int i=ch.length-1;i>=0;i--){
            newCh[j++] = ch[i];
        }
        return newCh;
    }

}
