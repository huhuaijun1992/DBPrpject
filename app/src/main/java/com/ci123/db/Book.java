package com.ci123.db;


import com.ci123.dbmodule.litepalmannager.DbSupport;

/**
 * @author: 11304
 * @date: 2020/2/18
 * @desc:
 */
public class Book extends DbSupport {
    int bid;
    String bookName;
    String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bid=" + bid +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
