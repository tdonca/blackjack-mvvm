package com.tudordonca.android.blackjackmvvm.userdata;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;

public class UserRepository {
    //TODO: remove this hardcoding of a single user
    private final String USERID = "user1";
    private UserDao userDao;
    private LiveData<User> user;

    public UserRepository(Application application){
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        // create a new user on the first startup
        User newUser = new User(USERID);
        newUser.setUserMoney(100);
        // on conflict will be ignored
        insertUser(newUser);
        // create LiveData of user
        user = userDao.getUser(USERID);
    }

    // data access/modification wrapper functions that abstract database access

    public LiveData<User> getUser(){
        return user;
    }

    public void insertUser(User user){
        //TODO: replace with RxJava
        new insertUserAsyncTask(userDao).execute(user);
    }

    public void updateMoney(int money){
        //TODO: replace with RxJava
        new updateMoneyAsyncTask(userDao, USERID).execute(money);
    }



    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void>{
        private UserDao userDao;
        insertUserAsyncTask(UserDao dao){
            userDao = dao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.insertUser(users[0]);
            Log.i("DaoAsyncTask", "just inserted a new user in the database");
            return null;
        }
    }


    private static class updateMoneyAsyncTask extends AsyncTask<Integer, Void, Void>{
        private UserDao userDao;
        private String userID;
        updateMoneyAsyncTask(UserDao dao, String id){
            userDao = dao;
            userID = id;
        }
        @Override
        protected Void doInBackground(Integer... integers) {
            userDao.updateUserMoney(userID, integers[0]);
            Log.i("DaoAsyncTask", "just updated the user's money with " + integers[0]);
            return null;
        }
    }
}
