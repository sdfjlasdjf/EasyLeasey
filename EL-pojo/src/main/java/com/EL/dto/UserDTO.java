package com.EL.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String name;

    private String phone;

    private String sex;

    private String idNumber;

    private String location;

    private Integer age;

}
