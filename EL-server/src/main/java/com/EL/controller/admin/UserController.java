package com.EL.controller.admin;

import com.EL.constant.JwtClaimsConstant;
import com.EL.dto.PasswordEditDTO;
import com.EL.dto.UserDTO;
import com.EL.dto.UserLoginDTO;
import com.EL.entity.User;
import com.EL.mapper.UserMapper;
import com.EL.properties.JwtProperties;
import com.EL.result.Result;
import com.EL.service.UserService;
import com.EL.service.impl.UserServiceImpl;
import com.EL.utils.JwtUtil;
import com.EL.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.aspectj.lang.annotation.AdviceName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.hibernate.validator.constraints.URL;
/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/user")
@Slf4j
@Api(tags = "User related interface")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * login
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(UserLoginDTO userLoginDTO) {
        log.info("User Login：{}", userLoginDTO);

        User user = userService.login(userLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
        operations.set(token,token,jwtProperties.getAdminTtl(), TimeUnit.SECONDS);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .name(user.getName())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    /**
     * logout
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {
        if (token != null) {
            stringRedisTemplate.delete(token);
        }
        return Result.success("Logged out successfully");
    }


    @GetMapping("/userInfo")
    public Result<User> getUserInfo(){
        return Result.success(userService.getUserInfo());
    }

    @ApiOperation("Add User")
    @PostMapping("/register")
    public Result save(UserDTO userDTO) {
        userService.save(userDTO);
        return Result.success();
    }

    @ApiOperation("Update Avatar")
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @ApiOperation("Update Profile")
    @PatchMapping("/update")
    public Result updateProfile(UserDTO userDTO) {
        userService.updateProfile(userDTO);
        return Result.success();
    }

    @ApiOperation("Update Password")
    @PatchMapping("/updatePwd")
    public Result updatePwd(PasswordEditDTO passwordEditDTO) {
        String oldPwd  = passwordEditDTO.getOldPassword();
        String newPwd = passwordEditDTO.getNewPassword();
        Long userId = passwordEditDTO.getUserId();
        if(!StringUtils.hasLength(oldPwd)||!StringUtils.hasLength(newPwd)||StringUtils.hasLength(String.valueOf(userId))){
            return Result.error("Missing necessary parameters");
        }
        return userService.updatePwd(oldPwd,newPwd,userId);
    }

    @ApiOperation("Get current user's location")
    @GetMapping("/location")
    public Result getUserLocation(){
        String location = userService.getUserLocation();
        return Result.success(location);
    }
}
