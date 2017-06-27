package com.example.testroom.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.testroom.entity.User;

import java.util.List;

/**
 * Created by yubo on 2017/5/25.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    List<User> selectAll();

    @Query("SELECT count(*) FROM User")
    int count();

    @Insert
    long[] insert(User... user);

    @Delete
    int delete(User... user); // 返回1表示删除成功，返回0表示删除失败

    @Query("SELECT * FROM User WHERE uid = :id")
    List<User> selectById(int id);

}
