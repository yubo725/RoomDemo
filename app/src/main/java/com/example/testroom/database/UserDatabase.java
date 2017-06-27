package com.example.testroom.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.testroom.dao.UserDao;
import com.example.testroom.entity.User;

/**
 * Created by yubo on 2017/5/25.
 */

@Database(entities = User.class, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static UserDatabase instance;

    public static UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, "TestDB")
                    .build();
        }
        return instance;
    }

    public UserDao getUserDao() {
        return userDao();
    }

}
