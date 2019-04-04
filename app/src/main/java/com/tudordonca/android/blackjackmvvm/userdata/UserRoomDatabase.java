package com.tudordonca.android.blackjackmvvm.userdata;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserRoomDatabase extends RoomDatabase {
    //TODO: fix this hardcoding
    private final static String USERID = "user1";
    public abstract UserDao userDao();
    private static UserRoomDatabase INSTANCE;

    public static UserRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (UserRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserRoomDatabase.class, "user_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(initialCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // To run the very first time the Database is created
    private static RoomDatabase.Callback initialCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate (@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final UserDao dao;
        PopulateDbAsync(UserRoomDatabase db) {
            dao = db.userDao();
        }
        @Override
        protected Void doInBackground(final Void... params) {
            User newUser = new User(USERID);
            newUser.setUserMoney(100);
            dao.insertUser(newUser);
            return null;
        }
    }

}
