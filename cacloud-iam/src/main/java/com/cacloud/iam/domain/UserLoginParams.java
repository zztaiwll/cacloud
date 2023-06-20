package com.cacloud.iam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="用户登录请求")
public class UserLoginParams {

    @ApiModelProperty(value="用户名称", required = true)
    private String userName;
    @ApiModelProperty(value="用户密码", required = true)
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
