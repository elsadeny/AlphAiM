package com.sadeny.alphaim;

import android.util.Log;

import java.util.Date;

public class Message {
    private String text;
    String uname;
    String time;
    String mydate;
   // String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    private boolean belongsToCurrentUser;

    public Message(String text,String uname, boolean belongsToCurrentUser,String mydate) {
        this.text = text;
        this.uname=uname;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.mydate=mydate;
    }

    public String getText() {
        return text;
    }



    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}