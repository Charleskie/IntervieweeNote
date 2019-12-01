package interviewee.Algorithm;

public class GCD {
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