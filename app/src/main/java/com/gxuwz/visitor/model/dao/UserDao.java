package com.gxuwz.visitor.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gxuwz.visitor.model.bean.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE phone = :phone")
    User getUserByPhone(String phone);

    @Update()
    void update(User user);

    @Delete
    void delete(User user);
}
