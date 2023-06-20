package com.cacloud.iam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "重置密码请求")
public class ChangeUserPasswordParams {

    @ApiModelProperty(value = "用户Id")
    private Long id;
    @ApiModelProperty(value = "旧密码", required = true)
    private String oldPassword;
    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;


}
