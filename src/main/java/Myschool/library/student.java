package Myschool.library;

import Myschool.library.*;
public class student extends person{
    String stuID;
    String name;
    int age;
    String stuClass;
    public student(String name,int age){
        super(name,age);
        this.name = name;
        this.age = age;
    }
    String getStuID(){
        return stuID;
    }
    @Override
    String getname(){
        return name;
    }
    @Override
    int getage(){ return age; }
    String getStuClass(){
        return stuClass;
    }
    void setStuID(String ID){
        this.stuID = ID;
    }
    void setStuClass(String Class){
        this.stuClass = Class;
    }
    @Override
    public String toString(){
        return "姓名："+name+"\n"+"学号："+stuID+"\n"+"年龄："+age+"\n"+"班级："+stuClass+"\n";
    }
}

abstract class person{
    String name;
    int age;
    public person(String name,int age){};
    abstract String getname();
    abstract int getage();
}