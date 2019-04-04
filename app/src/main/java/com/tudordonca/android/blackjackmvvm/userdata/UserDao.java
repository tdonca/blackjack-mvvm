package com.tudordonca.android.blackjackmvvm.userdata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;



@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("UPDATE user_table SET money = :userMoney WHERE userID = :id")
    void updateUserMoney(String id, int userMoney);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user_table WHERE userID = :id")
    LiveData<User> getUser(String id);


}
