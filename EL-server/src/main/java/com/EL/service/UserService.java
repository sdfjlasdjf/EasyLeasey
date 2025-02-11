package com.EL.service;

import com.EL.dto.UserDTO;
import com.EL.dto.UserLoginDTO;
import com.EL.entity.User;
import com.EL.result.Result;

public interface UserService {

    /**
     * User Login
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * Add User
     * @param userDTO
     *
     */
    void save(UserDTO userDTO);

    User getUserInfo();
    /**
     * Update avatar
     * @param avatarUrl
     */
    void updateAvatar(String avatarUrl);

    /**
     * Update password
     * @param newpwd,oldpwd,userId
     */
    Result updatePwd(String oldpwd, String newpwd, long userId);

    String getUserLocation();

    void updateProfile(UserDTO userDTO);
}
