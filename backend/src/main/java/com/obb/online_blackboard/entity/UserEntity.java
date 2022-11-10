package com.obb.online_blackboard.entity;

import com.obb.online_blackboard.entity.base.Date;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 陈桢梁
 * @desc UserEntity.java
 * @date 2022-10-27 09:10
 * @logs[0] 2022-10-27 09:10 陈桢梁 创建了UserEntity.java文件
 */

@Data
public class UserEntity extends Date {

    private long id;

    @NotNull
    @NotBlank
    @Length(min = 8, max = 16)
    private String username;

    @NotNull
    @NotBlank
    @Length(min = 8, max = 16)
    private String password;

    private UserDataEntity data;


}
