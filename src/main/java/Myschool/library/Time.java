package Myschool.library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time{
    static String getNextMonth(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
        }catch (ParseException e){
            System.out.println(e);
        }
        calendar.add(Calendar.DATE,+30);
        String deatime = sdf.format(calendar.getTime());
        return deatime;
    }
}