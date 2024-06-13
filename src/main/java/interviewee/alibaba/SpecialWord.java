package interviewee.alibaba;

/**
 * 请使用标准输入输出(System.in, System.out)；已禁用图形、文件、网络、系统相关的操作，如java.lang.Process , javax.swing.JFrame , Runtime.getRuntime；不要自定义包名称，否则会报错，即不要添加package answer之类的语句；您可以写很多个类，但是必须有一个类名为Main，并且为public属性，并且Main为唯一的public class，Main类的里面必须包含一个名字为'main'的静态方法（函数），这个方法是程序的入口
 * 时间限制: 3S (C/C++以外的语言为: 5 S)   内存限制: 128M (C/C++以外的语言为: 640 M)
 * 输入:
 * 输入包含两个参数：历史关键词列表和查询关键词；历史关键词列表的每一项被空格分割为两段，左半部分为真正的关键词，右半部分为受欢迎程度；
 * 输出:
 * 输出相似关键词列表，每一项被空格分割为两段，左半部分为真正的关键词，右半部分为受欢迎程度；
 * 输入范例:
 * 历史关键词列表范例：
 * 连衣裙 99
 * 衣裙连 10
 * 苹果 80
 * 手机 90
 *
 * 查询关键词范例：
 * 连衣裙
 * 输出范例:
 * 连衣裙 99
 * 衣裙连 10
 */


import java.util.*;

public class SpecialWord{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Map<String,Integer> StringList = new HashMap<>();
        String lookString = new String("");
        Map<String, Integer> StringLike = new HashMap<>();
        while(sc.hasNext()){
            String line = sc.nextLine();
            if (line.split(" ").length>1){
                StringList.put(line.split(" ")[0],Integer.valueOf(line.split(" ")[1]));
            }else {
                lookString = line;
            }
        }
        if(StringList.containsKey(lookString)){
            StringLike.put(lookString,StringList.get(lookString));
        }
        char[] like = lookString.toCharArray();
        for(String n:StringLike.keySet()){
            char[] nchar = n.toCharArray();
//            n.contains(like)

        }
        for(Map.Entry n: StringLike.entrySet()){
            System.out.println(n.getKey()+" "+ n.getValue());
        }
    }
}