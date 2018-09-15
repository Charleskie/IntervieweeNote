package Myschool.library;
public class book{
    String bookID;
    String bookName;
    String bookPrice;
    String bookAthuer;
    public book(String bookID,String bookName){
        this.bookID = bookID;
        this.bookName = bookName;
    }
    String getBookID(){
        return bookID;
    }
    String getBookName(){
        return bookName;
    }
    String getBookPrice(){
        return bookPrice;
    }
    String getBookAthuer(){
        return bookAthuer;
    }
    void setBookID(String bookID){
        this.bookID = bookID;
    }
}