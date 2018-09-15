package interviewee.xiecheng;

public class checkticket{
    public static void main(String[] args){
        String str = "1Ct3r4ip_ti4C6k9Et^";
        str = str.replaceAll("\\d+","");
        char[] chars = str.toCharArray();
        char[] newChar = new char[chars.length];
        for(char i:chars){
            if(i<='A' && i>='z' ||(i=='[')||(i==']')||(i=='\\')||(i=='^')||(i=='_')||(i=='\'')){
//                newChar[i]
            }
        }
        System.out.println(str);
    }
}