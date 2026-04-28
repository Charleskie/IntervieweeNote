package interviewee.Algorithm;

public class GCD {
    /**
     * 使用欧几里得算法递归计算两个整数的最大公约数。
     *
     * @param x 第一个整数
     * @param y 第二个整数
     * @return x 和 y 的最大公约数
     */
    private int gcd(int x, int y){
        if( y != 0 ){
            return gcd(y,x%y);
        }else {
            return x;
        }
    }
    //y?gcd(y,x%y):x
    public static void main(String[] args){
        GCD gcd = new GCD();
        System.out.println(gcd.gcd(12,96));
    }
}
