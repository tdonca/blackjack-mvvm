package com.tudordonca.android.blackjackmvvm.gameplay.userdata;

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

    public User(String userID){
        this.userID = userID;
    }

    public void setUserMoney(int money){
        this.userMoney = money;
    }

    public void addUserMoney(int money){
        this.userMoney += money;
    }

    public int getUserMoney(){
        return this.userMoney;
    }

    public String getUserID(){
        return this.userID;
    }
}
