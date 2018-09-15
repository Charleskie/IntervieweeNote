package Myschool.library;

import java.text.SimpleDateFormat;
import java.util.*;
import Myschool.library.Time.*;
public class borrowInfo{
    SimpleDateFormat sc = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    String bookname;
    String stuname;
    String time = "";
    String bookID;
    String stuID;
    String deadtime;
    public borrowInfo(String stuID,String bookID){
        this.stuID = stuID;
        this.bookID = bookID;
        this.time = sc.format(System.currentTimeMillis());
        this.deadtime = Time.getNextMonth(time);
    }
    @Override
    public String toString(){
        return "姓名："+stuID+"\n"+"书籍："+bookID+"\n"+"时间："+time+"\n"+"截止还书日期："+deadtime+"\n";
    }
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookID() {
        return bookID;
    }

    public String getStuID() {
        return stuID;
    }

    public void setStuID(String stuID) {
        this.stuID = stuID;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public String getDeadtime() {
        return deadtime;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getStuname() {
        return stuname;
    }

    public String getTime() {
        return time;
    }

    public void setTime() {
        this.time = time;
    }

}