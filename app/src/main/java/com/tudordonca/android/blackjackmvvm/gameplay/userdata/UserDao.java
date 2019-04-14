package com.tudordonca.android.blackjackmvvm.gameplay.userdata;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Single;


@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertUser(User user);

    @Update
    Completable updateUser(User user);

    @Query("UPDATE user_table SET money = :userMoney WHERE userID = :id")
    Completable updateUserMoney(String id, int userMoney);

    @Delete
    Completable deleteUser(User user);

    @Query("SELECT * FROM user_table WHERE userID = :id")
    Single<User> getUser(String id);

//    @Query("SELECT money FROM user_table WHERE userID = :id")
//    Single<int> getUserMoney(String id);





}
