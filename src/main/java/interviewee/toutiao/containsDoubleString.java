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

    private static boolean hasDouble(String[] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = i+1; j < data.length; j++) {
                if(doubleString(data[i],data[j])) return true;
            }
        }
        return false;
    }

    public static boolean doubleString(String str1, String str2){
        if(str1.equals(str2)) return true;
        for(int i=0;i<str1.toCharArray().length;i++){
            char[] newCh = getCircle(str1.toCharArray(),i);
            if(Arrays.equals(newCh,str2.toCharArray())) return true;
            if(Arrays.equals(getReverseCh(newCh),str2.toCharArray())) return true;
        }

        return false;
    }

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

    public static char[] getReverseCh(char[] ch){
        char[] newCh = new char[ch.length];
        int j=0;
        for(int i=ch.length-1;i>=0;i--){
            newCh[j++] = ch[i];
        }
        return newCh;
    }

}