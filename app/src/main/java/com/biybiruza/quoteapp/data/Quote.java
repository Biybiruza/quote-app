package com.biybiruza.quoteapp.data;

import java.util.ArrayList;
import java.util.List;

public class Quote {
    String imgUrl;
    String quote;
    String author;
    static List<Quote> list = new ArrayList<>();

    public Quote(String imgUrl, String quote,String author) {
        this.imgUrl = imgUrl;
        this.quote = quote;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public static List<Quote> quoteList() {

       /* list.add(new Quote(
                "",
                "It does not matter how slowly you go as long as you do not stop"
        ));*/
        
        return list;
    }
}
