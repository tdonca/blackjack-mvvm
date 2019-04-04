package com.tudordonca.android.blackjackmvvm.userdata;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey
    @NonNull
    private String userID;

    @ColumnInfo(name = "money")
    private int userMoney;

    public User(String id){
        this.userID = id;
    }

    public void setMoney(int money){
        this.userMoney = money;
    }

    public int getMoney(){
        return this.userMoney;
    }

    public String getUserID(){
        return this.userID;
    }
}
