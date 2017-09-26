package com.hdl.mapper;

import com.hdl.po.User;

import java.util.List;

/**
 * usermapper对象
 * Created by HDL on 2017/9/26.
 */
public interface UserMapper {
    User findUserById(int id);

    List<User> findUser();

    void addUser(User user);

    void updateUserById(User user);

    void deleteUserById(int id);
}
