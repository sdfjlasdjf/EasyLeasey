package com.EL.mapper;

import com.EL.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    /**
     * Query user given username
     * @param username
     * @return
     */
    @Select("select * from user where username = #{username}")
    User getByUsername(String username);


    /**
     * Query user by id
     * @param id
     * @return
     */
    @Select("select * from user where id = #{id}")
    User getById(Long id);
    /**
     * Insert user
     * @param user
     */
    @Insert("insert into user (name,username,password,phone,sex,id_number,create_time,update_time,status)" +
        "values" +
        "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{status})")
    void insert(User user);

    /**
     * update user avatar
     * @param avatarUrl
     * @param id
     */
    @Update("update user set avatar=#{avatarUrl},update_time=now() where id=#{id}")
    void updateAvatar(String avatarUrl, Long id);

    /**
     * update user password
     * @param newPwd
     * @param id
     */
    @Update("update user set password=#{newPwd},update_time=now() where id=#{id}")
    void updatePwd(String newPwd,long id);

    @Select("select location from user where id=#{userId}")
    String getUserLocation(Long userId);

    @Update("update user set name=#{name},update_time=now(),id_number=#{idNumber}, age=#{age} where id=#{userId}")
    void updateProfile(Long userId, String name, String idNumber, Integer age);

}
