package com.volkangorer.kimbuldu;

public class Post {
    public String category;
    public String product;
    public String date;
    public String downloadurl;
    public String id;
    public String verify;
    public int userId;
    public String explanation;
    public String status;
    public int advertNo;
    public String usermail;



    public Post(String category, String product, String date,String downloadurl,String id,String verify,int userId,String explanation,String status,int  advertNo,String usermail) {
        this.category = category;
        this.product = product;
        this.date = date;
        this.downloadurl= downloadurl;
        this.id = id;
        this.verify = verify;
        this.userId = userId;
        this.explanation = explanation;
        this.status = status;
        this.advertNo = advertNo;
        this.usermail = usermail;

    }
}
