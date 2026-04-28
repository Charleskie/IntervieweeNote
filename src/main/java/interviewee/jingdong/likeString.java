package interviewee.jingdong;

import java.util.Scanner;

public class likeString {


    /*请完成下面这个函数，实现题目要求的功能
    当然，你也可以不按照下面这个模板来作答，完全按照自己的想法来 ^-^
    ******************************开始写代码******************************/
    /**
     * 计算字符串 S 和 T 的相似度/匹配结果；当前方法体仍是待补全草稿。
     *
     * @param S 第一个字符串
     * @param T 第二个字符串
     * @return 当前草稿固定返回 0
     */
    static int solve(String S, String T) {
return 0;

    }
    /******************************结束写代码******************************/


    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int res;

        String _S;
        try {
            _S = in.nextLine();
        } catch (Exception e) {
            _S = null;
        }

        String _T;
        try {
            _T = in.nextLine();
        } catch (Exception e) {
            _T = null;
        }

        res = solve(_S, _T);
        System.out.println(String.valueOf(res));

    }
}
