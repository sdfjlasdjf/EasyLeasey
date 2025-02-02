package com.EL.service.impl;

import com.EL.constant.MessageConstant;
import com.EL.constant.PasswordConstant;
import com.EL.constant.StatusConstant;
import com.EL.context.BaseContext;
import com.EL.dto.UserDTO;
import com.EL.dto.UserLoginDTO;
import com.EL.entity.User;
import com.EL.exception.AccountLockedException;
import com.EL.exception.AccountNotFoundException;
import com.EL.exception.PasswordErrorException;
import com.EL.mapper.UserMapper;
import com.EL.result.Result;
import com.EL.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 员工登录
     *
     * @param userLoginDTO
     * @return
     */
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        //1、get user by username
        User user = userMapper.getByUsername(username);

        //2、handle some exceptions
        if (user == null) {
            //user does not exist
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //check password
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //wrong password
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (user.getStatus() == StatusConstant.DISABLE) {
            //account locked
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return user;
    }

    public void save(UserDTO userDTO){
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        user.setStatus(StatusConstant.ENABLE);
        user.setPassword(DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);

    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Long id = BaseContext.getCurrentId();
        userMapper.updateAvatar(avatarUrl,id);
    }

    @Override
    public User getUserInfo(){
        return userMapper.getById(BaseContext.getCurrentId());
    }

    @Override
    public Result updatePwd(String oldPwd, String newPwd, long userId){
        User user = userMapper.getById(userId);
        if(!(user.getPassword().equals(DigestUtils.md5DigestAsHex(oldPwd.getBytes())))){
            return Result.error("The original password is not correct");
        }
        if(!oldPwd.equals(newPwd)){
            return Result.error("Two passwords cannot be the same");
        }
        newPwd = DigestUtils.md5DigestAsHex(newPwd.getBytes());
        userMapper.updatePwd(newPwd,userId);
        return Result.success();
    }

    @Override
    public String getUserLocation(){
        Long userId = BaseContext.getCurrentId();
        return userMapper.getUserLocation(userId);
    }
}
