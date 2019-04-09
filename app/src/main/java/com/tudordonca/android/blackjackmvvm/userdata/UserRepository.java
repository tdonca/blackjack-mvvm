package com.tudordonca.android.blackjackmvvm.userdata;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class UserRepository {
    private final String LOG_TAG = "UserRepository";
    //TODO: remove this hardcoding of a single user
    private final String USERID = "user1";
    private UserDao userDao;
    private BehaviorSubject<User> user;

    public UserRepository(Application application){
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        userDao = db.userDao();
    }

    // data access/modification wrapper functions that abstract database access

    public Single<User> getUser(){
        return userDao.getUser(USERID);
    }

    public void insertUser(User user){
        //TODO: defer??????????
        Completable.defer(() -> {
            return userDao.insertUser(user);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(LOG_TAG, "Successfully inserted a new user.");
                }, throwable -> {
                    Log.e(LOG_TAG, "Failed to insert a user in the database!");
                    throwable.printStackTrace();
                });
    }

    public void updateMoney(int money) {
        //TODO: defer??????????
        Completable.defer(() -> {
            return userDao.updateUserMoney(USERID, money);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(LOG_TAG, "Successfully updated user money.");
                }, throwable -> {
                    Log.e(LOG_TAG, "Failed to update user money in the database!");
                    throwable.printStackTrace();
                });
    }
}
