import java.util.Stack;

public class test {
    /***
     * (1+(2+3)*(3+(8+0))+1-2)这是一个简单的数学表达式，今天不是计算它的值，而是比较它的括号匹配是否正确。
     * 前面这个式子可以简化为( () ( () ) )这样的括号我们认为它是匹配正确的，
     * 而( ( () )这样的我们就说他是错误的。注意括号里面的表达式可能是错的，也可能有多个空格，对于这些我们是不用去管的，我们只关心括号是否使用正确。
     * 给出一行表达式，请编码实现：如果匹配正确输出括号的对数，否则输出-1。
     *
     * 样例:
     * 输入样例1：
     * (1+(2+3)*(3+(8+0))+1-2)
     * 输出样例1：
     * 4
     *
     * 输入样例2：
     * (1+(2+3)*(3+2)
     * 输出样例2：
     * -1
     */
    public static int solution(String str) {
        int sum = 0;
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(') {
                stack.push(c);
            } else {
                if (c == ')') {
                    if (stack.isEmpty()) {
                        return -1;
                    }
                    stack.pop();
                    sum += 1;
                }

            }
        }
        if (!stack.isEmpty()) {
            return -1;
        }
        return sum;
    }


    public static void main(String[] args) {
        String s = "";
        System.out.println(solution(s));
    }
}