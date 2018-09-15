package Myschool.test;
public class test{
    public static void main(String[] args){
        String a = " abf dsfj, sfd";
        String b = "sdf,dsdfj ";
        System.out.println(a.trim());
        System.out.println(b.trim());
        String s1 = "hello";
        String s2 = "hell" + new String("o");
        String s = null;
//        test.de(s);
        int fy = 9;
        final int fi;
        fi = fy;
        System.out.println(fi);
        System.out.print(s1==s2);

    }

    public void de(String s){
        System.out.println(this.getClass().getName());
    }

}