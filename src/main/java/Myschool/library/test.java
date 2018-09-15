package Myschool.library;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        student a = new student("王胜", 24);
        a.setStuClass("1204班");
        a.setStuID("201212235141");
        System.out.println(a.toString());
        String time = "2012-03-02";
        System.out.println(Time.getNextMonth(time));
        borrowInfo b = new borrowInfo("2012", "1234");
        b.getDeadtime();
        b.getTime();
        System.out.println(b.toString());

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(String.valueOf(sdf.parse("2018-07-11 11:00:00").getTime()/1000));
            System.out.println(String.valueOf(sdf.parse("2018-07-11 11:10:00").getTime()/1000));

        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<String>();
        list.add("wang");
        list.add("sheng");list.add("kim");
        list.remove("wang");
        System.out.println(list);

    }
}